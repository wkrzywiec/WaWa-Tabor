package com.example.wojciechkrzywiec.wawa_tabor.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.wojciechkrzywiec.wawa_tabor.R;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by Wojtek Krzywiec on 30/07/2017.
 */

public class DatabaseSyncJobService extends JobService {

    AsyncTask<Void, Void, Boolean> mFetchTransportTask;

    private int lineType;

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {

        Bundle extras = jobParameters.getExtras();
        lineType = extras.getInt(getApplicationContext().getString(R.string.line_type));

        mFetchTransportTask = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {

                Context context = getApplicationContext();
                DatabaseSyncTask.syncDatabase(context, lineType);
                jobFinished(jobParameters, false);
                return true;
            }
            @Override
            protected void onPostExecute(Boolean success) {
                jobFinished(jobParameters, !success);
            }
        };

            mFetchTransportTask.execute();

        return true;
    }


    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.i("Wojciechkrzywiec", "servicejob sie skonczyl");
        if (mFetchTransportTask != null) {
            mFetchTransportTask.cancel(true);
        }
        jobFinished(jobParameters, true);
        return true;
    }

}
