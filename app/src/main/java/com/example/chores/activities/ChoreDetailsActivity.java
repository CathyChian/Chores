package com.example.chores.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chores.R;
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

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        chore = Parcels.unwrap(getIntent().getParcelableExtra("chore"));
        position = getIntent().getIntExtra("position", -1);
        Log.i(TAG, "Showing " + chore.getName() + " chore. Description: " + chore.getDescription());

        binding.tvName.setText(chore.getName());
        binding.tvDescription.setText(chore.getDescription());
        binding.tvRecurring.setText(chore.getRecurringText());

        binding.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chore.deleteInBackground();
                passBackChore("delete");
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
            passBackChore("update");
            return true;
        }
        if (item.getItemId() == R.id.miEdit) {
            launchEdit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        passBackChore("update");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == ListFragment.UPDATE_REQUEST_CODE && resultCode == RESULT_OK) {
            chore = Parcels.unwrap((data.getParcelableExtra("chore")));
            refresh(chore);
            Log.i(TAG, "onActivityResult: edit, name: " + chore.getName());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // TODO: Add pull to refresh to detailed view
    public void refresh(Chore chore) {
        binding.tvName.setText(chore.getName());
        binding.tvDescription.setText(chore.getDescription());
        binding.tvRecurring.setText(chore.getRecurringText());
    }

    public void launchEdit() {
        Intent intent = new Intent(ChoreDetailsActivity.this, EditActivity.class);
        intent.putExtra("chore", Parcels.wrap(chore));
        intent.putExtra("position", position);
        startActivityForResult(intent, ListFragment.UPDATE_REQUEST_CODE);
    }

    public void passBackChore(String operation) {
        Intent intent = new Intent();
        intent.putExtra("chore", Parcels.wrap(chore));
        intent.putExtra("position", position);
        intent.putExtra("operation", operation);
        setResult(RESULT_OK, intent);
        finish();
    }
}
