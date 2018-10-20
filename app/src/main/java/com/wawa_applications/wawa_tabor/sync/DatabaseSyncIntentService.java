package com.wawa_applications.wawa_tabor.sync;

import android.app.IntentService;
import android.content.Intent;

import com.wawa_applications.wawa_tabor.R;

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
        String lineNumber = intent.getStringExtra(getApplicationContext().getString(R.string.line_number));
        DatabaseSyncTask.syncDatabase(this, lineType, lineNumber);
    }
}
