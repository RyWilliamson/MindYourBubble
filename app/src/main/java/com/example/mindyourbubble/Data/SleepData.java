package com.example.mindyourbubble.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Arrays;

public class SleepData implements Serializable {
    private DataElements[] sleeps;

    public SleepData( JSONObject data ) throws JSONException {
        JSONArray sleepData = data.getJSONArray( "data" );
        this.sleeps = new DataElements[sleepData.length()];
        for ( int i = 0; i < sleepData.length(); i++ ) {
            JSONObject sleep = sleepData.getJSONObject( i );
            sleeps[i] = new DataElements( (float) sleep.getDouble( "duration" ),
                    (float) sleep.getDouble( "onset" ), sleep.getInt( "awakenings" ) );
        }
    }

    @Override
    public String toString() {
        return "SleepData{" +
                "sleeps=" + Arrays.toString( sleeps ) +
                '}';
    }
}

class DataElements implements Serializable {
    float duration;
    float onset;
    int awakenings;

    public DataElements( float duration, float onset, int awakenings ) {
        this.duration = duration;
        this.onset = onset;
        this.awakenings = awakenings;
    }

    @Override
    public String toString() {
        return "{" +
                duration +
                ", " + onset +
                ", " + awakenings +
                '}';
    }
}
