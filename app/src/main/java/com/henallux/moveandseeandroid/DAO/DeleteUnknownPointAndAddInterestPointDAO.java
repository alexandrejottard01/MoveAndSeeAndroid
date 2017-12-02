package com.henallux.moveandseeandroid.DAO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.henallux.moveandseeandroid.Model.Constants;
import com.henallux.moveandseeandroid.Model.Description;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Alexandre on 23-11-17.
 */

public class DeleteUnknownPointAndAddInterestPointDAO {
    public int deleteUnknownPointAndAddInterestPoint(String token, Description description, long idUnknownPoint)throws Exception{
        Gson gson = new GsonBuilder().create();
        String outputJsonString = gson.toJson(description);
        URL url = new URL(Constants.ADDRESS_API + "DeleteUnknownPointAndInterestPoint/DeleteUnknownPointAndInterestPoint/"+idUnknownPoint);
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
