package com.techdoctorbd.quizapplication.Interfaces;

import com.techdoctorbd.quizapplication.Models.QuizItem;

import java.util.List;

public interface onFireStoreTaskComplete{
    void quizListDataAdded(List<QuizItem> quizItemList);
    void onError(Exception e);
}
