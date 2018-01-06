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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.henallux.moveandseeandroid.DAO.LoginDAO;
import com.henallux.moveandseeandroid.DAO.UserDAO;
import com.henallux.moveandseeandroid.Enumeration.RegistrationCodeErrorEnumeration;
import com.henallux.moveandseeandroid.Model.AccessToken;
import com.henallux.moveandseeandroid.Model.User;
import com.henallux.moveandseeandroid.Model.UserLogin;
import com.henallux.moveandseeandroid.R;

/**
 * Created by Alexandre on 14-11-17.
 */

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setTitle(getString(R.string.title_registration));

        clickButtonRegister();
    }

    private void clickButtonRegister() {
        Button register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText pseudoString = findViewById (R.id.pseudo);
                String pseudo = pseudoString.getText().toString();

                EditText emailString = findViewById (R.id.email);
                String email = emailString.getText().toString();

                EditText passwordString = findViewById (R.id.password);
                String password = passwordString.getText().toString();

                EditText confirmationPasswordString = findViewById (R.id.confirmation_password);
                String confirmationPassword = confirmationPasswordString.getText().toString();

                User user = new User(pseudo,false,password,email,"French");

                RegistrationCodeErrorEnumeration code = user.isValid(confirmationPassword);
                switch (code){
                    case BAD_PSEUDO:
                        Toast.makeText(RegistrationActivity.this, com.henallux.moveandseeandroid.R.string.PseudoError, Toast.LENGTH_SHORT).show();
                        break;
                    case BAD_EMAIL:
                        Toast.makeText(RegistrationActivity.this, com.henallux.moveandseeandroid.R.string.emailError, Toast.LENGTH_SHORT).show();
                        break;
                    case BAD_PASSWORD:
                        Toast.makeText(RegistrationActivity.this, com.henallux.moveandseeandroid.R.string.passwordError, Toast.LENGTH_SHORT).show();
                        break;
                    case PASSWORD_MISS_MATCH:
                        Toast.makeText(RegistrationActivity.this, R.string.different_passwords, Toast.LENGTH_SHORT).show();
                        break;
                    case OK:
                        if(connectionInternetAvailable()){
                            new UserRegisterAsync().execute(user);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), R.string.not_internet, Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        Toast.makeText(RegistrationActivity.this, com.henallux.moveandseeandroid.R.string.errer_unknown, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean connectionInternetAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    private class UserRegisterAsync extends AsyncTask<User, Void, Integer> {

        @Override
        protected Integer doInBackground(User... params) {
            UserDAO userDAO = new UserDAO();
            Integer resultCode = 0;
            try {
                resultCode = userDAO.addUser(params[0]);
            }catch (Exception e) {
                e.printStackTrace();
            }
            return resultCode;
        }

        @Override
        protected void onPostExecute(Integer resultCode){
            if(resultCode == 200){
                Intent intentToHomeNotConnected = new Intent(RegistrationActivity.this, HomeNotConnectedActivity.class);
                startActivity(intentToHomeNotConnected);
            }else{
                Toast.makeText(RegistrationActivity.this, R.string.registration_error, Toast.LENGTH_LONG).show();
            }
        }
    }
}
