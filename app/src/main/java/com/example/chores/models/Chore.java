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
        put("name", name);
    }

    public String getDescription() {
        return getString("description");
    }

    public void setDescription(String description) {
        put("description", description);
    }

    public ParseUser getUser()  {
        return getParseUser("user");
    }

    public void setUser(ParseUser user) {
        put("user", user);
    }
}
