package com.henallux.moveandseeandroid.DAO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.henallux.moveandseeandroid.Model.DescriptionWithVote;
import com.henallux.moveandseeandroid.Model.InterestPointWithVote;
import com.henallux.moveandseeandroid.Model.UnknownPoint;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Alex on 16-11-17.
 */

public class UnknownPointDAO {

    public ArrayList<UnknownPoint> getAllUnknownPoint()throws Exception{

        ArrayList<UnknownPoint> listUnknownPoint = new ArrayList<>();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();

        URL url = new URL("http://moveandsee.azurewebsites.net/api/UnknownPoint/GetAllUnknownPoints");
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
            listUnknownPoint.add(gson.fromJson(jsonArray.getJSONObject(i).toString(),UnknownPoint.class));
        }


        return listUnknownPoint;
    }
}
