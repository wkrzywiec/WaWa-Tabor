package com.example.wojciechkrzywiec.wawa_tabor.data;

import android.content.ContentValues;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Wojtek Krzywiec on 27/07/2017.
 */

public class OpenTransportJSONUtils {

    private static String ARRAY_NAME = "result";

    private static String LAT_PARAM = "Lat";
    private static String LINES_PARAM = "Lines";
    private static String BRIGADE_PARAM = "Brigade";
    private static String LON_PARAM = "Lon";
    private static String TIME_PARAM = "Time";

    public static ContentValues[] getTransportContentValuesFromJson (String jsonString)
            throws JSONException{

        JSONObject transportJson = new JSONObject(jsonString);

        JSONArray transportArrayJson = transportJson.getJSONArray(ARRAY_NAME);

        ContentValues[] transportContentValues = new ContentValues[transportArrayJson.length()];

        for (int i = 0; i < transportArrayJson.length(); i++) {

            double lat;
            String lines;
            String brigade;
            double lon;
            String time;

            JSONObject singleTransport = transportArrayJson.getJSONObject(i);

            lat = singleTransport.getDouble(LAT_PARAM);
            lines = singleTransport.getString(LINES_PARAM);
            brigade = singleTransport.getString(BRIGADE_PARAM);
            lon = singleTransport.getDouble(LON_PARAM);
            time = singleTransport.getString(TIME_PARAM);

            ContentValues transportValues = new ContentValues();
            transportValues.put(TransportContract.TransportEntry.COLUMN_TIME, time);
            transportValues.put(TransportContract.TransportEntry.COLUMN_LINE, lines);
            transportValues.put(TransportContract.TransportEntry.COLUMN_BRIGADE, brigade);
            transportValues.put(TransportContract.TransportEntry.COLUMN_LAT, lat);
            transportValues.put(TransportContract.TransportEntry.COLUMN_LON, lon);

            transportContentValues[i] = transportValues;
        }

        return transportContentValues;

    }
}
