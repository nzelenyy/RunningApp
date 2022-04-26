package com.example.runningapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {



    private EditText height;
    private double[] zeroDist = new double[10000]; // массив из нулей -- по умолчанию соревнуемся с ним
    //10000 с = 2,7 часа -- максимальная продолжительность забега, все наши массивы такого размера

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        height = (EditText) findViewById(R.id.textHeight);
    }

    public void goToNewActivity(View view) {
        Intent intent = new Intent(this, TimeStepCounter.class);
        //RunRecord old_run = RunRecord(Integer.valueOf(height.getText()));
        RunRecord old_run = new RunRecord();
        intent.putExtra("old_run", old_run);
        if(height.getText().length()<2) intent.putExtra("height", 175);
        else intent.putExtra("height", Integer.valueOf(height.getText().toString()));
        startActivity(intent);
    }
}