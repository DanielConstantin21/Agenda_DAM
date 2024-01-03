package com.example.agenda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agenda.Database.EventService;
import com.example.agenda.Network.Callback;
import com.example.agenda.model.Event;
import com.example.agenda.model.EventType;
import com.example.agenda.utils.PreferencesSave;
import com.google.android.material.textfield.TextInputEditText;

public class AddModifyEvent extends AppCompatActivity {
    public static final String ADD_EVENT_KEY = "ADD_EVENT_KEY";
    public static final String OPERATION_TYPE = "ADD";
    public static final String MAX_DURATION = "MAX_DURATION";
    public static final String EVENT_DATE = "EVENT_DATE";
    TextView tv_date_info;
    TextView tv_start_time;
    NumberPicker np_durate;
    TextInputEditText event_tiet_title;
    TextInputEditText event_tiet_details;
    RadioGroup rg_event_type;

    RadioButton rb_task;
    Button btn_save;
    Intent intent;
    public static boolean newEvent= true;
    Event event;
    private EventService eventService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_modify_event);

        initComponents();
    }

    private void initComponents() {
        intent = getIntent();
        int max_duration = intent.getIntExtra(MAX_DURATION,1);
        event = intent.getParcelableExtra(ADD_EVENT_KEY);
        int beg = event.getBegin_time();
        tv_date_info = findViewById(R.id.add_modify_tv_date_info);

        tv_date_info.setText(newEvent?
                getResources().getString(R.string.tv_type_event_on_date,getString(R.string.new_title))
                :getResources().getString(R.string.tv_type_event_on_date,getString(R.string.edit_title)));
        tv_start_time= findViewById(R.id.add_modify_event_tv_start_time);

       tv_start_time.setText(event.getBegin_time()>9?getResources().getString(R.string.hour_double,event.getBegin_time()):getResources().getString(R.string.hour_simple,event.getBegin_time()));
        np_durate= findViewById(R.id.add_modify_event_np_durate);
        np_durate.setMinValue(1);
        np_durate.setMaxValue(max_duration);
        np_durate.setValue(event.getEnd_time()-beg);
        event_tiet_title = findViewById(R.id.add_modify_event_tiet_title);
        eventService=new EventService(getApplicationContext());

        if(!event.getTitle().isEmpty()){
            event_tiet_title.setText(event.getTitle());
        }
        event_tiet_details = findViewById(R.id.add_modify_event_tiet_details);
        if(!event.getDetails().isEmpty()){
            event_tiet_details.setText(event.getDetails());
        }
        rg_event_type = findViewById(R.id.add_modify_event_rg_event_type);
        rb_task = findViewById(R.id.add_modify_event_rb_task);
        if(event.getEventType() == EventType.TASK){
            rb_task.setChecked(true);
        }
        btn_save = findViewById(R.id.add_modify_event_btn_save);
        btn_save.setOnClickListener(getSaveListener());
    }

    private View.OnClickListener getSaveListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(isValid()) {
                   if(newEvent) {
                       Event e = createEvent();
                       e.setUser_id(PreferencesSave.getFromPreferences(getApplicationContext()).getUid());
                       eventService.insert(e, insertEventCallback());
                   }else{
                       event.setDetails(event_tiet_details.getText().toString());
                       event.setEventType(rg_event_type.getCheckedRadioButtonId()==R.id.add_modify_event_rb_meeting ? EventType.MEETING:EventType.TASK);
                       event.setTitle(event_tiet_title.getText().toString());
                       event.setEnd_time(event.getBegin_time()+np_durate.getValue());
                       eventService.update(event, updateEventCallback());
                   }
               }
            }
        };
    }

    private Callback<Event> updateEventCallback() {
        return new Callback<Event>() {
            @Override
            public void getInfoOnUiThread(Event result) {
               // Toast.makeText(getApplicationContext(),"UPDATED!"+result.toString(),Toast.LENGTH_LONG).show();
                intent.putExtra(ADD_EVENT_KEY,result);
                setResult(RESULT_OK, intent);
                finish();
            }
        };
    }

    private Callback<Event> insertEventCallback() {
        return new Callback<Event>() {
            @Override
            public void getInfoOnUiThread(Event result) {
                Toast.makeText(getApplicationContext(),"Event added!"+result.toString(),Toast.LENGTH_LONG).show();
                intent.putExtra(ADD_EVENT_KEY,result);
                setResult(RESULT_OK, intent);
                finish();
            }
        };
    }

    private Event createEvent() {
        return new Event(event.getEvent_date(), event.getBegin_time(),event.getBegin_time()+np_durate.getValue()
                ,event_tiet_title.getText().toString(),event_tiet_details.getText().toString()
                ,rg_event_type.getCheckedRadioButtonId()==R.id.add_modify_event_rb_meeting ? EventType.MEETING:EventType.TASK);
    }

    private boolean isValid() {
        if (event_tiet_title.getText() == null || event_tiet_title.getText().toString().trim().length() < 3) {
            Toast.makeText(getApplicationContext(), R.string.invalid_title_minim_3_characters, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

}