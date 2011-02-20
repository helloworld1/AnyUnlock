package org.liberty.android.anyunlock;

import android.app.*;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.os.SystemClock;


public class AnyUnlock extends Activity implements View.OnClickListener
{
    private Button disableLockButton;
    private Button enableLockButton;
    private PendingIntent mAlarmSender;
    private AlarmManager am;
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
    }

    @Override
    public void onClick(View v)
    {
        if(v == enableLockButton){
            Toast.makeText(this, "AnyUnlock Enabled", Toast.LENGTH_SHORT).show();
            long firstTime = SystemClock.elapsedRealtime();
            am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, 600*1000, mAlarmSender);
        }
        if(v == disableLockButton){
            Toast.makeText(this, "AnyUnlock Disabled", Toast.LENGTH_SHORT).show();
            am.cancel(mAlarmSender);
            stopService(new Intent(this, AnyUnlockService.class));
        }
    }
}
