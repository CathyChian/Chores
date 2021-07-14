package com.example.chores.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chores.databinding.ActivityChoreDetailsBinding;
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

        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick new chore button");
                Intent intent = new Intent(ChoreDetailsActivity.this, EditActivity.class);
                intent.putExtra("chore", Parcels.wrap(chore));
                startActivity(intent);
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
}
