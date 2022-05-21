package com.example.runningapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.example.runningapp.db.AppDatabase;
import com.example.runningapp.db.User;
import java.util.ArrayList;

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



        String tv_Distance_text = String.format("%.1f",old_run.GetDistanceOnTick(old_run.GetTicksAmount())) + " м", tv_Steps_text=old_run.GetCurrentStepCount() + " шагов", tv_Time_text=old_run.GetTicksAmount() + " c";
        tv_Distance.setText(tv_Distance_text);
        tv_Steps.setText(tv_Steps_text);
        tv_Time.setText(tv_Time_text);
        saveNewTrack(old_run);
    }
    public void saveNewTrack (RunRecord Record) { //сохраняем новый Track
        AppDatabase db  = AppDatabase.getDbInstance(this.getApplicationContext());
        ArrayList<Double> ArrayForRecord;
        int number_of_tracks=0;
        if (db.userDao().getMaxTrackID()!=null) {
            number_of_tracks = db.userDao().getMaxTrackID() + 1; //узнаем, какой track_id еще не занят
        }
        //basic=db.userDao().getMaxTrackID();
        int temp_seconds;
        temp_seconds=0;
        //double temp_distance;
        ArrayForRecord= Record.GetArrayList();
        for (Double i: ArrayForRecord) {
            saveNewDot(temp_seconds, i, number_of_tracks); //сохраняем в цикле точки
            ++temp_seconds;
        }
    }



    private void saveNewDot(int Seconds, double Distance, int number_of_tracks) { //с помощью этой функции мы сохраняем новую точку (User) в бд
        AppDatabase db  = AppDatabase.getDbInstance(this.getApplicationContext());

        User user = new User();
        user.seconds = Seconds;
        user.track_id=number_of_tracks;
        user.distance = Distance;
        db.userDao().insertUser(user);
    }

    public void competition (View view) {
        Intent intent = new Intent (this, TimeStepCounter.class);
        intent.putExtra("old_run", old_run);
        intent.putExtra("height", old_run.GetHeight());

        startActivity(intent);
    }

    public void reset (View view) {
        Intent intent = new Intent (this, MainActivity.class);
        startActivity(intent);
    }

}
