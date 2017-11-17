package com.henallux.moveandseeandroid.View;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.henallux.moveandseeandroid.DAO.InterestPointDAO;
import com.henallux.moveandseeandroid.Model.InterestPointWithVote;
import com.henallux.moveandseeandroid.R;

import java.util.ArrayList;

/**
 * Created by Alexandre on 14-11-17.
 */

public class HomeConnectedActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_connected);
        setTitle(getString(R.string.title_home_connected));

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.action_profile :
                Intent goToProfile = new Intent(HomeConnectedActivity.this, ProfileActivity.class);
                startActivity(goToProfile);
                return true;

            case R.id.action_create_interest_point :
                Intent goToCreateInterestPoint = new Intent(HomeConnectedActivity.this, CreateInterestPointActivity.class);
                startActivity(goToCreateInterestPoint);
                return true;

            case R.id.action_create_unknown_point :
                Intent goToCreateUnknownPoint = new Intent(HomeConnectedActivity.this, CreateUnknownPointActivity.class);
                startActivity(goToCreateUnknownPoint);
                return true;

            case R.id.action_sign_out :
                Intent goToHomeNotConnected = new Intent(HomeConnectedActivity.this, HomeNotConnectedActivity.class);
                startActivity(goToHomeNotConnected);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

        InterestPointDAO interestPointDAO = new InterestPointDAO();
        ArrayList<InterestPointWithVote> listInterestPoint = interestPointDAO.getAllInterestPoints();

        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        for (InterestPointWithVote item : listInterestPoint) {
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(item.interestPoint.latitude, item.interestPoint.longitude))
                    .title(item.interestPoint.name)
                    .snippet(Integer.toString(item.moyenne))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        }

        googleMap.setOnMarkerClickListener(this);

        goToLocationZoom(50.469424, 4.862533, 15);
    }

    private void goToLocationZoom(double latitude, double longitude, float zoom) {
        LatLng latitudeLongitude = new LatLng(latitude, longitude);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latitudeLongitude, zoom);
        map.moveCamera(update);
    }

    //Méthode pour récupérer la sellection de lieu par l'utilisateur
    /*protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }*/

    @Override
    public boolean onMarkerClick(final Marker marker) {
        Toast.makeText(this, marker.getTitle(), Toast.LENGTH_LONG).show();
        return true;
    }

}
