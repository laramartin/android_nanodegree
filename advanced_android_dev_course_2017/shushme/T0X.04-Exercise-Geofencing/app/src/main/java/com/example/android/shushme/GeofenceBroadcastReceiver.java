package com.example.android.shushme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = GeofenceBroadcastReceiver.class.getCanonicalName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(LOG_TAG, "Broadcast received!");
    }
}
