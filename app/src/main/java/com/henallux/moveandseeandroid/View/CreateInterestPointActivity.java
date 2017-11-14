package com.henallux.moveandseeandroid.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.henallux.moveandseeandroid.R;

/**
 * Created by Alexandre on 14-11-17.
 */

public class CreateInterestPointActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_interest_point);
        setTitle(getString(R.string.title_create_interest_point));
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
                Intent goToHomeConnected = new Intent(CreateInterestPointActivity.this, HomeConnectedActivity.class);
                startActivity(goToHomeConnected);
                return true;

            case R.id.action_profile :
                Intent goToProfile = new Intent(CreateInterestPointActivity.this, ProfileActivity.class);
                startActivity(goToProfile);
                return true;

            case R.id.action_create_unknown_point :
                Intent goToCreateUnknownPoint = new Intent(CreateInterestPointActivity.this, CreateUnknownPointActivity.class);
                startActivity(goToCreateUnknownPoint);
                return true;

            case R.id.action_sign_out :
                Intent goToHomeNotConnected = new Intent(CreateInterestPointActivity.this, HomeNotConnectedActivity.class);
                startActivity(goToHomeNotConnected);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
