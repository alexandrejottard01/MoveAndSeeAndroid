package com.henallux.moveandseeandroid.Model;

import java.util.Date;

/**
 * Created by Alexandre on 14-11-17.
 */

public class InterestPoint {

    public long idInterestPoint;
    public long idUser;
    public float latitude;
    public float longitude;
    public String name;
    public Date dateCreation;
    public User idUserNavigation;
}
