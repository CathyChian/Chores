package com.example.chores.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chores.R;
import com.example.chores.databinding.FragmentListBinding;
import com.example.chores.databinding.FragmentRoommatesBinding;
import com.example.chores.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RoommatesFragment extends Fragment {

    public static final String TAG = "PostsFragment";
    FragmentRoommatesBinding binding;
    User currentUser;

    public RoommatesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRoommatesBinding.inflate(getActivity().getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentUser = (User) ParseUser.getCurrentUser();
        binding.tvRoommates.setText(currentUser.getListOfUsers());

        binding.btnRoommates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUser.addUser(binding.etRoommates.getText().toString());
                binding.tvRoommates.setText(currentUser.getListOfUsers());
            }
        });
    }
}