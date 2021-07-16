package com.example.chores;

import com.example.chores.models.Chore;
import com.example.chores.models.User;
import com.parse.Parse;
import com.parse.ParseObject;

import android.app.Application;

public class ParseApplication extends Application {

    // Initializes Parse SDK as soon as the application is created
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Chore.class);
        ParseObject.registerSubclass(User.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("N5bepRnqADIqEaYwRTjIYZOFPKwjo8tFJ1dBR4W6")
                .clientKey("4tWURCPI0VFeTdmwz9Vgplm4MNqbaxrYayBz5pCw")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}

