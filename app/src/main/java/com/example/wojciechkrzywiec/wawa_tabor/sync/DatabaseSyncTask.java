package com.example.wojciechkrzywiec.wawa_tabor.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.widget.Toast;

import com.example.wojciechkrzywiec.wawa_tabor.data.NetworkUtils;
import com.example.wojciechkrzywiec.wawa_tabor.data.OpenTransportJSONUtils;
import com.example.wojciechkrzywiec.wawa_tabor.data.TransportContract;

import java.net.URL;

/**
 * Created by Wojtek Krzywiec on 30/07/2017.
 */

public class DatabaseSyncTask {

    public static final int NO_DATA_ADDED = 0;
    synchronized public static int syncDatabase(Context context, int lineType, String lineNumber) {

        try {

            URL requestedUrl = NetworkUtils.getURL(lineType, lineNumber);

            String jsonResponse = NetworkUtils.getRespondFromHttp(requestedUrl);

            ContentValues[] transportValues = OpenTransportJSONUtils
                    .getTransportContentValuesFromJson(jsonResponse);

            if (transportValues != null && transportValues.length != 0) {

                ContentResolver contentResolver = context.getContentResolver();

                contentResolver.delete(
                        TransportContract.TransportEntry.TABLE_URI,
                        null,
                        null);


                contentResolver.bulkInsert(
                        TransportContract.TransportEntry.TABLE_URI,
                        transportValues);

                return transportValues.length;
            }

            return NO_DATA_ADDED;
        } catch (Exception e) {

            e.printStackTrace();
        }
        return NO_DATA_ADDED;
    }
}
