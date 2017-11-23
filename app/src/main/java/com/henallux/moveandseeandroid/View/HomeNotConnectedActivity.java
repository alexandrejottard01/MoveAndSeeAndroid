package com.henallux.moveandseeandroid.View;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;
import com.henallux.moveandseeandroid.DAO.LoginDAO;
import com.henallux.moveandseeandroid.DAO.UserDAO;
import com.henallux.moveandseeandroid.Model.AccessToken;
import com.henallux.moveandseeandroid.Model.InterestPointWithVote;
import com.henallux.moveandseeandroid.Model.UnknownPoint;
import com.henallux.moveandseeandroid.Model.User;
import com.henallux.moveandseeandroid.Model.UserLogin;
import com.henallux.moveandseeandroid.R;

import java.util.HashMap;

/**
 * Created by Alexandre on 14-11-17.
 */

public class HomeNotConnectedActivity extends AppCompatActivity {

    //OnCreate
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_not_connected);
        setTitle(getString(R.string.title_home_not_connected));

        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText pseudoString = findViewById (R.id.pseudo);
                String pseudo = pseudoString.getText().toString();

                EditText passwordString = findViewById (R.id.password);
                String password = passwordString.getText().toString();

                UserLogin userLogin = new UserLogin(pseudo,password);
                new UserLoginAsync().execute(userLogin);
            }
        });
    }

    private class UserLoginAsync extends AsyncTask<UserLogin, Void, AccessToken> {

        @Override
        protected AccessToken doInBackground(UserLogin... params) {
            LoginDAO loginDAO = new LoginDAO();
            AccessToken token = null;
            try {
                token = loginDAO.connexion(params[0]);
                if(token == null){
                    Toast.makeText(HomeNotConnectedActivity.this,"token null", Toast.LENGTH_SHORT).show();

                }
                Log.i("Contenu inputJsonString",  token.toString());
            }catch (Exception e) {
                e.printStackTrace();
            }
            return token;
        }

        @Override
        protected void onPostExecute(AccessToken token){
            if(token != null){
                Gson gson = new Gson();
                String tokenConverted = gson.toJson(token);
                Intent intentToHome = new Intent(HomeNotConnectedActivity.this, ProfileActivity.class);
                intentToHome.putExtra("accessToken", tokenConverted);

                startActivity(intentToHome);
            }else{
                Toast.makeText(HomeNotConnectedActivity.this,"connexion Error", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
