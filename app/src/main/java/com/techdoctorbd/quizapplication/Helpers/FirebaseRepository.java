package com.techdoctorbd.quizapplication.Helpers;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.techdoctorbd.quizapplication.Interfaces.onFireStoreTaskComplete;
import com.techdoctorbd.quizapplication.Models.QuizItem;
import java.util.Objects;

public class FirebaseRepository {
    private onFireStoreTaskComplete onFireStoreTaskComplete;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private Query quizQuery = firebaseFirestore.collection("QuizList").whereEqualTo("visibility","public");

    public FirebaseRepository(onFireStoreTaskComplete onFireStoreTaskComplete){
        this.onFireStoreTaskComplete = onFireStoreTaskComplete;
    }

    public void getQuizData(){
        quizQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    onFireStoreTaskComplete.quizListDataAdded(Objects.requireNonNull(task.getResult()).toObjects(QuizItem.class));
                } else {
                    onFireStoreTaskComplete.onError(task.getException());
                }
            }
        });
    }
}
