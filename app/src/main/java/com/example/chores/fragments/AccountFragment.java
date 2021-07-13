package com.example.chores.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.chores.activities.MainActivity;
import com.example.chores.databinding.FragmentAccountBinding;
import com.parse.ParseUser;

public class AccountFragment extends Fragment {

    public static final String TAG = "PostsFragment";
    FragmentAccountBinding binding;

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

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick logout button");
                logout();
            }
        });
    }

    public void logout() {
        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser(); // currentUser will now be null
        Toast.makeText(getContext(), "Logged out!", Toast.LENGTH_SHORT).show();
        // goLoginActivity();
    }

//    private void goLoginActivity() {
//        Intent i = new Intent(this, LoginActivity.class);
//        startActivity(i);
//        finish();
//    }
}