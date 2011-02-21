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
