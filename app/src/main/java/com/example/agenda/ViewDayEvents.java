package com.example.agenda;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agenda.Database.EventService;
import com.example.agenda.Network.Callback;
import com.example.agenda.model.DayHours;
import com.example.agenda.model.Event;
import com.example.agenda.model.User;
import com.example.agenda.utils.DateConverter;
import com.example.agenda.utils.EventAdapter;
import com.example.agenda.utils.PreferencesSave;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ViewDayEvents extends AppCompatActivity {
    private TextView selectedDate;
    private ListView lv_dayEventsList;
    private Button btnAdd;
    private Button btnDelete;
    private Intent intent;
    //private Event event;
    int selectedPosition;
    private ActivityResultLauncher<Intent> launcher;
    private String data;
    private User user;
    private EventService eventService;
    ArrayList<DayHours> dayHoursList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_day_events);

       initComponents();
    }

    private void initComponents(){
            ActivityResultCallback<ActivityResult> callback = getEventCallback();
            launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    callback);
            intent = getIntent();
            data= intent.getStringExtra(AddModifyEvent.EVENT_DATE);
            selectedDate = findViewById(R.id.view_day_events_tv_date);
            selectedDate.setText(getResources().getString(R.string.events_on, data));
            btnAdd = findViewById(R.id.view_day_events_btn_add);
            btnAdd.setOnClickListener(getAddListener());
            btnDelete = findViewById(R.id.view_day_events_btn_delete);
            btnDelete.setOnClickListener(getDeleteListener());
            lv_dayEventsList= findViewById(R.id.view_day_events_lv_day_event_list);
            user = PreferencesSave.getFromPreferences(getApplicationContext());
            eventService=new EventService(getApplicationContext());
            eventService.getDateEvents(user.getUid(),data, getDataEventsCallback());
    }

    private View.OnClickListener getDeleteListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event e = ((DayHours)lv_dayEventsList.getItemAtPosition(selectedPosition)).getEvent();
                if(e != null) {
                    eventService.delete(e, deleteCallback());
                }
            }
        };
    }

    private Callback<Event> deleteCallback() {
        return new Callback<Event>() {
            @Override
            public void getInfoOnUiThread(Event result) {
                if(result !=null){
                    for(int i=result.getBegin_time();i<result.getEnd_time();i++){
                        ((DayHours)lv_dayEventsList.getItemAtPosition(i)).setEvent(null);
                    }

                    ArrayAdapter<DayHours> adapter
                            = (ArrayAdapter<DayHours>)
                            lv_dayEventsList.getAdapter();
                    adapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(getApplicationContext(),"Failed to delete event",Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    private Callback<List<Event>> getDataEventsCallback() {
        return new Callback<List<Event>>() {
            @Override
            public void getInfoOnUiThread(List<Event> result) {
                dayHoursList = DayHours.getHourList(data, getApplicationContext(),result);
                EventAdapter adapter = new EventAdapter(getApplicationContext(),R.layout.lv_row_view,dayHoursList,getLayoutInflater());
                lv_dayEventsList.setSelector(R.color.colorAccentLight);
                lv_dayEventsList.setAdapter(adapter);
                lv_dayEventsList.setOnItemClickListener(getListItemClickedListener());
            }
        };
    }

    private View.OnClickListener getAddListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Event event = ((DayHours)lv_dayEventsList.getItemAtPosition(selectedPosition)).getEvent();
                intent = new Intent(getApplicationContext(), AddModifyEvent.class);
                if (event != null) {
                    AddModifyEvent.newEvent = false;
                }else{
                    Date d= DateConverter.toDate(data);
                    event = new Event(d,selectedPosition);
                    AddModifyEvent.newEvent = true;
                }
                intent.putExtra(AddModifyEvent.ADD_EVENT_KEY, event);
                int maxDuration = getMaxDuration(event);
                intent.putExtra(AddModifyEvent.MAX_DURATION,maxDuration);
                launcher.launch(intent);
            }
        };
    }

    private int getMaxDuration(Event e) {
        int result =1;
        for(int i=e.getBegin_time()+1;i<dayHoursList.size();i++){
            if(dayHoursList.get(i).getEvent()==null || dayHoursList.get(i).getEvent().equals(e)){
                result +=1;
            }else{
                return result;
            }
        }
        return result;
    }

    private ActivityResultCallback<ActivityResult> getEventCallback() {
        return new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Event event = result.getData()
                            .getParcelableExtra(
                                    AddModifyEvent.ADD_EVENT_KEY);

                    int durata = event.getEnd_time()-event.getBegin_time();
                    int pos = event.getBegin_time();
                    int max = getMaxDuration(event);

                    for(int i=pos;i<pos+max;i++){
                        if(durata>0){
                            dayHoursList.get(i).setEvent(event);
                            durata--;
                        }else{
                            if(dayHoursList.get(i).getEvent()!=null){
                                dayHoursList.get(i).setEvent(null);
                            }else{
                               break;
                            }
                        }
                    }
                    ArrayAdapter<DayHours> adapter
                            = (ArrayAdapter<DayHours>)
                            lv_dayEventsList.getAdapter();
                    adapter.notifyDataSetChanged();
                }
            }
        };
    }

    private AdapterView.OnItemClickListener getListItemClickedListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPosition = position;
                view.setSelected(true);
                DayHours dayHours = (DayHours) parent.getItemAtPosition(position);
                if(dayHours.getEvent() != null){
                    btnAdd.setText(R.string.btn_edit_event_text);
                }else{
                    btnAdd.setText(R.string.btn_add_event_text);
                }
            }
        };
    }

}