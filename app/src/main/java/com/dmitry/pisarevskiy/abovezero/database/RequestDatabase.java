package com.dmitry.pisarevskiy.abovezero.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Request.class}, version = 1)
public abstract class RequestDatabase extends RoomDatabase {
    public abstract RequestDao getRequestDao();
}
