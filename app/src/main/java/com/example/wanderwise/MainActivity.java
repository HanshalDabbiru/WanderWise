package com.example.wanderwise;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import com.android.volley.AuthFailureError;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button submit = findViewById(R.id.submit);
        submit.setOnClickListener(v -> {
            try {
                submitQuery(submit);
            } catch (AuthFailureError e) {
                e.printStackTrace();
            }
        });

    }

    public void submitQuery(View v) throws AuthFailureError {
        SearchView input = findViewById(R.id.input);
        String parameters = input.getQuery().toString();
        startNewActivity(parameters);
    }

    public void startNewActivity(String parameters)
    {
        Intent secondActivityIntent = new Intent(getApplicationContext(), RatingsView.class);
        secondActivityIntent.putExtra("parameters", parameters);
        startActivity(secondActivityIntent);
    }


}