package com.henallux.moveandseeandroid.View;

import android.app.Dialog;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
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
import com.google.gson.Gson;
import com.henallux.moveandseeandroid.Adapter.CustomListDescription;
import com.henallux.moveandseeandroid.DAO.DescriptionDAO;
import com.henallux.moveandseeandroid.DAO.InterestPointDAO;
import com.henallux.moveandseeandroid.DAO.UnknownPointDAO;
import com.henallux.moveandseeandroid.DAO.VoteInterestPointDAO;
import com.henallux.moveandseeandroid.Model.DescriptionWithVote;
import com.henallux.moveandseeandroid.Model.InterestPoint;
import com.henallux.moveandseeandroid.Model.InterestPointWithVote;
import com.henallux.moveandseeandroid.Model.UnknownPoint;
import com.henallux.moveandseeandroid.Model.VoteInterestPoint;
import com.henallux.moveandseeandroid.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Alexandre on 14-11-17.
 */

public class HomeConnectedActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    //Variable d'instance
    private GoogleMap map;
    HashMap<Marker, InterestPointWithVote> hashMap = new HashMap<Marker, InterestPointWithVote>();
    HashMap<Marker, UnknownPoint> hashMapUnknownPoint = new HashMap<Marker, UnknownPoint>();
    private ListView listDescription;

    //OnCreate
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_connected);
        setTitle(getString(R.string.title_home_connected));

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (!googleServicesAvailable()) {
            Toast.makeText(this, "Le service Google Map ne fonctionne pas", Toast.LENGTH_LONG).show();
        }
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

    //OnMapReady
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        displayInterestPoints();
        displayUnknownPoints();
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        goToLocationZoom(50.469424, 4.862533, 15);
        googleMap.setOnMarkerClickListener(this);

        //Evenement quand on clique sur la map
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                LatLng latitudeLongitude = new LatLng(point.latitude, point.longitude);
                Gson gson = new Gson();
                Intent intentToCreateInterestPointActivity = new Intent(HomeConnectedActivity.this, CreateInterestPointActivity.class);
                intentToCreateInterestPointActivity.putExtra("latitudeLongitudeSelected", gson.toJson(latitudeLongitude));
                startActivity(intentToCreateInterestPointActivity);
            }
        });
    }

    private void displayInterestPoints(){
        try {
            new GetAllInterestPointsAsync().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayUnknownPoints(){
        try {
            new GetAllUnknownPointsAsync().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goToLocationZoom(double latitude, double longitude, float zoom) {
        LatLng latitudeLongitude = new LatLng(latitude, longitude);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latitudeLongitude, zoom);
        map.moveCamera(update);
    }

    //C'est quoi cette méthode ?
    //Méthode pour récupérer la sellection de lieu par l'utilisateur
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }

    }

    //Evenement Clique sur Marker
    @Override
    public boolean onMarkerClick(final Marker marker) {
        if(marker.getTitle() == null){ //Changer la condition pour détecter si c'est un point d'intéret ou point inconnu
            UnknownPoint unknownPoint = hashMapUnknownPoint.get(marker);

            Gson gson = new Gson();
            Intent intentCreateDescriptionOfUnknownPoint = new Intent(HomeConnectedActivity.this, CreateDescriptionOfUnknownPointActivity.class);
            intentCreateDescriptionOfUnknownPoint.putExtra("unknownPointSelected", gson.toJson(unknownPoint));
            startActivity(intentCreateDescriptionOfUnknownPoint);
        }
        else{
            final InterestPointWithVote interestPointWithVote = hashMap.get(marker);
            fillInterestPointInLayout(interestPointWithVote);

            //Gestion du bouton addDescription
            Button addDescription = (Button) findViewById(R.id.button_add_description);
            addDescription.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Gson gson = new Gson();
                    Intent intentToCreateDescriptionOfInterestPoint = new Intent(HomeConnectedActivity.this, CreateDescriptionOfInterestPointActivity.class);
                    intentToCreateDescriptionOfInterestPoint.putExtra("interestPointSelected", gson.toJson(interestPointWithVote));
                    startActivity(intentToCreateDescriptionOfInterestPoint);
                }
            });

            //Gestion du pouce positive
            ImageButton addVotePositive = (ImageButton) findViewById(R.id.thumb_up);
            addVotePositive.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    VoteInterestPoint voteInterestPointPositive = new VoteInterestPoint(true,1,interestPointWithVote.interestPoint.idInterestPoint);
                    new AddVoteInterestPointAsync().execute(voteInterestPointPositive);
                }
            });

            //Gestion du pouce négatif
            ImageButton addVoteNegative = (ImageButton) findViewById(R.id.thumb_down);
            addVoteNegative.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    VoteInterestPoint voteInterestPointNegative = new VoteInterestPoint(false,1,interestPointWithVote.interestPoint.idInterestPoint);
                    new AddVoteInterestPointAsync().execute(voteInterestPointNegative);
                }
            });
        }
        return true;
    }

    private void fillInterestPointInLayout(InterestPointWithVote interestPointWithVote){
        fillNameInterestPointInLayout(interestPointWithVote);
        fillAddressInterestPointInLayout(interestPointWithVote);
        fillAverageInterestPointInLayout(interestPointWithVote);
        fillDescriptionsInterestPointInLayout(interestPointWithVote);
    }

    private void fillNameInterestPointInLayout(InterestPointWithVote interestPointWithVote){
        TextView nameInterest;
        nameInterest = findViewById(R.id.name_interest);
        nameInterest.setText(interestPointWithVote.interestPoint.name);
    }

    private void fillAddressInterestPointInLayout(InterestPointWithVote interestPointWithVote){
        String stringAddress = findAddress(interestPointWithVote);

        TextView address;
        address = findViewById(R.id.address_interest);
        address.setText(stringAddress);
    }

    private String findAddress(InterestPointWithVote interestPointWithVote){
        Geocoder geocoder = new Geocoder(this);
        List<Address> listAddress = null;
        try {
            listAddress = geocoder.getFromLocation(interestPointWithVote.interestPoint.latitude, interestPointWithVote.interestPoint.longitude,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Address addressInterestPoint = listAddress.get(0);
        String stringAddress = addressInterestPoint.getAddressLine(0);

        return stringAddress;
    }

    private void fillAverageInterestPointInLayout(InterestPointWithVote interestPointWithVote){
        TextView average;
        average = findViewById(R.id.average_interest);

        if(interestPointWithVote.average != -1) {
            average.setText(Integer.toString(interestPointWithVote.average) + "%");
        }
        else{
            average.setText(" ");
        }
    }

    private void fillDescriptionsInterestPointInLayout(InterestPointWithVote interestPointWithVote){
        listDescription = findViewById(R.id.list_description);
        new GetAllDescriptionByInterestPointAsync().execute(interestPointWithVote);
    }

    public boolean googleServicesAvailable() { //Méthode pour savoir si le service Google Map est disponible (méthode toute faite) (A refactorer)
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else {
            if (api.isUserResolvableError(isAvailable)) {
                Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
                dialog.show();
            } else {
                Toast.makeText(this, "Le service Google Map ne fonctionne pas", Toast.LENGTH_LONG).show();
            }
            return false;
        }
    }

    //Classe Async (InterestPoint)
    private class GetInterestPointByIdAsync extends AsyncTask<Long,Void,InterestPointWithVote>
    {
        @Override
        protected InterestPointWithVote doInBackground(Long... params)
        {
            InterestPointWithVote interestPoint;
            InterestPointDAO interestPointDAO=new InterestPointDAO();
            try{
                interestPoint = interestPointDAO.getInterestPointById(params[0]);

            }
            catch (Exception e) {
                e.printStackTrace();
                interestPoint = null;
            }

            return interestPoint;
        }

        /*@Override
        protected InterestPointWithVote onPostExecute(InterestPointWithVote interestPointWithVote)
        {
            return interestPointWithVote;
        }*/
    }

    //Classe Async (InterestPoint)
    private class GetAllInterestPointsAsync extends AsyncTask<String,Void,ArrayList<InterestPointWithVote>>
    {
        @Override
        protected ArrayList<InterestPointWithVote> doInBackground(String... params)
        {
            ArrayList<InterestPointWithVote> listInterestPoints = new ArrayList<>();
            InterestPointDAO interestPointDAO=new InterestPointDAO();
            try{
                listInterestPoints=interestPointDAO.getAllInterestPoint();
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
                        .snippet(Integer.toString(item.average))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                hashMap.put(marker, item);
            }
        }
    }

    //Classe Async (UnknownPoint)
    private class GetAllUnknownPointsAsync extends AsyncTask<String,Void,ArrayList<UnknownPoint>>
    {
        @Override
        protected ArrayList<UnknownPoint> doInBackground(String... params)
        {
            ArrayList<UnknownPoint> listUnknownPoints = new ArrayList<>();
            UnknownPointDAO unknownPointDAO=new UnknownPointDAO();
            try{
                listUnknownPoints=unknownPointDAO.getAllUnknownPoint();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return listUnknownPoints;
        }

        @Override
        protected void onPostExecute(ArrayList<UnknownPoint> listUnknownPoints)
        {
            for (UnknownPoint item : listUnknownPoints) {
                Marker marker = map.addMarker(new MarkerOptions()
                        .position(new LatLng(item.latitude, item.longitude))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                hashMapUnknownPoint.put(marker, item);
            }
        }
    }

    //Classe Async (Description)
    private class GetAllDescriptionByInterestPointAsync extends AsyncTask<InterestPointWithVote,Void,ArrayList<DescriptionWithVote>>
    {
        @Override
        protected ArrayList<DescriptionWithVote> doInBackground(InterestPointWithVote... params) {
            DescriptionDAO descriptionDao=new DescriptionDAO();
            ArrayList<DescriptionWithVote> listDescriptionAsync=new ArrayList<>();
            try{
                listDescriptionAsync = descriptionDao.getAllDescriptionsByInterestPoint(params[0].interestPoint.idInterestPoint);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listDescriptionAsync;
        }

        @Override
        protected void onPostExecute(ArrayList<DescriptionWithVote> listDescriptionAsync)
        {
            listDescription.setAdapter(new CustomListDescription(HomeConnectedActivity.this,listDescriptionAsync));
        }
    }

    //Classe Async (VoteInterestPoint)
    private class AddVoteInterestPointAsync extends AsyncTask<VoteInterestPoint,Void,Integer>
    {
        @Override
        protected Integer doInBackground(VoteInterestPoint... params) {
            Integer resultCode =0;
            VoteInterestPointDAO voteInterestPointDAO=new VoteInterestPointDAO();

            try{
                resultCode = voteInterestPointDAO.addVoteInterestPoint(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultCode;
        }

        @Override
        protected void onPostExecute(Integer resultCode)
        {
            if(resultCode == HttpURLConnection.HTTP_OK){
                Toast.makeText(getApplicationContext(), "Vote enregistré", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Vote non enregistré", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
