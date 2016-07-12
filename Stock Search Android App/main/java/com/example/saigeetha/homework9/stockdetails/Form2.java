package com.example.saigeetha.homework9.stockdetails;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by SaiGeetha on 4/30/2016.
 */
public class Form2 extends AppCompatActivity {
    String name=null;
    String lastPrice=null;
    String compSymbol=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form2);
        Intent i=getIntent();
        compSymbol = i.getStringExtra("COMPANY_SYMBOL");

        new GETJSONTask().execute(compSymbol);
        /*//fb post
        FacebookSdk.sdkInitialize(getApplicationContext());
        ImageView fbIcon=(ImageView)findViewById(R.id.fb_icon);
        fbIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }

            final ShareDialog shareDialog=new ShareDialog(Form2.this);
            ShareLinkContent shareLinkContent=new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse("http://dev.markitondemand.com/MODApis/"))
                    .setContentTitle("Cooommbgh")
                    .setImageUrl(Uri.parse("http://chart.finance.yahoo.com/t?s=AAPL&lang=en-US&width=400&height=300"))
                    .build();

            shareDialog.show(shareLinkContent);
        });*/

        FacebookSdk.sdkInitialize(getApplicationContext());
        ImageView fbIcon=(ImageView)findViewById(R.id.fb_icon);

        fbIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Form2.this, "Loading facebook page",Toast.LENGTH_LONG).show();
                final ShareDialog shareDialog= new ShareDialog(Form2.this);
                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse("http://dev.markitondemand.com/MODApis/"))
                        .setContentTitle("Current Stock Price of "+name+", "+lastPrice)
                        .setImageUrl(Uri.parse("http://chart.finance.yahoo.com/t?s="+compSymbol+"&lang=en-US&width=400&height=300"))
                        .build();
                shareDialog.show(content);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("CURRENT"));
        tabLayout.addTab(tabLayout.newTab().setText("HISTORICAL"));
        tabLayout.addTab(tabLayout.newTab().setText("NEWS"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private class GETJSONTask extends AsyncTask<String, Void, String> {
        //private ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        String url = "http://csci571-web.appspot.com/?symbol=";
        String mTAG = "myAsyncTask";

        protected String doInBackground(String... urls) {
            Log.d(mTAG, "Background Work in Progress");
            try {
                url=url+ URLEncoder.encode(urls[0], "UTF-8");
              /*  url=url+"&getQuote=";
                url=url+URLEncoder.encode("true", "UTF-8");*/
                Log.i("main","URL"+url);

                String stockData=downloadUrl(url);
                JSONObject jsonObject=new JSONObject(stockData);
                name=jsonObject.getString("Name");
                lastPrice=jsonObject.getString("LastPrice");
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "true";
        }
    }


    private String downloadUrl(String myurl) throws IOException {
        Log.i("main","Inside downloadURL function");
        InputStream is = null;
        try {
            URL url = new URL(myurl);
            //Log.i("main",myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            Log.i("main","HTTP Conn exec");
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            Log.i("main","Connection established");
            int response = conn.getResponseCode();
            Log.i("main", "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public String readIt(InputStream stream) throws IOException, UnsupportedEncodingException {
        Log.i("main","Inside readIt function");
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        StringBuilder output = new StringBuilder();
        char[] buffer = new char[1000];
        for (; ; ) {
            int abh = reader.read(buffer, 0, buffer.length);
            if (abh < 0)
                break;
            output.append(buffer, 0, abh);
        }
        return output.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


