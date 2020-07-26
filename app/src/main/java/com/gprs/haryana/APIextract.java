package com.gprs.haryana;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class APIextract {

    Context context;
    TextView confirm, death;
    private RequestQueue queue;

    APIextract() {

    }

    public APIextract(Context context, TextView confirm, TextView death) {
        this.context = context;
        this.confirm = confirm;
        this.death = death;
        state();
    }

    public APIextract(home context, String city, String state) {
        this.context = context;
        queue = Volley.newRequestQueue(context);

    }

    public void state() {


        queue = Volley.newRequestQueue(context);

        new APIextract.RetrieveFeedTask().execute();

    }


    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

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

                confirm.setText(context.getString(R.string.confirmed) +" : "+ jsonObject.optString("confirmed"));
                death.setText(context.getString(R.string.death)  +" : "+ jsonObject.optString("deaths"));


            } catch (Exception e) {
                e.printStackTrace();


            }

        }
    }


}








