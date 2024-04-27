package com.zybooks.inventorymanager.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.zybooks.inventorymanager.Adapter.GridItemAdapter;
import com.zybooks.inventorymanager.Model.Item;
import com.zybooks.inventorymanager.MyApplication;
import com.zybooks.inventorymanager.R;
import com.zybooks.inventorymanager.Repository.ItemRepository;

import java.util.ArrayList;
import java.util.List;

public class GridFragment extends Fragment implements GridItemAdapter.OnItemClickListener{

    private GridView gridView;
    private GridItemAdapter adapter;
    private ItemRepository itemRepository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Set Action Bar Title
        ((MainActivity) requireActivity()).setActionBarTitle(getString(R.string.grid));

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_grid, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gridView = view.findViewById(R.id.stock_list_grid);
        adapter = new GridItemAdapter(requireContext(), new ArrayList<>());
        gridView.setAdapter(adapter);

        adapter.setOnItemClickListener(this);

        // Initialize ItemRepository
        itemRepository = new ItemRepository(MyApplication.getItemDatabase().itemDao());

        // Fetch items from the database
        fetchItems();
    }

    private void fetchItems() {
        String username = getUserIdFromSharedPreferences();
        itemRepository.getAllItems(username).observe(getViewLifecycleOwner(),
                new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                adapter.setItems(items);
            }
        });
    }

    private String getUserIdFromSharedPreferences() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(
                "user_pref", Context.MODE_PRIVATE);
        return sharedPreferences.getString("userId", "");
    }

    @Override
    public void onItemClick(Item item) {
        Intent intent = new Intent(requireContext(), ItemResultActivity.class);
        intent.putExtra("itemId", item.getId());
        intent.putExtra("itemName", item.getName());
        intent.putExtra("itemQuantity", item.getQuantity());
        intent.putExtra("itemLocation", item.getLocation());
        intent.putExtra("itemDescription", item.getDescription());
        intent.putExtra("userId", item.getUserId());
        startActivity(intent);
    }
}