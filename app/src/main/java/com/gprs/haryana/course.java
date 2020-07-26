package com.gprs.haryana;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class course extends AppCompatActivity {

    SearchView searchView;
    ToggleButton toggleButton;
    ListView listView;
    ArrayList name, ins, type, image, id;
    CustomCourseAdapter customCourseAdapter;
    String language = "en", key = "";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_course);

        searchView = findViewById(R.id.search);
        toggleButton = findViewById(R.id.lan);
        listView = findViewById(R.id.list);

        name = new ArrayList();
        ins = new ArrayList();
        type = new ArrayList();
        image = new ArrayList();
        id = new ArrayList();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(true);

        progressDialog.show();
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                progressDialog.show();
                if (isChecked) {

                    language = "hi";
                    requestWithSomeHttpHeaders();
                } else {
                    language = "en";
                    requestWithSomeHttpHeaders();
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                progressDialog.show();
                key = query;
                requestWithSomeHttpHeaders();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        customCourseAdapter = new CustomCourseAdapter(this, name, ins, type, image, id);
        listView.setAdapter(customCourseAdapter);


        requestWithSomeHttpHeaders();
    }

    public void requestWithSomeHttpHeaders() {
        name.clear();
        ins.clear();
        type.clear();
        image.clear();
        id.clear();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://www.udemy.com/api-2.0/courses/?search=" + key + "&language=" + language;
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // response
                        try {
                            JSONObject obj = (JSONObject) new JSONTokener(response).nextValue();
                            JSONArray array = (JSONArray) obj.getJSONArray("results");

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);
                                name.add(jsonObject.optString("title"));
                                JSONArray jsonArray = jsonObject.getJSONArray("visible_instructors");
                                ins.add(jsonArray.getJSONObject(0).optString("display_name"));
                                type.add(jsonObject.getJSONObject("price_detail").optString("amount"));
                                image.add(jsonObject.optString("image_125_H"));
                                id.add(jsonObject.optString("id"));
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    customCourseAdapter.notifyDataSetChanged();
                                    progressDialog.hide();
                                }
                            });


                        } catch (Exception e) {
                            e.printStackTrace();


                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR", "error => " + error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json, text/plain, */*");
                params.put("Authorization", "Basic MklCTUxWNEdaSTFLbXNwU0FNTVh2eXplSmVQUjc4SVFDT0VXN1pSRjpvbjh0UTdxa1dEaXBaT1E1TFEwc0RBdzZkNEdvcmg5c1FiTFRQWk1HcnYwS1pwbDZSdzZtbEdiajY1RnlreWRhTVFPNFhreUZNeDF5SXppR25pOHBGMnBaSzl4ZDBseW5BY0RUOWhNbkhmUDEzWHlyYTlMbGxZdUFtdW9BUmtMMg==");
                params.put("Content-Type", "application/json;charset=utf-8");

                return params;
            }
        };
        queue.add(getRequest);

    }
}
