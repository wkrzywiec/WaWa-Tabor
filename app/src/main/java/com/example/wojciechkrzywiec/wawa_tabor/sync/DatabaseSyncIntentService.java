package com.example.wojciechkrzywiec.wawa_tabor.sync;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by Wojtek Krzywiec on 30/07/2017.
 */

public class DatabaseSyncIntentService extends IntentService {

    public DatabaseSyncIntentService() {
        super("DatabaseSyncIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        DatabaseSyncTask.syncDatabase(this);
    }
}
