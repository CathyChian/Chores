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

import org.parceler.Parcels;

public class EditActivity extends AppCompatActivity {

    public static final String TAG = "EditActivity";
    ActivityComposeBinding binding;
    Chore chore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComposeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        chore = Parcels.unwrap(getIntent().getParcelableExtra("chore"));

        binding.etName.setText(chore.getName());
        binding.etDescription.setText(chore.getDescription());
        binding.tbtnRecurring.setChecked(chore.isRecurring());
        binding.etFrequency.setText(String.valueOf(chore.getFrequency()));

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chore.setName(binding.etName.getText().toString());
                chore.setDescription(binding.etDescription.getText().toString());
                chore.setRecurring(binding.tbtnRecurring.isChecked());
                if (binding.tbtnRecurring.isChecked())
                    chore.setFrequency(binding.etFrequency.getText().toString());

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

                        Intent intent = new Intent();
                        intent.putExtra("chore", Parcels.wrap(chore));
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
            }
        });

    }
}
