package com.example.chores.models;

import com.applandeo.materialcalendarview.EventDay;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.Calendar;

@Parcel(analyze = ChoreEvent.class)
public class ChoreEvent extends EventDay {
    Calendar day;
    int drawable;

    @ParcelConstructor
    public ChoreEvent(Calendar day, int drawable) {
        super(day, drawable);
        this.day = day;
        this.drawable = drawable;
    }
}

