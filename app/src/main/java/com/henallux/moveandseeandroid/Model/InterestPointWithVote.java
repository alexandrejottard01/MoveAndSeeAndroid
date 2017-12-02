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
}
