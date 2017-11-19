package com.henallux.moveandseeandroid.Model;

import java.util.Date;

/**
 * Created by Alexandre on 14-11-17.
 */

public class UnknownPoint {

    public long idUnknownPoint;
    public long idUser;
    public double latitude;
    public double longitude;
    public Date dateCreation;
    public User idUserNavigation;

    public UnknownPoint(long idUser, double latitude, double longitude, Date dateCreation) {
        this.idUser = idUser;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dateCreation = dateCreation;
    }


}
