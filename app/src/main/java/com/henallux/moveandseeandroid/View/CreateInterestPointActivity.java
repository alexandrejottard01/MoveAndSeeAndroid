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
import android.widget.TextView;
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
import com.henallux.moveandseeandroid.DAO.DescriptionDAO;
import com.henallux.moveandseeandroid.DAO.InterestPointDAO;
import com.henallux.moveandseeandroid.DAO.UserDAO;
import com.henallux.moveandseeandroid.Model.Description;
import com.henallux.moveandseeandroid.Model.InterestPoint;
import com.henallux.moveandseeandroid.Model.InterestPointWithVote;
import com.henallux.moveandseeandroid.Model.User;
import com.henallux.moveandseeandroid.R;

import java.net.HttpURLConnection;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Alexandre on 14-11-17.
 */

public class CreateInterestPointActivity extends AppCompatActivity implements OnMapReadyCallback {

    //Variable d'instance
    private User userCurrent;
    private SharedPreferences preferences;
    private String token;
    private GoogleMap map;
    private LatLng latitudeLongitudeSelected;

    //OnCreate
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_interest_point);
        setTitle(getString(R.string.title_create_interest_point));

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Recupération du LatLng Selected
        Gson gson = new Gson();
        String stringLatitudeLongitude = getIntent().getStringExtra("latitudeLongitudeSelected");
        latitudeLongitudeSelected = gson.fromJson(stringLatitudeLongitude, LatLng.class);

        //Bouton Submit InterestPoint
        Button addInterestPoint = findViewById(R.id.button_submit_interest_point);
        addInterestPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Création de InterestPoint
                EditText nameInterestPointEditText = findViewById (R.id.name_interest_point);
                String nameInterestPoint = nameInterestPointEditText.getText().toString();

                double latitude = latitudeLongitudeSelected.latitude;
                double longitude = latitudeLongitudeSelected.longitude;

                Date dateCreation = Calendar.getInstance().getTime();

                InterestPoint interestPoint = new InterestPoint(userCurrent.id,latitude,longitude,nameInterestPoint, dateCreation);

                //Création de Description
                EditText descriptionInterestPointEditText = findViewById (R.id.explication_interest);
                String descriptionInterestPoint = descriptionInterestPointEditText.getText().toString();

                Description description = new Description(descriptionInterestPoint, userCurrent.id, interestPoint);

                if(connectionInternetAvailable()){
                    new AddInterestPointAndDescriptionAsync().execute(description);
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
                Intent goToHomeConnected = new Intent(CreateInterestPointActivity.this, HomeConnectedActivity.class);
                startActivity(goToHomeConnected);
                return true;

            case R.id.action_profile :
                Intent goToProfile = new Intent(CreateInterestPointActivity.this, ProfileActivity.class);
                startActivity(goToProfile);
                return true;

            case R.id.action_create_unknown_point :
                Intent goToCreateUnknownPoint = new Intent(CreateInterestPointActivity.this, CreateUnknownPointActivity.class);
                startActivity(goToCreateUnknownPoint);
                return true;

            case R.id.action_sign_out :
                Intent goToHomeNotConnected = new Intent(CreateInterestPointActivity.this, HomeNotConnectedActivity.class);
                startActivity(goToHomeNotConnected);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //OnMapReady
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        //Affichage du LatLng Selected

        map.addMarker(new MarkerOptions()
                .position(new LatLng(latitudeLongitudeSelected.latitude,latitudeLongitudeSelected.longitude))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));


        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        goToLocationZoom(latitudeLongitudeSelected.latitude, latitudeLongitudeSelected.longitude, 16);
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

    //Classe AddInterestPointAndDescriptionAsync
    private class AddInterestPointAndDescriptionAsync extends AsyncTask<Description,Void,Integer>
    {
        @Override
        protected Integer doInBackground(Description... params)
        {
            Integer resultCode =0;
            DescriptionDAO descriptionDAO = new DescriptionDAO();

            try{
                resultCode=descriptionDAO.addDescription(token, params[0]);
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
                Intent goToHomeConnected = new Intent(CreateInterestPointActivity.this, HomeConnectedActivity.class);
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
