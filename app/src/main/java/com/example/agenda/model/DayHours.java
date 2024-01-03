package com.example.agenda.model;

import android.content.Context;
import com.example.agenda.R;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DayHours implements Serializable {
    private final String hourLabel;
    private Event event=null;

    public DayHours(String hourLabel) {
        this.hourLabel = hourLabel;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        if(event == null){
            this.event = null;
        }else {
            this.event = event;
        }
    }

    public String getHourLabel() {
        return hourLabel;
    }

    @Override
    public String toString() {
        return  hourLabel + " "+(event != null?event.getTitle():"");
    }

    public static ArrayList<DayHours> getHourList(String dateString, Context context, List<Event> events){

        ArrayList<DayHours> response = new ArrayList<>();
        for(int i =0;i<24;i++){
            response.add(new DayHours((i<10?context.getString(R.string.hour_simple,i):context.getString(R.string.hour_double,i))));
        }
        if(events !=null || events.size()>0) {
            for (int j = 0; j < events.size(); j++) {
                Event e = events.get(j);
                for (int k = e.getBegin_time(); k < e.getEnd_time(); k++) {
                    response.get(k).setEvent(e);
                }
            }
        }
        return response;
    }
}
