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

import com.example.chores.activities.ComposeActivity;
import com.example.chores.activities.LoginActivity;
import com.example.chores.activities.MainActivity;
import com.example.chores.databinding.FragmentAccountBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.parse.ParseUser;

public class AccountFragment extends Fragment {

    public static final String TAG = "PostsFragment";
    private static final int SIGN_IN = 77;
    FragmentAccountBinding binding;
    GoogleSignInClient googleSignInClient;

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

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope("https://www.googleapis.com/auth/calendar"))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(getContext(), gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        updateUI(account);

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick logout button");
                logout();
            }
        });

        binding.btnGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    public void logout() {
        ParseUser.logOut();
        Toast.makeText(getContext(), "Logged out!", Toast.LENGTH_SHORT).show();
        goLoginActivity();
    }

    private void goLoginActivity() {
        Intent i = new Intent(getActivity(), LoginActivity.class);
        startActivity(i);
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, SIGN_IN);
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account == null) {
            binding.btnGoogleSignIn.setVisibility(View.VISIBLE);
            binding.tvEmail.setText("");
        } else {
            binding.btnGoogleSignIn.setVisibility(View.INVISIBLE);
            binding.tvEmail.setText(account.getEmail());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                ParseUser.getCurrentUser().setEmail(account.getEmail());
            } catch (ApiException e) {
                Log.e(TAG, "signInResult:failed code=" + e.getStatusCode());
            }
        }
    }
}