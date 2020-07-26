package com.gprs.haryana;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.media.VolumeProviderCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.gprs.haryana.VictimAlertForegroundNotification.CHANNEL_ID;

public class AlarmForegroundNotification extends Service {

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    public static final String NOTIFICATION_CHANNEL_ID = "10005";
    Context context;
    Intent resultIntent;
    Ringtone ringtoneAlarm;
    int timer = 0;
    String extStorageDirectory = Environment.getExternalStorageDirectory()
            .toString();
    File f = new File(extStorageDirectory + "/COVI19RELIEF/alarm/obj.dat");
    VolumeProviderCompat myVolumeProvider;


    ArrayList<String> name, time;
    ArrayList<Boolean> onoff, repeat, deletea;
    private MediaSessionCompat mediaSession;

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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.println(Log.INFO, "Alarm", "Alarm started");
        String input = intent.getStringExtra("inputExtra");
        createNotificationChannel();

        Intent notificationIntent = new Intent(this, Splash.class);


        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("COVID19RELIEF")
                .setContentText("Stay Safe from COVID-19")
                .setSmallIcon(R.drawable.collective_intelligence_icon_use)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(101, notification);
        //do heavy work on a background thread
        //stopSelf();
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    public void startService(Class<?> serviceClass) {
        Intent serviceIntent = new Intent(this, serviceClass);
        serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    @Override
    public void onCreate() {
        this.context = this;
        Log.println(Log.INFO, "Alarm", "Alarm started running");
        resultIntent = new Intent(context, Alarm.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri alarmTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtoneAlarm = RingtoneManager.getRingtone(context, alarmTone);


        name = new ArrayList<>();
        time = new ArrayList<>();
        onoff = new ArrayList<>();

        loadMap();

        mediaSession = new MediaSessionCompat(context, "PlayerService");
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSession.setPlaybackState(new PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.STATE_PLAYING, 0, 0) //you simulate a player which plays something.
                .build());

        myVolumeProvider =
                new VolumeProviderCompat(VolumeProviderCompat.VOLUME_CONTROL_RELATIVE, /*max volume*/100, /*initial volume level*/100) {
                    @Override
                    public void onAdjustVolume(int direction) {
                        if (ringtoneAlarm.isPlaying())
                            ringtoneAlarm.stop();

                        mediaSession.setActive(false);

                    }
                };
        mediaSession.setPlaybackToRemote(myVolumeProvider);


        new CountDownTimer(3000, 3000) {
            public void onTick(long l) {
            }

            public void onFinish() {
                Log.println(Log.INFO, "Timer", String.valueOf(timer));
                if (timer == 0) {

                    if (f.exists()) {

                        loadMap();

                        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                        final String currentDateTime = dateFormat.format(new Date());
                        for (int i = 0; i < name.size(); i++) {
                            Log.println(Log.INFO, "array", time.get(i));
                            Log.println(Log.INFO, "cur", currentDateTime);
                            if (currentDateTime.equals(time.get(i)) && onoff.get(i)) {
                                mediaSession.setActive(true);
                                ringtoneAlarm.play();
                                notifyalarm(name.get(i));
                                timer = 20;
                                Intent intent1 = new Intent(context, alarmremember.class);
                                intent1.putExtra("name", name.get(i));
                                context.startActivity(intent1);
                                if (repeat.get(i).equals(false)) {
                                    onoff.set(i, false);
                                    saveMap();
                                }
                                if (deletea.get(i).equals(true)) {
                                    name.remove(i);
                                    time.remove(i);
                                    onoff.remove(i);
                                    repeat.remove(i);
                                    deletea.remove(i);
                                    saveMap();
                                }
                                break;
                            }
                        }
                        start();
                    } else
                        start();
                } else {
                    timer = timer - 1;
                    start();
                }
            }
        }.start();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        sendBroadcast(new Intent(this, Restarter.class).setAction("Alarm"));
    }

    @Override
    public void onStart(Intent intent, int startid) {

    }

    private void loadMap() {
        try {
            FileInputStream fileInputStream = new FileInputStream(f);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            name = (ArrayList<String>) objectInputStream.readObject();
            time = (ArrayList<String>) objectInputStream.readObject();
            repeat = (ArrayList<Boolean>) objectInputStream.readObject();
            deletea = (ArrayList<Boolean>) objectInputStream.readObject();
            onoff = (ArrayList<Boolean>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    void notifyalarm(String s) {
        resultIntent.putExtra("name", s);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context,
                0 /* Request code */, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.drawable.report);
        mBuilder.setContentTitle("Alarm")
                .setOnlyAlertOnce(true)
                .setContentText(s)
                .setOnlyAlertOnce(true)
                .setLargeIcon(drawableToBitmap(context.getDrawable(R.drawable.alarmtransparent)))
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setFullScreenIntent(resultPendingIntent, true)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(resultPendingIntent);

        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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
        mNotificationManager.notify(11/* Request Code */, mBuilder.build());
    }

    void saveMap() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(f);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(name);
            objectOutputStream.writeObject(time);
            objectOutputStream.writeObject(repeat);
            objectOutputStream.writeObject(deletea);
            objectOutputStream.writeObject(onoff);
            objectOutputStream.flush();
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


