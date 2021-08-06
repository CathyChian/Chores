package com.example.chores.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.chores.R;
import com.example.chores.databinding.ActivityMainBinding;
import com.example.chores.fragments.AccountFragment;
import com.example.chores.fragments.CalendarFragment;
import com.example.chores.fragments.ListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    public static final int ADD_REQUEST_CODE = 7;
    public static final int DELETE_REQUEST_CODE = 8;
    public static final int DETAILED_REQUEST_CODE = 9;
    public static final int UPDATE_REQUEST_CODE = 10;
    public final FragmentManager fragmentManager = getSupportFragmentManager();
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final Fragment listFragment = new ListFragment();
        final Fragment calendarFragment = new CalendarFragment();
        final Fragment accountFragment = new AccountFragment();

        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.action_list:
                        Log.i(TAG, "Clicked list menu item");
                        fragment = listFragment;
                        break;
                    case R.id.action_calendar:
                        Log.i(TAG, "Clicked calendar menu item");
                        fragment = calendarFragment;
                        break;
                    case R.id.action_account:
                        Log.i(TAG, "Clicked account menu item");
                        fragment = accountFragment;
                        break;
                    default:
                        Log.i(TAG, "Clicked default menu item");
                        fragment = listFragment;
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        binding.bottomNavigation.setSelectedItemId(R.id.action_list);
    }
}