package org.liberty.android.anyunlock;

import java.util.UUID;
import android.widget.*;
import android.view.*;
import android.app.*;
import android.os.*;
import android.util.Log;
import android.content.*;
import java.io.*;


public class AnyUnlockService extends Service{
    /* Magic number :D */
    private final int NOTIFY_ID = 0x994231;
    private static final String TAG = "AnyUnlockService";
    private KeyguardManager km;
    private KeyguardManager.KeyguardLock kl;
    private BroadcastReceiver mReceiver;
    
    @Override
    public void onCreate(){
        Log.v(TAG, "Service Started!!!!");
        km = (KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);
        kl = km.newKeyguardLock("AnyUnlock");
        kl.disableKeyguard();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction("android.intent.action.PHONE_STATE");
        mReceiver = new LockScreenReceiver();
        registerReceiver(mReceiver, filter);
    }

    public void onDestroy(){
        Log.v(TAG, "Service Destroyed!!!");
        kl.reenableKeyguard();
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /* We want this service to continue running until it is explicitly
         stopped, so return sticky.*/
        if(intent != null && intent.getAction() != null && intent.getAction().equals("anyunlock_lockscreen_intent")){
            Log.v("Hello", "Service onStartCommand");
            Intent myIntent = new Intent(this, LockScreen.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(myIntent);
        }
        return START_STICKY;
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
