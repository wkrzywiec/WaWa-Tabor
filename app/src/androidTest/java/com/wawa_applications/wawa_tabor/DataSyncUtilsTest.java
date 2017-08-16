package com.wawa_applications.wawa_tabor;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.wawa_applications.wawa_tabor.sync.DataSyncUtils;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Wojtek Krzywiec on 12/08/2017.
 */

@RunWith(AndroidJUnit4.class)
public class DataSyncUtilsTest {


    private final Context mContext = InstrumentationRegistry.getTargetContext();

    @Before
    public void before(){

    }

    @Test
    public void given_JobIsNotRunning_When_JobStarts_Then_JobIsRunning(){

        int actualStatus = DataSyncUtils.initialize(mContext, 1, "131");

        Assert.assertEquals(FirebaseJobDispatcher.SCHEDULE_RESULT_SUCCESS, actualStatus);

        DataSyncUtils.cancelScheduledJob();
    }

    @Test
    public void given_RunningJob_When_CancelJob_Then_JobIsCancels(){

        DataSyncUtils.initialize(mContext, 1, "131");

        Assert.assertEquals(FirebaseJobDispatcher.CANCEL_RESULT_SUCCESS, DataSyncUtils.cancelScheduledJob());

    }

}
