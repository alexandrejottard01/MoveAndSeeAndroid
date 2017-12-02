package com.henallux.moveandseeandroid.Model;

/**
 * Created by Alexandre on 14-11-17.
 */

public class DescriptionWithVote {

    public Description description;
    public int average;

    public DescriptionWithVote(Description description, int average) {
        this.description = description;
        this.average = average;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }
}


