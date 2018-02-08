package com.pyn.geoquiz;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String TAG = "CheatActivity";

    private static final String EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown";
    private static final String KEY_IS_ANSWER_SHOW = "is_answer_show";

    private TextView tvAnswer, tvCompileVersion;
    private Button btnShowAnswer;
    private boolean mAnswerIsTrue;

    private boolean isAnswerShow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        if (savedInstanceState != null) {
            isAnswerShow = savedInstanceState.getBoolean(KEY_IS_ANSWER_SHOW, false);
            Log.d(TAG, "savedInstanceState: " + isAnswerShow);
        }

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        tvAnswer = findViewById(R.id.tv_answer);
        btnShowAnswer = findViewById(R.id.btn_show_answer);
        tvCompileVersion = findViewById(R.id.tv_compile_version);
        tvCompileVersion.setText("API Lever " + Build.VERSION.SDK_INT);

        btnShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAnswerShow = true;
                if (mAnswerIsTrue) {
                    tvAnswer.setText(R.string.true_button);
                } else {
                    tvAnswer.setText(R.string.false_button);
                }

//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    int cx = btnShowAnswer.getWidth() / 2;
//                    int cy = btnShowAnswer.getHeight() / 2;
//                    float radius = btnShowAnswer.getWidth();
//                    Animator anim = ViewAnimationUtils
//                            .createCircularReveal(btnShowAnswer, cx, cy, radius, 0);
//                    anim.addListener(new AnimatorListenerAdapter() {
//                        @Override
//                        public void onAnimationEnd(Animator animation) {
//                            super.onAnimationEnd(animation);
//                            btnShowAnswer.setVisibility(View.INVISIBLE);
//                        }
//                    });
//                    anim.start();
//                } else {
//                    btnShowAnswer.setVisibility(View.INVISIBLE);
//                }
            }
        });
    }

    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShow);
        setResult(RESULT_OK, data);
        Log.d(TAG, "onBackPressed: " + isAnswerShow);
        super.onBackPressed();
    }

    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_IS_ANSWER_SHOW, isAnswerShow);
        Log.d(TAG, "onSaveInstanceState: " + isAnswerShow);
    }
}
