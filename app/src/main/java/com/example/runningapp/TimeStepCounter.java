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
    //переименовать все View, отсоединить ит интерфейса
    private TextView Steps;
    private TextView Distance;
    private TextView Diff;

    private double seconds;
    private boolean running;

    private int height = 170; // средний рост человека в России (М - 175, Ж - 164)


    private RunRecord currentRun, prevRun;
    private double MagnitudePrevious = 0; // шагомер
    private Integer stepCount = 0; //счетчик шагов
    private double distance = 0; // текущая дистанция
    private double zeroStep; //удалить
    // Перевести в arraylist
    private double[] zeroDist; //старый пробег(переименовать или удалить)
    private double[] Dist = new double[10000];
    private String[] stringDist = new String [10000]; //удалить

    private ArrayList<ProgressBar> pb_Array;
    ConstraintLayout constraintLayout;
    ProgressBar N_Pr;
    int pb_amount = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_step_counter);

        String stringHeight = getIntent().getStringExtra("height"); // считали рост

        currentRun = new RunRecord(Integer.parseInt(stringHeight));
        //zeroStep = ((double)height)/400 + 0.37; // посчитали "нулевой шаг
        // Вынести все ниже в 1 структуру в качестве аргумента, в creator написать заполнение нулями, удалить 50-53
        ArrayList old_run_data = getIntent().getParcelableExtra("old_run");
        if (old_run_data==null)
        {
            prevRun = new RunRecord();
        }
        else prevRun = new RunRecord(old_run_data);
        //double[] compDist = getIntent().getDoubleArrayExtra("comp_dist"); // считали массив, с которым будем сорвеноваться из FirstResults
        //if (compDist != null) // если этот массив не null (то есть мы перешли из FirstResults, a не из MainActivity), то соревнуемся с ним
        //{
        //zeroDist = compDist;
        //}

        Steps = findViewById(R.id.text_steps);
        Distance = findViewById(R.id.text_distance);
        Diff = findViewById(R.id.textdiff);
        constraintLayout= findViewById(R.id.ConstritLayout);

        runTimer();
        // ПЕРЕНЕСТИ ОБЪЯВЛЕНИЕ
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        pb_Array = new ArrayList<>();
        for(int i=0; i<pb_amount; i++)
        {
            N_Pr = (ProgressBar) getLayoutInflater().inflate(R.layout.small_progress_bar, null);
            N_Pr.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            N_Pr.setProgress(0);
            //N_Pr.setRotation(270);
            //N_Pr.setX(-450+100*i);
            N_Pr.setY(-250-50*i);
            if(i==0)
            {
                N_Pr.setProgressTintList(ColorStateList.valueOf(Color.BLUE));
            }
            constraintLayout.addView(N_Pr);
            pb_Array.add(N_Pr);
        }

        // шагомер:
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
                    //обработка условий остановки(остановки нет)
                    // вынести в отдельный метод взаимодействия с UI
                    Steps.setText((currentRun.GetCurrentStepCount()) + " шагов");
                    Distance.setText(String.format("%.1f", (currentRun.GetDistanceOnTick((int)seconds))) + " м");
                    Diff.setText(String.format("%.1f", (currentRun.GetDistanceOnTick((int)seconds)-prevRun.GetDistanceOnTick((int)seconds))) + " м");
                    //stringDist[(int)seconds] = String.format("%.1f", (distance - zeroStep)); //удалить?
                    Draw(currentRun.GetDistanceOnTick((int)seconds), prevRun.GetDistanceOnTick((int)seconds), true);
                }

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {//???

            }
        };

        sensorManager.registerListener(stepDetector, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    // таймер:
    private void runTimer() {
        final TextView textView = (TextView) findViewById(R.id.text_time); // в onCreate, переименовать
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
        //intent.putExtra("steps", Integer.toString((stepCount - 1))); // передаем количество шагов
        //intent.putExtra("seconds", Integer.toString((int) seconds)); // передаем количество секунд
        //intent.putExtra("dist", Dist); // передаем массив расстояний
        //intent.putExtra("height2", height); // передаем рост
        startActivity(intent); // переходим в FirstResults
    }

    public void Draw(double current_value, double value_to_compare, boolean animate)
    {
        double max_value;
        if(current_value>value_to_compare) max_value = current_value;
        else max_value = value_to_compare;
        int current_value_scaled, value_to_compare_scaled;
        current_value_scaled = (int)(current_value/max_value*100);
        value_to_compare_scaled = (int)(value_to_compare/max_value*100);
        if(value_to_compare_scaled < 5) value_to_compare_scaled=5;
        if(current_value_scaled < 5) current_value_scaled=5;
        pb_Array.get(0).setProgress(current_value_scaled, animate);
        pb_Array.get(1).setProgress(value_to_compare_scaled, animate);
    }

}