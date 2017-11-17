package com.henallux.moveandseeandroid.DAO;

import com.henallux.moveandseeandroid.Model.InterestPoint;
import com.henallux.moveandseeandroid.Model.InterestPointWithVote;
import com.henallux.moveandseeandroid.Model.User;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Alex on 16-11-17.
 */

public class InterestPointDAO {
    public ArrayList<InterestPointWithVote> getAllInterestPoints(){

        User u1 = new User(1,"Chronix","$2y$10$xDNtN7tc6AQdWXHNl0NhMedD2O8LfEOl2BGSdrdxNbfJMv9c1LKM2",false,"Alexandre Jottard","alexandre.jottard@gmail.com","français",true, new Date(1993, 07, 05),true,"AAAAAAAAD6o=".getBytes());
        User u2 = new User(2,"Cha","$2y$10$xDNtN7tc6AQdWXHNl0NhMedD2O8LfEOl2BGSdrdxNbfJMv9c1LKM2",false,"Charlotte Colin","charlotte.colin@gmail.com","français",true, new Date(1996, 06, 13),true,"AAAAAAAAD6o=".getBytes());

        InterestPoint iP1 = new InterestPoint(1,1,50.469262,4.862445,"Gare de Namur",new Date(2017,11,6),u1);
        InterestPoint iP2 = new InterestPoint(2,2,50.464326, 4.860263,"Place saint Aubain",new Date(2017,11,8),u2);

        InterestPointWithVote iPV1 = new InterestPointWithVote(iP1,78);
        InterestPointWithVote iPV2 = new InterestPointWithVote(iP2,84);

        ArrayList<InterestPointWithVote> listInterestPoint = new ArrayList<>();

        listInterestPoint.add(iPV1);
        listInterestPoint.add(iPV2);

        return listInterestPoint;
    }
}
