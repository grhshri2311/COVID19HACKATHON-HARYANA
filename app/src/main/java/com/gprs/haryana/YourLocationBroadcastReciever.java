package com.gprs.haryana;


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
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class YourLocationBroadcastReciever extends BroadcastReceiver {

    public static final String NOTIFICATION_CHANNEL_ID = "10052";
    private SharedPreferences pref;
    SharedPreferences.Editor editor;
    String message;
    Context context;
    Intent resultIntent;
    String newactive = null, newrecover = null, newdeath = null;

    @Override
    public void onReceive(final Context context, Intent intent) {

        this.context = context;
        pref = context.getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        resultIntent = new Intent(context, Splash.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        RequestQueue queue = Volley.newRequestQueue(context);

        Log.println(Log.INFO, "your location", "entered");
        new CountDownTimer(120000, 120000) {
            public void onTick(long l) {
            }

            public void onFinish() {

                pref = context.getSharedPreferences("user", 0);
                if (!pref.getString("user", "").equals("") && PreferenceManager.getDefaultSharedPreferences(context).getBoolean("notification", false) && PreferenceManager.getDefaultSharedPreferences(context).getBoolean("updates", false)) {
                    new YourLocationBroadcastReciever.RetrieveFeedTask1().execute();
                }

                start();
            }
        }.start();


    }

    class RetrieveFeedTask1 extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {


        }

        protected String doInBackground(Void... urls) {

            // Do some validation here
            try {
                URL url = new URL("https://api.covidindiatracker.com/state_data.json");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);

            }
            return null;
        }

        protected void onPostExecute(String response) {

            try {


                JSONArray jsonArray = (JSONArray) new JSONTokener(response).nextValue();

                for (int a = 0; a < jsonArray.length(); a++) {
                    JSONObject object = jsonArray.getJSONObject(a);
                    if (object.optString("state").equals("Haryana")) {
                        newactive = object.optString("cChanges");
                        newrecover = object.optString("rChanges");
                        newdeath = object.optString("dChanges");
                        break;
                    }

                    new YourLocationBroadcastReciever.RetrieveFeedTask2().execute();
                }


            } catch (Exception e) {
                e.printStackTrace();


            }

        }
    }

    class RetrieveFeedTask2 extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {


        }

        protected String doInBackground(Void... urls) {

            // Do some validation here
            try {
                URL url = new URL("https://api.covid19india.org/data.json");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);

            }
            return null;
        }

        protected void onPostExecute(String response) {

            try {

                JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
                JSONArray jsonArray = object.getJSONArray("statewise");
                JSONObject jsonObject = jsonArray.getJSONObject(0);


                message = "India\n Total cases : " + jsonObject.optString("confirmed") + "\n Total recovered : " + jsonObject.optString("recovered") + "\n Total death : " + jsonObject.optString("deaths");
                message = message + "\nHaryana \n Today new cases : " + newactive + "\n Today Recovered " + newrecover + "\n Today Death : " + newdeath;
                PendingIntent resultPendingIntent = PendingIntent.getActivity(context,
                        0 /* Request code */, resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
                mBuilder.setSmallIcon(R.drawable.report);
                mBuilder.setContentTitle("In your location")
                        .setContentText("Updates for Today")
                        .setAutoCancel(true)
                        .setLargeIcon(drawableToBitmap(context.getDrawable(R.drawable.corona)))
                        .setOnlyAlertOnce(true)
                        .setStyle(new NotificationCompat.BigTextStyle().setBigContentTitle("Latest Updates ").bigText(message))
                        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                        .setContentIntent(resultPendingIntent);

                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

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
                assert mNotificationManager != null;
                mNotificationManager.notify(22 /* Request Code */, mBuilder.build());
            } catch (Exception e) {
                e.printStackTrace();


            }

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