package com.example.chores.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chores.R;
import com.example.chores.databinding.FragmentCalendarBinding;
import com.example.chores.databinding.FragmentRoommatesBinding;
import com.example.chores.models.User;
import com.parse.ParseUser;

public class CalendarFragment extends Fragment {

    public static final String TAG = "CalendarFragment";
    FragmentCalendarBinding binding;

    public CalendarFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCalendarBinding.inflate(getActivity().getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}