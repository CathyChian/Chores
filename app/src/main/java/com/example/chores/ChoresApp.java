package com.example.chores;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import com.example.chores.GoogleCalendarClient;
import com.facebook.stetho.Stetho;

public class ChoresApp extends Application {

    public static GoogleCalendarClient getRestClient(Context context) {
        return (GoogleCalendarClient) GoogleCalendarClient.getInstance(GoogleCalendarClient.class, context);
    }
}