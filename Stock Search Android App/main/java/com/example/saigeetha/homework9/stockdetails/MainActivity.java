package com.example.saigeetha.homework9.stockdetails;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    private EditText companySymbol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);

        companySymbol = (EditText) findViewById(R.id.symbol);

        final AutoCompleteTextView autoCompleteTextView=(AutoCompleteTextView)findViewById(R.id.symbol);
        if(autoCompleteTextView!=null) {
            autoCompleteTextView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {


                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String compSy=s.toString();
                    if(companySymbol.length()>0) {
                        new AutoCompClass().execute(compSy);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        final Button button = (Button) findViewById(R.id.getQuoteButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                String compSymbol = "";

                compSymbol = autoCompleteTextView.getText().toString();
                Log.i("main", compSymbol);

                if(compSymbol.length()==0){
                    Toast.makeText(MainActivity.this,"Please enter a valid Symbol!",Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(MainActivity.this, Form2.class);
                intent.putExtra("COMPANY_SYMBOL",compSymbol);
                startActivity(intent);
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    //JSON data
                    new GETJSONTask().execute(compSymbol);
                } else {
                    Log.i("main","No network connection available.");
                    //textView.setText("No network connection available.");
                }
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public class AutoCompClass extends AsyncTask<String,Integer,String> {
        public void AutoCompClass(){

        }

        @Override
        protected String doInBackground(String... params) {
            URL url=null;
            String temp;
            StringBuilder stringBuilder=new StringBuilder();
            try{
                url=new URL("http://stockdetails-1274.appspot.com/?CompanySymbol="+params[0]);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.connect();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                while((temp=bufferedReader.readLine())!=null) {
                    stringBuilder.append(temp);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            final String symbol=s;
            JSONArray jsonArray=null;

            try {
                jsonArray=new JSONArray(s);
                String autoFill[]=new String[jsonArray.length()];
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    autoFill[i]=jsonObject.getString("Name")+"\n"+jsonObject.getString("Symbol");
                }
                Log.i("AutoFill",""+autoFill.length);
                final AutoCompleteTextView autoCompleteTextView=(AutoCompleteTextView)findViewById(R.id.symbol);
                ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,autoFill);

                autoCompleteTextView.setAdapter(arrayAdapter);
                autoCompleteTextView.setThreshold(1);
                autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            JSONArray jsonArray1=new JSONArray(symbol);
                            JSONObject jsonObject=jsonArray1.getJSONObject(position);
                            autoCompleteTextView.setText(jsonObject.getString("Symbol"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // To clear form values
    public void clearFormValues(View view) {
        companySymbol.setText("");
    }

    private class GETJSONTask extends AsyncTask<String, Void, String> {
        //private ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        String url = "http://stockdetails-1274.appspot.com/?CompanySymbol=";
        String mTAG = "myAsyncTask";

        protected String doInBackground(String... urls) {
            Log.d(mTAG, "Background Work in Progress");
            try {
                url=url+ URLEncoder.encode(urls[0], "UTF-8");
              /*  url=url+"&getQuote=";
                url=url+URLEncoder.encode("true", "UTF-8");*/
                Log.i("main","URL"+url);

                String stockData=downloadUrl(url);
                Log.i("main","downloadURL stockData"+stockData);
                Log.i("main", "Intent Created");
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
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
}
