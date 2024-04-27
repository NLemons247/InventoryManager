package com.zybooks.inventorymanager.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zybooks.inventorymanager.Adapter.ItemAdapter;
import com.zybooks.inventorymanager.Database.ItemDatabase;
import com.zybooks.inventorymanager.Database.ItemDao;
import com.zybooks.inventorymanager.Model.Item;
import com.zybooks.inventorymanager.MyApplication;
import com.zybooks.inventorymanager.R;
import com.zybooks.inventorymanager.Repository.ItemRepository;

import java.util.List;

public class HomeFragment extends Fragment implements ItemAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private ItemRepository itemRepository;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.main_screen_list);
        adapter = new ItemAdapter();

        // Set click listeners on the adapter
        adapter.setOnItemClickListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        ((MainActivity) requireActivity()).setActionBarTitle(getString(R.string.home));

        //RecyclerView divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                requireActivity(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(requireContext(),
                R.drawable.recycler_divider));
        recyclerView.addItemDecoration(dividerItemDecoration);

        String username = getUserIdFromSharedPreferences();

        // Initialize ItemDatabase and ItemDao
        ItemDatabase itemDatabase = MyApplication.getItemDatabase();
        ItemDao itemDao = itemDatabase.itemDao();

        // Initialize ItemRepo
        itemRepository = new ItemRepository(itemDao);

        // Observe LiveData from the repo and update the RecyclerView
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
    public void onItemClick(int position) {
        Item item = adapter.getItem(position);
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
