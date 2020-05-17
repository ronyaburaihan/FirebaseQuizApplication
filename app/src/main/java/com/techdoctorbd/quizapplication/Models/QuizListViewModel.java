package com.techdoctorbd.quizapplication.Models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.techdoctorbd.quizapplication.Helpers.FirebaseRepository;
import com.techdoctorbd.quizapplication.Interfaces.onFireStoreTaskComplete;

import java.util.List;

public class QuizListViewModel extends ViewModel implements onFireStoreTaskComplete {

    private MutableLiveData<List<QuizItem>> quizListMutableLiveData = new MutableLiveData<>();

    public LiveData<List<QuizItem>> getQuizListMutableLiveData() {
        return quizListMutableLiveData;
    }

    private FirebaseRepository firebaseRepository = new FirebaseRepository(this);

    public QuizListViewModel(){
        firebaseRepository.getQuizData();
    }

    @Override
    public void quizListDataAdded(List<QuizItem> quizItemList) {
        quizListMutableLiveData.setValue(quizItemList);
    }

    @Override
    public void onError(Exception e) {

    }
}
