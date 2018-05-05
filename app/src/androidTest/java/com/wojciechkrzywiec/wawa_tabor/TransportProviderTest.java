package com.wojciechkrzywiec.wawa_tabor;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.wawa_applications.wawa_tabor.data.TransportContract;
import com.wawa_applications.wawa_tabor.data.TransportDbHelper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Wojtek Krzywiec on 28/07/2017.
 */
@RunWith(AndroidJUnit4.class)
public class TransportProviderTest {

    private final Context mContext = InstrumentationRegistry.getTargetContext();


    @Before
    public void before(){
        TransportDbHelper helper = new TransportDbHelper(InstrumentationRegistry.getTargetContext());
        SQLiteDatabase database = helper.getWritableDatabase();

        database.delete(
                TransportContract.TransportEntry.TABLE_NAME,
                null,
                null
        );

        database.close();
    }

    @Test
    public void given_OneRowInDatabase_When_QueryAllRows_Then_ReceiveNotNullCursor(){

        //Given
        TransportDbHelper dbHelper = new TransportDbHelper(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues[] mockedContentValues = mockTransportContentValues();

        long insertedRowId = database.insert(
                TransportContract.TransportEntry.TABLE_NAME,
                null,
                mockedContentValues[0]
        );

        Assert.assertTrue("Row wasn't inserted into database.",insertedRowId != -1);
        database.close();

        //When
        Cursor transportCursor = mContext.getContentResolver().query(
                TransportContract.TransportEntry.TABLE_URI,
                null,
                null,
                null,
                null
        );

        //Then
        Assert.assertNotNull("Cursor is null!",transportCursor);

        validateCursorValues(transportCursor, mockedContentValues[0], 1);

    }

    @Test
    public void given_EmptyDatabase_When_InsertMultipleRows_Then_CheckNumberOfRows(){

        //Given
        ContentResolver resolver = mContext.getContentResolver();

        ContentValues[] mockedContentValues = mockTransportContentValues();

        //When
        long numberOfRows = resolver.bulkInsert(
                TransportContract.TransportEntry.TABLE_URI,
                mockedContentValues
        );

        Assert.assertEquals("New data was not inserted!", mockedContentValues.length, numberOfRows);
    }


    private void validateCursorValues(Cursor receivedCursor, ContentValues expectedValues, int index) {

        receivedCursor.move(index);

        Assert.assertEquals(
                "Invalid date!",
                expectedValues.getAsString(TransportContract.TransportEntry.COLUMN_TIME),
                receivedCursor.getString(receivedCursor.getColumnIndex(TransportContract.TransportEntry.COLUMN_TIME))
        );

        Assert.assertEquals(
                "Invalid line number!",
                expectedValues.getAsString(TransportContract.TransportEntry.COLUMN_LINE),
                receivedCursor.getString(receivedCursor.getColumnIndex(TransportContract.TransportEntry.COLUMN_LINE))
        );

        Assert.assertEquals(
                "Invalid brigade number!",
                expectedValues.getAsString(TransportContract.TransportEntry.COLUMN_BRIGADE),
                receivedCursor.getString(receivedCursor.getColumnIndex(TransportContract.TransportEntry.COLUMN_BRIGADE))
        );

        Assert.assertEquals(
                "Invalid GPS latitude!",
                Double.valueOf(expectedValues.getAsDouble(TransportContract.TransportEntry.COLUMN_LAT)),
                Double.valueOf(receivedCursor.getDouble(receivedCursor.getColumnIndex(TransportContract.TransportEntry.COLUMN_LAT)))
        );

        Assert.assertEquals(
                "Invalid GPS longitude!",
                Double.valueOf(expectedValues.getAsDouble(TransportContract.TransportEntry.COLUMN_LON)),
                Double.valueOf(receivedCursor.getDouble(receivedCursor.getColumnIndex(TransportContract.TransportEntry.COLUMN_LON)))
        );
    }



    private ContentValues[] mockTransportContentValues() {

        ContentValues[] testTransportValues = {new ContentValues(), new ContentValues()};

        testTransportValues[0].put(TransportContract.TransportEntry.COLUMN_TIME, "2017-07-28 06:04:04");
        testTransportValues[0].put(TransportContract.TransportEntry.COLUMN_LINE, "107");
        testTransportValues[0].put(TransportContract.TransportEntry.COLUMN_BRIGADE, "5");
        testTransportValues[0].put(TransportContract.TransportEntry.COLUMN_LAT, 52.207536);
        testTransportValues[0].put(TransportContract.TransportEntry.COLUMN_LON, 21.048609);

        testTransportValues[1].put(TransportContract.TransportEntry.COLUMN_TIME, "2017-07-28 06:04:03");
        testTransportValues[1].put(TransportContract.TransportEntry.COLUMN_LINE, "194");
        testTransportValues[1].put(TransportContract.TransportEntry.COLUMN_BRIGADE, "4");
        testTransportValues[1].put(TransportContract.TransportEntry.COLUMN_LAT, 52.225013);
        testTransportValues[1].put(TransportContract.TransportEntry.COLUMN_LON, 20.930482);

        return testTransportValues;
    }
}
