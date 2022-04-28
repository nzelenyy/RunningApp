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
    SharedPreferences sPrefHeight, sPrefC1, sPrefC2;

    final String SAVED_TEXT = "saved_text";


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
        sPrefHeight = getSharedPreferences("height", MODE_PRIVATE);
        SharedPreferences.Editor ed = sPrefHeight.edit();
        ed.putString(SAVED_TEXT, etextHeight.getText().toString());
//        String stringHeight = etextHeight.getText().toString();
//        ed.putInt("height", Integer.parseInt(stringHeight));
        ed.apply();

        sPrefC1 = getSharedPreferences("walking", MODE_PRIVATE);
        SharedPreferences.Editor ed1 = sPrefC1.edit();
        ed1.putString(SAVED_TEXT, etextWalking.getText().toString());
        ed1.apply();

        sPrefC2 = getSharedPreferences("running", MODE_PRIVATE);
        SharedPreferences.Editor ed2 = sPrefC2.edit();
        ed2.putString(SAVED_TEXT, etextRunning.getText().toString());
        ed2.apply();
    }


    private void loadText() {
        sPrefHeight = getSharedPreferences("height", MODE_PRIVATE);
        String savedHeight = sPrefHeight.getString(SAVED_TEXT, "");  // ВОТ ТУТ ПОЛУЧАЕМ ЗНАЧЕНИЕ
//        Integer savedHeight = sPrefHeight.getInt("height", 170);
        etextHeight.setText(savedHeight);

        sPrefC1 = getSharedPreferences("walking", MODE_PRIVATE);
        String savedC1 = sPrefC1.getString(SAVED_TEXT, "");
        etextWalking.setText(savedC1);

        sPrefC2 = getSharedPreferences("running", MODE_PRIVATE);
        String savedC2 = sPrefC2.getString(SAVED_TEXT, "");
        etextRunning.setText(savedC2);
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