package com.wawa_applications.wawa_tabor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.wawa_applications.wawa_tabor.data.TransportDbHelper;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Wojtek Krzywiec on 28/07/2017.
 */
@RunWith(AndroidJUnit4.class)
public class TransportDbHelperTest {

    Context mContext = InstrumentationRegistry.getTargetContext();

   @Test
   public void given_Database_When_DropDatabase_Then_DatabaseHasBeenDeleted(){
       Assert.assertTrue(mContext.deleteDatabase(TransportDbHelper.DATABASE_NAME));
   }

   @Test
   public void given_NoDatabase_When_CreateNewDatabase_Then_NewDatabaseWasCreated(){
       TransportDbHelper dbHelper = new TransportDbHelper(mContext);
       SQLiteDatabase database = dbHelper.getWritableDatabase();
       Assert.assertTrue(database.isOpen());
       database.close();
   }
}
