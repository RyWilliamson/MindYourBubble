package com.example.mindyourbubble;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.mindyourbubble.Data.PersonData;
import com.example.mindyourbubble.Utils.Keys;
import com.example.mindyourbubble.Utils.NavUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

public class LineActivity extends AppCompatActivity {

    private HashMap<String, PersonData> peopleData;
    private String currentUsername;

    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_line );

        Bundle bundle = getIntent().getBundleExtra( Keys.MAINBUNDLE );
        this.peopleData = (HashMap<String, PersonData>) bundle.getSerializable( Keys.PEOPLEDATA );
        this.currentUsername = bundle.getString(Keys.USERNAME);

        bottomNavigation = findViewById( R.id.bottom_navigation );
        NavUtils.setupBottomNav( bottomNavigation, R.id.line, navigationItemSelectedListener );
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = item -> {
        if ( item.getItemId() == R.id.line ) {
            // Move to line graph
            return true;
        } else if ( item.getItemId() == R.id.bubble ) {
            //Move to bubble
            NavUtils.switchToBubble( this, currentUsername, peopleData );
            return true;
        } else if ( item.getItemId() == R.id.map ) {
            // Move to map
            NavUtils.switchToMap( this, currentUsername, peopleData );
            return true;
        } else if ( item.getItemId() == R.id.profile ) {
            NavUtils.switchToProfile( this, currentUsername, this.peopleData );
            return true;
        } else if ( item.getItemId() == R.id.info ) {
            // Swap to info activity
            NavUtils.switchToInfo( this, currentUsername, this.peopleData );
            return true;
        }
        return false;
    };
}