package com.wawa_applications.wawa_tabor.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Wojtek Krzywiec on 27/07/2017.
 */

public class TransportContract {

    public static final String CONTENT_AUTHORITY = "com.wawa_applications.wawa_tabor";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_TRANSPORT = "transport";


    public static final class TransportEntry implements BaseColumns {

        public static final Uri TABLE_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_TRANSPORT)
                .build();

        public static final String TABLE_NAME = "transport";

        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_LINE = "line";
        public static final String COLUMN_BRIGADE = "brigade";
        public static final String COLUMN_LAT = "lat";
        public static final String COLUMN_LON = "lon";

    }
}
