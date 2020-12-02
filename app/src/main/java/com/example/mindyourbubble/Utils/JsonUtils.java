package com.example.mindyourbubble.Utils;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonUtils {
    public static JSONObject getJson( Context context, String filename ) {
        return convertToJson( readJsonFile( context, filename ) );
    }

    public static String readJsonFile( Context context, String filename ) {
        String jsonString;

        try {
            InputStream is = context.getAssets().open( filename );

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read( buffer );
            is.close();

            jsonString = new String( buffer, "UTF-8" );
        } catch ( IOException e ) {
            e.printStackTrace();
            return null;
        }
        return jsonString;
    }

    public static JSONObject convertToJson( String jsonString ) {
        try {
            return new JSONObject( jsonString );
        } catch ( JSONException e ) {
            e.printStackTrace();
            return null;
        }
    }

    public static float[] jsonArrayToFloatArray( JSONArray jsonArray ) throws JSONException {
        float[] floats = new float[jsonArray.length()];
        for ( int i = 0; i < jsonArray.length(); i++ ) {
            floats[i] = (float) jsonArray.getDouble( i );
        }
        return floats;
    }

    public static int[] jsonArrayToIntArray( JSONArray jsonArray ) throws JSONException {
        int[] ints = new int[jsonArray.length()];
        for ( int i = 0; i < jsonArray.length(); i++ ) {
            ints[i] = jsonArray.getInt( i );
        }
        return ints;
    }

    public static List<String> jsonArrayToStringList( JSONArray jsonArray ) throws JSONException {
        List<String> strings = new ArrayList<>();
        for ( int i = 0; i < jsonArray.length(); i++ ) {
            strings.add( jsonArray.getString( i ) );
        }
        return strings;
    }
}
