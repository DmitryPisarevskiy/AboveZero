package com.dmitry.pisarevskiy.abovezero.database;

import android.app.Application;

import androidx.room.Room;

public class App extends Application {
    private static App instance;
    private RequestDatabase db;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Сохраняем объект приложения (для Singleton’а)
        instance = this;
        // Строим базу
        db = Room.databaseBuilder(
                getApplicationContext(),
                RequestDatabase.class, "request_history_database")
                .allowMainThreadQueries()
                .build();
    }

    public RequestDao getRequestDao() {
        return db.getRequestDao();
    }
}
