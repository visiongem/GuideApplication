package com.pyn.draganddraw;

import android.graphics.PointF;

/**
 * 矩形框
 * Created by pengyanni on 2018/3/7.
 */
public class Box {

    private PointF mOrigin;
    private PointF mCurrent;

    public Box(PointF origin) {
        mOrigin = origin;
        mCurrent = origin;
    }

    public PointF getOrigin() {
        return mOrigin;
    }

    public PointF getCurrent() {
        return mCurrent;
    }

    public void setCurrent(PointF current) {
        mCurrent = current;
    }
}
