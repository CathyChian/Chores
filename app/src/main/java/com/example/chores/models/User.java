package com.example.chores.models;

import android.content.Context;

import com.parse.ParseClassName;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.parceler.Parcel;

import java.util.List;

@Parcel(analyze = User.class)
@ParseClassName("_User")
public class User extends ParseUser {

    private static final String TAG = "User";

    public User() {
    }

    public String getEmail() {
        return getString("email");
    }

    public void setEmail(String email) {
        put("email", email);
    }

    public JSONArray getRoommates() {
        return getJSONArray("roommates");
    }

    public void setRoommates(JSONArray roommates) {
        put("roommates", roommates);
    }

    public String getCalendarId() {
        return getString("calendarId");
    }

    public void setCalendarId(String calendarId) {
        put("calendarId", calendarId);
    }

    public String getListOfUsers() {
        List<String> list = ChoreObject.getUsernames(getRoommates());
        if (list == null || list.isEmpty()) {
            return "";
        }
        return "Roommates with: " + ChoreObject.getListOfUsers(list);
    }

    public void addUser(String username, Context context) {
        ChoreObject.addUser(context, this, getRoommates(), "roommates", username);
    }
}
