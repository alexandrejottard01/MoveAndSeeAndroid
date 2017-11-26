package com.henallux.moveandseeandroid.View;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.henallux.moveandseeandroid.DAO.DescriptionDAO;
import com.henallux.moveandseeandroid.DAO.UnknownPointDAO;
import com.henallux.moveandseeandroid.DAO.UserDAO;
import com.henallux.moveandseeandroid.Model.Description;
import com.henallux.moveandseeandroid.Model.UnknownPoint;
import com.henallux.moveandseeandroid.Model.User;
import com.henallux.moveandseeandroid.R;

import java.net.HttpURLConnection;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Alexandre on 14-11-17.
 */

public class CreateUnknownPointActivity extends AppCompatActivity implements OnMapReadyCallback {

    //Variable d'instance
    private User userCurrent;
    private SharedPreferences preferences;
    private String token;
    private GoogleMap map;
    private LatLng latitudeLongitude;

    //OnCreate
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_unknown_point);
        setTitle(getString(R.string.title_create_unknown_point));

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Bouton Submit UnknownPoint
        Button addUnknownPoint = findViewById(R.id.button_submit_unknown_point);
        addUnknownPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double latitude = latitudeLongitude.latitude;
                double longitude = latitudeLongitude.longitude;
                Date dateCreation = Calendar.getInstance().getTime();

                UnknownPoint unknownPoint = new UnknownPoint(userCurrent.id,latitude,longitude,dateCreation);

                new AddUnknownPointAsync().execute(unknownPoint);

            }
        });
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = preferences.getString("token",null);

        String pseudo = getUsernameByToken(token);
        fillUserCurrentById(pseudo);
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
                Intent goToHomeConnected = new Intent(CreateUnknownPointActivity.this, HomeConnectedActivity.class);
                startActivity(goToHomeConnected);
                return true;

            case R.id.action_profile :
                Intent goToProfile = new Intent(CreateUnknownPointActivity.this, ProfileActivity.class);
                startActivity(goToProfile);
                return true;

            case R.id.action_create_interest_point :
                Intent goToCreateInterestPoint = new Intent(CreateUnknownPointActivity.this, CreateInterestPointActivity.class);
                startActivity(goToCreateInterestPoint);
                return true;

            case R.id.action_sign_out :
                Intent goToHomeNotConnected = new Intent(CreateUnknownPointActivity.this, HomeNotConnectedActivity.class);
                startActivity(goToHomeNotConnected);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //OnMapReady
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        goToLocationZoom(50.469424, 4.862533, 15);

        //Evenement quand on clique sur la map
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {

                //Latitude et Longitude du UnknownPoint
                TextView latitudeLongitudeUnknownPointTextView;
                latitudeLongitudeUnknownPointTextView =(TextView)findViewById(R.id.latitude_longitude_unknown_point);
                latitudeLongitudeUnknownPointTextView.setText(Double.toString(point.latitude) + " ; " + Double.toString(point.longitude));

                latitudeLongitude = new LatLng(point.latitude,point.longitude);
            }
        });
    }

    private void goToLocationZoom(double latitude, double longitude, float zoom) {
        LatLng latitudeLongitude = new LatLng(latitude, longitude);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latitudeLongitude, zoom);
        map.moveCamera(update);
    }

    //Classe AddUnknownPointAsync
    private class AddUnknownPointAsync extends AsyncTask<UnknownPoint,Void,Integer>
    {
        @Override
        protected Integer doInBackground(UnknownPoint... params)
        {
            Integer resultCode =0;
            UnknownPointDAO unknownPointDAO = new UnknownPointDAO();

            try{
                resultCode=unknownPointDAO.addUnknownPoint(token, params[0]);
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
                Toast.makeText(getApplicationContext(), R.string.message_create_unknown_point, Toast.LENGTH_SHORT).show();
                Intent goToHomeConnected = new Intent(CreateUnknownPointActivity.this, HomeConnectedActivity.class);
                startActivity(goToHomeConnected);
            }
            else{
                Toast.makeText(getApplicationContext(), R.string.message_error_create_unknown_point, Toast.LENGTH_SHORT).show();
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
