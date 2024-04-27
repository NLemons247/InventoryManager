package com.zybooks.inventorymanager.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;


@Entity(tableName="users", indices = {@Index(value = {"username"}, unique = true)})
public class User {
    @PrimaryKey
    @NonNull
    private String username;

    @NonNull
    @ColumnInfo(name="password")
    private String password;

    @ColumnInfo(name="phoneNumber")
    private String phoneNumber;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() { return username; }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}


