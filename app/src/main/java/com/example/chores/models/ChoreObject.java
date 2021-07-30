package com.example.chores.models;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
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

    public static void addUser(Context context, ParseObject object, JSONArray jsonArray, final String KEY, String username) {
        if (username.isEmpty()) {
            return;
        }

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", username);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with finding user", e);
                    return;
                }
                if (users.isEmpty()) {
                    Toast.makeText(context, username + " does not exist", Toast.LENGTH_LONG).show();
                    Log.i(TAG, username + " does not exist");
                    return;
                }
                Log.i(TAG, "User objectId: " + users.get(0).getObjectId());

                addUser(context, object, jsonArray, KEY, username, users.get(0).getObjectId());
            }
        });
    }

    public static void addUser(Context context, ParseObject object, JSONArray jsonArray, final String KEY, String username, String objectId) {
        for (String s : getObjectIds(jsonArray)) {
            if (s.equals(objectId)) {
                Toast.makeText(context, username + " already added", Toast.LENGTH_LONG).show();
                Log.i(TAG, username + " already added, objectId: " + objectId);
                return;
            }
        }

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
        if (jsonArray == null) {
            return null;
        }

        List<String> list = new ArrayList<String>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                list.add(jsonArray.getJSONObject(i).getString("username"));
            } catch (JSONException e) {
                Log.e(TAG, "Json exception: " + e, e);
            }
        }
        return list;
    }

    public static List<String> getObjectIds(JSONArray jsonArray) {
        List<String> list = new ArrayList<String>();

        if (jsonArray == null) {
            return list;
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                list.add(jsonArray.getJSONObject(i).getString("objectId"));
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
