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
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.chores.R;
import com.example.chores.activities.ComposeActivity;
import com.example.chores.activities.MainActivity;
import com.example.chores.databinding.FragmentCalendarBinding;
import com.example.chores.models.Chore;
import com.example.chores.models.ChoreEvent;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;
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
                openDetailedView(eventDay);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MainActivity.ADD_REQUEST_CODE && resultCode == RESULT_OK) {
            Chore chore = Parcels.unwrap((data.getParcelableExtra("chore")));
            Calendar dateDue = Calendar.getInstance();
            dateDue.setTime(chore.getDateDue());

            Log.i(TAG, "onActivityResult, Chore: " + chore.getName() + ", description: " + chore.getDescription() + ", username: " + chore.getUser().getUsername());

            ListFragment.chores.add(0, chore);
            choreEvents.add(0, new ChoreEvent(dateDue, R.drawable.event_icons));
            ListFragment.adapter.notifyItemInserted(0);

            try {
                binding.calendarView.setDate(dateDue);
            } catch (OutOfDateRangeException e) {
                Log.i(TAG, "Exception when setting calendar date in onActivityResult(): " + e);
            }
            binding.calendarView.setEvents(choreEvents);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void createEvent() {
        Intent i = new Intent(getContext(), ComposeActivity.class);
        startActivityForResult(i, MainActivity.ADD_REQUEST_CODE);
    }

    public void openDetailedView(EventDay eventDay) {
        if (eventDay instanceof ChoreEvent) {
            ListFragment listFragment = new ListFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("position", choreEvents.indexOf(eventDay));
            listFragment.setArguments(bundle);

            getParentFragmentManager().beginTransaction().replace(R.id.flContainer, listFragment).commit();
        }
    }
}