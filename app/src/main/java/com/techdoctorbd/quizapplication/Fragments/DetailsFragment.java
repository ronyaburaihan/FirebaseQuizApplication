package com.techdoctorbd.quizapplication.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.techdoctorbd.quizapplication.Models.QuizItem;
import com.techdoctorbd.quizapplication.Models.QuizListViewModel;
import com.techdoctorbd.quizapplication.R;

import org.w3c.dom.Document;

import java.util.List;

public class DetailsFragment extends Fragment implements View.OnClickListener {

    private NavController navController;
    private FirebaseUser mUser;
    private FirebaseFirestore firebaseFirestore;

    private QuizListViewModel quizListViewModel;
    private int position;
    private long totalQuestions = 0;
    private TextView title,description,difficulty,questions,lastScore;
    private ImageView quizImage;
    private Button startQuiz;
    private String quizId,quizName,uID;

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        if (mUser != null){
            uID = mUser.getUid();
        }

        position = DetailsFragmentArgs.fromBundle(getArguments()).getPosition();

        //Initialize UI elements
        quizImage = view.findViewById(R.id.image_quiz_details);
        title = view.findViewById(R.id.title_quiz_details);
        description = view.findViewById(R.id.description_quiz_details);
        difficulty = view.findViewById(R.id.difficulty_text_quiz_details);
        questions = view.findViewById(R.id.total_questions_text_quiz_details);
        lastScore = view.findViewById(R.id.last_score_text_quiz_details);
        startQuiz = view.findViewById(R.id.start_button_quiz_details);

        startQuiz.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        quizListViewModel = new ViewModelProvider(requireActivity()).get(QuizListViewModel.class);
        quizListViewModel.getQuizListMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<QuizItem>>() {
            @Override
            public void onChanged(List<QuizItem> quizItems) {

                QuizItem quizItem = quizItems.get(position);

                Glide.with(getContext())
                        .load(quizItem.getImageUrl())
                        .centerCrop()
                        .placeholder(R.drawable.ic_placeholder_image)
                        .into(quizImage);

                title.setText(quizItem.getTitle());
                description.setText(quizItem.getDescription());
                difficulty.setText(quizItem.getDifficulty());
                questions.setText(String.valueOf(quizItem.getQuestions()));

                quizId = quizItem.getQuizId();
                totalQuestions = quizItem.getQuestions();
                quizName = quizItem.getTitle();

                showResults();


            }
        });
    }

    @Override
    public void onClick(View view) {
        DetailsFragmentDirections.ActionDetailsFragmentToQuizFragment action = DetailsFragmentDirections.actionDetailsFragmentToQuizFragment();
        action.setTotalQuestions(totalQuestions);
        action.setQuizId(quizId);
        action.setQuizName(quizName);
        navController.navigate(action);
    }

    void showResults(){
        firebaseFirestore.collection("QuizList").document(quizId).collection("Results").
                document(uID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot result = task.getResult();

                    if (result != null && result.exists()){
                        Long correct = result.getLong("correctAnswers");
                        Long wrong = result.getLong("wrongAnswers");
                        Long notAnswered = result.getLong("notAnswered");

                        long total = correct + wrong + notAnswered;
                        Long percentage = (correct*100) / total;

                        lastScore.setText(percentage+"%");
                    }
                }
            }
        });
    }
}
