package com.example.runningapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

public class TimeStepCounter extends AppCompatActivity {

    private TextView Steps;
    private TextView Distance;
    private TextView Diff;

    private double seconds;
    private boolean running;

    private int height = 170; // средний рост человека в России (М - 175, Ж - 164)


    private double MagnitudePrevious = 0;
    private Integer stepCount = 0;
    private double distance = 0;
    private double zeroStep;

    private double[] zeroDist;
    private double[] Dist = new double[10000];
    private String[] stringDist = new String [10000];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_step_counter);

        String stringHeight = getIntent().getStringExtra("height"); // считали рост
        height = Integer.parseInt(stringHeight); // перевели рост в инт

        zeroStep = ((double)height)/400 + 0.37; // посчитали "нулевой шаг"

        zeroDist = getIntent().getDoubleArrayExtra("zero_dist"); // считали массив нулей из MainActivity
        double[] compDist = getIntent().getDoubleArrayExtra("comp_dist"); // считали массив, с которым будем сорвеноваться из FirstResults
        if (compDist != null) // если этот массив не null (то есть мы перешли из FirstResults, a не из MainActivity), то соревнуемся с ним
        {
            zeroDist = compDist;
        }

        Steps = findViewById(R.id.text_steps);
        Distance = findViewById(R.id.text_distance);
        Diff = findViewById(R.id.textdiff);

        runTimer();

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // шагомер:
        SensorEventListener stepDetector = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent != null && running)
                {
                    float x_acceleration = sensorEvent.values[0];
                    float y_acceleration = sensorEvent.values[1];
                    float z_acceleration = sensorEvent.values[2];

                    double Magnitude = Math.sqrt(x_acceleration*x_acceleration + y_acceleration*y_acceleration + z_acceleration*z_acceleration);
                    double MagnitudeDelta = Magnitude - MagnitudePrevious;
                    MagnitudePrevious = Magnitude;
                    if (MagnitudeDelta > 10) // бег -- формула для длины шага из интернета
                    {
                        stepCount++;
                        distance += (height * 0.0065);

                    }
                    else if (MagnitudeDelta > 6) // ходьба -- формула для длины шага из интернета
                    {
                        stepCount++;
                        distance += (((double)height)/400 + 0.37);

                    }

                    Steps.setText((stepCount-1) + " шагов");
                    Distance.setText(String.format("%.1f", (distance - zeroStep)) + " м");
                    Diff.setText(String.format("%.1f", (distance - zeroStep - zeroDist[(int)seconds])) + " м");
                    stringDist[(int)seconds] = String.format("%.1f", (distance - zeroStep));
                    Dist[(int)seconds] = (distance - zeroStep);
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
        final TextView textView = (TextView) findViewById(R.id.text_time);
        final Handler handler= new Handler(); // хандлер используется для планирования выполнения кода в будущем
        handler.post(new Runnable() {
            @Override
            public void run() {
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

    public void onCLickStart(View view) {
        running = true;
    } // кнопка СТАРТ

    public void onClickStop(View view) {
        running = false;
    } // кнопка СТОП

    public void onClickFinish(View view) { // кнопка ФИНИШ
        running = false;

        Intent intent = new Intent(this, FirstResults.class);
        intent.putExtra("steps", Integer.toString((stepCount - 1))); // передаем количество шагов
        intent.putExtra("seconds", Integer.toString((int) seconds)); // передаем количество секунд
        intent.putExtra("dist", Dist); // передаем массив расстояний
        intent.putExtra("height2", height); // передаем рост
        startActivity(intent); // переходим в FirstResults
    }


}