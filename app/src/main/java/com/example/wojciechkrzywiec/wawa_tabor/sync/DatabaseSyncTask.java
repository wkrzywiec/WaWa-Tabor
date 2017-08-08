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

    synchronized public static void syncDatabase(Context context, int lineType) {

        try {

            URL requestedUrl = NetworkUtils.getURL(lineType, 0);

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

            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
