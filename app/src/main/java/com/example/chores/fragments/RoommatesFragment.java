package com.example.chores.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chores.R;
import com.example.chores.databinding.FragmentListBinding;
import com.example.chores.databinding.FragmentRoommatesBinding;

public class RoommatesFragment extends Fragment {

    public static final String TAG = "PostsFragment";
    FragmentRoommatesBinding binding;

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
    }
}