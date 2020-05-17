package com.techdoctorbd.quizapplication.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.techdoctorbd.quizapplication.R;

import java.util.Objects;

public class SplashFragment extends Fragment {

    private TextView tvStatus;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private String SPLASH_LOG = "Splash Log";
    private NavController navController;

    public SplashFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        navController = Navigation.findNavController(view);

        tvStatus = view.findViewById(R.id.status_splash_screen);
        progressBar = view.findViewById(R.id.progressbar_splash_screen);

        tvStatus.setText("Checking user account");

        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if (firebaseUser == null){
            tvStatus.setText("Creating user account");

            mAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        tvStatus.setText("Successfully created user account");
                        navController.navigate(R.id.action_splashFragment_to_listFragment);
                    } else {
                        Log.d(SPLASH_LOG," : "+Objects.requireNonNull(task.getException()));
                        tvStatus.setText(String.valueOf(task.getException()));
                    }
                }
            });
        } else {
            navController.navigate(R.id.action_splashFragment_to_listFragment);
        }

    }

}
