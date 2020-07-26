package com.gprs.haryana;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


class CustomCourseAdapter extends ArrayAdapter {

    private final ArrayList<String> type, name, ins, image, id;
    private Activity context;
    private ProgressDialog progressDialog;


    public CustomCourseAdapter(Activity context, ArrayList<String> name, ArrayList<String> ins, ArrayList<String> type, ArrayList image, ArrayList id) {
        super(context, R.layout.labsitem, name);
        this.context = context;
        this.name = name;
        this.ins = ins;
        this.type = type;
        this.image = image;
        this.id = id;
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);


    }

    public View getView(final int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = null;

        rowView = inflater.inflate(R.layout.courseitem, null, true);
        TextView name1 = rowView.findViewById(R.id.name);
        TextView ins1 = rowView.findViewById(R.id.type);
        TextView type1 = rowView.findViewById(R.id.type1);
        TextView getdirection = rowView.findViewById(R.id.getdirection);
        ImageView logo = rowView.findViewById(R.id.logo);

        new DownloadImageTask(logo)
                .execute(image.get(position));


        name1.setText(name.get(position));
        ins1.setText(ins.get(position));
        type1.setText("Rs." + type.get(position));

        getdirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();
                requestWithSomeHttpHeaders(id.get(position));
            }
        });


        return rowView;

    }

    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    public void requestWithSomeHttpHeaders(String cid) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "https://www.udemy.com/api-2.0/courses/" + cid;
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONObject obj = (JSONObject) new JSONTokener(response).nextValue();

                            String ins1 = "";
                            JSONArray array = obj.getJSONArray("visible_instructors");

                            for (int i = 0; i < array.length(); i++) {
                                ins1 = ins1 + array.getJSONObject(i).optString("title") + "," + array.getJSONObject(i).optString("job_title") + "\n";
                            }

                            viewcourse(obj.optString("title"), "https://www.udemy.com" + obj.optString("url"), "Rs." + obj.getJSONObject("price_detail").optString("amount"), obj.optString("image_480x270"), ins1);

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

    private void viewcourse(String title, final String url, String s, String image_480x270, String ins1) {

        Bundle bundle = new Bundle();
        bundle.putString("image_480x270", image_480x270);
        bundle.putString("title", title);
        bundle.putString("ins1", ins1);
        bundle.putString("s", s);
        bundle.putString("url", url);

        progressDialog.hide();
        BottomSheetDialogFragment f = new Bottomsheetcoursefragment();
        f.setArguments(bundle);
        FragmentActivity fr = (FragmentActivity) context;
        f.show(fr.getSupportFragmentManager(), "Dialog");


    }
}


