package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.geoquiz.model.ParseQuizQuestion;
import com.bignerdranch.android.geoquiz.model.QuizLoadedCallback;
import com.bignerdranch.android.geoquiz.model.QuizQuestionManager;

import java.util.List;

public class QuizActivity extends AppCompatActivity implements QuizLoadedCallback {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final int REQUEST_CODE_CHEAT = 0;
    private static final int REQUEST_CODE_ADD_QUESTION = 1;

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mAddQuestionButton;
    private Button mCheatButton;
    private TextView mQuestionTextView;

    private int mCurrentIndex = 0;

    private boolean mIsCheater;
    private QuizQuestionManager mQuizQuestionManager;
    List<ParseQuizQuestion> mParseQuizQuestionBank;
    private ProgressDialog mProgressDialog;

    private void updateQuestion() {
        mQuestionTextView.setText(mParseQuizQuestionBank.get(mCurrentIndex).getQuestionText());
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mParseQuizQuestionBank.get(mCurrentIndex).isAnswerTrue();

        int messageResId = 0;

        if (mIsCheater) {
            messageResId = R.string.judgment_toast;
        } else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onQuestionsLoaded(List<ParseQuizQuestion> questions) {
        mProgressDialog.hide();
        mParseQuizQuestionBank = questions;
        if (mParseQuizQuestionBank.size() == 0) {
            startAddQuestionActivity();
        } else {
            updateQuestion();
        }
    }

    private void startAddQuestionActivity() {
        startActivityForResult(new Intent(this, AddQuestionActivity.class), REQUEST_CODE_ADD_QUESTION);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQuizQuestionManager = new QuizQuestionManager();
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mTrueButton = (Button) findViewById(R.id.true_button);
        mAddQuestionButton = (Button) findViewById(R.id.add_question_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        mAddQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddQuestionActivity();
            }
        });
        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mParseQuizQuestionBank.size();
                mIsCheater = false;
                updateQuestion();
            }
        });

        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answerIsTrue = mParseQuizQuestionBank.get(mCurrentIndex).isAnswerTrue();
                Intent i = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(i, REQUEST_CODE_CHEAT);
            }
        });

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }
        mProgressDialog = ProgressDialog.show(this, "", getString(R.string.questions_loading_text), true);
        mQuizQuestionManager.loadQuestions(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_INDEX, mCurrentIndex);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        } else if (requestCode == REQUEST_CODE_ADD_QUESTION) {
            //reload questions
            mQuizQuestionManager.loadQuestions(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.quiz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
