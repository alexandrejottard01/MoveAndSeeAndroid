package com.henallux.moveandseeandroid.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Alexandre on 14-11-17.
 */

public class InterestPointWithVote {

    public InterestPoint interestPoint;
    public int average;

    public InterestPointWithVote(InterestPoint interestPoint, int average) {
        this.interestPoint = interestPoint;
        this.average = average;
    }

    public InterestPoint getInterestPoint() {
        return interestPoint;
    }

    public void setInterestPoint(InterestPoint interestPoint) {
        this.interestPoint = interestPoint;
    }

    public int getAverage() {
        return average;
    }

    public void setAverage(int average) {
        this.average = average;
    }
}
