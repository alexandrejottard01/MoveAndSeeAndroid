package com.henallux.moveandseeandroid.DAO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.henallux.moveandseeandroid.Model.Description;
import com.henallux.moveandseeandroid.Model.DescriptionWithVote;
import com.henallux.moveandseeandroid.Model.InterestPoint;
import com.henallux.moveandseeandroid.Model.InterestPointWithVote;
import com.henallux.moveandseeandroid.Model.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Alexandre on 18-11-17.
 */

public class DescriptionDAO {
    public ArrayList<DescriptionWithVote> getAllDescriptionsByInterestPoint(long idInterestPoint)throws Exception{

        ArrayList<DescriptionWithVote> listDescription = new ArrayList<>();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();

        URL url = new URL("http://moveandsee.azurewebsites.net/api/Description/GetAllDescriptionsByInterestPoint/" +idInterestPoint);
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
            listDescription.add(gson.fromJson(jsonArray.getJSONObject(i).toString(),DescriptionWithVote.class));
        }

        return listDescription;
    }

    public int addDescription(Description description)throws Exception{

        Gson gson = new GsonBuilder().create();
        String outputJsonString = gson.toJson(description);

        URL url = new URL("http://moveandsee.azurewebsites.net/api/Description/AddDescription");
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
}
