package com.example.mindyourbubble.Fragments;

import com.example.mindyourbubble.Utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapVisFragment extends BaseVisFragment {


    public MapVisFragment() {
        super( "mapVis.html" );
    }

    public void loadVisualisation() {
        String tempsData = JsonUtils.readJsonFile( this.getActivity(), "datasets/map_temps.json" );
        String mapData = JsonUtils.readJsonFile( this.getActivity(), "map/topo_scotland.json" );
        JSONObject caseData;
        try {
            caseData = processNHSData();
        } catch ( JSONException e ) {
            e.printStackTrace();
            return;
        }

        int width = 380;
        int height = 500;
        webView.loadUrl(
                "javascript:loadVisualisation(" + tempsData + "," + caseData + "," + mapData + "," +
                        width + "," + height + ")" );
    }

    // This should really be rewritten but for a prototype it'll do
    private JSONObject processNHSData() throws JSONException {
        JSONArray nhsData = JsonUtils.getJson( this.getActivity(),
                "datasets/data_scots.json" ).getJSONArray( "data" );

        JSONObject caseData = new JSONObject();
        for ( int i = 0; i < nhsData.length(); i++ ) {
            JSONObject datum = nhsData.getJSONObject( i );
            String areaName = datum.getString( "areaName" );
            String areaCode = datum.getString( "areaCode" );

            float casesRate;
            try {
                casesRate = (float) datum.getDouble( "cumCasesByPublishDateRate" );
            } catch ( JSONException e ) {
                continue;
            }

            if ( caseData.has( areaCode ) ) {
                JSONObject caseItem = caseData.getJSONObject( areaCode );
                int count = caseItem.getInt( "count" );
                if ( count >= 28 ) {
                    continue;
                }
                float[] newDayCases = JsonUtils.jsonArrayToFloatArray(
                        caseItem.getJSONArray( "cases" ) );
                newDayCases[count] = casesRate;
                caseItem.put( "cases", new JSONArray( newDayCases ) );
                caseItem.put( "count", ++count );
            } else {
                JSONObject caseItem = new JSONObject();
                caseItem.put( "count", 0 );
                caseItem.put( "cases", new JSONArray( new float[28] ) );
                caseItem.put( "areaName", areaName );
                caseData.put( areaCode, caseItem );
            }
        }
        return caseData;
    }
}