package org.liberty.android.anyunlock;

import android.app.*;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.util.Log;
import android.preference.PreferenceManager;

public class LockScreenReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent){
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            Log.v("Hello", "Hello OFF");
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);

            if(settings.getBoolean("enable_lock_screen", true)){
                Intent myIntent = new Intent(context, AnyUnlockService.class);

                myIntent.setAction("anyunlock_lockscreen_intent");
                context.startService(myIntent);
            }
            Log.v("Hello", "Hello ON");
        }
    }
}
