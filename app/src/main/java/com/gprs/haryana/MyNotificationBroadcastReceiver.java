package com.gprs.haryana;


import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.SystemClock;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;


public class MyNotificationBroadcastReceiver extends BroadcastReceiver {

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    public static final String NOTIFICATION_CHANNEL_ID = "10003";
    SharedPreferences.Editor editor;
    String message;
    Context context;
    Intent resultIntent;


    @Override
    public void onReceive(final Context context, Intent intent) {

        this.context = context;
        SharedPreferences pref1 = context.getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref1.edit();
        resultIntent = new Intent(context, notification.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        SharedPreferences pref;
        SharedPreferences.Editor editor;
        pref = context.getSharedPreferences("user", 0); // 0 - for private mode
        editor = pref.edit();

        if (!pref.getString("user", "").equals("") && PreferenceManager.getDefaultSharedPreferences(context).getBoolean("notification", false) && PreferenceManager.getDefaultSharedPreferences(context).getBoolean("notify", false)) {

            FirebaseDatabase.getInstance().getReference().child("Notification").child(pref.getString("user", "")).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null) {
                        String str = "0";
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            str = str + data.getKey() + '\n';
                        }
                        PendingIntent resultPendingIntent = PendingIntent.getActivity(context,
                                0 /* Request code */, resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT);
                        mBuilder = new NotificationCompat.Builder(context);
                        mBuilder.setSmallIcon(R.drawable.report);
                        mBuilder.setContentTitle("You got  new notification")
                                .setOnlyAlertOnce(true)
                                .setAutoCancel(true)
                                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                                .setContentIntent(resultPendingIntent);

                        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            int importance = NotificationManager.IMPORTANCE_HIGH;
                            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
                            notificationChannel.enableLights(true);
                            notificationChannel.setLightColor(Color.RED);
                            notificationChannel.enableVibration(true);
                            assert mNotificationManager != null;
                            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
                            mNotificationManager.createNotificationChannel(notificationChannel);
                        }

                        if (str.length() > 1) {
                            assert mNotificationManager != null;
                            mNotificationManager.notify(33, mBuilder.build());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
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
}