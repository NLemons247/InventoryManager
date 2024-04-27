package com.zybooks.inventorymanager.Repository;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Looper;
import android.telephony.SmsManager;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.zybooks.inventorymanager.Database.ItemDao;
import com.zybooks.inventorymanager.Model.Item;

import java.util.List;
import java.util.logging.Handler;


public class ItemRepository {
    private ItemDao itemDao;

    public ItemRepository(ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    public LiveData<List<Item>> getAllItems(String userId) {
        return itemDao.getAllItems(userId);
    }

    public void updateItem(Item item) {
        itemDao.updateItem(item);
    }

    public void sendTextMessage(String phoneNumber, String message) {
        //TODO: Add code to send a text message
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
