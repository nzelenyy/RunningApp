package com.example.runningapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.runningapp.db.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class runs_choice extends Activity {
    ArrayList<Integer> IDs;
    ArrayList<Integer> Timings;
    ArrayList<Double> Distances;

    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runs_choice);

        relativeLayout = findViewById(R.id.RelativeLayout);

        IDs = new ArrayList<Integer>();
        Timings = new ArrayList<Integer>();
        Distances = new ArrayList<Double>();

        GetIDs();
        GetTimings();
        GetDistances();
        for(int i=0; i<IDs.size(); i++)
        {
            TextView tv_ID=new TextView(runs_choice.this, null);
            TextView tv_Distance=new TextView(runs_choice.this, null);
            TextView tv_Time=new TextView(runs_choice.this, null);
            Button btn_Choose = new Button(runs_choice.this, null);

            tv_ID.setText((String)(IDs.get(i)+""));
            tv_Distance.setText((String)(Distances.get(i)+""));
            tv_Time.setText((String)(Timings.get(i)+""));
            btn_Choose.setText("Начать");

            tv_ID.setId(4*i);
            tv_Distance.setId(4*i+1);
            tv_Time.setId(4*i+2);
            btn_Choose.setId(4*i+3);

            tv_ID.setY(150*i);
            tv_ID.setX(0);
            tv_Distance.setY(150*i);
            tv_Distance.setX(50);
            tv_Time.setY(150*i+50);
            tv_Time.setX(50);
            btn_Choose.setY(150*i);
            btn_Choose.setX(200);

            btn_Choose.setOnClickListener(this::OnClick);

            relativeLayout.addView(tv_ID);
            relativeLayout.addView(tv_Distance);
            relativeLayout.addView(tv_Time);
            relativeLayout.addView(btn_Choose);
        }
    }

    private void GetDistances() {
        Distances.add(0.1);
        Distances.add(1.1);
        Distances.add(2.2);
        Distances.add(3.3);
        Distances.add(4.4);

    }

    void GetIDs()
    {
        IDs.add(0);
        IDs.add(1);
        IDs.add(2);
        IDs.add(3);
        IDs.add(4);
    }

    void GetTimings()
    {
        Timings.add(1);
        Timings.add(100);
        Timings.add(200);
        Timings.add(300);
        Timings.add(400);
    }

    void OnClick(View v)
    {
        TextView textView = findViewById(0);
        int chosen_id = ((int)v.getId()-3)/4;
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
        startActivity(intent);
    }
}
