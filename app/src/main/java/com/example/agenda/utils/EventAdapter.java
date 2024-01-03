package com.example.agenda.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.agenda.R;
import com.example.agenda.model.DayHours;
import com.example.agenda.model.EventType;
import java.util.List;

public class EventAdapter extends ArrayAdapter<DayHours> {
    private Context context;
    private int resource;
    private List<DayHours> objects;
    private LayoutInflater inflater;
    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public EventAdapter(@NonNull Context context, int resource, @NonNull List<DayHours> objects, LayoutInflater inflater) {
        super(context, resource, objects);
        this.context=context;
        this.resource = resource;
        this.objects =objects;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = inflater.inflate(resource,parent,false );
        DayHours dayHours = objects.get(position);
        if(dayHours==null){
            return view;
        }
        addHour(view, dayHours.getHourLabel());
        addIcon(view, dayHours.getEvent()==null?null:dayHours.getEvent().getEventType());
        addTitle(view, dayHours.getEvent()==null?"":dayHours.getEvent().getTitle());
        return view;
    }

    private void addTitle(View view, String s) {
        TextView textView = view.findViewById(R.id.lv_row_tv_title);
        populateTextView(textView,s);
        if(!s.trim().isEmpty()) {
            textView.setBackgroundResource(R.color.bkg_lv);
            textView.setTextColor(Color.WHITE);

        }
    }

    private void addIcon(View view, EventType eventType) {
        ImageView imageView = view.findViewById(R.id.lv_row_tv_img_task);
        if(eventType == null){
            imageView.setImageResource(android.R.color.transparent);
        }
        if(eventType !=null && eventType.equals(EventType.TASK)){
            imageView.setImageResource(R.drawable.baseline_task_24);
        } else if (eventType !=null && eventType.equals(EventType.MEETING)) {
            imageView.setImageResource(R.drawable.baseline_people_24);
        }else{

        }
    }

    private void addHour(View view, String hourLabel) {
        TextView textView = view.findViewById(R.id.lv_row_tv_hour);
        populateTextView(textView,hourLabel);
    }
    private  void populateTextView(TextView textView, String value){
        if(textView != null && !value.trim().isEmpty()){
            textView.setText(value);
        }else{
            textView.setText(R.string.empty_text);
        }
    }

}
