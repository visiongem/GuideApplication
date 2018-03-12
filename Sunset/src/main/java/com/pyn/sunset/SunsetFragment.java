package com.pyn.sunset;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

/**
 * Created by pengyanni on 2018/3/12.
 */
public class SunsetFragment extends Fragment {

    private View mSceneView;
    private View mSunView;
    private View mSkyView;

    private int mBlueSkyColor;
    private int mSunsetSkyColor;
    private int mNightSkyColor;

    private ObjectAnimator sunsetSkyAnimator;
    private ObjectAnimator nightSkyAnimator;

    private float sunYFirstStart;
    private float sunYFirstEnd;

    private boolean isFirstClick = true; // 是否第一次点击

    private ObjectAnimator downAnimatior;
    private AnimatorSet downAnimatorSet;
    private ObjectAnimator upAnimatior;
    private AnimatorSet upAnimatorSet;

    private boolean isSunDown = true; // 是否落日

    public static SunsetFragment newInstance() {
        SunsetFragment fragment = new SunsetFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sunset, container, false);

        mSceneView = view;
        mSunView = view.findViewById(R.id.sun);
        mSkyView = view.findViewById(R.id.sky);

        mBlueSkyColor = ContextCompat.getColor(getActivity(), R.color.blue_sky);
        mSunsetSkyColor = ContextCompat.getColor(getActivity(), R.color.sunset_sky);
        mNightSkyColor = ContextCompat.getColor(getActivity(), R.color.night_sky);

        initSkyAnimation();

        mSceneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFirstClick) {
                    sunYFirstStart = mSunView.getTop();
                    sunYFirstEnd = mSkyView.getHeight();
                    initUpDownAnimation();
                    isFirstClick = false;
                }
                if(!downAnimatorSet.isRunning() && !upAnimatorSet.isRunning()){
                    if (isSunDown) {
                        downAnimatorSet.start();
                    } else {
                        upAnimatorSet.start();
                    }
                    isSunDown = !isSunDown;
                }
            }
        });

        return view;
    }

    /**
     * 初始化天空颜色两种动画
     */
    private void initSkyAnimation() {
        sunsetSkyAnimator = ObjectAnimator.ofInt(mSkyView, "backgroundColor", mBlueSkyColor, mSunsetSkyColor).setDuration(3000);
        sunsetSkyAnimator.setEvaluator(new ArgbEvaluator());
        nightSkyAnimator = ObjectAnimator.ofInt(mSkyView, "backgroundColor", mSunsetSkyColor, mBlueSkyColor).setDuration(3000);
        nightSkyAnimator.setEvaluator(new ArgbEvaluator());
    }

    /**
     *  初始化落日，升日动画
     */
    private void initUpDownAnimation() {
        downAnimatior = ObjectAnimator.ofFloat(mSunView, "y", sunYFirstStart, sunYFirstEnd).setDuration(3000);
        downAnimatior.setInterpolator(new AccelerateInterpolator());
        downAnimatorSet = new AnimatorSet();
        downAnimatorSet.play(downAnimatior).with(sunsetSkyAnimator);

        upAnimatior = ObjectAnimator.ofFloat(mSunView, "y", sunYFirstEnd, sunYFirstStart).setDuration(3000);
        upAnimatior.setInterpolator(new AccelerateInterpolator());
        upAnimatorSet = new AnimatorSet();
        upAnimatorSet.play(upAnimatior).with(nightSkyAnimator);
    }

}
