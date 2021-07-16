package com.example.chores.models;

import com.parse.ParseClassName;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.parceler.Parcel;

import java.util.List;

@Parcel(analyze = User.class)
@ParseClassName("_User")
public class User extends ParseUser {

    public User() {
    }

    public JSONArray getRoommates() {
        return getJSONArray("roommates");
    }

    public void setRoommates(JSONArray roommates) {
        put("roommates", roommates);
    }

    public String getListOfUsers() {
        List<String> list = ChoreObject.getUsernames(getRoommates());
        if (list.size() == 0) {
            return "";
        }
        return "Roommates with: " + ChoreObject.getListOfUsers(list);
    }

    public void addUser(String username) {
        ChoreObject.addUser(this, getRoommates(), "roommates", username);
    }
}
