package com.example.saigeetha.homework9.stockdetails;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by SaiGeetha on 4/30/2016.
 */
public class HistoricalTab extends Fragment {
    WebView highChart;
   // View startView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View startView = inflater.inflate(R.layout.historical_tab, container, false);
        highChart=(WebView) startView.findViewById(R.id.highchart_icon);

        Intent i=getActivity().getIntent();
        final String symbol = i.getStringExtra("COMPANY_SYMBOL");

        final HighChartClass highChartClass = new HighChartClass(this.getContext());
        highChart.addJavascriptInterface(highChartClass,"AndroidFunction");
        highChart.getSettings().setBuiltInZoomControls(true);
        highChart.getSettings().setJavaScriptEnabled(true);
        highChart.loadUrl("file:///android_assets/highcharts.html");

        highChart.loadUrl("javascript:generateHistoricalChart()");

        highChart.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view,String url) {
                super.onPageFinished(view,url);
                highChart.loadUrl("javascript:generateHistoricalChart('"+symbol+"')");
            }
        });
        try
        {
            InputStream inputStream=((Activity)getActivity()).getAssets().open("highcharts.html");
            int len=inputStream.available();
            byte[] buff=new byte[len];
            inputStream.read(buff);
            inputStream.close();

            String html=new String(buff);
            highChart.loadDataWithBaseURL("file:///android_assets",html,"text/html","UTF-8",null);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return startView;
    }

    public class HighChartClass {
        Context context;
        HighChartClass(Context context){
            this.context=context;
        }
    }
}
