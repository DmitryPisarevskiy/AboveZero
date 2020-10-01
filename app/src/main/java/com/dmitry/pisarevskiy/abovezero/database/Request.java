package com.dmitry.pisarevskiy.abovezero.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"city","date","temperature"})})
public class Request {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name ="date")
    public String date;

    @ColumnInfo(name ="city")
    public String city;

    @ColumnInfo(name ="temperature")
    public float temperature;

}
