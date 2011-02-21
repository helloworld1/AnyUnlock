package org.liberty.android.anyunlock;

import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.util.Log;
import android.widget.RemoteViews;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.net.Uri;
import java.util.Date;
import android.app.PendingIntent;


public class AnyUnlockWidgetProvider extends AppWidgetProvider{

    private final static String TAG = "AnyUnlockWidgetProvider";
    private final int WIDGET_REQ = 0x7c539234;

    @Override
    public void onEnabled(Context context){
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context){
        super.onDisabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){


        final int N = appWidgetIds.length;

        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];
            RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.widget);
            ComponentName thisWidget = new ComponentName(context, AnyUnlockWidgetProvider.class);
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            Intent myIntent = new Intent(context, LockScreenService.class);
            PendingIntent pendingIntent = PendingIntent.getService(context, WIDGET_REQ, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            updateViews.setOnClickPendingIntent(R.id.off_button, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, updateViews);
        }

    }

}
