package com.gprs.haryana;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import androidx.core.content.ContextCompat;

import java.util.Calendar;

public class Restarter extends BroadcastReceiver {

    Context context;

    @Override
    public void onReceive(final Context context, Intent intent) {

        this.context = context;
        startHelpNeed(true, 3);
        startNotification();
        startYourLoction();
        String action = intent.getAction();

        switch (action) {
            case "VictimAlert":
                if (!isMyServiceRunning(VictimAlertForegroundNotification.class)) {
                    startService(VictimAlertForegroundNotification.class);
                    break;
                }
        }
        if (!isMyServiceRunning(AlarmForegroundNotification.class)) {
            startService(AlarmForegroundNotification.class);
        }


    }

    public void startService(Class<?> serviceClass) {
        Intent serviceIntent = new Intent(context, serviceClass);
        serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");
        ContextCompat.startForegroundService(context, serviceIntent);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    void startHelpNeed(boolean set, int sec) {


        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent;
        PendingIntent pendingIntent0 = null;

        // SET TIME HERE
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        myIntent = new Intent(context, HelpneededBroadcastReceiver.class);
        pendingIntent0 = PendingIntent.getBroadcast(context, 0, myIntent, 0);

        if (set) {

            manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() +
                            sec * 1000, pendingIntent0);

        } else if (manager != null) {
            manager.cancel(pendingIntent0);
        }

    }


    void startNotification() {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent;
        PendingIntent pendingIntent = null;

        // SET TIME HERE
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        myIntent = new Intent(context, MyNotificationBroadcastReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0);

        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() +
                        10 * 1000, pendingIntent);

    }

    void startYourLoction() {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent;


        // SET TIME HERE
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());


        manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = null;

        // SET TIME HERE
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        myIntent = new Intent(context, YourLocationBroadcastReciever.class);
        pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0);


        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() +
                        1 * 1000, pendingIntent);

    }
}