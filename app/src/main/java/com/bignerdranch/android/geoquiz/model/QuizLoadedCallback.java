package com.bignerdranch.android.geoquiz.model;

import java.util.List;

public interface QuizLoadedCallback {

    void onQuestionsLoaded(List<ParseQuizQuestion> questions);

}
