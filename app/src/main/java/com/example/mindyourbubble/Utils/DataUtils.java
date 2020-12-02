package com.example.mindyourbubble.Utils;

import android.util.Log;
import android.view.View;

import com.example.mindyourbubble.Data.PersonData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class DataUtils {
    public static String dataToBubbleFormat( Map<String, PersonData> data,
            String mainusername ) throws JSONException {
        JSONArray output = new JSONArray();
        HashMap<String, Integer> addedHouses = new HashMap<>();
        HashMap<String, Float> totals = new HashMap<>();
        int i = 0;

        for ( PersonData person : data.values() ) {
            if ( !data.get( mainusername ).getSocial().contains( person.getUserName() ) &&
                    !person.getUserName().equals( mainusername ) &&
                    !person.getHousehold().equals( data.get( mainusername ).getHousehold() ) ) {
                continue;
            }
            String household = person.getHousehold();
            float skinTemp = person.getHealthData().getSkinData().getAverageSkinTemp();

            if ( !addedHouses.containsKey( household ) ) {
                addedHouses.put( household, i++ );
                totals.put( household, skinTemp );
                output.put( createBubbleObject( person ) );
            } else {
                totals.put( person.getHousehold(), totals.get( person.getHousehold() ) + skinTemp );
                JSONObject person_json = new JSONObject();
                person_json.put( "name", person.getName() );
                person_json.put( "temp",
                        person.getHealthData().getSkinData().getAverageSkinTemp() );
                output.getJSONObject( addedHouses.get( household ) ).getJSONArray(
                        "visible_people" ).put( person_json );
            }

            HashSet<String> links = new HashSet<>();
            JSONArray known_links = output.getJSONObject(
                    addedHouses.get( household ) ).getJSONArray( "known_links" );
            links.addAll( JsonUtils.jsonArrayToStringList( known_links ) );
            for ( String username : person.getSocial() ) {
                if (data.get(mainusername).getSocial().contains(username)) {
                    Log.d("test", person.getName());
                    links.add( data.get( username ).getHousehold() );
                }
            }

            known_links = new JSONArray( links.toArray() );
            output.getJSONObject( addedHouses.get( household ) ).put( "known_links", known_links );
        }

        for ( String household : totals.keySet() ) {
            try {
                float average = totals.get( household ) /
                        (float) output.getJSONObject( addedHouses.get( household ) ).getJSONArray(
                                "visible_people" ).length();
                output.getJSONObject( addedHouses.get( household ) ).put( "avg_temp", average );
            } catch ( JSONException e ) {
                e.printStackTrace();
            }
        }

        //Log.d("test", output.toString(2));
        return output.toString();
    }

    private static JSONObject createBubbleObject( PersonData person ) {
        JSONObject output = new JSONObject();
        try {
            output.put( "name", person.getHousehold() );
            JSONObject person_json = new JSONObject();
            person_json.put( "name", person.getName() );
            person_json.put( "temp", person.getHealthData().getSkinData().getAverageSkinTemp() );
            output.put( "visible_people", new JSONArray().put( person_json ) );
            output.put( "known_links", new JSONArray() );
            output.put( "other_links", person.getOtherLinks() );
        } catch ( JSONException e ) {
            e.printStackTrace();
        }
        return output;
    }

    public static HashMap<String, PersonData> loadData( View view ) {
        HashMap<String, PersonData> people = new HashMap<>();
        String[] filenames;
        try {
            filenames = view.getContext().getAssets().list( "datasets/People" );
        } catch ( IOException e ) {
            e.printStackTrace();
            return people;
        }

        for ( final String filename : filenames ) {
            JSONObject userData = JsonUtils.getJson( view.getContext(),
                    "datasets/People/" + filename );
            try {
                people.put( userData.getString( "username" ), new PersonData( userData ) );
            } catch ( JSONException e ) {
                e.printStackTrace();
            }

        }
        return people;
    }
}
