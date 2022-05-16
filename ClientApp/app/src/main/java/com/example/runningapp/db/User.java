package com.example.runningapp.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo (name = "track_id")
    public int track_id;

    @ColumnInfo(name = "seconds")
    public int seconds;

    @ColumnInfo(name = "distance")
    public double distance;
}


