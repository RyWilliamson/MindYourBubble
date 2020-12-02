package com.example.mindyourbubble;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.example.mindyourbubble.Data.PersonData;
import com.example.mindyourbubble.Utils.Keys;
import com.example.mindyourbubble.Utils.NavUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

public class InfoActivity extends AppCompatActivity {

    private HashMap<String, PersonData> peopleData;
    private String currentUsername;

    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_info );

        Bundle bundle = getIntent().getBundleExtra( Keys.MAINBUNDLE );
        this.peopleData = (HashMap<String, PersonData>) bundle.getSerializable( Keys.PEOPLEDATA );
        this.currentUsername = bundle.getString(Keys.USERNAME);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        NavUtils.setupBottomNav(bottomNavigation, R.id.info, navigationItemSelectedListener);

        ((TextView) findViewById( R.id.TierZero )).setMovementMethod( LinkMovementMethod.getInstance() );
        ((TextView) findViewById( R.id.TierOne )).setMovementMethod( LinkMovementMethod.getInstance() );
        ((TextView) findViewById( R.id.TierTwo )).setMovementMethod( LinkMovementMethod.getInstance() );
        ((TextView) findViewById( R.id.TierThree )).setMovementMethod( LinkMovementMethod.getInstance() );
        ((TextView) findViewById( R.id.TierFour )).setMovementMethod( LinkMovementMethod.getInstance() );
        ((TextView) findViewById( R.id.MainTierLink )).setMovementMethod( LinkMovementMethod.getInstance() );
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = item -> {
        if ( item.getItemId() == R.id.line ) {
            // Move to line graph
            NavUtils.switchToLine( this, currentUsername, peopleData );
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
            // Do nothing since already in info
            return true;
        }
        return false;
    };
}