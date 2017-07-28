package com.example.wojciechkrzywiec.wawa_tabor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;

import com.example.wojciechkrzywiec.wawa_tabor.data.TransportContract;
import com.example.wojciechkrzywiec.wawa_tabor.data.TransportDbHelper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Wojtek Krzywiec on 28/07/2017.
 */

public class TransportProviderTest {

    private final Context mContext = InstrumentationRegistry.getTargetContext();

    /*
    @Before
    public void setUp(){

    }*/

    @Test
    public void given_OneRowsInDatabase_When_QueryAllRows_Then_ReceiveNotNullCursor(){

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
