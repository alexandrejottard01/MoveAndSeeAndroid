package com.henallux.moveandseeandroid.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
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
import com.henallux.moveandseeandroid.DAO.UserDAO;
import com.henallux.moveandseeandroid.Model.Description;
import com.henallux.moveandseeandroid.Model.InterestPointWithVote;
import com.henallux.moveandseeandroid.Model.User;
import com.henallux.moveandseeandroid.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

/**
 * Created by Alexandre on 14-11-17.
 */

public class CreateDescriptionOfInterestPointActivity extends AppCompatActivity implements OnMapReadyCallback {

    //Variable d'instance
    private User userCurrent;
    private SharedPreferences preferences;
    private String token;
    private GoogleMap map;
    private InterestPointWithVote interestPointWithVote;

    //OnCreate
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_description_of_interest_point);
        setTitle(getString(R.string.title_create_description));

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Recupération du InterestPointSelected
        Gson gson = new Gson();
        String stringInterestPoint = getIntent().getStringExtra("interestPointSelected");
        interestPointWithVote = gson.fromJson(stringInterestPoint, InterestPointWithVote.class);

        //Nom du InterestPoint
        TextView nameInterest = new TextView(this);
        nameInterest =(TextView)findViewById(R.id.name_interest_point);

        //Moyenne du InterestPoint
        if(interestPointWithVote.average != -1){
            nameInterest.setText(interestPointWithVote.interestPoint.name + " " + Integer.toString(interestPointWithVote.average)+"%");
        }
        else{
            nameInterest.setText(interestPointWithVote.interestPoint.name);
        }

        //Adresse du InterestPoint
        Geocoder geocoder = new Geocoder(this);
        List<Address> listAddress = null;
        try {
            listAddress = geocoder.getFromLocation(interestPointWithVote.interestPoint.latitude, interestPointWithVote.interestPoint.longitude,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Address addressInterestPoint = listAddress.get(0);
        String stringAddress = addressInterestPoint.getAddressLine(0);

        TextView address = new TextView(this);
        nameInterest =(TextView)findViewById(R.id.address_interest);
        nameInterest.setText(stringAddress);

        //Boutton Submit description
        Button addDescription = (Button) findViewById(R.id.button_submit_description);
        addDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            EditText explicationEditText = (EditText) findViewById (R.id.explication_interest);
            String explication = explicationEditText.getText().toString();

            long idInterestPoint = interestPointWithVote.interestPoint.idInterestPoint;
            Description description = new Description(explication,userCurrent.id,idInterestPoint);

            new AddDecriptionAsync().execute(description);

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
        new GetUserByIdAsync().execute(idUser);
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
                Intent goToHomeConnected = new Intent(CreateDescriptionOfInterestPointActivity.this, HomeConnectedActivity.class);
                startActivity(goToHomeConnected);
                return true;

            case R.id.action_profile :
                Intent goToProfile = new Intent(CreateDescriptionOfInterestPointActivity.this, ProfileActivity.class);
                startActivity(goToProfile);
                return true;

            case R.id.action_create_interest_point :
                Intent goToCreateInterestPoint = new Intent(CreateDescriptionOfInterestPointActivity.this, CreateInterestPointActivity.class);
                startActivity(goToCreateInterestPoint);
                return true;

            case R.id.action_create_unknown_point :
                Intent goToCreateUnknownPoint = new Intent(CreateDescriptionOfInterestPointActivity.this, CreateUnknownPointActivity.class);
                startActivity(goToCreateUnknownPoint);
                return true;

            case R.id.action_sign_out :
                Intent goToHomeNotConnected = new Intent(CreateDescriptionOfInterestPointActivity.this, HomeNotConnectedActivity.class);
                startActivity(goToHomeNotConnected);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //OnMapReady
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        //Affichage du InterestPointSelected
        double latitude = interestPointWithVote.interestPoint.latitude;
        double longitude= interestPointWithVote.interestPoint.longitude;

        map.addMarker(new MarkerOptions()
                .position(new LatLng(latitude,longitude))
                .title(interestPointWithVote.interestPoint.name)
                .snippet(Integer.toString(interestPointWithVote.average))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));


        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        goToLocationZoom(latitude, longitude, 16);
    }

    private void goToLocationZoom(double latitude, double longitude, float zoom) {
        LatLng latitudeLongitude = new LatLng(latitude, longitude);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latitudeLongitude, zoom);
        map.moveCamera(update);
    }

    //Classe AddDescriptionAsync
    private class AddDecriptionAsync extends AsyncTask<Description,Void,Integer>
    {
        @Override
        protected Integer doInBackground(Description... params)
        {
            Integer resultCode =0;
            DescriptionDAO descriptionDAO=new DescriptionDAO();
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
                Toast.makeText(getApplicationContext(), R.string.message_description_create, Toast.LENGTH_SHORT).show();
                Intent goToHomeConnected = new Intent(CreateDescriptionOfInterestPointActivity.this, HomeConnectedActivity.class);
                startActivity(goToHomeConnected);
            }
            else{
                Toast.makeText(getApplicationContext(), R.string.message_errer_description_no_create, Toast.LENGTH_SHORT).show();
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

