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
import android.widget.TextView;
import android.widget.Toast;

import com.auth0.android.jwt.JWT;
import com.henallux.moveandseeandroid.DAO.UserDAO;
import com.henallux.moveandseeandroid.Model.AccessToken;
import com.henallux.moveandseeandroid.Model.User;
import com.henallux.moveandseeandroid.R;

/**
 * Created by Alexandre on 14-11-17.
 */

public class ProfileActivity extends AppCompatActivity {

    //Variable d'instance
    private User userCurrent;
    private SharedPreferences preferences;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle(getString(R.string.title_profile));

        setTokenInPreferences();

        String pseudo = getUsernameByToken(token);
        fillUserCurrentById(pseudo);

        TextView pseudoTextView = findViewById(R.id.pseudo);
        pseudoTextView.setText(getString(R.string.username) + pseudo);

        TextView isCertifiedTextView = findViewById(R.id.isCertified);
        TextView nameCertifiedTextView = findViewById(R.id.nameCertified);

        if (userCurrent.isCertified) {
            isCertifiedTextView.setText(R.string.certified);
            nameCertifiedTextView.setText(getString(R.string.name_certified) + userCurrent.nameCertified);
        }else{
            isCertifiedTextView.setText(R.string.not_certified);
            nameCertifiedTextView.setText("");
        }

        TextView emailTextView = findViewById(R.id.email);
        emailTextView.setText(getString(R.string.email) + userCurrent.email);
    }

    private void setTokenInPreferences(){
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = preferences.getString("token",null);
    }

    private void fillUserCurrentById(String idUser){
        try{
            if(connectionInternetAvailable()){
                userCurrent = new GetUserByIdAsync().execute(idUser).get();
            }
            else{
                Toast.makeText(getApplicationContext(), R.string.not_internet, Toast.LENGTH_SHORT).show();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private String getUsernameByToken(String token){
        JWT jwt = new JWT(token);
        String subject = jwt.getSubject();
        return subject;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.action_home :
                Intent goToHomeConnected = new Intent(ProfileActivity.this, HomeConnectedActivity.class);
                startActivity(goToHomeConnected);
                return true;

            case R.id.action_create_unknown_point :
                Intent goToCreateUnknownPoint = new Intent(ProfileActivity.this, CreateUnknownPointActivity.class);
                startActivity(goToCreateUnknownPoint);
                return true;

            case R.id.action_sign_out :
                Intent goToHomeNotConnected = new Intent(ProfileActivity.this, HomeNotConnectedActivity.class);
                startActivity(goToHomeNotConnected);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean connectionInternetAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
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
                Toast.makeText(getApplicationContext(), R.string.user_not_find, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
