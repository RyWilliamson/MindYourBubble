package com.example.mindyourbubble.Fragments;

import android.util.Log;

import com.example.mindyourbubble.BubbleActivity;
import com.example.mindyourbubble.Utils.DataUtils;

import org.json.JSONException;

public class BubbleVisFragment extends BaseVisFragment {

    public BubbleVisFragment() {
        super( "bubbleVis.html" );
    }

    public void loadVisualisation() {
        BubbleActivity activity = (BubbleActivity) getActivity();
        String text = null;
        try {
            text = DataUtils.dataToBubbleFormat(activity.getPeopleData(), activity.getUsername());
        } catch ( JSONException e ) {
            e.printStackTrace();
        }
        int width = 380;
        int height = 500;
        webView.loadUrl(
                "javascript:loadVisualisation(" + text + "," + width + "," + height + ")" );
    }
}