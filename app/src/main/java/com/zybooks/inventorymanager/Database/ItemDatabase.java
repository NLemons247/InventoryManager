package com.zybooks.inventorymanager.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.zybooks.inventorymanager.Model.Item;


@Database(entities = {Item.class}, version = 1)
public abstract class ItemDatabase extends RoomDatabase {
    public abstract ItemDao itemDao();
}
