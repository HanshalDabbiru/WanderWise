package com.example.wanderwise;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Business[] business_obj_list = new Business[5];
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
        SearchView input = findViewById(R.id.input);
        String parameters = input.getQuery().toString();
        get_businesses(parameters);
    }

    public void get_businesses(String parameters) {
        String url = "https://api.yelp.com/v3/businesses/search?location=" + parameters + "&sort_by=rating&limit=20";
        RequestQueue queue = Volley.newRequestQueue(this);
        // Request a string response from the provided URL.
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                JSONArray business_list = response.getJSONArray("businesses");
                for (int i = 0; i < 5; i++) {
                    String name = business_list.getJSONObject(i).getString("name");
                    double rating = Double.parseDouble(business_list.getJSONObject(i).getString("rating"));
                    int num_ratings = Integer.parseInt(business_list.getJSONObject(i).getString("review_count"));
                    String link = business_list.getJSONObject(i).getString("url");
                    String image = business_list.getJSONObject(i).getString("image_url");
                    Business b = new Business(name, rating, num_ratings, link, image);
                    business_obj_list[i] = b;
                }
                Business[] five_b = selectBusinesses(business_obj_list);
                TextView output = findViewById(R.id.output);
                output.setText(five_b[0].name);

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }, null) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("accept", "application/json");
                params.put("Authorization", "Bearer koo-BOmofqJYENlp3gzJg-CSxvLxTWeG4tXmxWjENOV6H-0M4qBq9rwMuSoiRPZUKNIZyom-1ykaQ-IH5RDm0aN7rhczoc0dsZpkj6iMiGf0uGEZ0DOFUDmxiTVEZHYx");
                return params;
            }
        };
        queue.add(jsonRequest);
    }

    public Business[] selectBusinesses(Business[] businesses)
    {
        Arrays.sort(businesses, Comparator.comparing(b -> b.num_ratings));
        businesses = Arrays.copyOfRange(businesses, 0, 5);
        return businesses;
    }

    static class Business {
        public String name;
        public double rating;
        public int num_ratings;
        public String link;
        public String image;

        public Business(String name, double rating, int num_ratings, String link, String image)
        {
            this.name = name;
            this.rating = rating;
            this.num_ratings = num_ratings;
            this.link = link;
            this.image = image;
        }
    }

}