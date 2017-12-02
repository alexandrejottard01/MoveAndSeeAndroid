package com.henallux.moveandseeandroid.DAO;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.henallux.moveandseeandroid.Exception.HttpResultException;
import com.henallux.moveandseeandroid.Model.AccessToken;
import com.henallux.moveandseeandroid.Model.Constants;
import com.henallux.moveandseeandroid.Model.UserLogin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Alexandre on 23-11-17.
 */

public class LoginDAO {
    public AccessToken connexion(UserLogin userLogin)throws Exception{
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        String outputJsonString = gson.toJson(userLogin);
        URL url = new URL(Constants.ADDRESS_API + "Jwt");
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        byte[] outputBytes = outputJsonString.getBytes("UTF-8");
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(outputBytes);
        outputStream.flush();
        outputStream.close();
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
            Log.i("Contenu inputJsonString", inputJsonString);

            return gson.fromJson(inputJsonString,AccessToken.class);
        }
        throw new HttpResultException(connection.getResponseCode());
    }
}
