package com.example.mindyourbubble.Data;

import com.example.mindyourbubble.Utils.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Arrays;

public class RespiratoryData implements Serializable {
    private final String unit;
    private final float[] respiratoryRates;

    public RespiratoryData( JSONObject data ) throws JSONException {
        this.unit = data.getString( "unit" );
        this.respiratoryRates = JsonUtils.jsonArrayToFloatArray( data.getJSONArray( "data" ) );
    }

    @Override
    public String toString() {
        return "RespiratoryData{" +
                "unit='" + unit + '\'' +
                ", respiratoryRates=" + Arrays.toString( respiratoryRates ) +
                '}';
    }

    public String getUnit() {
        return unit;
    }

    public float[] getRespiratoryRates() {
        return respiratoryRates;
    }
}
