package com.example.chores.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chores.databinding.ActivityChoreDetailsBinding;
import com.example.chores.fragments.ListFragment;
import com.example.chores.models.Chore;

import org.parceler.Parcels;

public class ChoreDetailsActivity extends AppCompatActivity {

    public static final String TAG = "ChoreDetailsActivity";
    ActivityChoreDetailsBinding binding;
    Chore chore;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChoreDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        chore = Parcels.unwrap(getIntent().getParcelableExtra("chore"));
        position = getIntent().getIntExtra("position", -1);
        Log.i(TAG, "Showing " + chore.getName() + " chore. Description: " + chore.getDescription());

        binding.tvName.setText(chore.getName());
        binding.tvDescription.setText(chore.getDescription());
        setRecurring(chore);

        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick new chore button");
                Intent intent = new Intent(ChoreDetailsActivity.this, EditActivity.class);
                intent.putExtra("chore", Parcels.wrap(chore));
                intent.putExtra("position", position);
                startActivityForResult(intent, ListFragment.EDIT_REQUEST_CODE);
            }
        });

        binding.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("position", position);
                setResult(RESULT_OK, intent);
                chore.deleteInBackground();
                finish();
            }
        });
    }

    public void setRecurring(Chore chore) {
        if (chore.isRecurring()) {
            binding.tvRecurring.setText("Repeats every " + chore.getFrequency() + " days");
        } else {

            binding.tvRecurring.setText("");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == ListFragment.EDIT_REQUEST_CODE && resultCode == RESULT_OK) {
            Chore chore = Parcels.unwrap((data.getParcelableExtra("chore")));
            int position = data.getIntExtra("position", -1);
            refresh(chore);
            Log.i(TAG, "onActivityResult: edit, name: " + chore.getName());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // TODO: Add pull to refresh to detailed view
    public void refresh(Chore chore) {
        binding.tvName.setText(chore.getName());
        binding.tvDescription.setText(chore.getDescription());
        setRecurring(chore);
    }
}
