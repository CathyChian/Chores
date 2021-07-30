package com.example.chores.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chores.databinding.ActivityComposeBinding;
import com.example.chores.models.Chore;
import com.example.chores.models.ChoreObject;
import com.parse.ParseException;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.Calendar;
import java.util.List;

public class EditActivity extends AppCompatActivity {

    public static final String TAG = "EditActivity";
    ActivityComposeBinding binding;
    Chore chore;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComposeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        chore = Parcels.unwrap(getIntent().getParcelableExtra("chore"));
        position = getIntent().getIntExtra("position", -1);

        binding.etName.setText(chore.getName());
        binding.etDescription.setText(chore.getDescription());
        binding.tbtnRecurring.setChecked(chore.isRecurring());
        binding.etFrequency.setText(String.valueOf(chore.getFrequency()));
        binding.etPriority.setText(String.valueOf(chore.getFrequency()));

        // TODO: Get more than one user
        List<String> usernames = ChoreObject.getUsernames(chore.getSharedUsers());
        if (usernames != null) {
            binding.etSharedUsers.setText(ChoreObject.getUsernames(chore.getSharedUsers()).get(0));
        }

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chore.setName(binding.etName.getText().toString());
                chore.setDescription(binding.etDescription.getText().toString());
                chore.setRecurring(binding.tbtnRecurring.isChecked());
                chore.setFrequency(binding.etFrequency.getText().toString());
                chore.setPriority(binding.etPriority.getText().toString());
                chore.setWeight((binding.etPriority.getText().toString()), Calendar.getInstance(), chore.getFrequency());
                // TODO: Maybe ask if user wants to change due date?
                chore.addUser(binding.etSharedUsers.getText().toString(), EditActivity.this);

                chore.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e(TAG, "Error while saving " + e, e);
                            Toast.makeText(EditActivity.this, "Error while saving!", Toast.LENGTH_SHORT).show();
                        }
                        Log.i(TAG, "Post save was successful!");
                        binding.etName.setText("");
                        binding.etDescription.setText("");
                        binding.tbtnRecurring.setChecked(false);
                        binding.etFrequency.setText("");
                        binding.etPriority.setText("");
                        binding.etSharedUsers.setText("");

                        Intent intent = new Intent();
                        intent.putExtra("chore", Parcels.wrap(chore));
                        intent.putExtra("position", position);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
            }
        });

    }
}
