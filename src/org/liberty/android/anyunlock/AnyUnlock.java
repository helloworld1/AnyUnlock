/*
Copyright (C) 2010 Haowen Ning

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

*/
package org.liberty.android.anyunlock;

import android.app.*;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.telephony.*;
import android.os.Process;
import android.os.SystemClock;
import android.os.Handler;
import android.app.admin.DevicePolicyManager;


public class AnyUnlock extends Activity implements View.OnClickListener
{
    private Button disableLockButton;
    private Button enableLockButton;
    private PendingIntent mAlarmSender;
    private AlarmManager am;
    private Handler mHandler;
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
        mHandler = new Handler();
    }

    @Override
    public void onClick(View v)
    {
        if(v == enableLockButton){
            Toast.makeText(this, "AnyUnlock Enabled", Toast.LENGTH_SHORT).show();
            long firstTime = SystemClock.elapsedRealtime();
            am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, 600*1000, mAlarmSender);
            enableAdmin();

        }
        if(v == disableLockButton){
            Toast.makeText(this, "AnyUnlock Disabled, exit soon!", Toast.LENGTH_SHORT).show();
            am.cancel(mAlarmSender);
            disableAdmin();
            stopService(new Intent(this, AnyUnlockService.class));
            mHandler.postDelayed(new Runnable(){
                public void run(){
                    Process.killProcess(Process.myPid());
                }
            }
            ,400);

        }
    }

	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_screen_menu, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
    	Intent myIntent = new Intent();
	    switch (item.getItemId()) {
        case R.id.settings:{
    		myIntent.setClass(this, SettingsScreen.class);
    		startActivity(myIntent);
            return true;
        }

    	case R.id.about: {
            new AlertDialog.Builder(this)
                .setTitle("About AnyUnlock")
                .setMessage("AnyUnlock\nAuthor: Liberty (Haowen Ning)\nEmail: liberty@anymemo.org")
                .setPositiveButton("OK", null)
                .show();
                return true;
            }
	    }
	    return false;
	}

    @Override
    public void onResume(){
        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        if(tm.getCallState() != 0){
            finish();
        }
        super.onResume();
    }

    private void enableAdmin(){
        Intent myIntent = new Intent(this, AdminPermissionSet.class);
        startActivityForResult(myIntent, 18);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 18){
            KeyguardManager km = (KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);
            KeyguardManager.KeyguardLock kl = km.newKeyguardLock("AnyUnlock");
            kl.disableKeyguard();

        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    private void disableAdmin(){
        ComponentName mAdminReceiver= new ComponentName(this, AdminReceiver.class);
        DevicePolicyManager mDPM = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
        mDPM.removeActiveAdmin(mAdminReceiver);
    }

}
