package com.example.chores.models;

import android.util.Log;

import com.parse.ParseObject;
import com.parse.ParseClassName;
import com.parse.ParseUser;

import org.parceler.Parcel;

import java.util.Calendar;
import java.util.Date;
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

    public void setDateDue() {
        Calendar dueDay = Calendar.getInstance();
        dueDay.setTimeInMillis(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(getFrequency()));
        reduceDateToDay(dueDay);
        put("dateDue", dueDay.getTime());
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
}
