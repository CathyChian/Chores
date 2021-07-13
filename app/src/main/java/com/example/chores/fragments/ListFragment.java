package com.example.chores.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chores.R;
import com.example.chores.adapters.ListAdapter;
import com.example.chores.databinding.FragmentListBinding;
import com.example.chores.models.Chore;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {

    public static final String TAG = "PostsFragment";
    FragmentListBinding binding;

    protected ListAdapter adapter;
    protected List<Chore> chores;

    public ListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListBinding.inflate(getActivity().getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chores = new ArrayList<>();
        adapter = new ListAdapter(getContext(), chores);

        binding.rvList.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        binding.rvList.setLayoutManager(linearLayoutManager);
        queryChores();

        binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryChores();
            }
        });
        binding.swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    protected void queryChores() {
        ParseQuery<Chore> query = ParseQuery.getQuery(Chore.class);
        query.include("user");
        query.setLimit(20);
        query.addDescendingOrder(Chore.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Chore>() {
            @Override
            public void done(List<Chore> chores, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting chores", e);
                    return;
                }

                for (Chore chore : chores) {
                    Log.i(TAG, "Chore: " + chore.getDescription() + ", username: " + chore.getUser().getUsername());
                }

                adapter.clear();
                adapter.addAll(chores);
                binding.swipeContainer.setRefreshing(false);
            }
        });
    }
}