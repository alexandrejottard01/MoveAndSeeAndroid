package com.henallux.moveandseeandroid.DAO;

import com.google.gson.Gson;
import com.henallux.moveandseeandroid.Model.Description;
import com.henallux.moveandseeandroid.Model.DescriptionWithVote;
import com.henallux.moveandseeandroid.Model.InterestPoint;
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

    public ArrayList<DescriptionWithVote> getAllDescriptionsByInterestPoint(long id) throws Exception {

        URL url = new URL("http://moveandsee.azurewebsites.net/api/Description/GetAllDescriptionsByInterestPoint/"+id);
        URLConnection connection = url.openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String stringJson = "", line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        stringJson = sb.toString();

        JSONArray jsonsArray=new JSONArray(stringJson);
        ArrayList<DescriptionWithVote> listDescriptionWithVote=new ArrayList<>();

        for(int i=0;i<jsonsArray.length();i++) {

            JSONObject jsonDescription = jsonsArray.getJSONObject(i);
            JSONObject description_object = jsonDescription.getJSONObject("description");

            //remplissage de la description
            long idDescription = description_object.getLong("idDescription");
            String description_string = description_object.getString("explication");
            int average = jsonDescription.getInt("moyenne");

            JSONObject interestObject = description_object.getJSONObject("idInterestPointNavigation");

            long idInterestPoint = interestObject.getLong("idInterestPoint");
            long idUser = interestObject.getLong("idUser");
            double latitude = interestObject.getDouble("latitude");
            double longitude = interestObject.getDouble("longitude");
            String name = interestObject.getString("name");
            String dateFormat = interestObject.getString("dateCreation");
            Date dateCreation;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            dateCreation = format.parse(dateFormat);

            JSONObject userObject = description_object.getJSONObject("idUserNavigation");

            String pseudo = userObject.getString("pseudo");
            String password = userObject.getString("password");
            boolean isCertified = userObject.getBoolean("isCertified");
            String nameCertified = userObject.getString("nameCertified");
            String emailUser = userObject.getString("email");
            String language = userObject.getString("language");
            boolean isMale = userObject.getBoolean("isMale");
            String birthDateFormat = userObject.getString("birthDate");
            Date birthDate;
            birthDate = format.parse(birthDateFormat);
            boolean isAdmin = userObject.getBoolean("isAdmin");


            User user = new User(idUser, pseudo, password, isCertified, nameCertified, emailUser, language, isMale, birthDate, isAdmin);

            InterestPoint interestPoint = new InterestPoint(idInterestPoint, idUser, latitude, longitude, name, dateCreation, user);

            Description description = new Description(idDescription, description_string, idUser, idInterestPoint, interestPoint, user);

            DescriptionWithVote descriptionWithVote = new DescriptionWithVote(description, average);

            listDescriptionWithVote.add(descriptionWithVote);
        }

        return listDescriptionWithVote;
    }





    /*public int AddDescription(Description description) throws Exception
    {
        URL url=new URL("http://moveandsee.azurewebsites.net/api/Description/AddDescription");
        HttpURLConnection connection=(HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        //connection.setDoInput(true); //on va devoir ecrire
        connection.setDoOutput(true); //on lui dis  si on recuperer qq chose

        connection.setRequestProperty("Content-Type","application/json");// on lui donne le type de donee envoye
        Gson gson=new Gson();
        String description_output=gson.toJson(description);

        //byte [] output_byte=description_output.getBytes("UTF-8"); // on doit convertir en byte
        OutputStream stream_out=connection.getOutputStream();
        OutputStreamWriter writter=new OutputStreamWriter(stream_out);
        connection.connect();
        writter.write(description_output);
        writter.flush();

        stream_out.write(output_byte);
        stream_out.flush();
        stream_out.close();

        int statut=connection.getResponseCode();

        writter.close();
        stream_out.close();
        connection.disconnect();

        if(statut!=HttpURLConnection.HTTP_OK)
        {
            throw new Exception();
        }



        URLConnection connection=url.openConnection();
        BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder sb=new StringBuilder();
        String stringJson="",line;
        while ((line=br.readLine())!=null){
            sb.append(line);
        }
        br.close();
        stringJson=sb.toString();

        return statut;

    }*/

    /*
    public Description_interst_point objectToJson()
    {

    }

    public ArrayList<Description_interst_point> jsonToDescription(String stringJson) throws Exception{

        ArrayList<Description_interst_point> listDescrip = new ArrayList<>();
        Description_interst_point description;
        JSONArray jsonsArray = new JSONArray(stringJson);

        for (int i = 0; i < jsonsArray.length(); i++) {
            JSONObject jsonDescription = jsonsArray.getJSONObject(i);
            JSONObject description_object =jsonDescription.getJSONObject("description");
            JSONObject jsonUser=description_object.getJSONObject("idUserNavigation");

            //remplissage de la description
            long id=description_object.getLong("idDescription");
            String description_string=description_object.getString("explication");
            long id_user=description_object.getLong("idUser");
            long id_description=description_object.getLong("idInterestPoint");
            double average=jsonDescription.getDouble("moyenne");

            //remplissage du user pour la description
            User user;
            String pseudo=jsonUser.getString("pseudo");
            String password=jsonUser.getString("password");
            boolean is_certified=jsonUser.getBoolean("isCertified");
            String name_certified=jsonUser.getString("nameCertified");
            String mail=jsonUser.getString("email");
            String langue=jsonUser.getString("language");
            boolean is_male=jsonUser.getBoolean("isMale");
            //Date birthday=(Date) jsonUser.get("birthDate");
            boolean is_admin=jsonUser.getBoolean("isAdmin");

            user=new User(id_user,pseudo,password,is_certified,is_admin,name_certified,is_male,null,mail,langue);

            description=new Description_interst_point(id,description_string,id_user,user,id_description,average);

            listDescrip.add(description);
        }

        return listDescrip;
    }*/



}
