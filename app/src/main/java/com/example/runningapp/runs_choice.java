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
        for(int i=0; i<IDs.size()+1; i++)
        {
            TextView tv_ID=new TextView(runs_choice.this, null);
            TextView tv_Distance=new TextView(runs_choice.this, null);
            TextView tv_Time=new TextView(runs_choice.this, null);
            Button btn_Choose = new Button(runs_choice.this, null);

            if(i<IDs.size()) {
                String time = String.format("%d:%02d:%02d", Timings.get(i) / 3600, (Timings.get(i) % 3600) / 60, (Timings.get(i)) % 60);

                tv_ID.setText((String) (IDs.get(i) + ""));
                tv_Distance.setText((String) (Distances.get(i) + ""));
                tv_Time.setText(time);
                btn_Choose.setText("Начать");

                tv_ID.setId(4 * i);
                tv_Distance.setId(4 * i + 1);
                tv_Time.setId(4 * i + 2);
                btn_Choose.setId(4 * i + 3);

                tv_ID.setTextSize(30);


                tv_ID.setY(150 * i);
                tv_ID.setX(0);

                tv_Distance.setY(150 * i);
                tv_Distance.setX(100);

                tv_Time.setY(150 * i + 50);
                tv_Time.setX(100);

                btn_Choose.setY(150 * i);
                btn_Choose.setX(800);
            }
            else
            {

            }
            btn_Choose.setOnClickListener(this::OnClick);

            relativeLayout.addView(tv_ID);
            relativeLayout.addView(tv_Distance);
            relativeLayout.addView(tv_Time);
            relativeLayout.addView(btn_Choose);


        }


    }

    private void GetDistances() {
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        for(int i=0;i<IDs.size();i++)
        {
            Double newLastName;
            newLastName=db.userDao().getLastName(IDs.get(i));
            Distances.add(newLastName);
        }

    }

    void GetIDs()
    {
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        List<Integer> newIDs;
        newIDs=db.userDao().getIDs();
        for(int i=0; i<newIDs.size(); i++)
        {
            IDs.add(newIDs.get(i));
        }
    }

    void GetTimings()
    {
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());

        for(int i=0;i<IDs.size(); i++)
        {
            Integer newFirstName;
            newFirstName=db.userDao().getFirstName(IDs.get(i));
            Timings.add(newFirstName);
        }
    }

    void OnClick(View v)
    {
        RunRecord old_run;
        Intent intent = new Intent(this, TimeStepCounter.class);
        if(IDs.size()!=0) {


            TextView textView = findViewById(0);
            int chosen_id = ((int) v.getId() - 3) / 4;
            AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
            List<Double> newLastNames;
            newLastNames = db.userDao().getLastNames(IDs.get(chosen_id));
            ArrayList<Double> newArrayList = new ArrayList<Double>();
            for (Double i : newLastNames) {
                newArrayList.add(i);
            }
            old_run = new RunRecord(newArrayList);
        }
        else
        {
            old_run = new RunRecord(175);
        }
        intent.putExtra("old_run", old_run);
        startActivity(intent);
    }
}
