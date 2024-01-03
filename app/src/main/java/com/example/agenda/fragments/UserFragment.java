package com.example.agenda.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.agenda.LogInActivity;
import com.example.agenda.R;
import com.example.agenda.model.User;
import com.example.agenda.utils.PreferencesSave;
import com.google.firebase.auth.FirebaseAuth;


public class UserFragment extends Fragment {
    public static final String LOGIN_PREF_FILE ="login";
    private Button btnLogin;
    private TextView tvUser;
    FirebaseAuth auth;
    private SharedPreferences preferences;
    private User user;
    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        preferences = getActivity().getSharedPreferences(LOGIN_PREF_FILE, Context.MODE_PRIVATE);
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        initComponents(view);
        return view;
    }

    private void initComponents(View view) {
        user = PreferencesSave.getFromPreferences(getContext().getApplicationContext());
        btnLogin = view.findViewById(R.id.frg_user_btn_login);
        tvUser = view.findViewById(R.id.frg_user_tv_user_name);
        tvUser.setText(user.getEmail());
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth = FirebaseAuth.getInstance();
                auth.signOut();
                preferences.edit().clear().apply();
                Intent intent = new Intent(getContext().getApplicationContext(), LogInActivity.class);
                startActivity(intent);
            }
        });
    }
}