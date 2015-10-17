package com.example.tatparya.walmartbot;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;

public class WelcomeScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        SMSBroadcastReceiver receiver = new SMSBroadcastReceiver();
        Log.d("logtag", "Hello World!!");
        //  Init first message
        SmsManager sm = SmsManager.getDefault();
        String number = "7652320499";
        String msg = "1";
        sm.sendTextMessage(number, null, msg, null, null);

        Log.d("logtag", "Sent message!");

    }
}
