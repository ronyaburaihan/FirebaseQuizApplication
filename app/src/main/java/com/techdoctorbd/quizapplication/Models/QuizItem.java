package com.techdoctorbd.quizapplication.Models;

import com.google.firebase.firestore.DocumentId;

public class QuizItem {

    @DocumentId
    private String quizId;
    private String title,description,imageUrl,difficulty,visibility;
    private long questions;

    public QuizItem() {
    }

    public QuizItem(String quizId, String title, String description, String imageUrl, String difficulty, String visibility, long questions) {
        this.quizId = quizId;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.difficulty = difficulty;
        this.visibility = visibility;
        this.questions = questions;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public long getQuestions() {
        return questions;
    }

    public void setQuestions(long questions) {
        this.questions = questions;
    }
}
