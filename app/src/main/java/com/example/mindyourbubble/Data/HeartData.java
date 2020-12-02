package com.example.mindyourbubble.Data;

import com.example.mindyourbubble.Utils.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Arrays;

public class HeartData implements Serializable {
    private final String unit;
    private final float[] heartRates;

    public HeartData( JSONObject data ) throws JSONException {
        this.unit = data.getString( "unit" );
        this.heartRates = JsonUtils.jsonArrayToFloatArray( data.getJSONArray( "data" ) );
    }

    @Override
    public String toString() {
        return "HeartData{" +
                "unit='" + unit + '\'' +
                ", heartRates=" + Arrays.toString( heartRates ) +
                '}';
    }

    public String getUnit() {
        return unit;
    }

    public float[] getHeartRates() {
        return heartRates;
    }
}
