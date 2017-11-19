package com.henallux.moveandseeandroid.Model;

/**
 * Created by Alexandre on 14-11-17.
 */

public class DescriptionWithVote {

    public Description description;
    public int moyenne;

    public DescriptionWithVote(Description description, int moyenne) {
        this.description = description;
        this.moyenne = moyenne;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public int getMoyenne() {
        return moyenne;
    }

    public void setMoyenne(int moyenne) {
        this.moyenne = moyenne;
    }
}


