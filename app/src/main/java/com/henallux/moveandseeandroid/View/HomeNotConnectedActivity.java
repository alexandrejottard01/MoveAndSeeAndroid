package com.henallux.moveandseeandroid.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.henallux.moveandseeandroid.R;

/**
 * Created by Alexandre on 14-11-17.
 */

public class HomeNotConnectedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_not_connected);
        setTitle(getString(R.string.title_home_not_connected));
    }
}
