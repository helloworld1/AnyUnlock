package org.liberty.android.anyunlock;

import android.app.*;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.os.SystemClock;
import android.os.Handler;
import android.telephony.*;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import java.util.Calendar;


public class LockScreen extends Activity implements View.OnClickListener {
    private DateFormat dateFormatter = DateFormat.getDateInstance();
    private DateFormat timeFormatter = DateFormat.getTimeInstance();
    private DateFormat dayOfWeekFormatter = new SimpleDateFormat("E / F");
    private TelephonyManager tm;
    private TextView timeView;
    private TextView dateView;
    private TextView dowView;
    private TextView batteryView;
    private Handler mHandler;
    private long mStartTime;

    private static String TAG = "LockScreen";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lock_screen);
        tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        dateView = (TextView)findViewById(R.id.date);
        timeView = (TextView)findViewById(R.id.time);
        timeView = (TextView)findViewById(R.id.time);
        dowView = (TextView)findViewById(R.id.dow);
        batteryView = (TextView)findViewById(R.id.battery);
        mHandler = new Handler();

    }
    @Override
    public void onResume(){
        Date nd = Calendar.getInstance().getTime();
        dateView.setText(dateFormatter.format(nd));
        timeView.setText(timeFormatter.format(nd));
        dowView.setText(dayOfWeekFormatter.format(nd));
        if(tm.getCallState() != 0){
            finish();
        }
        mStartTime = System.currentTimeMillis();
        mHandler.removeCallbacks(mUpdateTimeTask);
        mHandler.postDelayed(mUpdateTimeTask, 100);

        super.onResume();
    }
    @Override
    public void onPause(){
        mHandler.removeCallbacks(mUpdateTimeTask);
        super.onPause();
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long start = SystemClock.uptimeMillis();

            Date nd = Calendar.getInstance().getTime();
            timeView.setText(timeFormatter.format(nd));


            mHandler.postDelayed(this, 1000);
        }
    };
    @Override
    public void onClick(View v) {}
}
