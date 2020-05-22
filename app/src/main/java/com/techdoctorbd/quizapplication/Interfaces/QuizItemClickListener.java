package com.techdoctorbd.quizapplication.Interfaces;

import android.widget.ImageView;
import android.widget.TextView;

public interface QuizItemClickListener {
    void onQuizItemClicked(int position, ImageView quizImage, TextView quizHeading);
}
