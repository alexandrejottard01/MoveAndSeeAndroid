package com.henallux.moveandseeandroid.Model;

/**
 * Created by Alexandre on 14-11-17.
 */

public class Description {

    public long idDescription;
    public String explication;
    public long idUser;
    public long idInterestPoint;
    public InterestPoint idInterestPointNavigation;
    public User idUserNavigation;

    public Description(long idDescription, String explication, long idUser, long idInterestPoint, InterestPoint idInterestPointNavigation, User idUserNavigation) {
        this.idDescription = idDescription;
        this.explication = explication;
        this.idUser = idUser;
        this.idInterestPoint = idInterestPoint;
        this.idInterestPointNavigation = idInterestPointNavigation;
        this.idUserNavigation = idUserNavigation;
    }

    public Description(String explication, long idUser, long idInterestPoint, InterestPoint idInterestPointNavigation, User idUserNavigation) {
        this.explication = explication;
        this.idUser = idUser;
        this.idInterestPoint = idInterestPoint;
        this.idInterestPointNavigation = idInterestPointNavigation;
        this.idUserNavigation = idUserNavigation;
    }

    public Description(String explication, long idUser, long idInterestPoint) {
        this.explication = explication;
        this.idUser = idUser;
        this.idInterestPoint = idInterestPoint;
    }

    public Description(String explication, long idUser, InterestPoint idInterestPointNavigation) {
        this.explication = explication;
        this.idUser = idUser;
        this.idInterestPointNavigation = idInterestPointNavigation;
    }



    public long getIdDescription() {
        return idDescription;
    }

    public void setIdDescription(long idDescription) {
        this.idDescription = idDescription;
    }

    public String getExplication() {
        return explication;
    }

    public void setExplication(String explication) {
        this.explication = explication;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public long getIdInterestPoint() {
        return idInterestPoint;
    }

    public void setIdInterestPoint(long idInterestPoint) {
        this.idInterestPoint = idInterestPoint;
    }

    public InterestPoint getIdInterestPointNavigation() {
        return idInterestPointNavigation;
    }

    public void setIdInterestPointNavigation(InterestPoint idInterestPointNavigation) {
        this.idInterestPointNavigation = idInterestPointNavigation;
    }

    public User getIdUserNavigation() {
        return idUserNavigation;
    }

    public void setIdUserNavigation(User idUserNavigation) {
        this.idUserNavigation = idUserNavigation;
    }
}
