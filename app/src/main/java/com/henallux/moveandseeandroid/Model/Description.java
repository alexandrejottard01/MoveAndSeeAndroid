package com.henallux.moveandseeandroid.Model;

/**
 * Created by Alexandre on 14-11-17.
 */

public class Description {

    public long idDescription;
    public String explication;
    public String idUser;
    public long idInterestPoint;
    public InterestPoint idInterestPointNavigation;
    public User idUserNavigation;

    public Description(long idDescription, String explication, String idUser, long idInterestPoint, InterestPoint idInterestPointNavigation, User idUserNavigation) {
        this.idDescription = idDescription;
        this.explication = explication;
        this.idUser = idUser;
        this.idInterestPoint = idInterestPoint;
        this.idInterestPointNavigation = idInterestPointNavigation;
        this.idUserNavigation = idUserNavigation;
    }

    public Description(String explication, String idUser, long idInterestPoint, InterestPoint idInterestPointNavigation, User idUserNavigation) {
        this.explication = explication;
        this.idUser = idUser;
        this.idInterestPoint = idInterestPoint;
        this.idInterestPointNavigation = idInterestPointNavigation;
        this.idUserNavigation = idUserNavigation;
    }

    public Description(String explication, String idUser, long idInterestPoint) {
        this.explication = explication;
        this.idUser = idUser;
        this.idInterestPoint = idInterestPoint;
    }

    public Description(String explication, String idUser, InterestPoint idInterestPointNavigation) {
        this.explication = explication;
        this.idUser = idUser;
        this.idInterestPointNavigation = idInterestPointNavigation;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
}
