package com.example.tatparya.walmartbot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tatparya on 10/17/15.
 */
public class SMSBroadcastReceiver extends BroadcastReceiver {

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "logtag";

    String message;
    int count = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Intent recieved: " + intent.getAction());

        if (intent.getAction() == SMS_RECEIVED) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[])bundle.get("pdus");
                final SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                if (messages.length > -1) {
                    int i = 0;
                    while( i < messages.length ){
                        message += messages[i].getMessageBody();
                        i += 1;
                    }
                    Log.d( TAG, "Message received : \n" + message );
                    parseString( message );
//                    SmsManager sm = SmsManager.getDefault();
//                    String number = "7652320499";
//                    String msg = "1";
//                    sm.sendTextMessage(number, null, msg, null, null);

                }
            }
        }
    }

    public void parseString( String str ){

        FetchDataTask task = new FetchDataTask();

        String pattern = ".*Next item: (.*)";
        Pattern pat = Pattern.compile(pattern);
        Matcher m = pat.matcher(str);
        if (m.find( )) {
            String found = m.group(0);
            found = found.replaceAll("Next item: ", "" );
            Log.d("logtag", "Found value: " + found);
            task.execute(found);
        } else {
            Log.d("logtag", "No match!" );
            //  Send text here
//            SmsManager sm = SmsManager.getDefault();
//            String number = "7652320499";
//            String msg = "1";
//            sm.sendTextMessage(number, null, msg, null, null);
        }


    }

    public void restartCycle( String price ){
        SmsManager sm = SmsManager.getDefault();
        String number = "7652320499";
        String msg = price;
        sm.sendTextMessage(number, null, msg, null, null);
    }

    //  ParseJSON
    private String getProductDataFromJson( String productJson )
        throws JSONException    {

        JSONObject prodJson = new JSONObject( productJson );
        JSONArray itemArray = prodJson.getJSONArray( "items" );

        double itemPrice = itemArray.getJSONObject( 0 ).getDouble("salePrice");

        if( itemPrice > 10 ){
            itemPrice /= 10;
        }

        String priceStr = "" + itemPrice;

        Log.d( "logtag", "Item Price found : " + itemPrice );

        //  Send text here
        restartCycle( priceStr );

        return priceStr;
    }

    public class FetchDataTask extends AsyncTask< String, Void, String > {

        //  Send message using this task

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Log.d("logtag", "Getting data!");
        }

        @Override
        protected void onPostExecute(String strings) {
            super.onPostExecute(strings);

            //  got price here
        }

        //  Get product price here
        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String apiKey = "nxguqk5spk56pecrwhucx3jz";
            String pubId = "tatparya";
            String description = "Cola";

            String returnJSONStr = null;
            String format = "json";

            try {
                //  Construct URL for query
                final String BASE_URL = "http://api.walmartlabs.com/v1/search?" +
                        "apiKey=nxguqk5spk56pecrwhucx3jz" +
                        "&lsPublisherId=tatparya";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter("query", params[0])
                        .build();

                //  Built url successfully
                Log.d("logtag", "Built URI : " + builtUri);

                URL url = new URL(builtUri.toString());

                //  Create request to Walmart api
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                //  Read input stream
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    returnJSONStr = null;
                    //  Send text here
                    restartCycle( "1" );
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append((line + "\n"));
                }

                if (buffer.length() == 0) {
                    returnJSONStr = null;
                    //  Send text here
                    restartCycle( "1" );
                }

                returnJSONStr = buffer.toString();
                Log.d("logtag", "returnJSONStr : \n" + returnJSONStr);

            } catch (IOException e) {
                Log.d("logtag", "Error: " + e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("logtag", "Error closing stream: " + e);
                        //  Send text here
                        restartCycle("1");
                    }
                }
            }

            //  Parse JSON string
            try {
                return getProductDataFromJson(returnJSONStr);
            } catch (Exception e) {
                Log.d("logtag", "Error : " + e);
                //  Send text here
                restartCycle("1");
            }

            return null;
        }


    }
}
