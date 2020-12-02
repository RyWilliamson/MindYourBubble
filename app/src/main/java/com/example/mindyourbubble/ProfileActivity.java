package com.example.mindyourbubble;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mindyourbubble.Data.HealthData;
import com.example.mindyourbubble.Data.PersonData;
import com.example.mindyourbubble.ListViewImpl.HouseholdAdapter;
import com.example.mindyourbubble.ListViewImpl.SocialsAdapter;
import com.example.mindyourbubble.Utils.DataUtils;
import com.example.mindyourbubble.Utils.JsonUtils;
import com.example.mindyourbubble.Utils.Keys;
import com.example.mindyourbubble.Utils.NavUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ProfileActivity extends AppCompatActivity {

    private HashMap<String, PersonData> peopleData;
    private PersonData currentPerson;

    private RecyclerView householdList;
    private RecyclerView socialList;

    private BottomNavigationView bottomNavigation;

    private PopupWindow popupWindow;

    private TextView personalInfoName;
    private TextView personalInfoUsername;
    private TextView householdName;
    private TextView householdPostcode;
    private TextView updatelabel;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_profile );

        Bundle bundle = getIntent().getExtras();
        if ( bundle.getString( Keys.ACTIVITY ).equals( Keys.LOGIN ) ) {
            peopleData = DataUtils.loadData( findViewById( R.id.ProfileWindow ) );
            login( bundle );
        } else if ( bundle.getString( Keys.ACTIVITY ).equals( Keys.REGISTER ) ) {
            peopleData = DataUtils.loadData( findViewById( R.id.ProfileWindow ) );
            register( bundle );
        } else if ( bundle.getString( Keys.ACTIVITY ).equals( Keys.HOUSEHOLD_ACTIVITY ) ) {
            this.peopleData = (HashMap<String, PersonData>) bundle.getBundle(
                    Keys.MAINBUNDLE ).getSerializable( Keys.PEOPLEDATA );
            login( bundle.getBundle( Keys.MAINBUNDLE ) );
            this.currentPerson.setHousehold( bundle.getBundle(
                    Keys.MAINBUNDLE ).getString( Keys.HOUSEHOLD ) );
        } else {
            this.peopleData = (HashMap<String, PersonData>) bundle.getBundle(
                    Keys.MAINBUNDLE ).getSerializable( Keys.PEOPLEDATA );
            login( bundle.getBundle( Keys.MAINBUNDLE ) );
        }

        bottomNavigation = findViewById( R.id.bottom_navigation );
        NavUtils.setupBottomNav( bottomNavigation, R.id.profile, navigationItemSelectedListener );

        personalInfoName = findViewById( R.id.PersonalInfoName );
        personalInfoUsername = findViewById( R.id.PersonalInfoUsername );
        householdName = findViewById( R.id.HouseholdName );
        householdPostcode = findViewById( R.id.HouseholdPostcode );
        updatelabel = findViewById( R.id.Updated );

        personalInfoName.setText( currentPerson.getName() );
        personalInfoUsername.setText( currentPerson.getUserName() );
        householdName.setText( currentPerson.getHousehold() );
        householdPostcode.setText( currentPerson.getPostcode() );

        if ( currentPerson.getHousehold() == null ) {
            setGoneVisible( R.id.YesHousehold, R.id.NoHousehold );
        } else {
            setGoneVisible( R.id.NoHousehold, R.id.YesHousehold );
            setupHouseList();
        }

        if ( currentPerson.getHealthData() == null ) {
            setGoneVisible( R.id.YesDevice, R.id.NoDevice );
        } else {
            setGoneVisible( R.id.NoDevice, R.id.YesDevice );
        }

        setupSocialList();

    }

    private void setGoneVisible( int viewid1, int viewid2 ) {
        findViewById( viewid1 ).setVisibility( GONE );
        findViewById( viewid2 ).setVisibility( VISIBLE );
    }

    private void setupSocialList() {
        socialList = findViewById( R.id.SocialBubble );
        SocialsAdapter socialsAdapter = new SocialsAdapter( currentPerson.getUserName(),
                peopleData );
        socialList.setAdapter( socialsAdapter );
        socialList.setLayoutManager( new LinearLayoutManager( this ) );
    }

    private void setupHouseList() {
        householdList = findViewById( R.id.HouseholdOccupants );
        HouseholdAdapter householdAdapter = new HouseholdAdapter( currentPerson.getUserName(),
                peopleData );
        householdList.setAdapter( householdAdapter );
        householdList.setLayoutManager( new LinearLayoutManager( this ) );
    }

    private void login( Bundle bundle ) {
        currentPerson = this.peopleData.get( bundle.getString( Keys.USERNAME ) );
    }

    private void register( Bundle bundle ) {
        currentPerson = new PersonData( bundle.getString( Keys.FULLNAME ),
                bundle.getString( Keys.USERNAME ), bundle.getString( Keys.PASSWORD ),
                bundle.getString( Keys.POSTCODE ) );
        this.peopleData.put( currentPerson.getUserName(), currentPerson );
    }

    public void syncData( View view ) {
        currentPerson.setHealthData( new HealthData( JsonUtils.getJson( view.getContext(),
                "datasets/People/" + currentPerson.getName().replace( " ", "_" ) +
                        ".json" ) ) );
        updatelabel.setText( R.string.time_now );
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
            NavUtils.switchToMap( this, currentPerson.getUserName(), peopleData );
            return true;
        } else if ( item.getItemId() == R.id.profile ) {
            // Do nothing since already in profile
            return true;
        } else if ( item.getItemId() == R.id.info ) {
            // Swap to info activity
            NavUtils.switchToInfo( this, currentPerson.getUserName(), this.peopleData );
            return true;
        }
        return false;
    };

    public void removeDevice( View view ) {
        this.currentPerson.setHealthData( null );
        setGoneVisible( R.id.YesDevice, R.id.NoDevice );
    }

    public void addDevice( View view ) {
        LayoutInflater layoutInflater = (LayoutInflater) ProfileActivity.this.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE );
        View customView = layoutInflater.inflate( R.layout.add_device_popup, null );

        Button closePopupBtn = (Button) customView.findViewById( R.id.SocialAdd );

        //instantiate popup window
        popupWindow = new PopupWindow( customView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT );

        //display the popup window
        popupWindow.showAtLocation( findViewById( R.id.SyncDataLabel ), Gravity.CENTER, 0, 0 );

        //close the popup window on button click
        closePopupBtn.setOnClickListener( v -> {
            if ( ( (RadioButton) customView.findViewById( R.id.PopupButton ) ).isChecked() ) {
                Log.d( "test", "test" );
                setGoneVisible( R.id.NoDevice, R.id.YesDevice );
                updatelabel.setText( R.string.time_never );
            }
            popupWindow.dismiss();
        } );
    }

    public void leaveHousehold( View view ) {
        this.currentPerson.setHousehold( null );

        setGoneVisible( R.id.YesHousehold, R.id.NoHousehold );
    }

    public void joinHousehold( View view ) {
        boolean found = false;
        String input = ( (EditText) findViewById( R.id.HouseholdInput ) ).getText().toString();
        for ( String username : peopleData.keySet() ) {
            Log.d( "test", input );
            if ( peopleData.get( username ).getHousehold() != null &&
                    peopleData.get( username ).getHousehold().equals( input ) ) {
                found = true;
                break;
            }
        }

        if ( found ) {
            this.currentPerson.setHousehold( input );
            setGoneVisible( R.id.NoHousehold, R.id.YesHousehold );
            setupHouseList();
            householdName.setText( input );
            householdPostcode.setText( currentPerson.getPostcode() );
        }
    }

    public void addSocial( View view ) {
        LayoutInflater layoutInflater = (LayoutInflater) ProfileActivity.this.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE );
        View customView = layoutInflater.inflate( R.layout.add_social_popup, null );

        Button closePopupBtn = (Button) customView.findViewById( R.id.PersonAdd );

        //instantiate popup window
        popupWindow = new PopupWindow( customView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT );

        popupWindow.setFocusable( true );

        //display the popup window
        popupWindow.showAtLocation( findViewById( R.id.SyncDataLabel ), Gravity.CENTER, 0, 0 );

        //close the popup window on button click
        closePopupBtn.setOnClickListener( v -> {
            if ( peopleData.containsKey( ( (EditText) customView.findViewById(
                    R.id.addUsername ) ).getText().toString() ) ) {
                this.currentPerson.getSocial().add( ( (EditText) customView.findViewById(
                        R.id.addUsername ) ).getText().toString() );
                peopleData.get( ( (EditText) customView.findViewById(
                        R.id.addUsername ) ).getText().toString() ).getSocial().add(
                        currentPerson.getUserName() );
                SocialsAdapter adapter = ( (SocialsAdapter) socialList.getAdapter() );
                adapter.getmPeople().add( peopleData.get( ( (EditText) customView.findViewById(
                        R.id.addUsername ) ).getText().toString() ) );
                adapter.notifyItemInserted( adapter.getItemCount() - 1 );
            }
            popupWindow.dismiss();
        } );
    }

    public void logout( View view ) {
        Intent intent = new Intent( this, MainActivity.class );
        startActivity( intent );
    }

    public void createHousehold( View view ) {
        Intent intent = new Intent( this, HouseholdActivity.class );
        Bundle bundle = new Bundle();
        bundle.putString( Keys.USERNAME, currentPerson.getUserName() );
        bundle.putSerializable( Keys.PEOPLEDATA, this.peopleData );
        intent.putExtra( Keys.MAINBUNDLE, bundle );
        startActivity( intent );
    }
}