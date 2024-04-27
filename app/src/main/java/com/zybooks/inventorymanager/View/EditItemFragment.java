package com.zybooks.inventorymanager.View;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zybooks.inventorymanager.Database.UserDao;
import com.zybooks.inventorymanager.Model.Item;
import com.zybooks.inventorymanager.MyApplication;
import com.zybooks.inventorymanager.R;
import com.zybooks.inventorymanager.Repository.ItemRepository;


public class EditItemFragment extends Fragment {

    private ItemRepository itemRepository;

    private EditText itemNameEditText;
    private EditText itemQuantitiyEditText;
    private EditText itemLocationEditText;
    private EditText itemDescriptionEditText;

    private Button saveButton;

    public EditItemFragment() {
        //Required empty public constructor
    }

    public interface OnItemSavedListener {
        void onItemSaved(Item updatedItem);
    }

    public static EditItemFragment newInstance(int itemId, String name, int quantity,
                                               String location, String description, String userId,
                                               String username) {
        EditItemFragment fragment = new EditItemFragment();
        Bundle args = new Bundle();
        args.putInt("itemId", itemId);
        args.putString("name", name);
        args.putInt("quantity", quantity);
        args.putString("location", location);
        args.putString("description", description);
        args.putString("userId", userId);
        args.putString("username", username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_item, container, false);

        itemNameEditText = view.findViewById(R.id.item_name_edit_text);
        itemQuantitiyEditText = view.findViewById(R.id.item_quantity_edit_text);
        itemLocationEditText = view.findViewById(R.id.item_location_edit_text);
        itemDescriptionEditText = view.findViewById(R.id.item_description_edit_text);

        saveButton = view.findViewById(R.id.save_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItem();
            }
        });

        Bundle args = getArguments();
        if (args != null) {
            String name = args.getString("name");
            int quantity = args.getInt("quantity");
            String location = args.getString("location");
            String description = args.getString("description");
            setItemDetails(name, quantity, location, description);
        }

        return view;
    }

    public void setItemDetails(String name, int quantity, String location, String description) {
        itemNameEditText.setText(name);
        itemQuantitiyEditText.setText(String.valueOf(quantity));
        itemLocationEditText.setText(location);
        itemDescriptionEditText.setText(description);
    }

    private void saveItem() {
        String name = itemNameEditText.getText().toString().trim();
        int quantity = Integer.parseInt(itemQuantitiyEditText.getText().toString().trim());
        String location = itemLocationEditText.getText().toString().trim();
        String description = itemDescriptionEditText.getText().toString().trim();

        UserDao userDao = MyApplication.getUserDatabase().userDao();

        if (name.length() > 15) {
            Toast.makeText(getActivity(), "Name too long, 15 character limit",
                    Toast.LENGTH_SHORT).show();
        }

        // Retrieve the item ID from arguments
        Bundle args = getArguments();
        int itemId = -1;
        String userId = "";
        String username = "";

        if (args != null) {
            itemId = args.getInt("itemId", -1);
            userId = args.getString("userId", "");
            username = args.getString("username", "");
        }

        // Create an instance of the Item class with the updated details
        final Item updatedItem = new Item();
        updatedItem.setId(itemId);
        updatedItem.setName(name);
        updatedItem.setQuantity(quantity);
        updatedItem.setLocation(location);
        updatedItem.setDescription(description);
        updatedItem.setUserId(userId);

        // Retrieve the user's phone number asynchronously
        userDao.getUserPhoneNumber(username).observe(getViewLifecycleOwner(),
                new Observer<String>() {
            @Override
            public void onChanged(@Nullable String phoneNumber) {
                // Check if the phone number is null
                if (phoneNumber != null) {
                    // Update the item asynchronously
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            ItemRepository itemRepository = new ItemRepository(MyApplication
                                    .getItemDatabase().itemDao());
                            itemRepository.updateItem(updatedItem);

                            if (quantity == 0) {
                                String message = "Your item " + name + " has reached zero quantity.";
                                itemRepository.sendTextMessage(phoneNumber, message);
                            }

                            // Display a toast message the main UI thread
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), "Item updated successfully",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });

                            // Notify the listener that the item is saved
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (getActivity() instanceof OnItemSavedListener) {
                                        ((OnItemSavedListener) getActivity())
                                                .onItemSaved(updatedItem);
                                    }
                                }
                            });
                        }
                    });
                } else {
                    // Update the item without sending a text message or notifying the user
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            ItemRepository itemRepository = new ItemRepository(MyApplication
                                    .getItemDatabase().itemDao());
                            itemRepository.updateItem(updatedItem);

                            // Display a toast message
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), "Item updated successfully",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });

                            // Notify the listener that the item is saved
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (getActivity() instanceof OnItemSavedListener) {
                                        ((OnItemSavedListener) getActivity())
                                                .onItemSaved(updatedItem);
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });
    }
}