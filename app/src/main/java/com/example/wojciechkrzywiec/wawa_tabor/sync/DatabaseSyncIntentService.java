package com.example.wojciechkrzywiec.wawa_tabor.sync;

import android.app.IntentService;
import android.content.Intent;

import com.example.wojciechkrzywiec.wawa_tabor.R;

/**
 * Created by Wojtek Krzywiec on 30/07/2017.
 */

public class DatabaseSyncIntentService extends IntentService {

    public DatabaseSyncIntentService() {
        super("DatabaseSyncIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        int lineType = intent.getIntExtra(getApplicationContext().getString(R.string.line_type), 1);
        DatabaseSyncTask.syncDatabase(this, lineType);
    }
}
