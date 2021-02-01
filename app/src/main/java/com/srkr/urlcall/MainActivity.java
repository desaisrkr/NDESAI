package com.srkr.urlcall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button bt;
    TextView tv,gen;
    ImageView img;
    String gender;
    String sname;
    String imgurl;
    String url = "https://randomuser.me/api/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.name);
        img = (ImageView) findViewById(R.id.image);
        gen = (TextView) findViewById(R.id.gender);

        bt = (Button) findViewById(R.id.button);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestRandomUser("female");

            }
        });
    }

    public void requestRandomUser(String pgender) {

        Log.v("URL is ", url);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest sr = new StringRequest(Request.Method.GET, url+"?gender="+pgender, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v("response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    getUserNameFromJson(jsonObject);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("response", error.toString());
            }
        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("gender", gender);
//                //params.put("subject", subject);
//
//                return params;
//            }

//                    @Override
//                    public Map<String, String> getHeaders() throws AuthFailureError {
//                        Map<String, String> params = new HashMap<String, String>();
//                        params.put("Content-Type", "application/x-www-form-urlencoded");
//                        return params;
//                    }
        };
        queue.add(sr);
    }

    public void getUserNameFromJson(JSONObject obj) {

        try {
            JSONArray jsoArrayResult = obj.getJSONArray("results");
            Log.v("Inside result", jsoArrayResult.toString());
            for (int i = 0; i < jsoArrayResult.length(); i++) {
                JSONObject jsonobject = jsoArrayResult.getJSONObject(i);
                gender = jsonobject.getString("gender");
                sname = formatName(jsonobject.getString("name"));
                imgurl = formatePicture(jsonobject.getString("picture"));
            }

            Log.v("===Inside result== ", gender);
            Log.v("===Inside result== ", sname);
            Log.v("===Inside result== ", imgurl);
            tv.setText(sname);
            gen.setText(gender);

            //img.setImageURI(Uri.parse(imgurl));

            Glide.with(this).load(imgurl).into(img);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String formatePicture(String jsonStringPicture) {
        String limgurl = "";
        try {
            JSONObject jsonObject = new JSONObject(jsonStringPicture);
            limgurl = jsonObject.getString("large");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return limgurl;

    }

    public String formatName(String jsonstringname) {
        String name = "";
        try {
            JSONObject jsonObject = new JSONObject(jsonstringname);
            //name":{"title":"Monsieur","first":"Carl","last":"Joly"}
            String title = jsonObject.getString("title");
            String first = jsonObject.getString("first");
            String last = jsonObject.getString("last");
            name = title + "." + first + " " + last;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }


}
