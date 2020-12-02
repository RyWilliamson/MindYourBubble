package com.example.mindyourbubble.Data;

import com.example.mindyourbubble.Utils.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Arrays;

public class SkinData implements Serializable {
    private final String unit;
    private final float[] skinTemps;

    public SkinData( JSONObject data ) throws JSONException {
        this.unit = data.getString( "unit" );
        this.skinTemps = JsonUtils.jsonArrayToFloatArray( data.getJSONArray( "data" ) );
    }

    @Override
    public String toString() {
        return "SkinData{" +
                "unit='" + unit + '\'' +
                ", skinTemps=" + Arrays.toString( skinTemps ) +
                '}';
    }

    public String getUnit() {
        return unit;
    }

    public float[] getSkinTemps() {
        return skinTemps;
    }

    public float getAverageSkinTemp() {
        if ( skinTemps.length == 0 ) {
            return 0;
        }
        float sum = 0;
        for ( float temp : skinTemps ) {
            sum += temp;
        }
        return sum / skinTemps.length;
    }
}
