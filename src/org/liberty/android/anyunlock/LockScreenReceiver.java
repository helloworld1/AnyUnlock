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
