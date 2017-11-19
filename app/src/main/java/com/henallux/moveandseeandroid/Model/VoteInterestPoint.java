package com.henallux.moveandseeandroid.Model;

/**
 * Created by Alexandre on 14-11-17.
 */

public class VoteInterestPoint {

    public boolean isPositiveAssessment;
    public long idUser;
    public long idInterestPoint;
    public InterestPoint idInterestPointNavigation;
    public User idUserNavigation;

    public VoteInterestPoint(boolean isPositiveAssessment, long idUser, long idInterestPoint) {
        this.isPositiveAssessment = isPositiveAssessment;
        this.idUser = idUser;
        this.idInterestPoint = idInterestPoint;
    }
}


