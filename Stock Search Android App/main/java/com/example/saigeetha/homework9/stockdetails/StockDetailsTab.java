package com.example.saigeetha.homework9.stockdetails;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by SaiGeetha on 4/30/2016.
 */
public class StockDetailsTab extends Fragment {
    Bitmap bitmap;
    String symbol=null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String stockData;
        Intent i=getActivity().getIntent();
        symbol = i.getStringExtra("COMPANY_SYMBOL");

        View startview = inflater.inflate(R.layout.stock_details_tab, container, false);
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
                    Log.i("symbol set in tab1",symbol);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        View startview_inner=inflater.inflate(R.layout.stock_temp_details,container,false);
        new StockDetails(getContext(),startview,startview_inner).execute(symbol);
        return startview;

    }


    public class StockAdapter extends ArrayAdapter<StockDetailsBean>{


        public StockAdapter(Context context, ArrayList<StockDetailsBean> stockDetails){

            super(context,0,stockDetails);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            StockDetailsBean stockDetailsBean=getItem(position);

            if(convertView==null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.stock_temp_details, parent, false);
            }
                TextView tvLabel=(TextView) convertView.findViewById(R.id.label);
                TextView tvValue=(TextView) convertView.findViewById(R.id.value);

                tvLabel.setText(stockDetailsBean.getLabel());
                tvValue.setText("\n"+stockDetailsBean.getValue());

                /*if(String.valueOf(tvLabel.getText()).trim().equals("Change")) {
                    if(Integer.parseInt(String.valueOf(tvValue.getText()))>0) {
                        tvValue.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.up,0);
                    } else {
                        tvValue.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.down,0);
                    }
                }*/
                return convertView;

            }
        }

    public class StockDetails extends AsyncTask<String, Integer, String>{


        public View startview1;
        private View startview;
        private Context context;
        public String jsData;

        public void StockDetails(){}

        public StockDetails(Context context, View startview, View startview1) {
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
                url=new URL("http://stockdetails-1274.appspot.com/?QuoteSymbol="+params[0]);
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
            URL urlYahoo = null;
            try {
                urlYahoo = new URL("http://chart.finance.yahoo.com/t?s="+symbol+"&lang=en-US&width=400&height=300");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                bitmap=BitmapFactory.decodeStream(urlYahoo.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        @Override
        protected void onPostExecute(String s) {
        super.onPostExecute(s);

            ArrayList<StockDetailsBean> stockDetailsBeanArrayList=new ArrayList<StockDetailsBean>();
            StockAdapter stockAdapter=new StockAdapter(getContext(),stockDetailsBeanArrayList);
            final ListView listview= (ListView) startview.findViewById(R.id.stockdetailsListView);
            listview.setAdapter(stockAdapter);

            DecimalFormat decimalFormat=new DecimalFormat("#.##");
        try {
            jsData=s;
            JSONObject jsonObject=new JSONObject(s);
            Log.i("StockData",s);
            List<StockDetailsBean> stockDetailsBeanList=new ArrayList<StockDetailsBean>();


            StockDetailsBean stockDetailsBean=null;
            stockDetailsBean=new StockDetailsBean();
            stockDetailsBean.setLabel("Name");
            stockDetailsBean.setValue(jsonObject.getString("Name"));
            stockDetailsBeanList.add(stockDetailsBean);

            stockDetailsBean=null;
            stockDetailsBean=new StockDetailsBean();
            stockDetailsBean.setLabel("Symbol");
            stockDetailsBean.setValue(jsonObject.getString("Symbol"));
            String csymb=jsonObject.getString("Symbol");
            stockDetailsBeanList.add(stockDetailsBean);

            stockDetailsBean=null;
            stockDetailsBean=new StockDetailsBean();
            stockDetailsBean.setLabel("LastPrice");
            stockDetailsBean.setValue(jsonObject.getString("LastPrice"));
            stockDetailsBeanList.add(stockDetailsBean);

            stockDetailsBean=null;
            stockDetailsBean=new StockDetailsBean();
            stockDetailsBean.setLabel("Change");

            Double change=Double.valueOf(decimalFormat.format(Double.parseDouble(jsonObject.getString("Change"))));
            Double changePercent=Double.valueOf(decimalFormat.format(Double.parseDouble(jsonObject.getString("ChangePercent"))));
            stockDetailsBean.setValue(change.toString()+"("+changePercent.toString()+"%)");
            stockDetailsBeanList.add(stockDetailsBean);
/*
            stockDetailsBean=null;
            stockDetailsBean=new StockDetailsBean();
            stockDetailsBean.setLabel("ChangePercent");
            stockDetailsBean.setValue(jsonObject.getString("ChangePercent"));
            stockDetailsBeanList.add(stockDetailsBean);*/

            stockDetailsBean=null;
            stockDetailsBean=new StockDetailsBean();
            stockDetailsBean.setLabel("Timestamp");
            stockDetailsBean.setValue(jsonObject.getString("Timestamp"));
            stockDetailsBeanList.add(stockDetailsBean);

            stockDetailsBean=null;
            stockDetailsBean=new StockDetailsBean();
            stockDetailsBean.setLabel("MarketCap");
            long market=Long.parseLong(jsonObject.getString("MarketCap"));
            String cap="";
            if((market/1000000000.0) < 0.01) {
                cap=Math.round(((market/1000000000.0)*1000.0)*100.0)/100.0+" Million";
            } else {
                cap=Math.round(((market/1000000000.0)*100.0)/100.0)+" Billion";
            }
            stockDetailsBean.setValue(cap);
            stockDetailsBeanList.add(stockDetailsBean);

            stockDetailsBean=null;
            stockDetailsBean=new StockDetailsBean();
            stockDetailsBean.setLabel("Volume");
            stockDetailsBean.setValue(jsonObject.getString("Volume"));
            stockDetailsBeanList.add(stockDetailsBean);

            stockDetailsBean=null;
            stockDetailsBean=new StockDetailsBean();
            stockDetailsBean.setLabel("ChangeYTD");
            Double changeYTD=Double.valueOf(decimalFormat.format(Double.parseDouble(jsonObject.getString("ChangeYTD"))));
            Double changePercentYTD=Double.valueOf(decimalFormat.format(Double.parseDouble(jsonObject.getString("ChangePercentYTD"))));
            stockDetailsBean.setValue(changeYTD.toString()+"("+changePercentYTD.toString()+"%)");
            stockDetailsBeanList.add(stockDetailsBean);

/*
            stockDetailsBean=null;
            stockDetailsBean=new StockDetailsBean();
            stockDetailsBean.setLabel("ChangePercentYTD");
            stockDetailsBean.setValue(jsonObject.getString("ChangePercentYTD"));
            stockDetailsBeanList.add(stockDetailsBean);*/

            stockDetailsBean=null;
            stockDetailsBean=new StockDetailsBean();
            stockDetailsBean.setLabel("High");
            stockDetailsBean.setValue(jsonObject.getString("High"));
            stockDetailsBeanList.add(stockDetailsBean);

            stockDetailsBean=null;
            stockDetailsBean=new StockDetailsBean();
            stockDetailsBean.setLabel("Low");
            stockDetailsBean.setValue(jsonObject.getString("Low"));
            stockDetailsBeanList.add(stockDetailsBean);


            stockDetailsBean=null;
            stockDetailsBean=new StockDetailsBean();
            stockDetailsBean.setLabel("Open");
            stockDetailsBean.setValue(jsonObject.getString("Open"));
            stockDetailsBeanList.add(stockDetailsBean);


            stockDetailsBean=null;
            stockDetailsBean=new StockDetailsBean();
            stockDetailsBean.setLabel("Todays Stock Activity");
            stockDetailsBean.setValue("");
            stockDetailsBeanList.add(stockDetailsBean);

            stockAdapter.addAll(stockDetailsBeanList);

            ImageView yahooImage=(ImageView) startview.findViewById(R.id.yahooChart_icon);
            PhotoViewAttacher photoViewAttacher;

            yahooImage.setImageBitmap(bitmap);
            photoViewAttacher=new PhotoViewAttacher(yahooImage);
            photoViewAttacher.update();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        }
    }
}



