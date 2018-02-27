package com.pyn.criminalintent;

import java.util.Date;
import java.util.UUID;

/**
 * Created by pengyanni on 2018/2/8.
 */
public class Crime {

    /* UUID是Android框架里的Java工具类 */
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    /* 是否需警方介入 */
    private boolean mRequiresPolice = false;

    public Crime() {
        // 产生一个随机唯一ID值
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public boolean isRequiresPolice() {
        return mRequiresPolice;
    }

    public void setRequiresPolice(boolean requiresPolice) {
        mRequiresPolice = requiresPolice;
    }
}
