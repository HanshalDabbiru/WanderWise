package com.example.wanderwise;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button submit = findViewById(R.id.submit);
        submit.setOnClickListener(v -> {
            try {
                testClick(submit);
            } catch (AuthFailureError e) {
                e.printStackTrace();
            }
        });

    }

    public void testClick(View v) throws AuthFailureError {
        String url = "https://api.yelp.com/v3/businesses/search?location='New York City'&sort_by=best_match&limit=20";
        RequestQueue queue = Volley.newRequestQueue(this);
        TextView output = findViewById(R.id.output);
        // Request a string response from the provided URL.
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                output.setText(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                output.setText(error.getMessage());
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accept", "application/json");
                params.put("Authorization", "Bearer koo-BOmofqJYENlp3gzJg-CSxvLxTWeG4tXmxWjENOV6H-0M4qBq9rwMuSoiRPZUKNIZyom-1ykaQ-IH5RDm0aN7rhczoc0dsZpkj6iMiGf0uGEZ0DOFUDmxiTVEZHYx");
                return params;
            }
        };
        queue.add(jsonRequest);
    }

}