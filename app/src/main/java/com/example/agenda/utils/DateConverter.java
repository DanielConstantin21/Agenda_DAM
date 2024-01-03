package com.example.agenda.utils;

import androidx.room.TypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateConverter {
    final static String DATE_FORMAT="dd-MM-yyyy";
    private final static SimpleDateFormat FORMATTER = new SimpleDateFormat(DATE_FORMAT, Locale.US);

    @TypeConverter
    public static Date toDate(String value){
        try {
            return FORMATTER.parse(value);
        } catch (ParseException e) {

            return null;
        }
    }

    @TypeConverter
    public static String fromDate(Date date){
        if(date == null){
            return null;
        }

        return FORMATTER.format(date);
    }

    public static String fromLong(Long longDate){
        if(longDate == null){
            return null;
        }
        Date d=new Date(longDate);
        return fromDate(d);
    }
}