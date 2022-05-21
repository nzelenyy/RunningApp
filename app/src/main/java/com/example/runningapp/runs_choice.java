package com.example.runningapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.runningapp.db.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class runs_choice extends Activity {
    ArrayList<Integer> IDs; // все track_id
    ArrayList<Integer> Timings; // все продолжительности забегов
    ArrayList<Double> Distances; // все расстояния забегов

    LinearLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runs_choice);

        linearLayout = findViewById(R.id.LinearLayoutInCsV);

        IDs = new ArrayList<>();
        Timings = new ArrayList<>();
        Distances = new ArrayList<>();
        UpdatePage(); //заполняет массивы и отображает все данные
    }

    private void GetDistances() {
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        for(int i=0;i<IDs.size();i++)
        {
            Double newLastName;
            newLastName=db.userDao().getDistance(IDs.get(i));
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
            newFirstName=db.userDao().getSecond(IDs.get(i));
            Timings.add(newFirstName);
        }
    }

    void OnClick(View v)  //  Обработчик кнопки  "начать"
    {
        RunRecord old_run;
        int chosen_id = IDs.get((int)v.getTag()); // находит нужный id

        Intent intent = new Intent(this, TimeStepCounter.class);

        TextView textView = findViewById(0);
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        List<Double> newLastNames;
        newLastNames = db.userDao().getDistances(IDs.get(chosen_id)); //считывает все записанные точки из базы
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
        int chosen_id = IDs.get((int)v.getTag());// находит нужный id
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        db.userDao().DeleteGivenTrack(chosen_id); //Удаляет запись
        Intent intent = new Intent(this, runs_choice.class); // перезапускает страницу
        startActivity(intent);
    }

    void OnClickTest(View v) //  Обработчик кнопки "Тест"
    {
        int test_num = (int)v.getTag();
        Intent intent = new Intent(this, TimeStepCounter.class);
        RunRecord old_run = new RunRecord(175);
        old_run.SetTesting(test_num);
        intent.putExtra("old_run", old_run);
        startActivity(intent);
    }

    void UpdatePage()
    {
        RunRecord runRecord = new RunRecord();
        GetIDs();
        GetTimings();
        GetDistances();
        for(int i=0; i<IDs.size()+runRecord.GetTestAmounts(); i++)
        {
            LinearLayout T_LL, B_LL;
            if(i<IDs.size()) {
                String time = String.format("%d:%02d:%02d", Timings.get(i) / 3600, (Timings.get(i) % 3600) / 60, (Timings.get(i)) % 60);

                T_LL =  (LinearLayout) getLayoutInflater().inflate(R.layout.rc_single_run_text, null);
                B_LL = (LinearLayout) getLayoutInflater().inflate(R.layout.rc_single_run_buttons, null);

                Button btn_Choose = (Button) B_LL.getChildAt(0);
                Button btn_Delete = (Button) B_LL.getChildAt(1);

                btn_Choose.setOnClickListener(this::OnClick);
                btn_Delete.setOnClickListener(this::OnClickDelete);

                btn_Delete.setBackgroundColor(Color.RED);
                btn_Choose.setBackgroundColor(Color.GREEN);


                TextView tv_ID = (TextView) T_LL.getChildAt(0);
                TextView tv_Distance = (TextView)T_LL.getChildAt(1);
                TextView tv_Timing = (TextView)T_LL.getChildAt(2);
                ImageView iv_Downloaded = (ImageView)T_LL.getChildAt(3);

                tv_Distance.setX(150);
                tv_Timing.setX(250);
                iv_Downloaded.setX(300);


                tv_Distance.setTextSize(40);
                tv_Timing.setTextSize(40);
                tv_ID.setTextSize(40);

                tv_ID.setText((String)(IDs.get(i)+""));
                tv_Distance.setText((String)(String.format("%.1f", Distances.get(i))+" м"));
                tv_Timing.setText(time);

                btn_Choose.setTag(i);
                btn_Delete.setTag(i);

                linearLayout.addView(T_LL);
                linearLayout.addView(B_LL);
            }
            else
            {
                int test_number=i-IDs.size()+1;

                Button btn_RunTest = new Button(this);
                btn_RunTest.setTag(test_number);
                btn_RunTest.setText("Test "+test_number+": "+runRecord.GetTestDescription(test_number));
                btn_RunTest.setOnClickListener(this::OnClickTest);
                linearLayout.addView(btn_RunTest);
            }



        }
    }
}
