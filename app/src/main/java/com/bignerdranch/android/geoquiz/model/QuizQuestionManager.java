package com.bignerdranch.android.geoquiz.model;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

public class QuizQuestionManager {


    public static void registerParseObjects() {
        ParseObject.registerSubclass(ParseQuizQuestion.class);
    }

    public void addQuestion(String question, boolean isTrue, final QuizAddedCallback quizAddedCallback) {
        ParseQuizQuestion parseQuizQuestion = new ParseQuizQuestion();
        parseQuizQuestion.setQuestion(question);
        parseQuizQuestion.setIsAnswerTrue(isTrue);

        parseQuizQuestion.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                quizAddedCallback.onQuizAdded();
            }
        });
    }

    public void loadQuestions(final QuizLoadedCallback callback) {
        ParseQuery<ParseQuizQuestion> query = new ParseQuery<>(ParseQuizQuestion.MODEL_NAME);
        query.findInBackground(new FindCallback<ParseQuizQuestion>() {
            @Override
            public void done(List<ParseQuizQuestion> list, ParseException e) {
                callback.onQuestionsLoaded(list);
            }
        });
    }
}
