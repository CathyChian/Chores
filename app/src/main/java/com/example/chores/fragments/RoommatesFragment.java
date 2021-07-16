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

        binding.tvRoommates.setText(getRoommatesText());

        binding.btnRoommates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRoommate(binding.etRoommates.getText().toString());
                ParseUser.getCurrentUser().put("roommates", binding.etRoommates.getText().toString());
                ParseUser.getCurrentUser().saveInBackground();
            }
        });
    }

    public void saveRoommate(String username) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", username);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e){
                if (e != null) {
                    Log.e(TAG, "Issue with finding user", e);
                    return;
                }
                Log.i(TAG, "User objectId: " + users.get(0).getObjectId());

                ParseUser currentUser = ParseUser.getCurrentUser();
                JSONArray roommates = currentUser.getJSONArray("roommates");
                if (roommates == null) {
                    roommates = new JSONArray();
                }

                JSONObject user = new JSONObject();
                try {
                    user.put("username", username);
                    user.put("objectId", users.get(0).getObjectId());
                } catch (JSONException jsonException) {
                    Log.e(TAG, "Json exception: " + jsonException, jsonException);
                }
                roommates.put(user);
                currentUser.put("roommates", roommates);
                currentUser.saveInBackground();

                binding.tvRoommates.setText(getRoommatesText());
            }
        });
    }

    public String getRoommatesText() {
        List<String> list = getRoommatesUsernames();

        if (list.size() == 0) {
            return "";
        }

        String text = "Roommates with: ";
        for (int i = 0; i < list.size(); i++) {
            text += list.get(i) + " ";
        }
        return text;
    }

    public List<String> getRoommatesUsernames() {
        JSONArray roommates = ParseUser.getCurrentUser().getJSONArray("roommates");
        List<String> list = new ArrayList<String>();

        for (int i = 0; i < roommates.length(); i++) {
            try {
                list.add(roommates.getJSONObject(i).getString("username"));
            } catch (JSONException e) {
                Log.e(TAG, "Json exception: " + e, e);
            }
        }
        return list;
    }
}