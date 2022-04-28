package com.example.runningapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Settings extends AppCompatActivity implements View.OnClickListener{

    EditText etextHeight , etextWalking, etextRunning;
    Button btnSave, btnLoad;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        etextHeight = (EditText)findViewById(R.id.etextHeight);
        etextWalking = (EditText)findViewById(R.id.etextWalking);
        etextRunning = (EditText)findViewById(R.id.etextRunning);

        btnSave = (Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        btnLoad = (Button)findViewById(R.id.btnLoad);
        btnLoad.setOnClickListener(this);

        loadText();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSave:
                saveText();
                break;
            case R.id.btnLoad:
                loadText();
                break;
            default:
                break;
        }
    }



    private void saveText() {
        SharedPreferences.Editor ed = getPreferences(MODE_PRIVATE).edit();
        String stringHeight = etextHeight.getText().toString();
        ed.putInt("height", Integer.parseInt(stringHeight));
        ed.apply();

        SharedPreferences.Editor ed1 = getPreferences(MODE_PRIVATE).edit();
        String stringC1 = etextWalking.getText().toString();
        ed1.putInt("walking", Integer.parseInt(stringC1));
        ed1.apply();

        SharedPreferences.Editor ed2 = getPreferences(MODE_PRIVATE).edit();
        String stringC2 = etextRunning.getText().toString();
        ed2.putInt("running", Integer.parseInt(stringC2));
        ed2.apply();
    }


    private void loadText() {
        SharedPreferences sPrefHeight = getPreferences(MODE_PRIVATE);
        int savedHeight = sPrefHeight.getInt("height", 170);        // ВОТ ТУТ ПОЛУЧАЕМ ДАННЫЕ
        etextHeight.setText(String.valueOf(savedHeight));

        SharedPreferences sPrefC1 = getPreferences(MODE_PRIVATE);
        int savedC1 = sPrefC1.getInt("walking", 6);
        etextWalking.setText(String.valueOf(savedC1));

        SharedPreferences sPrefC2 = getPreferences(MODE_PRIVATE);
        int savedC2 = sPrefC2.getInt("running", 10);
        etextRunning.setText(String.valueOf(savedC2));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveText();
    }

    public void backToMain(View view) {
        Intent intent = new Intent(Settings.this, MainActivity.class);
        startActivity(intent);
    }
}