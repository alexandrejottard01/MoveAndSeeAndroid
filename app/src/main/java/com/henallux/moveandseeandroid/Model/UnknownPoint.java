package com.henallux.moveandseeandroid.Model;

import java.util.Date;

/**
 * Created by Alexandre on 14-11-17.
 */

public class UnknownPoint extends Point{

    public long idUnknownPoint;

    public UnknownPoint(String idUser, double latitude, double longitude, Date dateCreation) {
        this.idUser = idUser;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dateCreation = dateCreation;
    }
}
