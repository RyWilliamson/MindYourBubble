package com.example.mindyourbubble;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mindyourbubble.Data.PersonData;
import com.example.mindyourbubble.Utils.Keys;
import com.example.mindyourbubble.Utils.NavUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

import static android.view.View.VISIBLE;

public class MapActivity extends AppCompatActivity {

    private HashMap<String, PersonData> peopleData;
    private PersonData currentPerson;

    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_map );

        Bundle bundle = getIntent().getBundleExtra( Keys.MAINBUNDLE );
        this.peopleData = (HashMap<String, PersonData>) bundle.getSerializable( Keys.PEOPLEDATA );
        this.currentPerson = peopleData.get(bundle.getString(Keys.USERNAME));

        bottomNavigation = findViewById(R.id.bottom_navigation);
        NavUtils.setupBottomNav(bottomNavigation, R.id.map, navigationItemSelectedListener);
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = item -> {
        if ( item.getItemId() == R.id.line ) {
            // Move to line graph
            NavUtils.switchToLine( this, currentPerson.getUserName(), peopleData );
            return true;
        } else if ( item.getItemId() == R.id.bubble ) {
            //Move to bubble
            NavUtils.switchToBubble( this, currentPerson.getUserName(), peopleData );
            return true;
        } else if ( item.getItemId() == R.id.map ) {
            // Move to map
            return true;
        } else if ( item.getItemId() == R.id.profile ) {
            // Swap to profile
            NavUtils.switchToProfile( this, currentPerson.getUserName(), this.peopleData );
            return true;
        } else if ( item.getItemId() == R.id.info ) {
            // Swap to info activity
            NavUtils.switchToInfo( this, currentPerson.getUserName(), this.peopleData );
            return true;
        }
        return false;
    };

    public void addData( View view) {
        findViewById( R.id.textView2 ).setVisibility( VISIBLE );
    }
}