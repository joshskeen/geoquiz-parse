package com.bignerdranch.android.geoquiz;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.bignerdranch.android.geoquiz.model.QuizAddedCallback;
import com.bignerdranch.android.geoquiz.model.QuizQuestionManager;

public class AddQuestionActivity extends AppCompatActivity implements QuizAddedCallback {

    private CheckBox mQuestionTrueCheckbox;
    private EditText mQuestionText;
    private Button mSaveQuestionButton;
    private QuizQuestionManager mQuizQuestionManager;
    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);
        mQuestionTrueCheckbox = (CheckBox) findViewById(R.id.question_true);
        mQuestionText = (EditText) findViewById(R.id.question_text);
        mSaveQuestionButton = (Button) findViewById(R.id.save_question);
        mQuizQuestionManager = new QuizQuestionManager();
        mSaveQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuizQuestionManager.addQuestion(mQuestionText.getText().toString(),
                        mQuestionTrueCheckbox.isChecked(),
                        AddQuestionActivity.this);
                mProgressDialog = ProgressDialog.show(AddQuestionActivity.this, "", getString(R.string.questions_saving_text), true);
            }
        });
    }

    @Override
    public void onQuizAdded() {
        Toast.makeText(this, R.string.quiz_added, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

}
