package com.henallux.moveandseeandroid.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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
    }
}