package com.example.agenda.Network;

public interface Callback<R>{

     void getInfoOnUiThread(R result);
}
