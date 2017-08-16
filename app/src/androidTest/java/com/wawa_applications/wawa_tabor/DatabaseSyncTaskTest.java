package com.wawa_applications.wawa_tabor;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.wawa_applications.wawa_tabor.sync.DatabaseSyncTask;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Wojtek Krzywiec on 12/08/2017.
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseSyncTaskTest {

    Context mContext = InstrumentationRegistry.getTargetContext();

    @Test
    public void given_RequestToSyncDatabase_When_StartDownloadingAndAddingToDataToDatabase_Then_DatabaseUpdated(){

        int count = DatabaseSyncTask.syncDatabase(mContext, 1, "131");

        Assert.assertNotEquals("New buses was not added to database!", DatabaseSyncTask.NO_DATA_ADDED, count);
    }
}
