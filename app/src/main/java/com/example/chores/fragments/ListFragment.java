package com.example.chores.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.chores.R;
import com.example.chores.activities.ComposeActivity;
import com.example.chores.activities.MainActivity;
import com.example.chores.adapters.ListAdapter;
import com.example.chores.databinding.FragmentListBinding;
import com.example.chores.models.Chore;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ListFragment extends Fragment {

    public static final String TAG = "PostsFragment";
    public static final int ADD_REQUEST_CODE = 7;
    public static final int DELETE_REQUEST_CODE = 8;
    public static final int UPDATE_REQUEST_CODE = 9;
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
        setHasOptionsMenu(true);
        ((MainActivity) getActivity()).setSupportActionBar(binding.toolbar);

        chores = new ArrayList<>();
        adapter = new ListAdapter(getContext(), ListFragment.this, chores);

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.miEdit) {
            compose();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void compose() {
        Log.i(TAG, "onClick new chore button");
        Intent i = new Intent(getContext(), ComposeActivity.class);
        startActivityForResult(i, ADD_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            Chore chore = Parcels.unwrap((data.getParcelableExtra("chore")));
            int position = data.getIntExtra("position", -1);
            String operation = data.getStringExtra("operation");
            Log.i(TAG, "onActivityResult, Chore: " + chore.getName() + ", description: " + chore.getDescription() + ", username: " + chore.getUser().getUsername());

            if (requestCode == ADD_REQUEST_CODE) {
                chores.add(0, chore);
                adapter.notifyItemInserted(0);
            } else if (requestCode == UPDATE_REQUEST_CODE && operation.equals("update")) {
                chores.set(position, chore);
                adapter.notifyItemChanged(position);
            } else {
                // else either delete request code or operation is "delete"
                chores.remove(position);
                adapter.notifyItemRemoved(position);
            }
            binding.rvList.smoothScrollToPosition(position);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void queryChores() {
        ParseQuery<Chore> query = ParseQuery.getQuery(Chore.class);
        query.include("user");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
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
                    Log.i(TAG, "Chore: " + chore.getName() + ", description: " + chore.getDescription() + ", username: " + chore.getUser().getUsername());
                }

                adapter.clear();
                adapter.addAll(chores);
                binding.swipeContainer.setRefreshing(false);
            }
        });
    }
}