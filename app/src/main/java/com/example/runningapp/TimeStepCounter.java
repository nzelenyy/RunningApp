package com.example.runningapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;


public class TimeStepCounter extends AppCompatActivity{
    private TextView tv_Steps;
    private TextView tv_Distance;
    private TextView tv_Diff;

    private double seconds;
    private boolean running;



    private RunRecord currentRun, prevRun;
    private double MagnitudePrevious = 0; // шагомер

    private ArrayList<ProgressBar> pb_Array;
    ConstraintLayout constraintLayout;
    ProgressBar N_Pr;
    int pb_amount = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_step_counter);

        int height = getIntent().getIntExtra("height", 170);// считали рост

        currentRun = new RunRecord(height);

        prevRun = (RunRecord) getIntent().getSerializableExtra("old_run");

        tv_Steps = findViewById(R.id.text_steps);
        tv_Distance = findViewById(R.id.text_distance);
        tv_Diff = findViewById(R.id.textdiff);
        constraintLayout= findViewById(R.id.ConstritLayout);

        runTimer();
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        pb_Array = new ArrayList<>();
        for(int i=0; i<pb_amount; i++)
        {
            N_Pr = (ProgressBar) getLayoutInflater().inflate(R.layout.small_progress_bar, null);
            N_Pr.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            N_Pr.setProgress(0);
            //N_Pr.setX(-450+100*i);
            N_Pr.setY(-250-50*i);
            if(i==0)
            {
                N_Pr.setProgressTintList(ColorStateList.valueOf(Color.BLUE));
            }
            constraintLayout.addView(N_Pr);
            pb_Array.add(N_Pr);
        }

        tv_Diff.setText((String)(currentRun.GetHeight()+""));

        SensorEventListener stepDetector = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent != null && running)
                {// минимальная магнитуда
                    float x_acceleration = sensorEvent.values[0];
                    float y_acceleration = sensorEvent.values[1];
                    float z_acceleration = sensorEvent.values[2];

                    double Magnitude = Math.sqrt(x_acceleration*x_acceleration + y_acceleration*y_acceleration + z_acceleration*z_acceleration);
                    double MagnitudeDelta = Magnitude - MagnitudePrevious;
                    MagnitudePrevious = Magnitude;// вынести значения в константы
                    if (MagnitudeDelta > 10) // бег -- формула для длины шага из интернета
                    {
                        currentRun.AddRunStep((int)seconds);

                    }
                    else if (MagnitudeDelta > 1) // ходьба -- формула для длины шага из интернета
                    {
                        currentRun.AddStep((int)seconds);
                    }
                    tv_Steps.setText(((currentRun.GetCurrentStepCount()) + " шагов"));
                    tv_Distance.setText((String.format("%.1f", (currentRun.GetDistanceOnTick((int)seconds))) + " м").toString());
                    tv_Diff.setText((String)(String.format("%.1f", (currentRun.GetDistanceOnTick((int)seconds)-prevRun.GetDistanceOnTick((int)seconds))) + " м"));
                    Draw(currentRun.GetDistanceOnTick((int)seconds), prevRun.GetDistanceOnTick((int)seconds), true);
                }

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        sensorManager.registerListener(stepDetector, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    // таймер:
    private void runTimer() {
        final TextView textView = findViewById(R.id.text_time); // в onCreate, переименовать
        final Handler handler= new Handler(); // хандлер используется для планирования выполнения кода в будущем
        handler.post(new Runnable() {
            @Override
            public void run() {// нормальный тип времени???
                int hours = ((int)seconds)/3600;
                int minutes = ((int)seconds%3600)/60;
                int secon = ((int)seconds)%60;

                String time = String.format("%d:%02d:%02d", hours, minutes, secon);
                textView.setText(time);

                if(running)
                {
                    seconds++;
                }
                handler.postDelayed(this, 1000);

            }
        });

    }

    public void onCLickStart(View view) { //изменять видимость или доступность
        running = true;
    } // кнопка СТАРТ

    public void onClickStop(View view) {//изменять видимость или доступность
        running = false;
    } // кнопка СТОП

    public void onClickFinish(View view) { // кнопка ФИНИШ
        running = false;

        Intent intent = new Intent(this, FirstResults.class);
        intent.putExtra("old_run", currentRun);
        startActivity(intent); // переходим в FirstResults
    }

    public void Draw(double current_value, double value_to_compare, boolean animate)
    {
        double max_value = Math.max(current_value, value_to_compare);

        int current_value_scaled, value_to_compare_scaled;
        current_value_scaled = (int)(current_value/max_value*100);
        value_to_compare_scaled = (int)(value_to_compare/max_value*100);
        if(value_to_compare_scaled < 5) value_to_compare_scaled=5;
        if(current_value_scaled < 5) current_value_scaled=5;
        pb_Array.get(0).setProgress(current_value_scaled, animate);
        pb_Array.get(1).setProgress(value_to_compare_scaled, animate);
    }

}