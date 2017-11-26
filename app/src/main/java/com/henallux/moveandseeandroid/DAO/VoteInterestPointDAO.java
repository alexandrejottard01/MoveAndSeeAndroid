package com.henallux.moveandseeandroid.DAO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.henallux.moveandseeandroid.Model.AccessToken;
import com.henallux.moveandseeandroid.Model.InterestPoint;
import com.henallux.moveandseeandroid.Model.VoteInterestPoint;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Alexandre on 19-11-17.
 */

public class VoteInterestPointDAO {

    public int addVoteInterestPoint(String token, VoteInterestPoint voteInterestPoint)throws Exception{

        Gson gson = new GsonBuilder().create();
        String outputJsonString = gson.toJson(voteInterestPoint);

        URL url = new URL("http://moveandsee.azurewebsites.net/api/VoteInterestPoint/AddVoteInterestPoint");
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + token);
        connection.setDoInput(true);
        connection.setRequestProperty("Content-Type", "application/json");

        byte[] outputBytes = outputJsonString.getBytes("UTF-8");
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(outputBytes); //Execution du post
        outputStream.flush();
        outputStream.close();


        return connection.getResponseCode();
    }
}
