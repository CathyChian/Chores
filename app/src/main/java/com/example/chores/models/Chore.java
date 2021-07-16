package com.example.chores.models;

import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseClassName;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Parcel(analyze = Chore.class)
@ParseClassName("Chore")
public class Chore extends ParseObject {

    private static final String TAG = "Chore";

    public Chore() {
    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        if (!name.isEmpty())
            put("name", name);
    }

    public String getDescription() {
        return getString("description");
    }

    public void setDescription(String description) {
        if (!description.isEmpty())
            put("description", description);
    }

    public ParseUser getUser() {
        return getParseUser("user");
    }

    public void setUser(ParseUser user) {
        put("user", user);
    }

    public boolean isRecurring() {
        return getBoolean("recurring");
    }

    public void setRecurring(boolean recurring) {
        put("recurring", recurring);
    }

    public int getFrequency() {
        return getNumber("frequency").intValue();
    }

    public void setFrequency(String frequency) {
        if (!frequency.isEmpty())
            put("frequency", Integer.valueOf(frequency));
    }

    public int getPriority() {
        return getNumber("priority").intValue();
    }

    public void setPriority(String priority) {
        if (!priority.isEmpty())
            put("priority", Integer.valueOf(priority));
    }

    public Date getDateDue() {
        return getDate("dateDue");
    }

    public void setDateDue(Calendar dueDay, int offset) {
        reduceDateToDay(dueDay);
        dueDay.setTimeInMillis(dueDay.getTimeInMillis() + TimeUnit.DAYS.toMillis(offset));
        put("dateDue", dueDay.getTime());
    }

    public JSONArray getSharedUsers() {
        return getJSONArray("sharedUsers");
    }

    public void setSharedUsers(JSONArray sharedUsers) {
        put("sharedUsers", sharedUsers);
    }

    public String getRecurringText() {
        if (!isRecurring() || getFrequency() < 1)
            return "Does not repeat";
        if (getFrequency() == 1)
            return "Repeats every day";

        return "Repeats every " + getFrequency() + " days";
    }

    public String getRelativeDateText() {
        long diffInMillis = getDateDue().getTime() - reduceDateToDay(Calendar.getInstance()).getTimeInMillis();
        int diffInDays = (int) TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);

        if (diffInDays < -1)
            return "Due " + Math.abs(diffInDays) + " days ago";
        if (diffInDays == -1)
            return "Due yesterday";
        if (diffInDays == 0)
            return "Due today";
        if (diffInDays == 1)
            return "Due tomorrow";

        return "Due in " + diffInDays + " days";
    }

    public Calendar reduceDateToDay(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    public void addSharedUser(String username, String objectId) {
        JSONArray sharedUsers = getSharedUsers();
        JSONObject user = new JSONObject();
        try {
            user.put("username", username);
            user.put("objectId", objectId);
        } catch (JSONException e) {
            Log.e(TAG, "Json exception: " + e, e);
        }
        sharedUsers.put(user);
        put("sharedUsers", sharedUsers);
        saveInBackground();
    }

    public void addSharedUserByUsername(String username) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", username);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e){
                if (e != null) {
                    Log.e(TAG, "Issue with finding user", e);
                    return;
                }
                Log.i(TAG, "User objectId: " + users.get(0).getObjectId());

                addSharedUser(username, users.get(0).getObjectId());
            }
        });
    }

    public String getSharedUsersText() {
        JSONArray sharedUsers = getSharedUsers();

        if (sharedUsers.length() == 0) {
            return "";
        }

        String text = "Shared with: ";
        for (int i = 0; i < sharedUsers.length(); i++) {
            try {
                text += sharedUsers.getJSONObject(i).getString("username") + " ";
            } catch (JSONException e) {
                Log.e(TAG, "Json exception: " + e, e);
            }
        }
        return text;
    }
}