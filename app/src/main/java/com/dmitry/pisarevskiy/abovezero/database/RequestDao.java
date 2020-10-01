package com.dmitry.pisarevskiy.abovezero.database;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface RequestDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRequest(Request request);

    @Query("SELECT * FROM Request")
    List<Request> getAllRequests();

    @Query("SELECT COUNT() FROM Request")
    long getCountRequests();
}
