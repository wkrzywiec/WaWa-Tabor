package com.wawa_applications.wawa_tabor.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import com.wawa_applications.wawa_tabor.R;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by Wojtek Krzywiec on 30/07/2017.
 */

public class DatabaseSyncJobService extends JobService {

    AsyncTask<Void, Void, Boolean> mFetchTransportTask;

    private int mLineType;
    private String mLineNumber;

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {

        Bundle extras = jobParameters.getExtras();
        mLineType = extras.getInt(getApplicationContext().getString(R.string.line_type));
        mLineNumber = extras.getString(getApplicationContext().getString(R.string.line_number));

        mFetchTransportTask = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {

                Context context = getApplicationContext();
                DatabaseSyncTask.syncDatabase(context, mLineType, mLineNumber);
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
        if (mFetchTransportTask != null) {
            mFetchTransportTask.cancel(true);
        }
        jobFinished(jobParameters, false);
        return false;
    }

}
