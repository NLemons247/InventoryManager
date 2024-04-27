package com.zybooks.inventorymanager;

import android.app.Application;

import androidx.room.Room;

import com.zybooks.inventorymanager.Database.ItemDatabase;
import com.zybooks.inventorymanager.Database.UserDatabase;

public class MyApplication extends Application {
    private static ItemDatabase itemDatabase;
    private static UserDatabase userDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        //Init Room Databases
        itemDatabase = Room.databaseBuilder(getApplicationContext(),
                ItemDatabase.class, "item-db").build();

        userDatabase = Room.databaseBuilder(getApplicationContext(),
                UserDatabase.class, "user-db").build();
    }

    public static ItemDatabase getItemDatabase() {return itemDatabase;}

    public static UserDatabase getUserDatabase() {
        return userDatabase;
    }

}
