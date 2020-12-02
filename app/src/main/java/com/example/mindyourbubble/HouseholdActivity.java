package com.example.mindyourbubble;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.mindyourbubble.Data.PersonData;
import com.example.mindyourbubble.Utils.Keys;
import com.example.mindyourbubble.Utils.NavUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

public class HouseholdActivity extends AppCompatActivity {

    private HashMap<String, PersonData> peopleData;
    private PersonData currentPerson;

    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_household );

        Bundle bundle = getIntent().getBundleExtra( Keys.MAINBUNDLE );
        this.peopleData = (HashMap<String, PersonData>) bundle.getSerializable(
                Keys.PEOPLEDATA );
        this.currentPerson = this.peopleData.get( bundle.getString( Keys.USERNAME ) );

        bottomNavigation = findViewById(R.id.bottom_navigation);
        NavUtils.setupBottomNav(bottomNavigation, R.id.profile, navigationItemSelectedListener);
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = item -> {
        if ( item.getItemId() == R.id.line ) {
            NavUtils.switchToLine( this, currentPerson.getUserName(), peopleData );
            return true;
        } else if ( item.getItemId() == R.id.bubble ) {
            //Move to bubble
            NavUtils.switchToBubble( this, currentPerson.getUserName(), peopleData );
            return true;
        } else if ( item.getItemId() == R.id.map ) {
            // Move to map
            NavUtils.switchToMap( this, currentPerson.getUserName(), peopleData );
            return true;
        } else if ( item.getItemId() == R.id.profile ) {
            backToProfile(null);
            return true;
        } else if ( item.getItemId() == R.id.info ) {
            // Swap to info activity
            NavUtils.switchToInfo( this, currentPerson.getUserName(), this.peopleData );
            return true;
        }
        return false;
    };

    private void backToProfile(String household) {
        Intent intent = new Intent( this, ProfileActivity.class );
        Bundle bundle = new Bundle();
        bundle.putString( Keys.USERNAME, currentPerson.getUserName() );
        bundle.putString( Keys.HOUSEHOLD, household );
        bundle.putSerializable( Keys.PEOPLEDATA, this.peopleData );
        intent.putExtra(Keys.ACTIVITY, Keys.HOUSEHOLD_ACTIVITY);
        intent.putExtra( Keys.MAINBUNDLE, bundle );
        startActivity( intent );
    }

    public void createHousehold( View view ) {
        if (((EditText) findViewById( R.id.EnterHouseholdName )).getText().toString() != null) {
            backToProfile(((EditText) findViewById( R.id.EnterHouseholdName )).getText().toString());
        }
    }

    public void cancel( View view ) {
        backToProfile(null);
    }
}