package com.zybooks.inventorymanager.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.zybooks.inventorymanager.Model.Item;

import java.util.List;

@Dao
public interface ItemDao {
    @Insert
    void insert(Item item);

    @Query("SELECT * FROM items WHERE userId = :userId")
    LiveData<List<Item>> getAllItems(String userId);

    @Query("SELECT * FROM items WHERE id = :itemId")
    Item getItemByIdSync(int itemId);

    @Query("SELECT * FROM items WHERE name = :name AND userId = :userId")
    Item getItemByNameSync(String name, String userId);

    @Query("SELECT * FROM items WHERE userId = :userId AND quantity = 0")
    LiveData<List<Item>> getItemsWithZeroQuantity(String userId);

    @Update
    void updateItem(Item item);

    @Delete
    void deleteItem(Item item);

}
