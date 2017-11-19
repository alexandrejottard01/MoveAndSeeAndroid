package com.henallux.moveandseeandroid.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Alexandre on 14-11-17.
 */

public class InterestPointWithVote {

    public InterestPoint interestPoint;
    public int moyenne;

    public InterestPointWithVote(InterestPoint interestPoint, int moyenne) {
        this.interestPoint = interestPoint;
        this.moyenne = moyenne;
    }
}
