package com.example.chores.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.chores.R;
import com.example.chores.activities.ComposeActivity;
import com.example.chores.activities.MainActivity;
import com.example.chores.adapters.ListAdapter;
import com.example.chores.databinding.FragmentListBinding;
import com.example.chores.models.Chore;
import com.example.chores.models.ChoreEvent;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ListFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    public static final String TAG = "PostsFragment";
    FragmentListBinding binding;

    public static ListAdapter adapter;
    public static List<Chore> chores;
    public static String order = "weight";

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

        binding.rvList.addItemDecoration(new DividerItemDecoration(binding.rvList.getContext(), DividerItemDecoration.VERTICAL));
        binding.rvList.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        binding.rvList.setLayoutManager(linearLayoutManager);
        queryChores();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.queries_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(adapter);
        binding.spinner.setOnItemSelectedListener(this);

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
        if (item.getItemId() == R.id.miCompose) {
            compose();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        String selected = parent.getItemAtPosition(pos).toString();
        if (selected.equals("Recommended")) {
            order = "weight";
        } else if (selected.equals("Due Date")) {
            order = "dateDue";
        } else if (selected.equals("Priority")) {
            order = "priority";
        } else if (selected.equals("Date Created")) {
            order = "createdAt";
        } else if (selected.equals("Last Updated")) {
            order = "updatedAt";
        }
        queryChores();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Keep the same selection
    }

    public void compose() {
        Log.i(TAG, "onClick new chore button");
        Intent i = new Intent(getContext(), ComposeActivity.class);
        startActivityForResult(i, MainActivity.ADD_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            Chore chore = Parcels.unwrap((data.getParcelableExtra("chore")));
            Calendar dateDue = Calendar.getInstance();
            dateDue.setTime(chore.getDateDue());

            int position = data.getIntExtra("position", -1);
            Log.i(TAG, "onActivityResult, Chore: " + chore.getName() + ", description: " + chore.getDescription() + ", username: " + chore.getUser().getUsername());

            if (requestCode == MainActivity.ADD_REQUEST_CODE) {
                chores.add(0, chore);
                CalendarFragment.choreEvents.add(0, new ChoreEvent(dateDue, R.drawable.event_icons));
                adapter.notifyItemInserted(0);
            } else if (requestCode == MainActivity.UPDATE_REQUEST_CODE) {
                chores.set(position, chore);
                CalendarFragment.choreEvents.set(position, new ChoreEvent(dateDue, R.drawable.event_icons));
                adapter.notifyItemChanged(position);
            } else if (requestCode == MainActivity.DELETE_REQUEST_CODE) {
                chores.remove(position);
                CalendarFragment.choreEvents.remove(position);
                adapter.notifyItemRemoved(position);
            } else if (requestCode == MainActivity.DETAILED_REQUEST_CODE) {
                String operation = data.getStringExtra("operation");
                if (operation.equals("update")) {
                    chores.set(position, chore);
                    CalendarFragment.choreEvents.set(position, new ChoreEvent(dateDue, R.drawable.event_icons));
                    adapter.notifyItemChanged(position);
                } else if (operation.equals("delete")){
                    chore.deleteInBackground();
                    chores.remove(position);
                    CalendarFragment.choreEvents.remove(position);
                    adapter.notifyItemRemoved(position);
                }
            }
            binding.rvList.smoothScrollToPosition(position);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void queryChores() {
        // TODO: Remove this, just temporary for testing Google sign-in
        if (ParseUser.getCurrentUser() == null) {
            Log.i(TAG, "Not signed into Parse");
            return;
        }

        List<ParseQuery<Chore>> queries = new ArrayList<>();

        ParseQuery<Chore> ownedQuery = ParseQuery.getQuery(Chore.class);
        ownedQuery.whereEqualTo("user", ParseUser.getCurrentUser());
        queries.add(ownedQuery);

        ParseQuery<Chore> sharedQuery = ParseQuery.getQuery(Chore.class);
        ownedQuery.whereContains("sharedUsers", ParseUser.getCurrentUser().getObjectId());
        queries.add(sharedQuery);

        ParseQuery<Chore> query = ParseQuery.or(queries);
        query.include("user");
        if (order.equals("dateDue")) {
            query.addAscendingOrder(order);
        } else {
            query.addDescendingOrder(order);
        }

        query.findInBackground(new FindCallback<Chore>() {
            @Override
            public void done(List<Chore> chores, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting chores", e);
                    return;
                }

                CalendarFragment.choreEvents.clear();

                for (Chore chore : chores) {
                    Log.i(TAG, "Chore: " + chore.getName() + ", description: " + chore.getDescription() + ", username: " + chore.getUser().getUsername());
                    Calendar dateDue = Calendar.getInstance();
                    dateDue.setTime(chore.getDateDue());
                    CalendarFragment.choreEvents.add(new ChoreEvent(dateDue, R.drawable.event_icons));
                }

                adapter.clear();
                adapter.addAll(chores);
                binding.swipeContainer.setRefreshing(false);
            }
        });
    }
}