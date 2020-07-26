package com.gprs.haryana;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class InternetBroadcastReciever extends BroadcastReceiver {
    private static final String TAG = "MyBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (!NetworkUtil.getConnectivityStatusString(context)) {
            Intent i = new Intent(context, internetalert.class);
            context.startActivity(i);
        }
    }
}