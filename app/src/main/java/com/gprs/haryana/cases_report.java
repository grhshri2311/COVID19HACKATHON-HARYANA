package com.gprs.haryana;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class cases_report extends AppCompatActivity {

    Button button;
    Spinner spinner, spinner2;
    ArrayList<String> arrayList, district;
    ArrayList<String> arrayList1, district1;
    ArrayList<String> active;
    ArrayList<String> confirm, cconfirm;
    ArrayList<String> death, cdeath;
    ArrayList<String> recover, crecover;
    ArrayList<String> active1;
    ArrayList<String> confirm1;
    ArrayList<String> death1;
    ArrayList<String> recover1;
    ArrayAdapter<String> dataAdapter, dataAdapter2;
    String state = null;
    ArrayList<JSONObject> object;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cases_report2);

        button = findViewById(R.id.button2);

        RequestQueue queue = Volley.newRequestQueue(this);
        arrayList = new ArrayList();
        district = new ArrayList<>();
        arrayList1 = new ArrayList();
        active = new ArrayList();
        confirm = new ArrayList();
        recover = new ArrayList();
        death = new ArrayList();
        cconfirm = new ArrayList();
        crecover = new ArrayList();
        cdeath = new ArrayList();
        active1 = new ArrayList();
        confirm1 = new ArrayList();
        recover1 = new ArrayList();
        district1 = new ArrayList<>();
        death1 = new ArrayList();
        object = new ArrayList<>();

        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startActivity(new Intent(cases_report.this, cases_report.class));
                swipeRefreshLayout.setRefreshing(false);
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(cases_report.this, visualize.class));
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        spinner = findViewById(R.id.state_select);
        spinner2 = findViewById(R.id.state_district);


        dataAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, R.id.txt_bundle, arrayList);
        dataAdapter2 = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, R.id.txt_bundle, district);


        spinner.setAdapter(dataAdapter);
        spinner.setGravity(11);

        int initialposition = spinner.getSelectedItemPosition();
        spinner.setSelection(initialposition, false);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    for (int i = 0; i < arrayList1.size(); i++) {
                        if (arrayList1.get(i).equals(parent.getItemAtPosition(position))) {
                            state = arrayList1.get(i);
                            district.clear();
                            district1.clear();
                            active1.clear();
                            recover1.clear();
                            death1.clear();
                            confirm1.clear();
                            dataAdapter2.notifyDataSetChanged();
                            new cases_report.RetrieveFeedTask1().execute();
                            set(i);
                            break;
                        }
                    }
                } else {
                    state = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner2.setAdapter(dataAdapter2);
        spinner2.setGravity(11);

        spinner2.setSelection(0, false);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    for (int i = 0; i < district1.size(); i++) {
                        if (district1.get(i).equals(parent.getItemAtPosition(position))) {
                            set1(i);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        new cases_report.RetrieveFeedTask().execute();

    }

    private void set1(int i) {
        TextView head = findViewById(R.id.heading);
        TextView textView = findViewById(R.id.textView5);
        TextView textView1 = findViewById(R.id.textView6);
        TextView textView2 = findViewById(R.id.textView7);
        TextView textView3 = findViewById(R.id.textview8);
        TextView textView4 = findViewById(R.id.textView61);
        TextView textView5 = findViewById(R.id.textView71);
        TextView textView6 = findViewById(R.id.textview81);

        head.setText(district1.get(i));
        textView.setText(active1.get(i));
        textView1.setText(confirm1.get(i));
        textView2.setText(death1.get(i));
        textView3.setText(recover1.get(i));

        textView4.setText("");
        textView5.setText("");
        textView6.setText("");

    }

    private void set(int i) {
        TextView head = findViewById(R.id.heading);
        TextView textView = findViewById(R.id.textView5);
        TextView textView1 = findViewById(R.id.textView6);
        TextView textView2 = findViewById(R.id.textView7);
        TextView textView3 = findViewById(R.id.textview8);
        TextView textView4 = findViewById(R.id.textView61);
        TextView textView5 = findViewById(R.id.textView71);
        TextView textView6 = findViewById(R.id.textview81);


        head.setText(arrayList1.get(i));
        textView.setText(active.get(i));
        textView1.setText(confirm.get(i));
        textView2.setText(death.get(i));
        textView3.setText(recover.get(i));
        textView4.setText('(' + cconfirm.get(i) + ')');
        textView5.setText('(' + cdeath.get(i) + ')');
        textView6.setText('(' + crecover.get(i) + ')');

    }

    public void visualize(View view) {
        startActivity(new Intent(cases_report.this, visualize.class));
    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {


        }

        protected String doInBackground(Void... urls) {

            // Do some validation here
            // NO NEW CASES DAILY https://api.covid19india.org/data.json
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
                    if (urlConnection != null)
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
                    arrayList.add(object.optString("state"));
                    arrayList1.add(object.optString("state"));
                    active.add(object.optString("active"));
                    confirm.add(object.optString("confirmed"));
                    death.add(object.optString("deaths"));
                    recover.add(object.optString("recovered"));
                    cconfirm.add(object.optString("cChanges"));
                    cdeath.add(object.optString("dChanges"));
                    crecover.add(object.optString("rChanges"));

                }


            } catch (Exception e) {
                e.printStackTrace();


            }
            Collections.sort(arrayList);
            Collections.reverse(arrayList);
            arrayList.add("Select State");
            Collections.reverse(arrayList);
            dataAdapter.notifyDataSetChanged();

        }
    }

    class RetrieveFeedTask1 extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {


        }

        protected String doInBackground(Void... urls) {

            // Do some validation here
            try {
                URL url = new URL("https://api.covid19india.org/state_district_wise.json");
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
                if (state != null) {


                    JSONObject object1 = (JSONObject) new JSONTokener(response).nextValue();
                    object1 = object1.getJSONObject(state).getJSONObject("districtData");

                    Iterator<String> keys = object1.keys();

                    while (keys.hasNext()) {
                        String key = keys.next();
                        district.add(key);
                        district1.add(key);
                        active1.add(object1.getJSONObject(key).optString("active"));
                        confirm1.add(object1.getJSONObject(key).optString("confirmed"));
                        death1.add(object1.getJSONObject(key).optString("deceased"));
                        recover1.add(object1.getJSONObject(key).optString("recovered"));
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();


            }
            Collections.sort(district);
            Collections.reverse(district);
            district.add("Select District");
            Collections.reverse(district);
            dataAdapter2.notifyDataSetChanged();

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}


