package com.example.mindyourbubble.Data;

import android.util.Log;

import com.example.mindyourbubble.Utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PersonData implements Serializable {
    private String name;
    private String username;
    // Password stuff would be replaced using the Android secrets system in a real app
    private String password;
    private String postcode;
    private HealthData healthData;
    private String household;
    private List<String> social;
    private int otherLinks;

    public PersonData( JSONObject data ) {
        try {
            this.name = data.getString( "name" );
            this.username = data.getString( "username" );
            this.password = data.getString( "password" );
            this.postcode = data.getString( "postcode" );
            this.healthData = new HealthData( data );
            this.household = data.getString( "household" );
            this.social = createSocialList( data.getJSONArray( "social_bubble" ) );
            this.otherLinks = data.getInt("other_connections");
        } catch ( JSONException e ) {
            Log.e( "Person Data Constructor",
                    "failed to construct from json: " + e.toString() );
        }
    }

    public PersonData(String name, String username, String password, String postcode) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.postcode = postcode;
        this.healthData = null;
        this.household = null;
        this.social = new ArrayList<String>();
        this.otherLinks = 0;
    }

    public PersonData( String jsonString ) {
        this( JsonUtils.convertToJson( jsonString ) );
    }

    private ArrayList<String> createSocialList( JSONArray arr ) {
        ArrayList<String> output = new ArrayList<>();
        if (arr == null) {
            return output;
        }

        for (int i = 0; i < arr.length(); i++) {
            try {
                output.add(arr.getString(i));
            } catch ( JSONException e ) {
                e.printStackTrace();
                break;
            }
        }

        return output;
    }

    @Override
    public String toString() {
        return "PersonData{" +
                "name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", postcode='" + postcode + '\'' +
                "\n" + healthData +
                "\n" + household +
                "\n social=" + social +
                '}';
    }

    public String getName() {
        return name;
    }

    public String getUserName() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPostcode() {
        return postcode;
    }

    public HealthData getHealthData() {
        return healthData;
    }

    public String getHousehold() {
        return household;
    }

    public List<String> getSocial() {
        return social;
    }

    public void setHousehold( String household ) {
        this.household = household;
    }

    public void setHealthData( HealthData data) {
        this.healthData = data;
    }

    public int getOtherLinks() {
        return otherLinks;
    }
}
