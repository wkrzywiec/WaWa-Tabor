package com.example.wojciechkrzywiec.wawa_tabor.sync;

import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by Wojtek Krzywiec on 30/07/2017.
 */

public class DatabaseSyncJobService extends JobService {

    private AsyncTask<Void, Void, Void> mFetchTransportTask;

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {

        mFetchTransportTask = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                Context context = getApplicationContext();
                DatabaseSyncTask.syncDatabase(context);
                jobFinished(jobParameters, false);
//TODO Add Intent that will be sent to LocalBroadcastManagera & update Activities
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                jobFinished(jobParameters, false);
            }
        };

        mFetchTransportTask.execute();
        return true;
    }


    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if (mFetchTransportTask != null) {
            mFetchTransportTask.cancel(true);
        }
        return true;
    }

}
