package com.example.chores.models;

import com.parse.ParseObject;
import com.parse.ParseClassName;
import com.parse.ParseUser;

import org.parceler.Parcel;

@Parcel(analyze = Chore.class)
@ParseClassName("Chore")
public class Chore extends ParseObject {

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

    public ParseUser getUser()  {
        return getParseUser("user");
    }

    public void setUser(ParseUser user) {
        put("user", user);
    }

    public boolean isRecurring()  {
        return getBoolean("recurring");
    }

    public void setRecurring(boolean recurring) {
        put("recurring", recurring);
    }

    public Number getFrequency()  {
        return getNumber("frequency");
    }

    public void setFrequency(String frequency) {
        if (!frequency.isEmpty())
            put("frequency", Integer.valueOf(frequency));
    }

    public int getPriority()  {
        return getNumber("priority").intValue();
    }

    public void setPriority(String priority) {
        if (!priority.isEmpty())
            put("priority", Integer.valueOf(priority));
    }
}
