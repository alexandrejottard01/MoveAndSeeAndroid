package com.henallux.moveandseeandroid.DAO;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

    public ArrayList<InterestPointWithVote> getAllInterestPoint()throws Exception{

        ArrayList<InterestPointWithVote> listInterestPoint = new ArrayList<>();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();

        URL url = new URL("http://moveandsee.azurewebsites.net/api/InterestPoint/GetAllInterestPoints");
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoInput(true);


            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream())); //Execute
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

    public int addInterestPoint(InterestPoint interestPoint)throws Exception{

        Gson gson = new GsonBuilder().create();
        String outputJsonString = gson.toJson(interestPoint);

        URL url = new URL("http://moveandsee.azurewebsites.net/api/InterestPoint/AddInterestPoint");
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setRequestProperty("Content-Type", "application/json");

        byte[] outputBytes = outputJsonString.getBytes("UTF-8");
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(outputBytes); //Execution du post
        outputStream.flush();
        outputStream.close();


        return connection.getResponseCode();
    }

    /* //Méthode bryan pour récupérer un objet
    public ArrayList<InterestPointWithVote> getAllInterestPointBertrand()throws Exception{

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();

        URL url = new URL("http://moveandsee.azurewebsites.net/api/InterestPoint/GetAllInterestPoints");
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");

        if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String inputJsonString = "",line;
            while((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line);
            }
            bufferedReader.close();
            connection.disconnect();

            inputJsonString = stringBuilder.toString();

            for()
            return gson.fromJson(inputJsonString,User.class);
        }
        return listInterestPoint;
    }
     */

}
