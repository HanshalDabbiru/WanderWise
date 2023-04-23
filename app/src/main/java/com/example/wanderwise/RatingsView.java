package com.example.wanderwise;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class RatingsView extends AppCompatActivity {

    Business[] business_obj_list = new Business[20];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings_view);

        Intent intent = getIntent();
        String parameters = intent.getStringExtra("parameters");
        getUnderratedBusinesses(parameters);
        getOverratedBusiness(parameters);
        TextView t = findViewById(R.id.name1);
        t.setText("here");
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

    public void getUnderratedBusinesses(String parameters)
    {
        String url = "https://api.yelp.com/v3/businesses/search?location=" + parameters + "&sort_by=rating&limit=20";
        RequestQueue queue = Volley.newRequestQueue(RatingsView.this);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, response ->
        {
            try
            {
                JSONArray business_list = response.getJSONArray("businesses");
                for (int i = 0; i < 20; i++) {
                    String name = business_list.getJSONObject(i).getString("name");
                    double rating = Double.parseDouble(business_list.getJSONObject(i).getString("rating"));
                    int num_ratings = Integer.parseInt(business_list.getJSONObject(i).getString("review_count"));
                    String link = business_list.getJSONObject(i).getString("url");
                    String image = business_list.getJSONObject(i).getString("image_url");
                    Business b = new Business(name, rating, num_ratings, link, image);
                    business_obj_list[i] = b;
                }
                Business[] underrated_businesses = selectUnderratedBusinesses(business_obj_list);
                setUnderratedUI(underrated_businesses);

            } catch (JSONException e)
            {
                throw new RuntimeException(e);
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
            TextView t = findViewById(R.id.name1);
            t.setText("That didn't work!");
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("accept", "application/json");
                params.put("Authorization", "Bearer koo-BOmofqJYENlp3gzJg-CSxvLxTWeG4tXmxWjENOV6H-0M4qBq9rwMuSoiRPZUKNIZyom-1ykaQ-IH5RDm0aN7rhczoc0dsZpkj6iMiGf0uGEZ0DOFUDmxiTVEZHYx");
                return params;
            }
        };
        queue.add(jsonRequest);
    }

    public Business[] selectUnderratedBusinesses(Business[] businesses)
    {
        Arrays.sort(businesses, Comparator.comparing(b -> b.num_ratings));
        businesses = Arrays.copyOfRange(businesses, 0, 5);
        return businesses;
    }

    public void setUnderratedUI(Business[] underrated)
    {
        int[] names = {R.id.name1, R.id.name2, R.id.name3, R.id.name4, R.id.name5};
        int[] ratingbar = {R.id.ratingbar1, R.id.ratingBar2, R.id.ratingBar3, R.id.ratingBar4, R.id.ratingBar5};
        int[] links = {R.id.link1, R.id.link2, R.id.link3, R.id.link4, R.id.link5};
        int [] images = {R.id.image1, R.id.image2, R.id.image3, R.id.image4, R.id.image5};
        int [] ratings = {R.id.rating_1, R.id.rating_2, R.id.rating_3, R.id.rating_4, R.id.rating_5};
        for(int i = 0; i < 5; i++)
        {
            Button b = findViewById(R.id.button);
            b.setText("testing");
            TextView test = findViewById(R.id.name1);
            test.setText("why is the not working");
            TextView tv = findViewById(names[i]);
            tv.setText(underrated[i].name);
            RatingBar rb = findViewById(ratingbar[i]);
            rb.setRating((float) underrated[i].rating);
            TextView link = findViewById(links[i]);
            link.setText(underrated[i].link);
            TextView rating = findViewById(ratings[i]);
            rating.setText((int) underrated[i].rating);
        }
    }



    public void getOverratedBusiness(String parameters)
    {
        String url = "https://api.yelp.com/v3/businesses/search?location=" + parameters + "&sort_by=review_count&limit=20";
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, response ->
        {
            try
            {
                JSONArray business_list = response.getJSONArray("businesses");
                for (int i = 0; i < 20; i++) {
                    String name = business_list.getJSONObject(i).getString("name");
                    double rating = Double.parseDouble(business_list.getJSONObject(i).getString("rating"));
                    int num_ratings = Integer.parseInt(business_list.getJSONObject(i).getString("review_count"));
                    String link = business_list.getJSONObject(i).getString("url");
                    String image = business_list.getJSONObject(i).getString("image_url");
                    Business b = new Business(name, rating, num_ratings, link, image);
                    business_obj_list[i] = b;
                }
                TextView t = findViewById(R.id.name1);
                t.setText("test plsss");
                Business[] overrated_businesses = selectOverratedBusinesses(business_obj_list);
                setOverratedUI(overrated_businesses);

            } catch (JSONException e)
            {
                throw new RuntimeException(e);
            }
        }, null)
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("accept", "application/json");
                params.put("Authorization", "Bearer koo-BOmofqJYENlp3gzJg-CSxvLxTWeG4tXmxWjENOV6H-0M4qBq9rwMuSoiRPZUKNIZyom-1ykaQ-IH5RDm0aN7rhczoc0dsZpkj6iMiGf0uGEZ0DOFUDmxiTVEZHYx");
                return params;
            }
        };
        queue.add(jsonRequest);
    }

    public Business[] selectOverratedBusinesses(Business[] businesses)
    {
        Arrays.sort(businesses, Comparator.comparing(b -> b.num_ratings));
        businesses = Arrays.copyOfRange(businesses, 0, 5);
        return businesses;
    }

    public void setOverratedUI(Business[] overrated)
    {
        int[] names = {R.id.name1, R.id.name2, R.id.name3, R.id.name4, R.id.name5};
        int[] ratingbar = {R.id.ratingbar1, R.id.ratingBar2, R.id.ratingBar3, R.id.ratingBar4, R.id.ratingBar5};
        int[] links = {R.id.link1, R.id.link2, R.id.link3, R.id.link4, R.id.link5};
        int [] images = {R.id.image1, R.id.image2, R.id.image3, R.id.image4, R.id.image5};
        int [] ratings = {R.id.rating_1, R.id.rating_2, R.id.rating_3, R.id.rating_4, R.id.rating_5};
        for(int i = 0; i < 5; i++)
        {
            Button b = findViewById(R.id.button);
            b.setText("testing");
            TextView test = findViewById(R.id.name1);
            test.setText("why is the not working");
            TextView tv = findViewById(names[i]);
            tv.setText(overrated[i].name);
            RatingBar rb = findViewById(ratingbar[i]);
            rb.setRating((float) overrated[i].rating);
            TextView link = findViewById(links[i]);
            link.setText(overrated[i].link);
            TextView rating = findViewById(ratings[i]);
            rating.setText((int) overrated[i].rating);
        }
    }
}