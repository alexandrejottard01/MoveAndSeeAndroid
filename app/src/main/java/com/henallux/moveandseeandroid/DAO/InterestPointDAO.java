package com.henallux.moveandseeandroid.DAO;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.henallux.moveandseeandroid.Model.AccessToken;
import com.henallux.moveandseeandroid.Model.Constants;
import com.henallux.moveandseeandroid.Model.Description;
import com.henallux.moveandseeandroid.Model.InterestPoint;
import com.henallux.moveandseeandroid.Model.InterestPointWithVote;
import com.henallux.moveandseeandroid.Model.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Alex on 16-11-17.
 */

public class InterestPointDAO {
    public ArrayList<InterestPointWithVote> getAllInterestPoint(String token)throws Exception{
        ArrayList<InterestPointWithVote> listInterestPoint = new ArrayList<>();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        URL url = new URL(Constants.ADDRESS_API + "InterestPoint/GetAllInterestPoints");
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + token);
        connection.setDoInput(true);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();
        String inputJsonString = "",line;

        while((line = bufferedReader.readLine()) != null){
            stringBuilder.append(line);
        }

        bufferedReader.close();
        connection.disconnect();
        inputJsonString = stringBuilder.toString();
        JSONArray jsonArray = new JSONArray(inputJsonString);
        for (int i = 0; i<jsonArray.length();i++){
            listInterestPoint.add(gson.fromJson(jsonArray.getJSONObject(i).toString(),InterestPointWithVote.class));
        }

        return listInterestPoint;
    }

    public InterestPointWithVote getInterestPointById(String token, long idInterestPoint)throws Exception{
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        URL url = new URL(Constants.ADDRESS_API + "InterestPoint/GetInterestPointById/"+idInterestPoint);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + token);
        connection.setDoInput(true);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();
        String inputJsonString = "",line;

        while((line = bufferedReader.readLine()) != null){
            stringBuilder.append(line);
        }

        bufferedReader.close();
        connection.disconnect();
        inputJsonString = stringBuilder.toString();
        InterestPointWithVote interestPoint = gson.fromJson(inputJsonString,InterestPointWithVote.class);

        return interestPoint;
    }
}
