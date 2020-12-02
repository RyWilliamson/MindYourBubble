package com.example.mindyourbubble.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.mindyourbubble.BubbleActivity;
import com.example.mindyourbubble.Data.PersonData;
import com.example.mindyourbubble.InfoActivity;
import com.example.mindyourbubble.LineActivity;
import com.example.mindyourbubble.MapActivity;
import com.example.mindyourbubble.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

public class NavUtils {
    public static void setupBottomNav( BottomNavigationView bottomNavigation, int id,
            BottomNavigationView.OnNavigationItemSelectedListener listener ) {
        bottomNavigation.setOnNavigationItemSelectedListener( listener );
        bottomNavigation.getMenu().findItem( id ).setChecked( true );
    }

    public static void switchToInfo( Activity activity, String username,
            HashMap<String, PersonData> peopleData ) {
        Intent intent = new Intent( activity, InfoActivity.class );
        Bundle bundle = new Bundle();
        bundle.putString( Keys.USERNAME, username );
        bundle.putSerializable( Keys.PEOPLEDATA, peopleData );
        intent.putExtra( Keys.MAINBUNDLE, bundle );
        activity.startActivity( intent );
    }

    public static void switchToProfile( Activity activity, String username,
            HashMap<String, PersonData> peopleData ) {
        Intent intent = new Intent( activity, ProfileActivity.class );
        Bundle bundle = new Bundle();
        bundle.putString( Keys.USERNAME, username );
        bundle.putSerializable( Keys.PEOPLEDATA, peopleData );
        intent.putExtra( Keys.ACTIVITY, Keys.OTHER );
        intent.putExtra( Keys.MAINBUNDLE, bundle );
        activity.startActivity( intent );
    }

    public static void switchToLine( Activity activity, String username,
            HashMap<String, PersonData> peopleData ) {
        Intent intent = new Intent( activity, LineActivity.class );
        Bundle bundle = new Bundle();
        bundle.putString( Keys.USERNAME, username );
        bundle.putSerializable( Keys.PEOPLEDATA, peopleData );
        intent.putExtra( Keys.MAINBUNDLE, bundle );
        activity.startActivity( intent );
    }

    public static void switchToBubble( Activity activity, String username,
            HashMap<String, PersonData> peopleData ) {
        if (peopleData.get(username).getHealthData() != null) {
            Intent intent = new Intent( activity, BubbleActivity.class );
            Bundle bundle = new Bundle();
            bundle.putString( Keys.USERNAME, username );
            bundle.putSerializable( Keys.PEOPLEDATA, peopleData );
            intent.putExtra( Keys.MAINBUNDLE, bundle );
            activity.startActivity( intent );
        }
    }

    public static void switchToMap( Activity activity, String username,
            HashMap<String, PersonData> peopleData ) {
        Intent intent = new Intent( activity, MapActivity.class );
        Bundle bundle = new Bundle();
        bundle.putString( Keys.USERNAME, username );
        bundle.putSerializable( Keys.PEOPLEDATA, peopleData );
        intent.putExtra( Keys.MAINBUNDLE, bundle );
        activity.startActivity( intent );
    }

}
