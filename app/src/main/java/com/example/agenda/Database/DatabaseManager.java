package com.example.agenda.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.agenda.model.Event;
import com.example.agenda.model.User;
import com.example.agenda.utils.DateConverter;

@Database(entities = {Event.class, User.class}, version=1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class DatabaseManager extends RoomDatabase{
    private static DatabaseManager databaseManager;

    public static DatabaseManager getInstance(Context context){
        if(databaseManager == null){
            databaseManager = Room.databaseBuilder(context, DatabaseManager.class,"agenda_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return databaseManager;
    }

    public abstract EventDao getEventDao();
    public abstract UserDao getUserDao();
}
