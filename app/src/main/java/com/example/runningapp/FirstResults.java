package com.example.runningapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.example.runningapp.db.AppDatabase;
import com.example.runningapp.db.User;

import java.util.ArrayList;
import java.util.List;

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
        saveNewTrack(old_run);


        //insert savenewtrack
    }
    public void saveNewTrack (RunRecord Record) {
        AppDatabase db  = AppDatabase.getDbInstance(this.getApplicationContext());
        ArrayList<Double> ArrayForRecord;
       // int number_of_tracks=1;
        int number_of_tracks=0;
        if (db.userDao().getMaxTrackID()!=null) {
            number_of_tracks = db.userDao().getMaxTrackID() + 1;
        }
        //int basic;
        //basic=db.userDao().getMaxTrackID();
        int temp_seconds;
        temp_seconds=0;
        //double temp_distance;
        ArrayForRecord= Record.GetArrayList();
        for (Double i: ArrayForRecord) {
            saveNewUser(temp_seconds, i, number_of_tracks);
            ++temp_seconds;
        }
    }



    private void saveNewUser(int firstName, double lastName, int number_of_tracks) {
        AppDatabase db  = AppDatabase.getDbInstance(this.getApplicationContext());

        User user = new User();
        user.firstName = firstName;
        user.track_id=number_of_tracks;
        user.lastName = lastName;
        db.userDao().insertUser(user);

       // finish();

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