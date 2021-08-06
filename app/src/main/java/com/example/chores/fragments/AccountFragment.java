package com.example.chores.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.chores.ChoresApp;
import com.example.chores.GoogleCalendarClient;
import com.example.chores.activities.LoginActivity;
import com.example.chores.databinding.FragmentAccountBinding;
import com.example.chores.models.User;
import com.parse.ParseUser;

import org.json.JSONException;

import okhttp3.Headers;

public class AccountFragment extends Fragment {

    public static final String TAG = "PostsFragment";
    private static final int SIGN_IN = 77;
    FragmentAccountBinding binding;
    private GoogleCalendarClient client;
    User currentUser;

    public AccountFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(getActivity().getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        client = ChoresApp.getRestClient(getContext());
        currentUser = (User) ParseUser.getCurrentUser();

        binding.tvUsername.setText(currentUser.getUsername());
        getCalendar();

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick logout button");
                logout();
            }
        });

        binding.btnNewCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCalendar();
            }
        });
    }

    public void logout() {
        ParseUser.logOut();
        // client.clearAccessToken();
        Toast.makeText(getContext(), "Logged out!", Toast.LENGTH_SHORT).show();
        goLoginActivity();
    }

    private void goLoginActivity() {
        Intent i = new Intent(getActivity(), LoginActivity.class);
        startActivity(i);
    }

    public void createCalendar() {
        String title = binding.etCalendarTitle.getText().toString();
        Log.i(TAG, "Title: " + title);
        client.createCalendar(title, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JsonHttpResponseHandler.JSON json) {
                Log.i(TAG, "onSuccess to create new calendar. Title: " + title);
                try {
                    String summary = json.jsonObject.getString("summary");
                    String id = json.jsonObject.getString("id");
                    currentUser.setCalendarId(id);
                    currentUser.saveInBackground();
                    Log.i(TAG, "New calendar created. Title: " + summary + ", id: " + id);

                    binding.tvCalendar.setText(summary);
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

                    binding.tvCalendar.setText(title);
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
}

