package com.example.runningapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.runningapp.db.AppDatabase;
import com.example.runningapp.db.User;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {



    private EditText height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        height = findViewById(R.id.textHeight);
    }

    public void goToNewActivity(View view) {
        AppDatabase db  = AppDatabase.getDbInstance(this.getApplicationContext());
     //   List<Integer> newFirstNames;
        List<Double> newLastNames;
       // newFirstNames=db.userDao().getFirstNames(2);
        newLastNames=db.userDao().getLastNames(0);
        ArrayList<Double> newArrayList=new ArrayList<Double>();
        for (Double i: newLastNames) {
            newArrayList.add(i);
        }

        Intent intent = new Intent(this, TimeStepCounter.class);
        //RunRecord old_run = RunRecord(Integer.valueOf(height.getText()));
        RunRecord old_run = new RunRecord(newArrayList);
        intent.putExtra("old_run", old_run);
        if(height.getText().length()<2) intent.putExtra("height", 175);
        else intent.putExtra("height", Integer.valueOf(height.getText().toString()));
        startActivity(intent);
    }
}