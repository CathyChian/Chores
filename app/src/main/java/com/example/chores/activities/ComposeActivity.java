package com.example.chores.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chores.databinding.ActivityComposeBinding;
import com.example.chores.databinding.ActivityLoginBinding;
import com.example.chores.models.Chore;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.parceler.Parcels;

import java.util.Calendar;
import java.util.Date;

public class ComposeActivity extends AppCompatActivity {

    public static final String TAG = "ComposeActivity";
    ActivityComposeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComposeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Chore chore = new Chore();
                chore.setName(binding.etName.getText().toString());
                chore.setDescription(binding.etDescription.getText().toString());
                chore.setUser(ParseUser.getCurrentUser());
                chore.setRecurring(binding.tbtnRecurring.isChecked());
                chore.setFrequency(binding.etFrequency.getText().toString());
                chore.setDateDue(Calendar.getInstance(), chore.getFrequency());
                chore.setSharedUsers(new JSONArray());
                // TODO: Add ability to share with more than one user
                chore.addSharedUser(binding.etSharedUsers.getText().toString());

                chore.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e(TAG, "Error while saving " + e, e);
                            Toast.makeText(ComposeActivity.this, "Error while saving!", Toast.LENGTH_SHORT).show();
                        }
                        Log.i(TAG, "Post save was successful!");
                        binding.etName.setText("");
                        binding.etDescription.setText("");
                        binding.tbtnRecurring.setChecked(false);
                        binding.etFrequency.setText("");
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
}
