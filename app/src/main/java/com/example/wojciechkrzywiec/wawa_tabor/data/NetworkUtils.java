package com.example.wojciechkrzywiec.wawa_tabor.data;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

import static android.content.ContentValues.TAG;

/**
 * Created by Wojtek Krzywiec on 25/07/2017.
 *
 * This class is used for two purposes:
 * 1. Create an URL which will be used for connecting with database
 * 2. Connect with database and retrieve data (buses/trains) position as a String in JSON format
 */

public class NetworkUtils {

    private static final String API_WEBSITE_URL = "https://api.um.warszawa.pl/api/action/busestrams_get/";
    private static final String RESOURCE_ID_PARAM = "resource_id";
    private static final String API_KEY_PARAM = "apikey";
    private static final String TYPE_PARAM = "type";
    private static final String LINE_PARAM = "line";

    private static final String RESOURCE_ID = "f2e5503e-927d-4ad3-9500-4ab9e55deb59";
    private static final String API_KEY = "89abea05-01e5-4726-8cfb-2fcc5e31c364";

    public static URL getURL(Context context){
        return getAllBusesURL();
    }

    public static String getRespondFromHttp(URL url) {
        return null;
    }

    private static URL getAllBusesURL(){
        Uri busesQueryUri = Uri.parse(API_WEBSITE_URL).buildUpon()
                .appendQueryParameter(RESOURCE_ID_PARAM, RESOURCE_ID)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .appendQueryParameter(TYPE_PARAM, Integer.toString(1))
                .build();

        try {
            URL busesURL = new URL(busesQueryUri.toString());
            Log.v(TAG, "Buses URL: " + busesURL);
            return busesURL;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

    }
}
