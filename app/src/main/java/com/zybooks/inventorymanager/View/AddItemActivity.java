package com.zybooks.inventorymanager.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.zybooks.inventorymanager.Database.ItemDao;
import com.zybooks.inventorymanager.Model.Item;
import com.zybooks.inventorymanager.MyApplication;
import com.zybooks.inventorymanager.R;

public class AddItemActivity extends AppCompatActivity {

    private Button saveButton;
    private EditText nameEditText, quantityEditText, descriptionEditText, locationEditText;

    private ItemDao itemDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        saveButton = findViewById(R.id.save_button);
        nameEditText = findViewById(R.id.item_name_edit_text);
        quantityEditText = findViewById(R.id.item_quantity_edit_text);
        descriptionEditText = findViewById(R.id.item_description_edit_text);
        locationEditText = findViewById(R.id.item_location_edit_text);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        itemDao = MyApplication.getItemDatabase().itemDao();

        saveButton.setOnClickListener(view -> {
            saveItem();
        });

        setTitle(R.string.edit_item_activity_label);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void saveItem() {
        String name = nameEditText.getText().toString();
        String quantityText = quantityEditText.getText().toString();
        int quantity = Integer.parseInt(quantityText);
        String description = descriptionEditText.getText().toString();
        String location = locationEditText.getText().toString();

        String userId = getUserIdFromSharedPreferences();

        if (name.length() > 15) {
            Toast.makeText(this, "Name too long, 15 character limit",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Item newItem = new Item();
        newItem.setName(name);
        newItem.setQuantity(quantity);
        newItem.setDescription(description);
        newItem.setLocation(location);

        newItem.setUserId(userId);

        new InsertItemAsyncTask().execute(newItem);
    }

    private class InsertItemAsyncTask extends AsyncTask<Item, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Item... items) {
            try {
                itemDao.insert(items[0]);
                return true;
            }
            catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean isInserted) {
            if (isInserted) {
                Toast.makeText(AddItemActivity.this, "Item Saved",
                                Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddItemActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                Toast.makeText(AddItemActivity.this, "Item Not Saved",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getUserIdFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(
                "user_pref", Context.MODE_PRIVATE);
        return sharedPreferences.getString("userId", "");
    }
}