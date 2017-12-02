package com.henallux.moveandseeandroid.DAO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.henallux.moveandseeandroid.Model.AccessToken;
import com.henallux.moveandseeandroid.Model.Constants;
import com.henallux.moveandseeandroid.Model.VoteDescription;
import com.henallux.moveandseeandroid.Model.VoteInterestPoint;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Alexandre on 20-11-17.
 */

public class VoteDescriptionDAO {
    public int addVoteDescription(String token, VoteDescription voteDescription)throws Exception{
        Gson gson = new GsonBuilder().create();
        String outputJsonString = gson.toJson(voteDescription);
        URL url = new URL(Constants.ADDRESS_API + "VoteDescription/AddVoteDescription");
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + token);
        connection.setDoInput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        byte[] outputBytes = outputJsonString.getBytes("UTF-8");
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(outputBytes);
        outputStream.flush();
        outputStream.close();

        return connection.getResponseCode();
    }
}
