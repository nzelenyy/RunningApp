package com.example.runningapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;


public class TimeStepCounter extends AppCompatActivity{
    ConstraintLayout constraintLayout;
    private TextView tv_Steps; // TextView для отображения актуального числа шагов
    private TextView tv_Distance; // TextView для отображения актуальной проденой дистанции
    private TextView tv_Diff; // TextView для отображения актуальной разницы
    private ArrayList<ProgressBar> pb_Array; // Массив ProgressBar'ов для отображения статуса забега
    private Button btn_Start;
    private Button btn_Stop;
    private Button btn_Finish;


    private double seconds; //Счетчик, указывающий, сколько идет запись
    private boolean running; // Булева переменная, указывающая, работает ли таймер
    private RunRecord currentRun, prevRun;
    private double MagnitudePrevious = 0; // Переменная, необходимая для храниения амплитуды

    int pb_amount = 2;// Константа количества одновременных забегов

    private SharedPreferences sharedPrefs;
    SharedPreferences.Editor ed;

    public static final String PREF = "myprefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_step_counter);

        sharedPrefs = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        int height = sharedPrefs.getInt("height", 170);

        currentRun = new RunRecord(height); //Вызвали конструктор текущего забега.

        prevRun = (RunRecord) getIntent().getSerializableExtra("old_run"); //Получили данные о prevRun из предыдущего activity

        tv_Steps = findViewById(R.id.text_steps);
        tv_Distance = findViewById(R.id.text_distance);
        tv_Diff = findViewById(R.id.textdiff);
        btn_Start = findViewById(R.id.btnStart);
        btn_Stop = findViewById(R.id.btnStop);
        btn_Finish = findViewById(R.id.btnFinish);
        constraintLayout= findViewById(R.id.ConstritLayout);

        runTimer();
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        pb_Array = new ArrayList<>();
        for(int i=0; i<pb_amount; i++)
        {
            ProgressBar N_Pr = (ProgressBar) getLayoutInflater().inflate(R.layout.small_progress_bar, null);
            N_Pr.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            N_Pr.setProgress(0);
            N_Pr.setY(-250-50*i);
            if(i==0)
            {
                N_Pr.setProgressTintList(ColorStateList.valueOf(Color.BLUE));
            }
            constraintLayout.addView(N_Pr);
            pb_Array.add(N_Pr);
        }

        //tv_Diff.setText((String)(currentRun.GetHeight()+""));

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
                    sharedPrefs = getSharedPreferences(PREF, Context.MODE_PRIVATE);
                    int C1 = sharedPrefs.getInt("walking", 1);
                    int C2 = sharedPrefs.getInt("running", 10);
                    if (MagnitudeDelta > C1) //Сравнили со значением, соответствующему магнитуде шага при беге
                    {
                        currentRun.AddRunStep((int)seconds);
                        UpdateTextViews();
                        UpdateProgressBars(currentRun.GetDistanceOnTick((int)seconds), prevRun.GetDistanceOnTick((int)seconds), true);

                    }
                    else if (MagnitudeDelta > C2) //Сравнили со значением, соответствующему магнитуде шага
                    {
                        currentRun.AddStep((int)seconds);
                        UpdateTextViews();
                        UpdateProgressBars(currentRun.GetDistanceOnTick((int)seconds), prevRun.GetDistanceOnTick((int)seconds), true);

                    }
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
                    UpdateTextViews();
                    UpdateProgressBars(currentRun.GetDistanceOnTick((int)seconds), prevRun.GetDistanceOnTick((int)seconds), true);

                }
                handler.postDelayed(this, 1000);
            }
        });

    }

    public void onCLickStart(View view) { //изменять видимость или доступность
        running = true;
        btn_Start.setVisibility(View.INVISIBLE);
        btn_Stop.setVisibility(View.VISIBLE);
        btn_Finish.setVisibility(View.VISIBLE);
        //currentRun.AddStep((int)seconds);
        UpdateTextViews();
        UpdateProgressBars(currentRun.GetDistanceOnTick((int)seconds), prevRun.GetDistanceOnTick((int)seconds), true);

    } // кнопка СТАРТ

    public void onClickStop(View view) {//изменять видимость или доступность
        running = false;
        btn_Start.setVisibility(View.VISIBLE);
        btn_Finish.setVisibility(View.VISIBLE);
        btn_Stop.setVisibility(View.INVISIBLE);
    } // кнопка СТОП

    public void onClickFinish(View view) { // кнопка ФИНИШ
        running = false;

        Intent intent = new Intent(this, FirstResults.class);
        intent.putExtra("old_run", currentRun);
        startActivity(intent); // переходим в FirstResults
    }

    public void UpdateTextViews()
    {
        //Обновили значения всех TextView
        tv_Steps.setText(((currentRun.GetCurrentStepCount()) + " шагов"));
        tv_Distance.setText((String.format("%.1f", (currentRun.GetDistanceOnTick((int)seconds))) + " м").toString());
        tv_Diff.setText((String)(String.format("%.1f", (currentRun.GetDistanceOnTick((int)seconds)-prevRun.GetDistanceOnTick((int)seconds))) + " м"));
    }
    public void UpdateProgressBars(double current_value, double value_to_compare, boolean animate)
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