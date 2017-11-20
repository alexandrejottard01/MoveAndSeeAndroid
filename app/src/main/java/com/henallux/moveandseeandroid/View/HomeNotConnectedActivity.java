package com.henallux.moveandseeandroid.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.henallux.moveandseeandroid.Model.InterestPointWithVote;
import com.henallux.moveandseeandroid.Model.UnknownPoint;
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
    }
}
