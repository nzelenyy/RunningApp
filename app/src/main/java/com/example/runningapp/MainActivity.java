package com.example.runningapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
        intent.putExtra("height", height.getText().toString()); // передаем введенный рост
        intent.putExtra("zero_dist", zeroDist); //передаем массив с нулями
        startActivity(intent);
    }
}