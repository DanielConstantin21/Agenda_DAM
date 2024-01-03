package com.example.agenda.Database;

import android.content.Context;

import com.example.agenda.Network.AsyncTask;
import com.example.agenda.Network.Callback;
import com.example.agenda.model.Event;

import java.util.List;
import java.util.concurrent.Callable;

public class EventService {
    private final EventDao eventDao;
    private final AsyncTask asyncTask= new AsyncTask();

    public EventService(Context context){
        eventDao=DatabaseManager.getInstance(context).getEventDao();
    }
    public void insert(Event event, Callback<Event> insertCallback){
        Callable<Event> callable = new Callable<Event>() {
            @Override
            public Event call()  {
                if(event.getId_event() >0){
                    return null;
                }
                long id = eventDao.insert(event);
                if(id<0){
                    return null;
                }
                event.setId_event(id);
                return event;
            }
        };
        asyncTask.executeAsync(callable,insertCallback);
    }
    public void getDateEvents(String uid, String date, Callback<List<Event>> getDateEventsCallback ){
        Callable <List<Event>> callable = new Callable<List<Event>>() {
            @Override
            public List<Event> call() {
                return eventDao.getDateEvents(uid, date);
            }
        };
        asyncTask.executeAsync(callable,getDateEventsCallback);
    }

    public void update(Event event, Callback<Event> updateCallback){
        Callable<Event> callable = new Callable<Event>() {
            @Override
            public Event call()  {
                if(event.getId_event() ==0){
                    return null;
                }
                int rows = eventDao.update(event);
                if(rows>0) {
                    return event;
                }
                return null;
            }
        };
        asyncTask.executeAsync(callable,updateCallback);
    }
    public void delete(Event event, Callback<Event> deleteCallback){
        Callable<Event> callable = new Callable<Event>() {
            @Override
            public Event call()  {
                if(event.getId_event() ==0){
                    return null;
                }
                int rows = eventDao.delete(event);
                if(rows>0) {
                    return event;
                }
                return null;
            }
        };
        asyncTask.executeAsync(callable,deleteCallback);
    }
}

