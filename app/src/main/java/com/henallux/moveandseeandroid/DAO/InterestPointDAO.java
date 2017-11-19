package com.henallux.moveandseeandroid.DAO;

import com.google.gson.Gson;
import com.henallux.moveandseeandroid.Model.InterestPoint;
import com.henallux.moveandseeandroid.Model.InterestPointWithVote;
import com.henallux.moveandseeandroid.Model.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
    public ArrayList<InterestPointWithVote> getAllInterestPointSimulation(){

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



    public ArrayList<InterestPointWithVote> getAllInterestPoint() throws Exception {

        URL url = new URL("http://moveandsee.azurewebsites.net/api/InterestPoint/GetAllInterestPoints");
        URLConnection connection = url.openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String stringJson = "", line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        stringJson = sb.toString();
        Gson gson=new Gson();

        ArrayList<InterestPointWithVote> listInterestPoint= new ArrayList<>();
        InterestPointWithVote interestPointVote;
        JSONArray jsonsArray=new JSONArray(stringJson);

        for(int i=0;i<jsonsArray.length();i++)
        {
            JSONObject jsonObject=jsonsArray.getJSONObject(i);
            //interest_point=gson.fromJson(jsonObject.toString(),InterestPointVote.class);
            JSONObject interestObject=jsonObject.getJSONObject("interestPoint");
            int averageObject=jsonObject.getInt("moyenne");

            long id=interestObject.getLong("idInterestPoint");
            long idUser=interestObject.getLong("idUser");
            double latitude=interestObject.getDouble("latitude");
            double longitude=interestObject.getDouble("longitude");
            String name=interestObject.getString("name");
            String dateFormat=interestObject.getString("dateCreation");
            Date dateCreation;
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
            dateCreation=format.parse(dateFormat);


            JSONObject userObject=interestObject.getJSONObject("idUserNavigation");

            String pseudo=userObject.getString("pseudo");
            String password=userObject.getString("password");
            boolean isCertified=userObject.getBoolean("isCertified");
            String nameCertified=userObject.getString("nameCertified");
            String emailUser=userObject.getString("email");
            String language=userObject.getString("language");
            boolean isMale=userObject.getBoolean("isMale");
            String birthDateFormat=userObject.getString("birthDate");
            Date birthDate;
            birthDate=format.parse(birthDateFormat);
            boolean isAdmin=userObject.getBoolean("isAdmin");


            User user=new User(idUser,pseudo,password,isCertified,nameCertified,emailUser,language,isMale,birthDate,isAdmin);

            InterestPoint interestPoint=new InterestPoint(id,idUser,latitude,longitude,name,dateCreation,user);
            interestPointVote=new InterestPointWithVote(interestPoint,averageObject);

            listInterestPoint.add(interestPointVote);
        }

        return listInterestPoint;
    }

}
