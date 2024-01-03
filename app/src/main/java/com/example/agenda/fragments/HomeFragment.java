package com.example.agenda.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.agenda.AddModifyEvent;
import com.example.agenda.R;
import com.example.agenda.ViewDayEvents;
import com.example.agenda.utils.DateConverter;
import com.google.android.material.navigation.NavigationView;

import java.util.Date;


public class HomeFragment extends Fragment {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private CalendarView calendar;
    private TextView currentDate;
    private Button btnViewEvents;
  //  private ArrayList<Calendar> calendars = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_home, container, false);
        initComponents(view);
        return view;
    }
    private void initComponents(View view) {


        calendar = view.findViewById(R.id.home_cv_calendar);
        calendar.setFirstDayOfWeek(java.util.Calendar.MONDAY);
        btnViewEvents = view.findViewById(R.id.home_btn_view_events);
        btnViewEvents.setOnClickListener(getViewEventsOnClickListner());
        currentDate = view.findViewById(R.id.home_tv_current_day);
        Date dataCurenta = new Date(calendar.getDate());
        currentDate.setText(getString(R.string.main_current_date_value, DateConverter.fromDate(dataCurenta)));
        calendar.setOnDateChangeListener(getDateChangeListener());
    }
    private View.OnClickListener getViewEventsOnClickListner() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getContext()!=null) {
                    Intent intent = new Intent(getContext().getApplicationContext(), ViewDayEvents.class);
                    intent.putExtra(AddModifyEvent.EVENT_DATE, DateConverter.fromLong(calendar.getDate()));
                    startActivity(intent);
                }
            }
        };
    }

    private CalendarView.OnDateChangeListener getDateChangeListener() {
        return new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Date dataCurenta = DateConverter.toDate(getString(R.string.date_format,
                        dayOfMonth,(month+1),year));
                calendar.setDate(dataCurenta.getTime());
                currentDate.setText(getString(R.string.main_current_date_value,DateConverter.fromDate(dataCurenta)));
            }
        };

    }
}