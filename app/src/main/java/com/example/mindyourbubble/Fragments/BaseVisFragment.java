package com.example.mindyourbubble.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.mindyourbubble.Data.PersonData;
import com.example.mindyourbubble.R;

import java.util.HashMap;

public abstract class BaseVisFragment extends Fragment {

    private View topLevelView;
    protected WebView webView;
    private String filepath;

    public BaseVisFragment(String filename) {
        filepath = "file:///android_asset/html/" + filename;
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState ) {
        super.onCreateView( inflater, container, savedInstanceState );

        boolean attachToRoot = false;
        topLevelView = inflater.inflate( R.layout.fragment_base_vis, container, attachToRoot );

        initVisualisation();

        return topLevelView;
    }

    @SuppressLint( "SetJavaScriptEnabled" )
    public void initVisualisation() {
        View stub = topLevelView.findViewById( R.id.visualisation_stub );

        if ( stub instanceof ViewStub ) {
            stub.setVisibility( View.VISIBLE );

            webView = topLevelView.findViewById( R.id.visualisation_webview );
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled( true );
            webView.setWebChromeClient( new WebChromeClient() );
            webView.setWebViewClient( new WebViewClient() {
                @Override
                public void onPageFinished( WebView view, String url ) {
                    loadVisualisation();
                }
            });

            webView.loadUrl( filepath );
        }
    }

    public abstract void loadVisualisation();
}