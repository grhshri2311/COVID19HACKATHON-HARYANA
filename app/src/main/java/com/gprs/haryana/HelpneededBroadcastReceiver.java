
package com.gprs.haryana;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.media.VolumeProviderCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;


public class HelpneededBroadcastReceiver extends BroadcastReceiver {

    public static final String NOTIFICATION_CHANNEL_ID = "100010";
    String message;
    Context context;
    private SharedPreferences pref;
    SharedPreferences.Editor editor;
    Intent resultIntent;

    private MediaPlayer player;
    private MediaSessionCompat mediaSession;
    UserLocationHelper mylocation;
    private VolumeProviderCompat myVolumeProvider;
    UserLocationHelper userLocationHelper;

    @Override
    public void onReceive(final Context context, Intent intent) {
        pref = context.getSharedPreferences("user", 0);
        this.context = context;
        resultIntent = new Intent(context, help.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        player = MediaPlayer.create(context, Settings.System.DEFAULT_RINGTONE_URI);


        mediaSession = new MediaSessionCompat(context, "PlayerService");
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSession.setPlaybackState(new PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.STATE_PLAYING, 0, 0) //you simulate a player which plays something.
                .build());

        if (!pref.getString("user", "").equals("")) {
            FirebaseDatabase.getInstance().getReference().child("Users").child(pref.getString("user", "")).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null) {
                        mylocation = dataSnapshot.getValue(UserLocationHelper.class);

                        FirebaseDatabase.getInstance().getReference().child("Respond").child("Help").child(pref.getString("user", "")).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() != null) {
                                    player.start();
                                    userLocationHelper = dataSnapshot.getValue(UserLocationHelper.class);
                                    myVolumeProvider =
                                            new VolumeProviderCompat(VolumeProviderCompat.VOLUME_CONTROL_RELATIVE, /*max volume*/100, /*initial volume level*/100) {
                                                @Override
                                                public void onAdjustVolume(int direction) {
                                                    player.pause();
                                                    mediaSession.setActive(false);

                                                }
                                            };

                                    notifyalert();
                                    mediaSession.setPlaybackToRemote(myVolumeProvider);
                                    mediaSession.setActive(true);

                                    Intent intent = new Intent(context, help.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);
                                    //           FirebaseDatabase.getInstance().getReference().child("Respond").child("reply").child(userLocationHelper.getPhone()).setValue(mylocation);
                                    reset(true, 60);
                                } else
                                    reset(true, 3);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    } else
                        reset(true, 3);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }


    }


    void notifyalert() {
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context,
                0 /* Request code */, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.drawable.report);
        mBuilder.setContentTitle("Need Help !")
                .setContentText(userLocationHelper.getFname() + " need your  help !!!")
                .setOnlyAlertOnce(true)
                .setAutoCancel(true)
                .setLargeIcon(drawableToBitmap(context.getDrawable(R.drawable.helpicon)))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setFullScreenIntent(resultPendingIntent, true)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert mNotificationManager != null;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(17/* Request Code */, mBuilder.build());
    }

    void reset(boolean set, int sec) {


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

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
