package com.example.runningapp;

import java.io.Serializable;
import java.util.ArrayList;


public class RunRecord extends TimeStepCounter implements Serializable {
    private int _height;private ArrayList _records;
    private int _ticks_amount;
    private int _current_steps_amount;
    private int _current_run_steps_amount;
    private double _current_distance;

    RunRecord() {
        _records = new ArrayList<>();
        _height = 170;
        _current_distance = (_height) / 400.0 + 0.37;
        _current_run_steps_amount = 0;
        _current_steps_amount = 0;
        _ticks_amount = 0;
    }

    RunRecord(ArrayList new_ArrayList)
    {
        _records.add(new_ArrayList);
        _height = 170;
        _current_steps_amount = _records.size();
        _ticks_amount = _records.size();
        _current_run_steps_amount=0;
        if(_ticks_amount==0) _current_distance=0;
        else _current_distance=(double)_records.get(_ticks_amount-1);
    }

    RunRecord(int height) {
        _records = new ArrayList<>();
        _height = height;
        _current_distance = (_height) / 400.0 + 0.37;
        _current_run_steps_amount = 0;
        _current_steps_amount = 0;
        _ticks_amount = 0;
    }

    boolean AddRunStep(int new_time) {
        while(_ticks_amount< new_time+1)
        {
            _records.add(_current_distance);
            _ticks_amount++;
        }
        _ticks_amount++;
        _current_run_steps_amount++;
        _current_distance += (_height * 0.0065);
        _records.add(_current_distance);
        return true;
    }

    boolean AddStep(int new_time) {
        while(_ticks_amount< new_time+1)
        {
            _records.add(_current_distance);
            _ticks_amount++;
        }
        _ticks_amount++;
        _current_steps_amount++;
        _current_distance += ((_height) / 400.0 + 0.37);
        _records.add(_current_distance);
        return true;
    }

    double GetDistanceOnTick(int requested_tick) {
        if (requested_tick >= _ticks_amount) //return _current_distance;
            return _current_distance;
        return ((double) _records.get(requested_tick));

    }

    int GetCurrentStepCount()
    {
        return _current_run_steps_amount+_current_steps_amount;
    }

    ArrayList GetArrayList()
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
}
