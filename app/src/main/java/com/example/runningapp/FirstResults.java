package com.example.runningapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class FirstResults extends AppCompatActivity {

    private TextView textDistance;
    private TextView textSteps;
    private TextView textTime;

    private double[] Dist;

    private String stringSeconds;
    private int height;
    private String stringHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_results);

        textDistance = (TextView) findViewById(R.id.textDistance);
        textSteps = (TextView) findViewById(R.id.textSteps);
        textTime = (TextView) findViewById(R.id.textTime);

        Dist = getIntent().getDoubleArrayExtra("dist");

        stringSeconds = getIntent().getStringExtra("seconds");

        for (int i = Integer.parseInt(stringSeconds)+1; i < Dist.length; i++)
        {
            Dist[i] = Dist[Integer.parseInt(stringSeconds)];
        }

        height = getIntent().getIntExtra("height2", 170);
        stringHeight = Integer.toString(height);

        textDistance.setText(String.format("%.1f", Dist[Integer.parseInt(stringSeconds)]) + " м");
        textSteps.setText(getIntent().getStringExtra("steps") + " шагов");
        textTime.setText(getIntent().getStringExtra("seconds") + " c");
    }

    public void competition (View view) {
        Intent intent1 = new Intent (this, TimeStepCounter.class);
        intent1.putExtra("comp_dist", Dist);
        intent1.putExtra("height", stringHeight);
        startActivity(intent1);
    }

    public void reset (View view) {
        Intent intent = new Intent (this, MainActivity.class);
        startActivity(intent);
    }

}