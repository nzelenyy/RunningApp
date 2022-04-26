package com.example.runningapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class FirstResults extends AppCompatActivity {


    private RunRecord old_run;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_results);

        TextView textDistance;
        TextView textSteps;
        TextView textTime;


        textDistance = findViewById(R.id.textDistance);
        textSteps =  findViewById(R.id.textSteps);
        textTime = findViewById(R.id.textTime);

        old_run = (RunRecord) getIntent().getSerializableExtra("old_run");



        String Distance_text=old_run.GetDistanceOnTick(old_run.GetTicksAmount()) + " м", Steps_text = old_run.GetCurrentStepCount() + " шагов", Time_text = old_run.GetTicksAmount() + " c";
        textDistance.setText(Distance_text);
        textSteps.setText(Steps_text);
        textTime.setText(Time_text);
    }

    public void competition (View view) {
        Intent intent1 = new Intent (this, TimeStepCounter.class);
        intent1.putExtra("old_run", old_run);
        intent1.putExtra("height", old_run.GetHeight());

        startActivity(intent1);
    }

    public void reset (View view) {
        Intent intent = new Intent (this, MainActivity.class);
        startActivity(intent);
    }

}