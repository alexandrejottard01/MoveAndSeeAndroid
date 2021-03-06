package com.henallux.moveandseeandroid.View;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.auth0.android.jwt.JWT;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.henallux.moveandseeandroid.Adapter.CustomListDescription;
import com.henallux.moveandseeandroid.DAO.DescriptionDAO;
import com.henallux.moveandseeandroid.DAO.InterestPointDAO;
import com.henallux.moveandseeandroid.DAO.UnknownPointDAO;
import com.henallux.moveandseeandroid.DAO.UserDAO;
import com.henallux.moveandseeandroid.DAO.VoteInterestPointDAO;
import com.henallux.moveandseeandroid.Model.DescriptionWithVote;
import com.henallux.moveandseeandroid.Model.InterestPointWithVote;
import com.henallux.moveandseeandroid.Model.UnknownPoint;
import com.henallux.moveandseeandroid.Model.User;
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

public class HomeConnectedActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener {

    private InterestPointWithVote interestPointWithVoteCurrent;
    private User userCurrent;
    private SharedPreferences preferences;
    private String token;
    private GoogleMap map;
    HashMap<Marker, Long> hashMap = new HashMap();
    HashMap<Marker, UnknownPoint> hashMapUnknownPoint = new HashMap();
    private ListView listDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_connected);
        setTitle(getString(R.string.title_home_connected));

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        alertGoogleServicesNotAvailable();

        setTokenInPreferences();
        fillUserCurrentByToken(token);
    }

    private void setTokenInPreferences() {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = preferences.getString("token", null);
    }

    private void fillUserCurrentByToken(String token) {
        String pseudoUser = getUsernameByToken(token);
        if(connectionInternetAvailable()){
            new GetUserByPseudoAsync().execute(pseudoUser);
        }
        else{
            Toast.makeText(getApplicationContext(), R.string.not_internet, Toast.LENGTH_SHORT).show();
        }
    }

    private String getUsernameByToken(String token) {
        JWT jwt = new JWT(token);
        return jwt.getSubject();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_profile:
                Intent goToProfile = new Intent(HomeConnectedActivity.this, ProfileActivity.class);
                startActivity(goToProfile);
                return true;

            case R.id.action_create_unknown_point:
                Intent goToCreateUnknownPoint = new Intent(HomeConnectedActivity.this, CreateUnknownPointActivity.class);
                startActivity(goToCreateUnknownPoint);
                return true;

            case R.id.action_sign_out:
                Intent goToHomeNotConnected = new Intent(HomeConnectedActivity.this, HomeNotConnectedActivity.class);
                startActivity(goToHomeNotConnected);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        displayInterestPoints();
        displayUnknownPoints();

        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        goToLocationZoom(50.469424, 4.862533, 15);
        googleMap.setOnMarkerClickListener(this);

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                LatLng latitudeLongitude = new LatLng(point.latitude, point.longitude);
                goToCreateInterestPointActivity(latitudeLongitude);
            }
        });

    }

    private void displayInterestPoints(){
        try {
            if(connectionInternetAvailable()){
                new GetAllInterestPointsAsync().execute();
            }
            else{
                Toast.makeText(getApplicationContext(), R.string.not_internet, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayUnknownPoints(){
        try {
            if(connectionInternetAvailable()){
                new GetAllUnknownPointsAsync().execute();
            }
            else{
                Toast.makeText(getApplicationContext(), R.string.not_internet, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goToLocationZoom(double latitude, double longitude, float zoom) {
        LatLng latitudeLongitude = new LatLng(latitude, longitude);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latitudeLongitude, zoom);
        map.moveCamera(update);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        if(marker.getTitle() == null){ //Changer la condition pour détecter si c'est un point d'intéret ou point inconnu
            UnknownPoint unknownPoint = hashMapUnknownPoint.get(marker);
            goToCreateDescriptionOfUnknownPointActivity(unknownPoint);
        }
        else{

            long idInterestPointSelected = hashMap.get(marker);

            try{
                if(connectionInternetAvailable()){
                    interestPointWithVoteCurrent = new GetInterestPointByIdAsync().execute(idInterestPointSelected).get();
                }
                else{
                    Toast.makeText(getApplicationContext(), R.string.not_internet, Toast.LENGTH_SHORT).show();
                }
            }catch(Exception e){
                e.printStackTrace();
            }

            fillInterestPointInLayout(interestPointWithVoteCurrent);

            clickButtonAddDescription();
            clickButtonAddVotePositiveInterestPoint();
            clickButtonVoteNegativeInterestPoint();
        }
        return true;
    }

    private void clickButtonAddDescription() {
        Button addDescription = findViewById(R.id.button_add_description);
            addDescription.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                goToCreateDescriptionOfInterestPointActivity(interestPointWithVoteCurrent);
            }
        });
        addDescription.setVisibility(View.VISIBLE);
    }

    private void clickButtonVoteNegativeInterestPoint() {
        ImageButton addVoteNegative = findViewById(R.id.thumb_down);
        addVoteNegative.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                long idInterestPointCurrent = interestPointWithVoteCurrent.interestPoint.idInterestPoint;
                VoteInterestPoint voteInterestPointNegative = new VoteInterestPoint(false,userCurrent.id,idInterestPointCurrent);
                if(connectionInternetAvailable()){
                    new AddVoteInterestPointAsync().execute(voteInterestPointNegative);
                }
                else{
                    Toast.makeText(getApplicationContext(), R.string.not_internet, Toast.LENGTH_SHORT).show();
                }

                try{
                    if(connectionInternetAvailable()){
                        interestPointWithVoteCurrent = new GetInterestPointByIdAsync().execute(idInterestPointCurrent).get();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), R.string.not_internet, Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }

                fillAverageInterestPointInLayout(interestPointWithVoteCurrent);
            }
        });
        addVoteNegative.setVisibility(View.VISIBLE);
    }

    private void clickButtonAddVotePositiveInterestPoint() {
        ImageButton addVotePositive = findViewById(R.id.thumb_up);
        addVotePositive.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                long idInterestPointCurrent = interestPointWithVoteCurrent.interestPoint.idInterestPoint;
                VoteInterestPoint voteInterestPointPositive = new VoteInterestPoint(true,userCurrent.id,idInterestPointCurrent);
                if(connectionInternetAvailable()){
                    new AddVoteInterestPointAsync().execute(voteInterestPointPositive);
                }
                else{
                    Toast.makeText(getApplicationContext(), R.string.not_internet, Toast.LENGTH_SHORT).show();
                }


                try{
                    if(connectionInternetAvailable()){
                        interestPointWithVoteCurrent = new GetInterestPointByIdAsync().execute(idInterestPointCurrent).get();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), R.string.not_internet, Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
                fillAverageInterestPointInLayout(interestPointWithVoteCurrent);
            }
        });
        addVotePositive.setVisibility(View.VISIBLE);
    }

    private void goToCreateDescriptionOfInterestPointActivity(InterestPointWithVote interestPointWithVote){
        if(connectionInternetAvailable()){
            Gson gson = new Gson();
            Intent intentToCreateDescriptionOfInterestPoint = new Intent(HomeConnectedActivity.this, CreateDescriptionOfInterestPointActivity.class);
            intentToCreateDescriptionOfInterestPoint.putExtra("interestPointSelected", gson.toJson(interestPointWithVote));
            startActivity(intentToCreateDescriptionOfInterestPoint);
        }
        else{
            Toast.makeText(getApplicationContext(), R.string.not_internet, Toast.LENGTH_SHORT).show();
        }
    }

    private void goToCreateDescriptionOfUnknownPointActivity(UnknownPoint unknownPoint){
        if(connectionInternetAvailable()){
            Gson gson = new Gson();
            Intent intentCreateDescriptionOfUnknownPoint = new Intent(HomeConnectedActivity.this, CreateDescriptionOfUnknownPointActivity.class);
            intentCreateDescriptionOfUnknownPoint.putExtra("unknownPointSelected", gson.toJson(unknownPoint));
            startActivity(intentCreateDescriptionOfUnknownPoint);
        }
        else{
            Toast.makeText(getApplicationContext(), R.string.not_internet, Toast.LENGTH_SHORT).show();
        }
    }

    private void goToCreateInterestPointActivity(LatLng latitudeLongitude){
        if(connectionInternetAvailable()){
            Gson gson = new Gson();
            Intent intentToCreateInterestPointActivity = new Intent(HomeConnectedActivity.this, CreateInterestPointActivity.class);
            intentToCreateInterestPointActivity.putExtra("latitudeLongitudeSelected", gson.toJson(latitudeLongitude));
            startActivity(intentToCreateInterestPointActivity);
        }
        else{
            Toast.makeText(getApplicationContext(), R.string.not_internet, Toast.LENGTH_SHORT).show();
        }
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
        if(connectionInternetAvailable()){
            new GetAllDescriptionByInterestPointAsync().execute(interestPointWithVote);
        }
        else{
            Toast.makeText(getApplicationContext(), R.string.not_internet, Toast.LENGTH_SHORT).show();
        }
    }

    private void alertGoogleServicesNotAvailable(){
        if (!googleServicesAvailable()) {
            Toast.makeText(this, R.string.message_service_unavailable_google_map, Toast.LENGTH_LONG).show();
        }
    }

    private boolean googleServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        return(isAvailable == ConnectionResult.SUCCESS);
    }

    public String getToken(){
        return token;
    }

    public User getUserCurrent() {
        return userCurrent;
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), R.string.back_not_available, Toast.LENGTH_SHORT).show();
    }

    private class GetAllInterestPointsAsync extends AsyncTask<String,Void,ArrayList<InterestPointWithVote>>
    {
        @Override
        protected ArrayList<InterestPointWithVote> doInBackground(String... params)
        {
            ArrayList<InterestPointWithVote> listInterestPoints = new ArrayList<>();
            InterestPointDAO interestPointDAO=new InterestPointDAO();
            try{
                listInterestPoints = interestPointDAO.getAllInterestPoint(token);
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

                hashMap.put(marker, item.interestPoint.idInterestPoint);
            }
        }
    }

    private boolean connectionInternetAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    private class GetAllUnknownPointsAsync extends AsyncTask<String,Void,ArrayList<UnknownPoint>>
    {
        @Override
        protected ArrayList<UnknownPoint> doInBackground(String... params)
        {
            ArrayList<UnknownPoint> listUnknownPoints = new ArrayList<>();
            UnknownPointDAO unknownPointDAO=new UnknownPointDAO();
            try{
                listUnknownPoints=unknownPointDAO.getAllUnknownPoint(token);
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

    private class GetAllDescriptionByInterestPointAsync extends AsyncTask<InterestPointWithVote,Void,ArrayList<DescriptionWithVote>>
    {
        @Override
        protected ArrayList<DescriptionWithVote> doInBackground(InterestPointWithVote... params) {
            DescriptionDAO descriptionDao=new DescriptionDAO();
            ArrayList<DescriptionWithVote> listDescriptionAsync=new ArrayList<>();
            try{
                listDescriptionAsync = descriptionDao.getAllDescriptionsByInterestPoint(token, params[0].interestPoint.idInterestPoint);
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

    private class AddVoteInterestPointAsync extends AsyncTask<VoteInterestPoint,Void,Integer>
    {
        @Override
        protected Integer doInBackground(VoteInterestPoint... params) {
            Integer resultCode =0;
            VoteInterestPointDAO voteInterestPointDAO=new VoteInterestPointDAO();

            try{
                resultCode = voteInterestPointDAO.addVoteInterestPoint(token, params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultCode;
        }

        @Override
        protected void onPostExecute(Integer resultCode)
        {
            if(resultCode != HttpURLConnection.HTTP_OK){
                Toast.makeText(getApplicationContext(), R.string.message_vote_not_recorded, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class GetUserByPseudoAsync extends AsyncTask<String,Void,User>
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
                Toast.makeText(getApplicationContext(), R.string.user_not_find, Toast.LENGTH_SHORT).show();
            }

            userCurrent = user;
        }
    }

    private class GetInterestPointByIdAsync extends AsyncTask<Long,Void,InterestPointWithVote>
    {
        @Override
        protected InterestPointWithVote doInBackground(Long... params) {
            InterestPointWithVote interestPointWithVote;
            InterestPointDAO interestPointDAO = new InterestPointDAO();

            try{
                interestPointWithVote = interestPointDAO.getInterestPointById(token, params[0]);
            } catch (Exception e) {
                e.printStackTrace();
                interestPointWithVote = null;
            }
            return interestPointWithVote;
        }

        @Override
        protected void onPostExecute(InterestPointWithVote interestPointWithVote)
        {
            if(interestPointWithVote == null){
                Toast.makeText(getApplicationContext(), R.string.interest_point_not_found, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
