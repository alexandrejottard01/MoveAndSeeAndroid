package com.henallux.moveandseeandroid.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.auth0.android.jwt.JWT;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.henallux.moveandseeandroid.DAO.DeleteUnknownPointAndAddInterestPointDAO;
import com.henallux.moveandseeandroid.DAO.DescriptionDAO;
import com.henallux.moveandseeandroid.DAO.UnknownPointDAO;
import com.henallux.moveandseeandroid.DAO.UserDAO;
import com.henallux.moveandseeandroid.Model.Description;
import com.henallux.moveandseeandroid.Model.InterestPoint;
import com.henallux.moveandseeandroid.Model.InterestPointWithVote;
import com.henallux.moveandseeandroid.Model.UnknownPoint;
import com.henallux.moveandseeandroid.Model.User;
import com.henallux.moveandseeandroid.R;

import java.net.HttpURLConnection;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Alexandre on 14-11-17.
 */

public class CreateDescriptionOfUnknownPointActivity extends AppCompatActivity implements OnMapReadyCallback {

    //Variable d'instance
    private User userCurrent;
    private SharedPreferences preferences;
    private String token;
    private GoogleMap map;
    private UnknownPoint unknownPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_description_of_unknown_point);
        setTitle(getString(R.string.title_create_description));

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Recupération du UnknownPointSelected
        Gson gson = new Gson();
        String stringUnknownPoint = getIntent().getStringExtra("unknownPointSelected");
        unknownPoint = gson.fromJson(stringUnknownPoint, UnknownPoint.class);

        //Bouton Submit InterestPoint
        Button addInterestPoint = findViewById(R.id.button_submit_interest_point);
        addInterestPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Création de InterestPoint
                EditText nameInterestPointEditText = findViewById (R.id.name_interest_point);
                String nameInterestPoint = nameInterestPointEditText.getText().toString();

                double latitude = unknownPoint.latitude;
                double longitude = unknownPoint.longitude;

                Date date = Calendar.getInstance().getTime();

                InterestPoint interestPoint = new InterestPoint(userCurrent.id,latitude,longitude,nameInterestPoint, date);

                //Création de Description
                EditText descriptionInterestPointEditText = findViewById (R.id.explication_interest);
                String descriptionInterestPoint = descriptionInterestPointEditText.getText().toString();

                Description description = new Description(descriptionInterestPoint, userCurrent.id, interestPoint);

                long idUnknownPoint = unknownPoint.idUnknownPoint;

                if(connectionInternetAvailable()){
                    new DeleteUnknownPointAndAddInterestPointAsync().execute(description,idUnknownPoint);
                }
                else{
                    Toast.makeText(getApplicationContext(), R.string.not_internet, Toast.LENGTH_SHORT).show();
                }
            }
        });

        setTokenInPreferences();

        String pseudo = getUsernameByToken(token);
        fillUserCurrentById(pseudo);
    }

    private void setTokenInPreferences(){
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = preferences.getString("token",null);
    }

    private void fillUserCurrentById(String idUser){
        if(connectionInternetAvailable()){
            new GetUserByIdAsync().execute(idUser);
        }
        else{
            Toast.makeText(getApplicationContext(), R.string.not_internet, Toast.LENGTH_SHORT).show();
        }
    }

    private String getUsernameByToken(String token){
        JWT jwt = new JWT(token);
        String subject = jwt.getSubject();
        return subject;
    }

    //App Bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.action_home :
                Intent goToHomeConnected = new Intent(CreateDescriptionOfUnknownPointActivity.this, HomeConnectedActivity.class);
                startActivity(goToHomeConnected);
                return true;

            case R.id.action_profile :
                Intent goToProfile = new Intent(CreateDescriptionOfUnknownPointActivity.this, ProfileActivity.class);
                startActivity(goToProfile);
                return true;

            case R.id.action_create_unknown_point :
                Intent goToCreateUnknownPoint = new Intent(CreateDescriptionOfUnknownPointActivity.this, CreateUnknownPointActivity.class);
                startActivity(goToCreateUnknownPoint);
                return true;

            case R.id.action_sign_out :
                Intent goToHomeNotConnected = new Intent(CreateDescriptionOfUnknownPointActivity.this, HomeNotConnectedActivity.class);
                startActivity(goToHomeNotConnected);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //OnMapReady
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        //Affichage du UnknownPointSelected
        double latitude = unknownPoint.latitude;
        double longitude= unknownPoint.longitude;

        map.addMarker(new MarkerOptions()
                .position(new LatLng(latitude,longitude))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));


        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        goToLocationZoom(latitude, longitude, 16);
    }

    private void goToLocationZoom(double latitude, double longitude, float zoom) {
        LatLng latitudeLongitude = new LatLng(latitude, longitude);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latitudeLongitude, zoom);
        map.moveCamera(update);
    }

    private boolean connectionInternetAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    private class DeleteUnknownPointAndAddInterestPointAsync extends AsyncTask<Object,Void,Integer>
    {
        @Override
        protected Integer doInBackground(Object... params)
        {
            Integer resultCode =0;
            DeleteUnknownPointAndAddInterestPointDAO deleteUnknownPointAndAddInterestPointDAO = new DeleteUnknownPointAndAddInterestPointDAO();

            Description description = (Description) params[0];
            Long idUnknownPoint = (Long) params[1];

            try{
                resultCode=deleteUnknownPointAndAddInterestPointDAO.deleteUnknownPointAndAddInterestPoint(token, description, idUnknownPoint);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return resultCode;
        }

        @Override
        protected void onPostExecute(Integer resultCode)
        {
            if(resultCode == HttpURLConnection.HTTP_OK){
                Toast.makeText(getApplicationContext(), R.string.interest_point_create, Toast.LENGTH_SHORT).show();
                Intent goToHomeConnected = new Intent(CreateDescriptionOfUnknownPointActivity.this, HomeConnectedActivity.class);
                startActivity(goToHomeConnected);
            }
            else{
                Toast.makeText(getApplicationContext(), R.string.errer_create_interest_point, Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Classe Async (User)
    private class GetUserByIdAsync extends AsyncTask<String,Void,User>
    {
        @Override
        protected User doInBackground(String... params) {
            User user;
            UserDAO userDAO=new UserDAO();

            try{
                user = userDAO.getUserByPseudo(token, params[0]);
            } catch (Exception e) {
                e.printStackTrace();
                user = null;
            }
            return user;
        }

        @Override
        protected void onPostExecute(User user)
        {
            if(user == null){
                Toast.makeText(getApplicationContext(), "User non trouvé", Toast.LENGTH_SHORT).show();
            }

            userCurrent = user;
        }
    }
}
