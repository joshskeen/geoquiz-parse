package com.bignerdranch.android.geoquiz.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("ParseQuizQuestion")
public class ParseQuizQuestion extends ParseObject {

    public static final String MODEL_NAME = "ParseQuizQuestion";

    public static final String QUESTION_FIELD_KEY = "question";
    public static final String ANSWER_IS_TRUE = "is_true";

    public ParseQuizQuestion() {
    }

    public String getQuestionText() {
        return getString(QUESTION_FIELD_KEY);
    }

    public void setQuestion(String question) {
        put(QUESTION_FIELD_KEY, question);
    }

    public boolean isAnswerTrue() {
        return getBoolean(ANSWER_IS_TRUE);
    }

    public void setIsAnswerTrue(boolean isTrue) {
        put(ANSWER_IS_TRUE, isTrue);
    }

    @Override
    public String toString() {
        return "ParseQuizQuestion{" + getQuestionText() + ", }";
    }

}
