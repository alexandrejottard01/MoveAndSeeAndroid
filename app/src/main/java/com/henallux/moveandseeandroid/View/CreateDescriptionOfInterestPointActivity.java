package com.henallux.moveandseeandroid.View;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.henallux.moveandseeandroid.DAO.DescriptionDAO;
import com.henallux.moveandseeandroid.DAO.InterestPointDAO;
import com.henallux.moveandseeandroid.Model.InterestPointWithVote;
import com.henallux.moveandseeandroid.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexandre on 14-11-17.
 */

public class CreateDescriptionOfInterestPointActivity extends AppCompatActivity implements OnMapReadyCallback {

    //Variable d'instance
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
        nameInterest =(TextView)findViewById(R.id.name_interest_with_average);

        //Moyenne du InterestPoint
        if(interestPointWithVote.moyenne == -1){
            nameInterest.setText(interestPointWithVote.interestPoint.name);
        }
        else{
            nameInterest.setText(interestPointWithVote.interestPoint.name + " " + Integer.toString(interestPointWithVote.moyenne)+"%");
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
        String stringAddress = addressInterestPoint.getAddressLine(0) + " " + addressInterestPoint.getAddressLine(1);

        TextView address = new TextView(this);
        nameInterest =(TextView)findViewById(R.id.address_interest);
        nameInterest.setText(stringAddress);

        //Boutton Submit description
        Button addDescription = (Button) findViewById(R.id.button_submit_description);
        addDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Toast.makeText(getApplicationContext(), interestPointWithVote.interestPoint.name, Toast.LENGTH_SHORT).show();

            }
        });


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
                .snippet(Integer.toString(interestPointWithVote.moyenne))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));


        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        goToLocationZoom(latitude, longitude, 16);
    }

    private void goToLocationZoom(double latitude, double longitude, float zoom) {
        LatLng latitudeLongitude = new LatLng(latitude, longitude);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latitudeLongitude, zoom);
        map.moveCamera(update);
    }

    /*private class CreateDecription extends AsyncTask<String,Void,int>
    {
        int statut= HttpURLConnection.HTTP_OK;

        @Override
        protected int doInBackground(String... params)
        {
            int resultCode;
            DescriptionDAO descriptionDAO=new DescriptionDAO();
            try{
                resultCode=interestPointDAO.getAllInterestPoint();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return listInterestPoints;
        }

        @Override
        protected void onPostExecute(ArrayList<InterestPointWithVote> listInterestPoints)
        {
            for (InterestPointWithVote item : listInterestPoints) {
                Marker marker = map.addMarker(new MarkerOptions()
                        .position(new LatLng(item.interestPoint.latitude, item.interestPoint.longitude))
                        .title(item.interestPoint.name)
                        .snippet(Integer.toString(item.moyenne))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                hashMap.put(marker, item);
            }
        }
    }*/
}

