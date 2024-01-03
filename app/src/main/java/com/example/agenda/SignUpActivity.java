package com.example.agenda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agenda.Database.UserService;
import com.example.agenda.Network.Callback;
import com.example.agenda.model.User;
import com.example.agenda.utils.PreferencesSave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    // Declare any other necessary variables.
    private FirebaseAuth auth;
    private EditText signupEmail, signupPassword;
    private Button signupButton;
    private TextView loginRedirectText;
    private  UserService userService;
    public static final String LOGIN_PREF_FILE ="login";
    private SharedPreferences preferences;
  //  private boolean isOpen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        preferences = getApplicationContext().getSharedPreferences(LOGIN_PREF_FILE, Context.MODE_PRIVATE);
        //Initialize the FirebaseAuth instance in the onCreate()
        auth = FirebaseAuth.getInstance();
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        userService = new UserService(getApplicationContext());
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = signupEmail.getText().toString().trim();
                String pass = signupPassword.getText().toString().trim();

                if (user.isEmpty()){
                    signupEmail.setError(getString(R.string.empty_email));
                }
                if(pass.isEmpty()){
                    signupPassword.setError(getString(R.string.empty_password));
                } else{
                    auth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                String id = task.getResult().getUser().getUid();
                                User user = new User(id, task.getResult().getUser().getEmail());
                                userService.insert(user, getInsertCallback());
                                Toast.makeText(SignUpActivity.this, R.string.signup_successfully, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUpActivity.this, LogInActivity.class));
                            } else{
                                Toast.makeText(SignUpActivity.this, getString(R.string.signup_failed) + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LogInActivity.class));
            }
        });

    }

    private Callback<User> getInsertCallback() {
        return new Callback<User>() {
            @Override
            public void getInfoOnUiThread(User result) {
                if(result != null) {
                    startActivity(new Intent(SignUpActivity.this, LogInActivity.class));
                  //  PreferencesSave.saveToPreferences(result, getApplicationContext());
                }
            }
        };
    }

}