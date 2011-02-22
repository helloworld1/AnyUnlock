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

import java.util.UUID;
import android.widget.*;
import android.view.*;
import android.app.*;
import android.os.*;
import android.util.Log;
import android.content.*;
import java.io.*;
import android.provider.Settings;
import android.app.admin.DevicePolicyManager;


public class LockScreenService extends Service{
    /* Magic number :D */
    private final int NOTIFY_ID = 0x9fe231;
    private static final String TAG = "LockScreenService";
    private DevicePolicyManager mDPM;
    private ComponentName mAdminReceiver;
    
    @Override
    public void onCreate(){
        Log.v(TAG, "LockScreenService Started!!!!");
        mDPM = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
        mAdminReceiver = new ComponentName(this, AdminReceiver.class);
        if(mDPM.isAdminActive(mAdminReceiver)){
            mDPM.lockNow();
        }
        else{
            Intent myIntent = new Intent(this, AdminPermissionSet.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(myIntent);
        }
        stopSelf();
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final IBinder mBinder = new Binder() {
        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply,
                int flags) throws RemoteException {
            return super.onTransact(code, data, reply, flags);
        }
    };

}
