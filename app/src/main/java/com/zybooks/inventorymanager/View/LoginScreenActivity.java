package com.zybooks.inventorymanager.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.zybooks.inventorymanager.Database.UserDao;
import com.zybooks.inventorymanager.Database.UserDatabase;
import com.zybooks.inventorymanager.MyApplication;
import com.zybooks.inventorymanager.R;

public class LoginScreenActivity extends AppCompatActivity {

    private EditText userNameEditText, passwordEditText;
    private TextView newUserTextView;
    private Button loginButton;
    private UserDao userDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        UserDatabase userDatabase = MyApplication.getUserDatabase();
        userDao = userDatabase.userDao();

        userNameEditText = findViewById(R.id.username_EditText);
        passwordEditText = findViewById(R.id.password_editText);
        newUserTextView = findViewById(R.id.newUser_static);
        loginButton = findViewById(R.id.login_button);

        newUserTextView.setOnClickListener(view -> {
            Intent intent = new Intent(LoginScreenActivity.this, NewUserActivity.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(view -> {
            loginUser();
        });


    }

    @Override
    protected void onPause() {
        super.onPause();

        userNameEditText.setText("");
        passwordEditText.setText("");
    }

    private void loginUser() {
        String username = userNameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginScreenActivity.this, "Please enter both username " +
                    "and password", Toast.LENGTH_SHORT).show();
        }
        else {
            userDao.getUserLiveData(username, password).observe(this, user -> {
                if (user != null) {
                    Intent intent = new Intent(LoginScreenActivity.this,
                            MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginScreenActivity.this,
                            "Invalid username or password",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

        saveUserIdToSharedPreferences(username);
    }

    private void saveUserIdToSharedPreferences(String userId) {
        SharedPreferences sharedPreferences = getSharedPreferences(
                "user_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userId", userId);
        editor.apply();
    }
}