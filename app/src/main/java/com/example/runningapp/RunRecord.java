package com.example.runningapp;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.Serializable;
import java.util.ArrayList;


public class RunRecord implements Serializable {
    private final int _height; //Высота человека
    private final ArrayList<Double> _records; //Массив всех результатов
    private int _ticks_amount; // Количество записанных "тиков"
    private int _current_steps_amount;
    private int _current_run_steps_amount;
    private double _current_distance;
    private int _is_testing_record;
    private int _tests_amount=2;


    RunRecord() {
        _is_testing_record = 0;
        _records = new ArrayList<>();
        _height = 170;
        _current_distance = -(_height) / 400.0 + 0.37;
        _current_run_steps_amount = 0;
        _current_steps_amount = 0;
        _ticks_amount = 0;
        _tests_amount=2;
    }

    RunRecord(ArrayList<Double> new_ArrayList)
    {
        _is_testing_record=0;
        _records = new_ArrayList;
        _height = 170;
        _current_steps_amount = _records.size();
        _ticks_amount = _records.size();
        _current_run_steps_amount=0;
        if(_ticks_amount==0) _current_distance=0;
        else _current_distance=_records.get(_ticks_amount-1);
        _tests_amount=2;
    }

    RunRecord(int height) {
        _records = new ArrayList<>();
        _height = height;
        _is_testing_record=0;
        _current_distance = -(_height) / 400.0 + 0.37;
        _current_run_steps_amount = 0;
        _current_steps_amount = 0;
        _ticks_amount = 0;
        _tests_amount=2;
    }

    void AddRunStep(int new_time) {
        while(_ticks_amount< new_time+1)
        {
            _records.add(_current_distance);
            _ticks_amount++;
        }
        _ticks_amount++;
        _current_run_steps_amount++;
        _current_distance += (_height * 0.0065);
        _records.add(_current_distance);
    }

    void AddStep(int new_time) {
        while(_ticks_amount< new_time+1)
        {
            _records.add(_current_distance);
            _ticks_amount++;
        }
        _ticks_amount++;
        _current_steps_amount++;
        _current_distance += ((_height) / 400.0 + 0.37);
        _records.add(_current_distance);
    }

    double GetDistanceOnTick(int requested_tick) {

        if(_is_testing_record==0) {
            if (requested_tick >= _ticks_amount) //return _current_distance;
                return _current_distance;
            return (_records.get(requested_tick));
        }
        else return GetTestResult(requested_tick);

    }

    int GetCurrentStepCount()
    {
        return _current_run_steps_amount+_current_steps_amount;
    }

    ArrayList<Double> GetArrayList()
    {
        return _records;
    }

    int GetHeight()
    {
        return _height;
    }

    int GetTicksAmount(){
        return _ticks_amount;
    }

    void SetTesting()
    {
        _is_testing_record=1;
    }

    void SetTesting(int test_num)
    {
        _is_testing_record=test_num;
    }

    String GetTestDescription(int test_num)
    {
        switch (test_num){
            case 1:
                return "Standing still";
            case 2:
                return "A320 flying";
            default:
                return "Test not found";
        }
    }

    String GetTestDescription()
    {
        return GetTestDescription(_is_testing_record);
    }

    double GetTestResult(int test_num, int requested_tick)
    {
        switch (test_num){
            case 1:
                return 0.0;
            case 2:
                return (247.2*requested_tick);
            default:
                return 0.0;
        }
    }

    double GetTestResult(int requested_tick)
    {
        switch (_is_testing_record){
            case 1:
                return 0.0;
            case 2:
                return (247.2*requested_tick);
            default:
                return 0.0;
        }
    }

    int GetTestAmounts()
    {
        return _tests_amount;
    }
}
