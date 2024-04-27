package com.zybooks.inventorymanager.View;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.zybooks.inventorymanager.Database.UserDao;
import com.zybooks.inventorymanager.Model.User;
import com.zybooks.inventorymanager.MyApplication;
import com.zybooks.inventorymanager.R;

public class NewUserActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_SMS = 1;

    private EditText userNameEditText, passwordEditText;
    private Button saveButton;
    private UserDao userDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        userNameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        saveButton = findViewById(R.id.save_button);
        userDao = MyApplication.getUserDatabase().userDao();

        saveButton.setOnClickListener(view -> {
            String username = userNameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username or password is empty",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (ContextCompat.checkSelfPermission(NewUserActivity.this,
                    Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted, request it from the user
                ActivityCompat.requestPermissions(NewUserActivity.this,
                        new String[]{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_SMS);
            } else {
                // Permission is already granted, prompt for phone number
                promptForPhoneNumber();
            }
        });

        // Enable the up button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setTitle(R.string.new_user_activity_label);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, prompt for phone number
                promptForPhoneNumber();
            } else {
                // Permission denied, show a message
                Toast.makeText(this, "SMS permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void promptForPhoneNumber() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Phone Number");
        builder.setMessage("Please enter your phone number:");
        final EditText input = new EditText(this);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String username = userNameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String phoneNumber = input.getText().toString();

                User user = new User();
                user.setUsername(username);
                user.setPassword(password);
                user.setPhoneNumber(phoneNumber);

                new InsertUserAsyncTask().execute(user);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private class InsertUserAsyncTask extends AsyncTask<User, Void, Boolean> {
        @Override
        protected Boolean doInBackground(User... users) {
            try {
                userDao.insert(users[0]);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean isInserted) {
            if (isInserted) {
                Toast.makeText(NewUserActivity.this, "User saved successfully",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(NewUserActivity.this,
                        LoginScreenActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(NewUserActivity.this, "Username already exists",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
