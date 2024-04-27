package com.zybooks.inventorymanager.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zybooks.inventorymanager.R;


public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bottomNavigationView = findViewById(R.id.bottom_nav_bar);
        floatingActionButton = findViewById(R.id.floating_action_button);

        floatingActionButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
            startActivity(intent);
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainScreenFragmentContainer, new HomeFragment())
                    .commit();
        }

        bottomNavigationView.setOnItemSelectedListener(
                new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int menuItemId = menuItem.getItemId();

                if (menuItemId == R.id.bottom_home) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.mainScreenFragmentContainer, new HomeFragment())
                            .commit();
                    setFloatingActionButtonVisibility(true);
                }
                else if (menuItemId == R.id.bottom_search) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.mainScreenFragmentContainer, new SearchFragment())
                            .commit();
                    setFloatingActionButtonVisibility(false);
                }
                else if (menuItemId == R.id.bottom_grid_view) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.mainScreenFragmentContainer, new GridFragment())
                            .commit();
                    setFloatingActionButtonVisibility(true);
                }
                return true;
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_screen_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            showLogoutConfirmationDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logout();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void logout() {
        Intent intent = new Intent(this, LoginScreenActivity.class);
        startActivity(intent);
        finish();
    }

    public void setActionBarTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    public void setFloatingActionButtonVisibility(boolean visible) {
        if (visible) {
            floatingActionButton.setVisibility(View.VISIBLE);
        }
        else {
            floatingActionButton.setVisibility(View.GONE);
        }
    }


}