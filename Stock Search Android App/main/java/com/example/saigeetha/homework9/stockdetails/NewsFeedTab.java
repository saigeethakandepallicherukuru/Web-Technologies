package com.example.saigeetha.homework9.stockdetails;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by SaiGeetha on 4/30/2016.
 */
public class NewsFeedTab extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String stockData;
        String symbol="AAPL";

        Intent i=getActivity().getIntent();
        symbol = i.getStringExtra("COMPANY_SYMBOL");

        View startview = inflater.inflate(R.layout.newsfeed_tab, container, false);
        if (savedInstanceState == null) {
            Bundle extras = getActivity().getIntent().getExtras();
            if (extras == null) {
                stockData = null;
            } else {
                stockData = extras.getString("COMPANY_SYMBOL");
                Log.i("form2", "Yay!! " + stockData);
                try {
                    JSONObject jsonObject1 = new JSONObject(stockData);
                    symbol = String.valueOf(jsonObject1.getString("Symbol")).trim();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        View startview_inner=inflater.inflate(R.layout.news_temp_details,container,false);
        new NewsDetails(getContext(),startview,startview_inner).execute(symbol);
        return startview;

    }


    public class NewsAdapter extends ArrayAdapter<NewsDetailsBean>{


        public NewsAdapter(Context context, ArrayList<NewsDetailsBean> newsDetailsBeen){

            super(context,0,newsDetailsBeen);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            NewsDetailsBean newsDetailsBean=getItem(position);

            if(convertView==null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_temp_details, parent, false);
            }
            TextView tvTitle=(TextView) convertView.findViewById(R.id.news_title);
            TextView tvDesc=(TextView) convertView.findViewById(R.id.desc);
            TextView tvSource=(TextView) convertView.findViewById(R.id.source);
            TextView tvDate=(TextView) convertView.findViewById(R.id.date);

            tvTitle.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
            tvTitle.setText(newsDetailsBean.getTitle());
            tvDesc.setText("\n"+newsDetailsBean.getDesc());
            tvSource.setText("\n Publisher: "+newsDetailsBean.getSource());

            String pattern="yyyy-MM-dd'T'HH:mm:ssZ";
            String pattern1="dd MMMM yyyy, HH:mm:ss";
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat(pattern1);
            SimpleDateFormat format=new SimpleDateFormat(pattern);
            SimpleDateFormat simpleDateFormat1=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            simpleDateFormat1.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date date= null;
            try {
                date = simpleDateFormat1.parse(newsDetailsBean.getDate().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String formatDate=simpleDateFormat.format(date);
            tvDate.setText("\n Date: "+formatDate);

            return convertView;

        }
    }

    public class NewsDetails extends AsyncTask<String, Integer, String>{


        public View startview1;
        private View startview;
        private Context context;
        public String jsData;

        public void Newsdetails(){}

        public NewsDetails(Context context, View startview, View startview1) {
            this.startview1 = startview1;
            this.startview = startview;
            this.context = context;
        }


        @Override
        protected String doInBackground(String... params) {
            URL url=null;
            String temp;
            StringBuilder stringBuilder=new StringBuilder();
            try{
                url=new URL("http://stockdetails-1274.appspot.com/?CompanyNews="+params[0]);
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            ArrayList<NewsDetailsBean> newsDetailsBeanArrayList=new ArrayList<NewsDetailsBean>();
            NewsAdapter newsAdapter=new NewsAdapter(getContext(),newsDetailsBeanArrayList);
            final ListView listview= (ListView) startview.findViewById(R.id.newsdetailsListView);
            listview.setAdapter(newsAdapter);

            jsData=s;
            try {
                JSONObject jsonObject=new JSONObject(s);
                Log.i("--NewsFeed--",s);
                JSONObject jsonObject1=new JSONObject(jsonObject.getString("d"));

                JSONArray jsonArray=new JSONArray(jsonObject1.getString("results"));

                List<NewsDetailsBean> newsDetailsBeanList=new ArrayList<NewsDetailsBean>();
                NewsDetailsBean newsDetailsBean=null;
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject2=new JSONObject(jsonArray.getString(i));
                    String title=jsonObject2.getString("Title");
                    String desc=jsonObject2.getString("Description");
                    String source=jsonObject2.getString("Source");
                    String date=jsonObject2.getString("Date");
                    newsDetailsBean=new NewsDetailsBean(title,desc,source,date);
                    newsDetailsBeanArrayList.add(newsDetailsBean);
                }
                newsAdapter.addAll(newsDetailsBeanArrayList);

                listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){


                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        JSONObject jsonObject1= null;
                        try {
                            JSONObject jsonObject=new JSONObject(jsData);
                            jsonObject1 = new JSONObject(jsonObject.getString("d"));
                            JSONArray jsonArray = new JSONArray(jsonObject1.getString("results"));

                            JSONObject jsonObject2=new JSONObject(jsonArray.getString(position));
                            Intent NewsDetailsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(jsonObject2.getString("Url")));
                            startActivity(NewsDetailsIntent);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}



