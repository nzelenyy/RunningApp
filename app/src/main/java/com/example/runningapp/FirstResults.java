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

        TextView tv_Distance;
        TextView tv_Steps;
        TextView tv_Time;


        tv_Distance = findViewById(R.id.textDistance);
        tv_Steps =  findViewById(R.id.textSteps);
        tv_Time = findViewById(R.id.textTime);

        old_run = (RunRecord) getIntent().getSerializableExtra("old_run");



        tv_Distance.setText((String)(old_run.GetDistanceOnTick(old_run.GetTicksAmount()) + " м"));
        tv_Steps.setText((String)(old_run.GetCurrentStepCount() + " шагов"));
        tv_Time.setText((String)(old_run.GetTicksAmount() + " c"));
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