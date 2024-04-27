package com.zybooks.inventorymanager.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.zybooks.inventorymanager.Model.Item;
import com.zybooks.inventorymanager.MyApplication;
import com.zybooks.inventorymanager.R;


public class ItemResultActivity extends AppCompatActivity
        implements EditItemFragment.OnItemSavedListener {

    private EditItemFragment editItemFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_result);

        toggleViews(true);

        Intent intent = getIntent();
        if (intent != null) {

            final int itemId = intent.getIntExtra("itemId", 0);
            String itemName = intent.getStringExtra("itemName");
            int itemQuantity = intent.getIntExtra("itemQuantity", 0);
            String itemLocation = intent.getStringExtra("itemLocation");
            String itemDescription = intent.getStringExtra("itemDescription");
            String userId = intent.getStringExtra("userId");

            SharedPreferences sharedPreferences = this.getSharedPreferences("user_pref",
                    Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("userId", "");


            TextView itemNameTextView = findViewById(R.id.item_name);
            TextView itemQuantityTextView = findViewById(R.id.item_quantity);
            TextView itemLocationTextView = findViewById(R.id.item_location);
            TextView itemDescriptionTextView = findViewById(R.id.item_description);

            itemNameTextView.setText(itemName);
            itemQuantityTextView.setText(String.valueOf(itemQuantity));
            itemLocationTextView.setText(itemLocation);
            itemDescriptionTextView.setText(itemDescription);

            editItemFragment = EditItemFragment.newInstance(itemId, itemName, itemQuantity,
                    itemLocation, itemDescription, userId, username);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_item_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        } else if (id == R.id.delete) {
            deleteItem();
            return true;
        } else if (id == R.id.edit) {
            toggleViews(false);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.editScreenFragmentContainer, editItemFragment)
                    .commit();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void deleteItem() {
        Intent intent = getIntent();
        if (intent != null) {
            final int itemId = intent.getIntExtra("itemId", 0);

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    Item item = MyApplication.getItemDatabase().itemDao().getItemByIdSync(itemId);

                    // Check if item is not null
                    if (item != null) {
                        // Delete the item from the database
                        MyApplication.getItemDatabase().itemDao().deleteItem(item);
                    }
                }
            });
            finish();
        }
    }

    private void toggleViews(boolean showViews) {
        findViewById(R.id.item_name).setVisibility(showViews ? View.VISIBLE : View.GONE);
        findViewById(R.id.item_quantity).setVisibility(showViews ? View.VISIBLE : View.GONE);
        findViewById(R.id.item_location).setVisibility(showViews ? View.VISIBLE : View.GONE);
        findViewById(R.id.item_description).setVisibility(showViews ? View.VISIBLE : View.GONE);

        findViewById(R.id.editScreenFragmentContainer)
                .setVisibility(showViews ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onItemSaved(Item updatedItem) {
        // Update TextViews with the new data
        TextView itemNameTextView = findViewById(R.id.item_name);
        TextView itemQuantityTextView = findViewById(R.id.item_quantity);
        TextView itemLocationTextView = findViewById(R.id.item_location);
        TextView itemDescriptionTextView = findViewById(R.id.item_description);

        itemNameTextView.setText(updatedItem.getName());
        itemQuantityTextView.setText(String.valueOf(updatedItem.getQuantity()));
        itemLocationTextView.setText(updatedItem.getLocation());
        itemDescriptionTextView.setText(updatedItem.getDescription());

        // Show the original views
        toggleViews(true);
    }
}