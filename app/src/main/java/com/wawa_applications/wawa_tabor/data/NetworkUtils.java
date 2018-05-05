package com.wawa_applications.wawa_tabor.data;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

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

    private static final String RESOURCE_ID = "";
    private static final String API_KEY = "";

    public static URL getURL(int lineType, String lineNumber){
        if (lineType == 1 || lineType == 2){

            if(lineNumber != null) return getAllTransportByLineURL(lineType, lineNumber);
            return null;
        }

        return null;

    }

    public static String getRespondFromHttp(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }

    private static URL getAllTransportByLineURL(int lineType, String lineNumber){
        Uri busesQueryUri = Uri.parse(API_WEBSITE_URL).buildUpon()
                .appendQueryParameter(RESOURCE_ID_PARAM, RESOURCE_ID)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .appendQueryParameter(TYPE_PARAM, Integer.toString(lineType))
                .appendQueryParameter(LINE_PARAM, lineNumber)
                .build();

        try {
            URL requestURL = new URL(busesQueryUri.toString());
            return requestURL;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

    }

    private static URL getAllBusesURL(){
        Uri busesQueryUri = Uri.parse(API_WEBSITE_URL).buildUpon()
                .appendQueryParameter(RESOURCE_ID_PARAM, RESOURCE_ID)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .appendQueryParameter(TYPE_PARAM, Integer.toString(1))
                .build();

        try {
            URL busesURL = new URL(busesQueryUri.toString());
            return busesURL;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static URL getAllTramsURL() {
        Uri tramsQueryUri = Uri.parse(API_WEBSITE_URL).buildUpon()
                .appendQueryParameter(RESOURCE_ID_PARAM, RESOURCE_ID)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .appendQueryParameter(TYPE_PARAM, Integer.toString(2))
                .build();

        try {
            URL tramsURL = new URL(tramsQueryUri.toString());
            return tramsURL;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
