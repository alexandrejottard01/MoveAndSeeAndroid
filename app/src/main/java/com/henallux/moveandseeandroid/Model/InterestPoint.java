package com.henallux.moveandseeandroid.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Alexandre on 14-11-17.
 */

public class InterestPoint extends Point{

    public long idInterestPoint;
    public String name;
    public User idUserNavigation;

    public InterestPoint(long idInterestPoint, String idUser, double latitude, double longitude, String name, Date dateCreation, User idUserNavigation) {
        this.idInterestPoint = idInterestPoint;
        this.idUser = idUser;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.dateCreation = dateCreation;
        this.idUserNavigation = idUserNavigation;
    }

    public InterestPoint(String idUser, double latitude, double longitude, String name, Date dateCreation) {
        this.idUser = idUser;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.dateCreation = dateCreation;
    }
}
