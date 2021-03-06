package com.example.mindyourbubble;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.mindyourbubble.Data.PersonData;
import com.example.mindyourbubble.Utils.Keys;
import com.example.mindyourbubble.Utils.NavUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

public class BubbleActivity extends AppCompatActivity {

    private HashMap<String, PersonData> peopleData;
    private PersonData currentPerson;

    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_bubble );

        Bundle bundle = getIntent().getBundleExtra( Keys.MAINBUNDLE );
        this.peopleData = (HashMap<String, PersonData>) bundle.getSerializable( Keys.PEOPLEDATA );
        this.currentPerson = peopleData.get(bundle.getString(Keys.USERNAME));

        bottomNavigation = findViewById( R.id.bottom_navigation );
        NavUtils.setupBottomNav( bottomNavigation, R.id.bubble, navigationItemSelectedListener );
    }

    public HashMap<String, PersonData> getPeopleData() {
        return peopleData;
    }

    public String getUsername() { return currentPerson.getUserName(); }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = item -> {
        if ( item.getItemId() == R.id.line ) {
            NavUtils.switchToLine( this, currentPerson.getUserName(), peopleData );
            return true;
        } else if ( item.getItemId() == R.id.bubble ) {
            //Move to bubble
            return true;
        } else if ( item.getItemId() == R.id.map ) {
            // Move to map
            NavUtils.switchToMap( this, currentPerson.getUserName(), peopleData );
            return true;
        } else if ( item.getItemId() == R.id.profile ) {
            // Swap to profile
            NavUtils.switchToProfile( this, currentPerson.getUserName(), peopleData );
            return true;
        } else if ( item.getItemId() == R.id.info ) {
            // Swap to info activity
            NavUtils.switchToInfo( this, currentPerson.getUserName(), this.peopleData );
            return true;
        }
        return false;
    };
}