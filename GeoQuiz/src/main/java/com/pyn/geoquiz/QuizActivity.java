package com.pyn.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_QUESTION_ANSWERED = "answered";
    private static final String KEY_TRUE_ANSWER_COUNT = "true_answer_count";
    private static final String KEY_IS_QUESTIONS_CHEATER = "is_questions_cheater";
    private static final int REQUEST_CODE_CHEAT = 0;

    private TextView tvQuestion, tvCheatCount;
    private Button btnTrue, btnFalse, btnNext, btnLast, btnCheat;
    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true)
    };
    private boolean[] mQuestionsAnswered = new boolean[mQuestionBank.length];
    private int mCurrentIndex = 0;
    private int mTrueAnswerCount = 0;
    private boolean[] isQuestionsCheater = new boolean[mQuestionBank.length];
    private int mCheatCount = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mQuestionsAnswered = savedInstanceState.getBooleanArray(KEY_QUESTION_ANSWERED);
            mTrueAnswerCount = savedInstanceState.getInt(KEY_TRUE_ANSWER_COUNT);
            isQuestionsCheater = savedInstanceState.getBooleanArray(KEY_IS_QUESTIONS_CHEATER);
        }

        btnTrue = findViewById(R.id.btn_true);
        btnFalse = findViewById(R.id.btn_false);
        tvQuestion = findViewById(R.id.tv_question);
        btnNext = findViewById(R.id.btn_next);
        btnLast = findViewById(R.id.btn_last);
        btnCheat = findViewById(R.id.btn_cheat);
        tvCheatCount = findViewById(R.id.tv_cheat_count);

        Log.d(TAG, "Current question index: " + mCurrentIndex);
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        tvQuestion.setText(question);

        btnTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(true);
            }
        });

        btnFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(false);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        btnLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + mQuestionBank.length - 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        tvQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        btnCheat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });

        updateQuestion();
        checkCheatCount();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_INDEX, mCurrentIndex);
        outState.putBooleanArray(KEY_QUESTION_ANSWERED, mQuestionsAnswered);
        outState.putInt(KEY_TRUE_ANSWER_COUNT, mTrueAnswerCount);
        outState.putBooleanArray(KEY_IS_QUESTIONS_CHEATER, isQuestionsCheater);
    }

    /**
     * 更新问题
     */
    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        tvQuestion.setText(question);

        setBtnEnabled(!mQuestionsAnswered[mCurrentIndex]);
    }

    /**
     * 检查问题答案
     *
     * @param userPressedTrue
     */
    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
        if (checkCheatCount() == 0) {
            messageResId = R.string.judgment_toast;
        } else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
                mTrueAnswerCount++;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }
        setBtnEnabled(false);
        mQuestionsAnswered[mCurrentIndex] = true;

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();

        getScoreResult();
    }

    private void setBtnEnabled(boolean enabled) {
        btnTrue.setEnabled(enabled);
        btnFalse.setEnabled(enabled);
    }

    /**
     * 评分
     */
    private void getScoreResult() {

        boolean isAllAnswered = true;
        for (int i = 0; i < mQuestionBank.length; i++) {

            if (!mQuestionsAnswered[i]) {
                isAllAnswered = false;
                return;
            }
        }
        if (isAllAnswered) {
            Toast.makeText(this, mTrueAnswerCount * 100 / mQuestionBank.length + "%", Toast.LENGTH_LONG).show();
        }
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
            if (CheatActivity.wasAnswerShown(data)) {
                isQuestionsCheater[mCurrentIndex] = true;
                checkCheatCount();
            }
        }
    }

    /**
     * 检查偷看次数
     */
    private int checkCheatCount() {

        int usedCheatCount = 0;
        for (int i = 0; i < mQuestionBank.length; i++) {
            if (isQuestionsCheater[i]) {
                usedCheatCount++;
                if (usedCheatCount == 3) {
                    btnCheat.setEnabled(false);
                    break;
                }
            }
        }
        int reCount = mCheatCount - usedCheatCount;
        tvCheatCount.setText("还剩下 " + reCount + " 次偷看答案机会");
        return reCount;
    }
}
