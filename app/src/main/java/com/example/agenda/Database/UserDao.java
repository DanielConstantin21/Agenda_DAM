package com.example.agenda.Database;

import androidx.room.Dao;
import androidx.room.Insert;

import com.example.agenda.model.User;

@Dao
public interface UserDao {

    @Insert
    long insert(User user);
}
