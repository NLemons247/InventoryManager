package com.zybooks.inventorymanager.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.zybooks.inventorymanager.Model.User;
import com.zybooks.inventorymanager.Database.UserDao;

@Database(entities = {User.class}, version = 1)
public abstract class UserDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}
