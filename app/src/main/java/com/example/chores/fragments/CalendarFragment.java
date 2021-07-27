package com.example.chores.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.chores.activities.ComposeActivity;
import com.example.chores.activities.MainActivity;
import com.example.chores.databinding.FragmentCalendarBinding;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class CalendarFragment extends Fragment {

    public static final String TAG = "CalendarFragment";
    FragmentCalendarBinding binding;

    public static List<EventDay> choreEvents = new ArrayList<>();

    public CalendarFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCalendarBinding.inflate(getActivity().getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.calendarView.setEvents(choreEvents);

        for (EventDay choreEvent : choreEvents) {
            Log.i(TAG, "Day: " + choreEvent.getCalendar());
        }

        binding.fabCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "clicked on compose fab");
                createEvent();
            }
        });

        binding.calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Log.i(TAG, "clicked on event day: " + eventDay.toString());
                openDetailedView();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MainActivity.ADD_REQUEST_CODE && resultCode == RESULT_OK) {
            // TODO: update event
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void createEvent() {
        Intent i = new Intent(getContext(), ComposeActivity.class);
        startActivityForResult(i, MainActivity.ADD_REQUEST_CODE);
    }

    public void openDetailedView() {

    }
}