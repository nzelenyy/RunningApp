package com.example.runningapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Settings extends AppCompatActivity implements View.OnClickListener{

    EditText etextHeight , etextWalking, etextRunning;
    Button btnSave, btnLoad;

    private SharedPreferences sharedPrefs;
    SharedPreferences.Editor ed;
    public static final String PREF = "myprefs";


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

        sharedPrefs = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        ed = sharedPrefs.edit();

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
        String stringHeight = etextHeight.getText().toString();
        String stringC1 = etextWalking.getText().toString();
        String stringC2 = etextRunning.getText().toString();

        ed.putInt("height", Integer.parseInt(stringHeight));
        ed.putInt("walking", Integer.parseInt(stringC1));
        ed.putInt("running", Integer.parseInt(stringC2));

        ed.apply();
    }


    private void loadText() {
        int savedHeight = sharedPrefs.getInt("height", 170);
        int savedC1 = sharedPrefs.getInt("walking", 6);
        int savedC2 = sharedPrefs.getInt("running", 10);

        etextHeight.setText(String.valueOf(savedHeight));
        etextWalking.setText(String.valueOf(savedC1));
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