package com.zybooks.inventorymanager.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.zybooks.inventorymanager.Model.User;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    LiveData<User> getUserLiveData(String username, String password);

    @Query("SELECT phoneNumber FROM users WHERE username = :username")
    LiveData<String> getUserPhoneNumber(String username);
}
