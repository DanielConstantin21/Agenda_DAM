package com.example.agenda.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.agenda.model.User;

public class PreferencesSave {
    public static final String LOGIN_PREF_FILE ="login";
    private static SharedPreferences preferences;




    public static void saveToPreferences(User user, Context context){
        preferences = context.getSharedPreferences(LOGIN_PREF_FILE, Context.MODE_PRIVATE);
        preferences.edit().clear().apply();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("USER_ID", user.getUid());
        editor.putString("USER_EMAIL",user.getEmail());
        editor.apply();
    }
    public static User getFromPreferences(Context context){
        preferences = context.getSharedPreferences(LOGIN_PREF_FILE, Context.MODE_PRIVATE);
        String user_id = preferences.getString("USER_ID","");
        String user_email = preferences.getString("USER_EMAIL","");
        User user = new User(user_id,user_email);
        return user;
    }
}
