package com.example.chores.models;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChoreObject {

    private static final String TAG = "ChoreObject";

    public static Calendar reduceDateToDay(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    public static void addUser(ParseObject object, JSONArray jsonArray, final String KEY, String username) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", username);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e){
                if (e != null || users.size() == 0) {
                    Log.e(TAG, "Issue with finding user", e);
                    return;
                }
                Log.i(TAG, "User objectId: " + users.get(0).getObjectId());

                addUser(object, jsonArray, KEY, username, users.get(0).getObjectId());
            }
        });
    }

    public static void addUser(ParseObject object, JSONArray jsonArray, final String KEY, String username, String objectId) {
        if (jsonArray == null) {
            jsonArray = new JSONArray();
        }
        JSONObject user = new JSONObject();
        try {
            user.put("username", username);
            user.put("objectId", objectId);
        } catch (JSONException e) {
            Log.e(TAG, "Json exception: " + e, e);
        }
        jsonArray.put(user);
        object.put(KEY, jsonArray);
        object.saveInBackground();
    }

    public static List<String> getUsernames(JSONArray jsonArray) {
        List<String> list = new ArrayList<String>();

        if (jsonArray == null) {
            return list;
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                list.add(jsonArray.getJSONObject(i).getString("username"));
            } catch (JSONException e) {
                Log.e(TAG, "Json exception: " + e, e);
            }
        }
        return list;
    }

    public static String getListOfUsers(List<String> list) {
        if (list.size() == 1) {
            return list.get(0);
        }
        if (list.size() == 2) {
            return list.get(0) + " and " + list.get(1);
        }

        String text = "";
        for (int i = 0; i < list.size() - 1; i++) {
            text += list.get(i) + ", ";
        }
        return (text += "and " + list.get(list.size() - 1));
    }
}
