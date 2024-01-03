package com.example.agenda.Database;

import android.content.Context;

import com.example.agenda.Network.AsyncTask;
import com.example.agenda.Network.Callback;
import com.example.agenda.model.User;


import java.util.concurrent.Callable;

public class UserService {

    private final UserDao userDao;
    private final AsyncTask asyncTask= new AsyncTask();

    public UserService(Context context){
        userDao=DatabaseManager.getInstance(context).getUserDao();
    }
    public void insert(User user, Callback<User> insertCallback){
        Callable<User> callable = new Callable<User>() {
            @Override
            public User call()  {
                if(user.getUid().isEmpty()){
                    return null;
                }
                long id = userDao.insert(user);
                if(id<0){
                    return null;
                }

                return user;
            }
        };
        asyncTask.executeAsync(callable,insertCallback);
    }
}
