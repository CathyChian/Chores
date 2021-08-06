package com.example.chores.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.chores.ChoresApp;
import com.example.chores.GoogleCalendarClient;
import com.example.chores.databinding.ActivityComposeBinding;
import com.example.chores.models.Chore;
import com.example.chores.models.User;
import com.google.android.material.slider.Slider;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.parceler.Parcels;

import java.util.Calendar;
import java.util.Date;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {

    public static final String TAG = "ComposeActivity";
    ActivityComposeBinding binding;
    private GoogleCalendarClient client;
    User currentUser;
    int priority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComposeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        client = ChoresApp.getRestClient(ComposeActivity.this);
        currentUser = (User) ParseUser.getCurrentUser();

        binding.sPriority.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                priority = (int) value;
            }
        });

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Chore chore = new Chore();
                chore.setName(binding.etName.getText().toString());
                chore.setDescription(binding.etDescription.getText().toString());
                chore.setUser(ParseUser.getCurrentUser());
                chore.setRecurring(binding.tbtnRecurring.isChecked());
                chore.setFrequency(binding.etFrequency.getText().toString());
                chore.setPriority(priority);
                chore.setWeight(priority, Calendar.getInstance(), chore.getFrequency());
                chore.setDateDue(Calendar.getInstance(), chore.getFrequency());
                chore.addUser(binding.etSharedUsers.getText().toString(), ComposeActivity.this);

                chore.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e(TAG, "Error while saving " + e, e);
                            Toast.makeText(ComposeActivity.this, "Error while saving!", Toast.LENGTH_SHORT).show();
                        }
                        createEvent(binding.etName.getText().toString(), chore.getDateDue());
                        Log.i(TAG, "Post save was successful!");

                        binding.etName.setText("");
                        binding.etDescription.setText("");
                        binding.tbtnRecurring.setChecked(false);
                        binding.etFrequency.setText("");
                        binding.sPriority.setValue(0);
                        binding.etSharedUsers.setText("");

                        Intent intent = new Intent();
                        intent.putExtra("chore", Parcels.wrap(chore));
                        intent.putExtra("position", 0);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
            }
        });
    }

    public void createEvent(String title, Date date) {
        Log.i(TAG, "Title: " + title);
        client.createEvent(currentUser.getCalendarId(), title, date, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JsonHttpResponseHandler.JSON json) {
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
