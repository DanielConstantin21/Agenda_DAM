package com.example.agenda.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.agenda.model.Event;


import java.util.List;

@Dao
public interface EventDao {

    @Insert
    long insert(Event event);
    @Query("SELECT * FROM events join User on events.user_id = User.uid WHERE user_id like :userId and event_date like :date")
    List<Event> getDateEvents(String userId, String date);
    @Update
    int update(Event event);
    @Delete
    int delete(Event event);
}
