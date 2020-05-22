package com.techdoctorbd.quizapplication.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.techdoctorbd.quizapplication.Models.QuestionItem;
import com.techdoctorbd.quizapplication.Models.QuizItem;
import com.techdoctorbd.quizapplication.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuizFragment extends Fragment implements View.OnClickListener {

    private NavController navController;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser mUser;
    private String quizId,quizName,uID;
    private TextView tvQuizName,tvQuestionNumber,tvTimer,tvQuestion,tvQuestionFeedback;
    private Button btnOptionA,btnOptionB,btnOptionC,buttonNext;
    private ProgressBar progressBar;

    private List<QuestionItem> allQuestionsList,questionsToAnswer;
    private long totalQuestionsToAnswer = 0;
    private CountDownTimer countdownTimer;
    private Boolean canAnswer = false;
    private int currentQuestion = 0, correctAnswers = 0, wrongAnswers = 0, notAnswered = 0;

    public QuizFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvQuizName = view.findViewById(R.id.quiz_title);
        tvQuestionNumber = view.findViewById(R.id.quiz_question_number);
        tvTimer = view.findViewById(R.id.quiz_question_time);
        tvQuestion = view.findViewById(R.id.quiz_question);
        tvQuestionFeedback = view.findViewById(R.id.quiz_question_feedback);
        btnOptionA = view.findViewById(R.id.quiz_option_one);
        btnOptionB = view.findViewById(R.id.quiz_option_two);
        btnOptionC = view.findViewById(R.id.quiz_option_three);
        buttonNext = view.findViewById(R.id.quiz_next_btn);
        progressBar = view.findViewById(R.id.quiz_question_progress);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        navController = Navigation.findNavController(view);

        if (mUser != null){
            uID = mUser.getUid();
        }

        allQuestionsList = new ArrayList<>();
        questionsToAnswer = new ArrayList<>();

        quizId = QuizFragmentArgs.fromBundle(getArguments()).getQuizId();
        totalQuestionsToAnswer = QuizFragmentArgs.fromBundle(getArguments()).getTotalQuestions();
        quizName = QuizFragmentArgs.fromBundle(getArguments()).getQuizName();

        firebaseFirestore.collection("QuizList").document(quizId).collection("Questions")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    allQuestionsList = task.getResult().toObjects(QuestionItem.class);
                    pickQuestions();

                } else {
                    tvQuestion.setText("Error loading question");
                }
            }
        });

        btnOptionA.setOnClickListener(this);
        btnOptionB.setOnClickListener(this);
        btnOptionC.setOnClickListener(this);
        buttonNext.setOnClickListener(this);

    }

    private void loadUI(){
        tvQuizName.setText(quizName);

        enableOptions(true);
        loadQuestions(0);
    }

    private void loadQuestions(int i) {
        QuestionItem questionItem = questionsToAnswer.get(i);

        tvQuestion.setText(questionItem.getQuestion());
        tvQuestionNumber.setText(String.valueOf(i+1));
        btnOptionA.setText(questionItem.getOptionA());
        btnOptionB.setText(questionItem.getOptionB());
        btnOptionC.setText(questionItem.getOptionC());

        canAnswer = true;
        currentQuestion = i;

        startTimer(questionItem.getTime()+1);
    }

    private void startTimer(final long time) {
        tvTimer.setText(String.valueOf(time));

        countdownTimer = new CountDownTimer(time*1000,10){

            @Override
            public void onTick(long l) {
                tvTimer.setText(String.valueOf(l/1000));
                Long progress = l/(time*10);
                progressBar.setProgress(progress.intValue());
            }

            @Override
            public void onFinish() {
                canAnswer = false;
                notAnswered++;
                enableOptions(false);
                tvQuestionFeedback.setText("Times Up ! Answer was submitted");
            }
        };

        countdownTimer.start();
    }

    private void enableOptions(Boolean enable) {
        btnOptionA.setEnabled(enable);
        btnOptionB.setEnabled(enable);
        btnOptionC.setEnabled(enable);

        buttonNext.setEnabled(!enable);

        if (enable){
            tvQuestionFeedback.setVisibility(View.INVISIBLE);
            buttonNext.setVisibility(View.INVISIBLE);
        } else {
            if (currentQuestion == totalQuestionsToAnswer - 1){
                buttonNext.setText("Submit Results");
            }
            tvQuestionFeedback.setVisibility(View.VISIBLE);
            buttonNext.setVisibility(View.VISIBLE);
        }
    }

    private void pickQuestions() {
        for (int i = 0; i < totalQuestionsToAnswer; i++){
            int randomNumber = getRandomNumber(0,allQuestionsList.size());
            questionsToAnswer.add(allQuestionsList.get(randomNumber));
            allQuestionsList.remove(randomNumber);
        }

        loadUI();
    }

    public static int getRandomNumber(int minimum, int maximum){
        return ((int) (Math.random() * (maximum - minimum))) + minimum;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.quiz_option_one:
                verifyAnswer(btnOptionA);
                break;
            case R.id.quiz_option_two:
                verifyAnswer(btnOptionB);
                break;
            case R.id.quiz_option_three:
                verifyAnswer(btnOptionC);
                break;
            case R.id.quiz_next_btn:
                if (currentQuestion < totalQuestionsToAnswer - 1){
                    currentQuestion++;
                    loadQuestions(currentQuestion);
                    resetOptions();
                } else {
                    QuizFragmentDirections.ActionQuizFragmentToResultFragment action = QuizFragmentDirections.actionQuizFragmentToResultFragment();
                    action.setQuizId(quizId);
                    action.setCorrectAnswer(correctAnswers);
                    action.setWrongAnswer(wrongAnswers);
                    action.setNotAnswered(notAnswered);
                    navController.navigate(action);
                }
                break;
        }
    }

    private void resetOptions() {
        btnOptionA.setBackgroundResource(R.drawable.outline_light_button_background);
        btnOptionB.setBackgroundResource(R.drawable.outline_light_button_background);
        btnOptionC.setBackgroundResource(R.drawable.outline_light_button_background);

        btnOptionA.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        btnOptionB.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        btnOptionC.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

        enableOptions(true);

    }

    private void verifyAnswer(Button optionButton) {
        if (canAnswer){

            canAnswer = false;

            optionButton.setTextColor(getResources().getColor(R.color.white));

            if (questionsToAnswer.get(currentQuestion).getAnswer().equals(optionButton.getText())){
                optionButton.setBackgroundResource(R.drawable.correct_answer_background);
                tvQuestionFeedback.setText("Correct answer");
                correctAnswers++;
            } else {
                optionButton.setBackgroundResource(R.drawable.wrong_answer_background);
                tvQuestionFeedback.setText("Wrong answer");
                wrongAnswers++;
            }

            countdownTimer.cancel();
        }

        enableOptions(false);
    }

}
