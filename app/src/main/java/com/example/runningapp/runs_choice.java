package com.example.runningapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.runningapp.db.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class runs_choice extends Activity {
    ArrayList<Integer> IDs; // все track_id
    ArrayList<Integer> Timings; // все продолжительности забегов
    ArrayList<Double> Distances; // все расстояния забегов

    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runs_choice);

        relativeLayout = findViewById(R.id.RelativeLayout);

        IDs = new ArrayList<Integer>();
        Timings = new ArrayList<Integer>();
        Distances = new ArrayList<Double>();

        UpdatePage(); //заполняет массивы и отображает все данные
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
        // считывание track_id из базы
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
        // считывание Timings из базы

        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());

        for(int i=0;i<IDs.size(); i++)
        {
            Integer newFirstName;
            newFirstName=db.userDao().getFirstName(IDs.get(i));
            Timings.add(newFirstName);
        }
    }

    void OnClick(View v)  //  Обработчик кнопки  "начать"
    {
        RunRecord old_run;
        int chosen_id = IDs.get(((int) v.getId() - 3) / 5); // находит нужный id

        Intent intent = new Intent(this, TimeStepCounter.class);

        TextView textView = findViewById(0);
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        List<Double> newLastNames;
        newLastNames = db.userDao().getLastNames(IDs.get(chosen_id)); //считывает все записанные точки из базы
        ArrayList<Double> newArrayList = new ArrayList<Double>();
        for (Double i : newLastNames) {
            newArrayList.add(i);
        }
        old_run = new RunRecord(newArrayList);
        intent.putExtra("old_run", old_run); //передает intent в следующий activity
        startActivity(intent);
    }

    void OnClickDelete(View v)  //  Обработчик кнопки  "Удалить"
    {
        int chosen_id = IDs.get(((int) v.getId() - 4) / 5);// находит нужный id
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        db.userDao().DeleteGivenTrack(chosen_id); //Удаляет запись
        Intent intent = new Intent(this, runs_choice.class); // перезапускает страницу
        startActivity(intent);
    }

    void OnClickTest(View v) //  Обработчик кнопки "Тест"
    {
        Intent intent = new Intent(this, TimeStepCounter.class);
        RunRecord old_run = new RunRecord(175);
        intent.putExtra("old_run", old_run);
        startActivity(intent);
    }
    void UpdatePage()
    {
        GetIDs();
        GetTimings();
        GetDistances();
        for(int i=0; i<IDs.size()+1; i++)
        {
            TextView tv_ID=new TextView(runs_choice.this, null);
            TextView tv_Distance=new TextView(runs_choice.this, null);
            TextView tv_Time=new TextView(runs_choice.this, null);
            Button btn_Choose = new Button(runs_choice.this, null);
            Button btn_Remove = new Button(runs_choice.this, null);

            if(i<IDs.size()) {
                String time = String.format("%d:%02d:%02d", Timings.get(i) / 3600, (Timings.get(i) % 3600) / 60, (Timings.get(i)) % 60);

                btn_Remove.setText("Remove");
                tv_ID.setText((String) (IDs.get(i) + ""));
                tv_Distance.setText((String) (Distances.get(i) + ""));
                tv_Time.setText(time);
                btn_Choose.setText("Начать");

                tv_ID.setId(5 * i);
                tv_Distance.setId(5 * i + 1);
                tv_Time.setId(5 * i + 2);
                btn_Choose.setId(5 * i + 3);
                btn_Remove.setId(5*i+4);

                tv_ID.setTextSize(30);


                tv_ID.setY(150 * i);
                tv_ID.setX(0);

                tv_Distance.setY(150 * i);
                tv_Distance.setX(100);

                tv_Time.setY(150 * i + 50);
                tv_Time.setX(100);

                btn_Choose.setY(150 * i);
                btn_Choose.setX(800);
                btn_Remove.setY(150 * i);
                btn_Remove.setX(500);

                btn_Remove.setBackgroundColor(Color.RED);

                btn_Remove.setOnClickListener(this::OnClickDelete);
                btn_Choose.setOnClickListener(this::OnClick);

                relativeLayout.addView(btn_Remove);
            }
            else
            {
                tv_ID.setText((String) (""));
                tv_Distance.setText("");
                tv_Time.setText(" ");
                btn_Choose.setText("Тест");

                tv_ID.setId(5 * i);
                tv_Distance.setId(5 * i + 1);
                tv_Time.setId(5 * i + 2);
                btn_Choose.setId(5 * i + 3);

                tv_ID.setTextSize(30);


                tv_ID.setY(150 * i);
                tv_ID.setX(0);

                tv_Distance.setY(150 * i);
                tv_Distance.setX(100);

                tv_Time.setY(150 * i + 50);
                tv_Time.setX(100);

                btn_Choose.setY(150 * i);
                btn_Choose.setX(800);

                btn_Choose.setOnClickListener(this::OnClickTest);
            }

            relativeLayout.addView(tv_ID);
            relativeLayout.addView(tv_Distance);
            relativeLayout.addView(tv_Time);
            relativeLayout.addView(btn_Choose);


        }
    }
}
