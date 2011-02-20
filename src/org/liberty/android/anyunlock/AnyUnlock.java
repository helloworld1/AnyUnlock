package org.liberty.android.anyunlock;

import android.app.*;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.os.SystemClock;
import android.app.admin.DevicePolicyManager;


public class AnyUnlock extends Activity implements View.OnClickListener
{
    private Button disableLockButton;
    private Button enableLockButton;
    private PendingIntent mAlarmSender;
    private AlarmManager am;
    private DevicePolicyManager mDPM;
    ComponentName mAdminReceiver;
    /* Magic number */
    private final static int REQ_CODE = 0x575f72a;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        disableLockButton = (Button)findViewById(R.id.disable_button);
        enableLockButton = (Button)findViewById(R.id.enable_button);
        disableLockButton.setOnClickListener(this);
        enableLockButton.setOnClickListener(this);
        am = (AlarmManager)getSystemService(ALARM_SERVICE);
        mAlarmSender = PendingIntent.getService(this, REQ_CODE, new Intent(this, AnyUnlockService.class), 0);
        mDPM = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
        mAdminReceiver = new ComponentName(this, AdminReceiver.class);
    }

    @Override
    public void onClick(View v)
    {
        if(v == enableLockButton){
            Toast.makeText(this, "AnyUnlock Enabled", Toast.LENGTH_SHORT).show();
            long firstTime = SystemClock.elapsedRealtime();
            am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, 600*1000, mAlarmSender);
            if(!mDPM.isAdminActive(mAdminReceiver)){
                enableAdmin();
            }
            else{
                Intent myIntent = new Intent(this, AnyUnlockService.class);
                myIntent.setAction("anyunlock_disable_lock_intent");
                this.startService(myIntent);
            }

        }
        if(v == disableLockButton){
            Toast.makeText(this, "AnyUnlock Disabled", Toast.LENGTH_SHORT).show();
            am.cancel(mAlarmSender);
            stopService(new Intent(this, AnyUnlockService.class));
            if(mDPM.isAdminActive(mAdminReceiver)){
                disableAdmin();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                Intent myIntent = new Intent(this, AnyUnlockService.class);
                myIntent.setAction("anyunlock_disable_lock_intent");
                this.startService(myIntent);
                return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void enableAdmin(){
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                mAdminReceiver);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                "AnyUnlock require this permission to turn off the phone without using power off button.");
        startActivityForResult(intent, 1);
    }

    private void disableAdmin(){
        mDPM.removeActiveAdmin(mAdminReceiver);
    }

}
