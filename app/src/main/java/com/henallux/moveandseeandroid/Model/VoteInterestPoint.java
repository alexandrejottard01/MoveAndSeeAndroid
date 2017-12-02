package com.henallux.moveandseeandroid.Model;

/**
 * Created by Alexandre on 14-11-17.
 */

public class VoteInterestPoint {

    public boolean isPositiveAssessment;
    public String idUser;
    public long idInterestPoint;

    public VoteInterestPoint(boolean isPositiveAssessment, String idUser, long idInterestPoint) {
        this.isPositiveAssessment = isPositiveAssessment;
        this.idUser = idUser;
        this.idInterestPoint = idInterestPoint;
    }
}


