package org.liberty.android.anyunlock;

import android.app.*;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.util.Log;

import android.app.admin.DevicePolicyManager;


public class AdminPermissionSet extends Activity {
    private static String TAG = "AdminPermissionSet";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ComponentName mAdminReceiver= new ComponentName(this, AdminReceiver.class);
        Intent myIntent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        myIntent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                mAdminReceiver);
        myIntent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                "AnyUnlock require this permission to turn off the phone without using power off button.");
        startActivityForResult(myIntent, 18);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 18){
            finish();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
