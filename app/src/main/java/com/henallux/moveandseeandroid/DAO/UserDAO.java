package com.henallux.moveandseeandroid.DAO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.henallux.moveandseeandroid.Model.InterestPoint;
import com.henallux.moveandseeandroid.Model.InterestPointWithVote;
import com.henallux.moveandseeandroid.Model.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Alex on 16-11-17.
 */

public class UserDAO {

    public User getUserByPseudo(String token, String pseudoUser)throws Exception{

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();

        URL url = new URL("http://moveandsee.azurewebsites.net/api/User/GetUserByPseudo/"+pseudoUser);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + token);
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

        User user = gson.fromJson(inputJsonString,User.class);

        return user;
    }

    public int addUser(User user)throws Exception{

        Gson gson = new GsonBuilder().create();
        String outputJsonString = gson.toJson(user);

        URL url = new URL("http://moveandsee.azurewebsites.net/api/Account");
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
