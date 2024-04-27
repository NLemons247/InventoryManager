package com.zybooks.inventorymanager.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zybooks.inventorymanager.Database.ItemDao;
import com.zybooks.inventorymanager.Model.Item;
import com.zybooks.inventorymanager.MyApplication;
import com.zybooks.inventorymanager.R;

public class SearchFragment extends Fragment {

    private EditText itemNameEditText;
    private Button searchButton;
    private ItemDao itemDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) requireActivity()).setActionBarTitle(getString(R.string.search));

        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        itemNameEditText = rootView.findViewById(R.id.item_search_name);
        searchButton = rootView.findViewById(R.id.search_button_search);
        itemDao = MyApplication.getItemDatabase().itemDao();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = itemNameEditText.getText().toString().trim();
                if (!itemName.isEmpty()) {
                    searchItemByName(itemName);
                }
            }
        });

        return rootView;
    }

    private void searchItemByName(String itemName) {
        String username = getUserIdFromSharedPreferences();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Item item = itemDao.getItemByNameSync(itemName, username);
                    if (item != null) {
                        Intent intent = new Intent(requireContext(), ItemResultActivity.class);
                        intent.putExtra("itemId", item.getId());
                        intent.putExtra("itemName", item.getName());
                        intent.putExtra("itemQuantity", item.getQuantity());
                        intent.putExtra("itemLocation", item.getLocation());
                        intent.putExtra("itemDescription", item.getDescription());
                        intent.putExtra("userId", item.getUserId());
                        startActivity(intent);
                    } else {
                        // Item not found
                        showItemNotFoundMessage();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    // Display an error message to the user
                    showErrorToast();
                }
            }
        });
    }

    private void showItemNotFoundMessage() {
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(requireContext(), "Sorry, that item does not exist",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showErrorToast() {
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(requireContext(),
                        "An error occurred while searching for the item",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getUserIdFromSharedPreferences() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(
                "user_pref", Context.MODE_PRIVATE);
        return sharedPreferences.getString("userId", "");
    }
}