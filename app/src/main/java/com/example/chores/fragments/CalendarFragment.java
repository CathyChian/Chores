package com.example.chores.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.chores.ChoresApp;
import com.example.chores.GoogleCalendarClient;
import com.example.chores.databinding.FragmentCalendarBinding;
import com.example.chores.models.User;
import com.parse.ParseUser;

import org.json.JSONException;

import java.util.Date;

import okhttp3.Headers;

public class CalendarFragment extends Fragment {

    public static final String TAG = "CalendarFragment";
    FragmentCalendarBinding binding;
    private GoogleCalendarClient client;
    User currentUser;

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
        client = ChoresApp.getRestClient(getActivity());
        currentUser = (User) ParseUser.getCurrentUser();

        binding.btnNewCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCalendar();
            }
        });

        binding.btnGetCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCalendar();
            }
        });

        binding.btnNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEvent();
            }
        });
    }

    public void createCalendar() {
        String title = binding.etCalendarTitle.getText().toString();
        Log.i(TAG, "Title: " + title);
        client.createCalendar(title, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess to create new calendar. Title: " + title);
                try {
                    String summary = json.jsonObject.getString("summary");
                    String id = json.jsonObject.getString("id");
                    currentUser.setCalendarId(id);
                    currentUser.saveInBackground();
                    Log.i(TAG, "New calendar created. Title: " + summary + ", id: " + id);
                } catch (JSONException e) {
                    Log.i(TAG, "error trying to create new calendar", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.i(TAG, "onFailure to create new calendar. Title: " + title + ", Response: " + response + ", Status code: " + statusCode, throwable);
            }
        });
    }

    public void getCalendar() {
        String calendarId = currentUser.getCalendarId();
        client.getCalendar(calendarId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess to get calendar. Calendar id: " + calendarId);
                try {
                    String title = json.jsonObject.getString("summary");
                    String id = json.jsonObject.getString("id");
                    Log.i(TAG, "Title: " + title + ", id: " + id);

                    binding.tvCalendarTitle.setText(title);
                    binding.tvCalendarId.setText("id: " + id);
                } catch (JSONException e) {
                    Log.i(TAG, "error trying to get calendar", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.i(TAG, "onFailure to create new calendar. Response: " + response + ", Status code: " + statusCode, throwable);
            }
        });
    }

    public void createEvent() {
        String title = binding.etEventTitle.getText().toString();
        Log.i(TAG, "Title: " + title);
        client.createEvent(currentUser.getCalendarId(), title, new Date(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess to create new event. Title: " + title);
                try {
                    String summary = json.jsonObject.getString("summary");
                    String id = json.jsonObject.getString("id");
                    Log.i(TAG, "New event created. Title: " + summary + ", id: " + id);
                } catch (JSONException e) {
                    Log.i(TAG, "error trying to create new event", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.i(TAG, "onFailure to create new event. Title: " + title + ", Response: " + response + ", Status code: " + statusCode, throwable);
            }
        });
    }
}