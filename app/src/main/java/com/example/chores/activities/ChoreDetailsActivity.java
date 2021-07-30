package com.example.chores.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chores.R;
import com.example.chores.databinding.ActivityChoreDetailsBinding;
import com.example.chores.models.Chore;

import org.parceler.Parcels;

import java.util.Calendar;

import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class ChoreDetailsActivity extends AppCompatActivity {

    public static final String TAG = "ChoreDetailsActivity";
    ActivityChoreDetailsBinding binding;
    Chore chore;
    int position;
    String operation = "update";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChoreDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        chore = Parcels.unwrap(getIntent().getParcelableExtra("chore"));
        position = getIntent().getIntExtra("position", -1);
        Log.i(TAG, "Showing " + chore.getName() + " chore. Description: " + chore.getDescription());

        setViews();

        binding.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operation = "delete";
                passBackChore();
            }
        });

        // TODO: Better indicator to user when chore is marked done
        binding.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                konfetti();
                if (chore.isRecurring()) {
                    chore.setDateDue(Calendar.getInstance(), chore.getFrequency());
                    chore.saveInBackground();
                    setViews();
                } else {
                    operation = "delete";
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            passBackChore();
            return true;
        }
        if (item.getItemId() == R.id.miCompose) {
            launchEdit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        passBackChore();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == MainActivity.UPDATE_REQUEST_CODE && resultCode == RESULT_OK) {
            chore = Parcels.unwrap((data.getParcelableExtra("chore")));
            setViews();
            Log.i(TAG, "onActivityResult: edit, name: " + chore.getName());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // TODO: Add pull to refresh to detailed view
    public void setViews() {
        binding.tvName.setText(chore.getName());
        binding.tvDescription.setText(chore.getDescription());
        binding.tvRecurring.setText(chore.getRecurringText());
        binding.tvDateDue.setText(chore.getRelativeDateText());
        binding.tvSharedUsers.setText(chore.getListOfUsers());
    }

    public void launchEdit() {
        Intent intent = new Intent(ChoreDetailsActivity.this, EditActivity.class);
        intent.putExtra("chore", Parcels.wrap(chore));
        intent.putExtra("position", position);
        startActivityForResult(intent, MainActivity.UPDATE_REQUEST_CODE);
    }

    public void passBackChore() {
        Intent intent = new Intent();
        intent.putExtra("chore", Parcels.wrap(chore));
        intent.putExtra("position", position);
        intent.putExtra("operation", operation);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void konfetti() {
        binding.konfetti.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                .setDirection(0.0, 359.0)
                .setSpeed(2f, 10f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.RECT, Shape.CIRCLE)
                .addSizes(new Size(12, 5f))
                .setPosition(-50f, binding.konfetti.getWidth() + 50f, -50f, -50f)
                .streamFor(100, 1000L);

    }
}
