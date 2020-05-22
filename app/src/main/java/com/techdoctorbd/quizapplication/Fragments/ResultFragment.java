package com.techdoctorbd.quizapplication.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.techdoctorbd.quizapplication.R;

import java.util.HashMap;

public class ResultFragment extends Fragment implements View.OnClickListener {

    private NavController navController;
    private FirebaseUser mUser;
    private FirebaseFirestore firebaseFirestore;

    private String quizId,uID;
    private int correctAnswers,wrongAnswers,notAnswered;

    private TextView resultPercentage,resultCorrect,resultWrong,resultMissed;
    private Button backToHome;
    private ProgressBar progressBar;

    public ResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.results_progress);
        resultPercentage = view.findViewById(R.id.results_percent);
        resultCorrect = view.findViewById(R.id.results_correct_text);
        resultWrong = view.findViewById(R.id.results_wrong_text);
        resultMissed = view.findViewById(R.id.results_missed_text);
        backToHome = view.findViewById(R.id.results_home_btn);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        navController = Navigation.findNavController(view);

        if (mUser != null){
            uID = mUser.getUid();
        }

        quizId = ResultFragmentArgs.fromBundle(getArguments()).getQuizId();
        correctAnswers = ResultFragmentArgs.fromBundle(getArguments()).getCorrectAnswer();
        wrongAnswers = ResultFragmentArgs.fromBundle(getArguments()).getWrongAnswer();
        notAnswered = ResultFragmentArgs.fromBundle(getArguments()).getNotAnswered();

        submitResultToDatabase();

        resultCorrect.setText(String.valueOf(correctAnswers));
        resultWrong.setText(String.valueOf(wrongAnswers));
        resultMissed.setText(String.valueOf(notAnswered));

        int total = correctAnswers + wrongAnswers + notAnswered;
        int percentage = (correctAnswers*100) / total;

        resultPercentage.setText(percentage + "%");
        progressBar.setProgress(percentage);

        backToHome.setOnClickListener(this);

    }

    private void submitResultToDatabase() {
        HashMap<String,Object> resultMap = new HashMap<>();
        resultMap.put("correctAnswers",correctAnswers);
        resultMap.put("wrongAnswers",wrongAnswers);
        resultMap.put("notAnswered",notAnswered);

        try {
            firebaseFirestore.collection("QuizList").document(quizId).collection("Results").document(uID).set(resultMap);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        navController.navigate(R.id.action_resultFragment_to_listFragment);
    }
}
