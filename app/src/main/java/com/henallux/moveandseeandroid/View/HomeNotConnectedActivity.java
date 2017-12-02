package com.henallux.moveandseeandroid.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.henallux.moveandseeandroid.DAO.LoginDAO;
import com.henallux.moveandseeandroid.Exception.HttpResultException;
import com.henallux.moveandseeandroid.Model.AccessToken;
import com.henallux.moveandseeandroid.Model.UserLogin;
import com.henallux.moveandseeandroid.R;

import java.net.HttpURLConnection;

/**
 * Created by Alexandre on 14-11-17.
 */

public class HomeNotConnectedActivity extends AppCompatActivity {

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_not_connected);
        setTitle(getString(R.string.title_home_not_connected));

        clickButtonLogin();
        clickButtonRegister();
    }

    private void clickButtonRegister() {
        Button register = findViewById(R.id.toRegistration);
        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intentToRegistration = new Intent(HomeNotConnectedActivity.this, RegistrationActivity.class);
                startActivity(intentToRegistration);
            }
        });
    }

    private void clickButtonLogin() {
        Button login = findViewById(R.id.login);
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

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), R.string.back_not_available, Toast.LENGTH_SHORT).show();
    }

    private class UserLoginAsync extends AsyncTask<UserLogin, Void, AccessToken> {
        private int resultCode;
        @Override
        protected AccessToken doInBackground(UserLogin... params) {
            LoginDAO loginDAO = new LoginDAO();
            AccessToken token = null;
            try {
                token = loginDAO.connexion(params[0]);
            }catch (HttpResultException e){
                resultCode = e.getResultCode();
            }catch (Exception e){
                e.printStackTrace();
            }
            return token;
        }

        @Override
        protected void onPostExecute(AccessToken token){
            if(token != null){
                preferences = PreferenceManager.getDefaultSharedPreferences(HomeNotConnectedActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("token",token.getToken());
                editor.commit();

                Intent intentToHomeConnected = new Intent(HomeNotConnectedActivity.this, HomeConnectedActivity.class);
                startActivity(intentToHomeConnected);
            }else{
                switch (resultCode){
                    case HttpURLConnection.HTTP_UNAUTHORIZED:
                    case HttpURLConnection.HTTP_BAD_REQUEST:
                        Toast.makeText(HomeNotConnectedActivity.this, R.string.username_or_password_fail, Toast.LENGTH_SHORT).show();
                        break;

                    case HttpURLConnection.HTTP_UNAVAILABLE:
                        Toast.makeText(HomeNotConnectedActivity.this, R.string.service_not_available, Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        System.out.println(getString(R.string.error_login));
                }
           }
        }
    }
}
