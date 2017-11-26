package com.henallux.moveandseeandroid.Model;

/**
 * Created by Alexandre on 14-11-17.
 */

public class VoteDescription {

    public boolean isPositiveAssessment;
    public String idUser;
    public long idDescription;
    public Description idDescriptionNavigation;
    public User idUserNavigation;

    public VoteDescription(boolean isPositiveAssessment, String idUser, long idDescription) {
        this.isPositiveAssessment = isPositiveAssessment;
        this.idUser = idUser;
        this.idDescription = idDescription;
    }
}
