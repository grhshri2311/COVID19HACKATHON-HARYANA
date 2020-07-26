package com.gprs.haryana;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SigninActivity {
    Context context;
    private RequestQueue queue;

    SigninActivity() {

    }

    public SigninActivity(Context context) {
        this.context = context;
        state();
    }

    public SigninActivity(home context, String city, String state) {
        this.context = context;
        queue = Volley.newRequestQueue(context);

    }

    public void state() {


        queue = Volley.newRequestQueue(context);

        new SigninActivity.RetrieveFeedTask().execute();

    }


    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {


        }

        protected String doInBackground(Void... urls) {

            // Do some validation here
            try {
                URL url = new URL("http://192.168.43.203/Hackathon/userregistration.php?name=sdsdv&phone=61565161&email=sdbdgb&pass=dgdb&lat=1.0002&lon=1.0022&role=dfbdb");
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
                Toast.makeText(context, response, Toast.LENGTH_LONG).show();


            } catch (Exception e) {
                e.printStackTrace();


            }

        }
    }


}








