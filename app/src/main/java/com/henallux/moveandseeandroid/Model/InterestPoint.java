package com.henallux.moveandseeandroid.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Alexandre on 14-11-17.
 */

public class InterestPoint {

    public long idInterestPoint;
    public long idUser;
    public double latitude;
    public double longitude;
    public String name;
    public Date dateCreation;
    public User idUserNavigation;

    public InterestPoint(long idInterestPoint, long idUser, double latitude, double longitude, String name, Date dateCreation, User idUserNavigation) {
        this.idInterestPoint = idInterestPoint;
        this.idUser = idUser;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.dateCreation = dateCreation;
        this.idUserNavigation = idUserNavigation;
    }
}
