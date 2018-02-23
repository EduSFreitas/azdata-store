package com.azdatastore.events;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.drive.events.ChangeEvent;
import com.google.android.gms.drive.events.DriveEventService;

public class MyDriveEventService extends DriveEventService {
    private static final String TAG = "MyDriveEventService";

    public static final String CHANGE_EVENT =
            "com.azdatastore.CHANGE_EVENT";
    private LocalBroadcastManager mBroadcaster;

    @Override
    public void onCreate() {
        super.onCreate();
        mBroadcaster = LocalBroadcastManager.getInstance(getApplicationContext());
    }

    @Override
    public void onChange(ChangeEvent changeEvent) {
        Log.d(TAG, "Received event: " + changeEvent);
        Intent intent = new Intent(CHANGE_EVENT);
        intent.putExtra("event", changeEvent);
        mBroadcaster.sendBroadcast(intent);
    }
}