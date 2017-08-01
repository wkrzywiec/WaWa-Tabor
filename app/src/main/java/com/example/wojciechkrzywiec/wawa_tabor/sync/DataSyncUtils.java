package com.example.wojciechkrzywiec.wawa_tabor.sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.example.wojciechkrzywiec.wawa_tabor.data.TransportContract;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

/**
 * Created by Wojtek Krzywiec on 30/07/2017.
 */

public class DataSyncUtils {

    private static boolean sInitialized;

    private static final String DATABASE_SYNC_TAG = "database_sync";
    private static final int SYNC_INTERVAL_SECONDS = 10;
    private static final int SYNC_FLEXTIME_SECONDS = 5;

    private static Driver driver;
    private static FirebaseJobDispatcher dispatcher;

    synchronized public static void initialize(@NonNull final Context context) {

        if (sInitialized) return;
        sInitialized = true;

        scheduleJobDispatcher(context);

        Thread checkForEmpty = new Thread(new Runnable() {

            @Override
            public void run() {

                Uri checkEmptyTableQueryUri = TransportContract.TransportEntry.TABLE_URI;

                String[] projectionColumns = {TransportContract.TransportEntry._ID};

                Cursor cursor = context.getContentResolver().query(
                        checkEmptyTableQueryUri,
                        projectionColumns,
                        null,
                        null,
                        null);


                if (null == cursor || cursor.getCount() == 0) {
                    startImmediateSync(context);
                }

                cursor.close();
            }
        });

        checkForEmpty.start();

    }


    static void scheduleJobDispatcher(@NonNull final Context context) {

        driver = new GooglePlayDriver(context);
        dispatcher = new FirebaseJobDispatcher(driver);


        Job databaseSyncJob = dispatcher.newJobBuilder()

                .setService(DatabaseSyncJobService.class)

                .setTag(DATABASE_SYNC_TAG)

                .setConstraints(Constraint.ON_ANY_NETWORK)

                .setLifetime(Lifetime.UNTIL_NEXT_BOOT)

                .setRecurring(true)

                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)

                .setTrigger(Trigger.executionWindow(
                        SYNC_INTERVAL_SECONDS,
                        SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))

                .setReplaceCurrent(false)

                .build();

        dispatcher.mustSchedule(databaseSyncJob);
        //dispatcher.schedule(databaseSyncJob);


    }

    public static void startImmediateSync(@NonNull final Context context) {

        Intent intentToSyncDatabase = new Intent(context, DatabaseSyncIntentService.class);
        context.startService(intentToSyncDatabase);

    }

    public static void cancelScheduledJob(){
        dispatcher.cancel(DATABASE_SYNC_TAG);
    }
}
