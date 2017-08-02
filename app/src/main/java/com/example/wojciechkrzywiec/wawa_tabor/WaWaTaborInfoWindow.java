package com.example.wojciechkrzywiec.wawa_tabor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Wojtek Krzywiec on 02/08/2017.
 */

public class WaWaTaborInfoWindow implements GoogleMap.InfoWindowAdapter {

    Context mContext;

    public WaWaTaborInfoWindow(Context context){
        mContext = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View v = inflater.inflate(R.layout.info_window, null);

        TextView lineNumber = (TextView) v.findViewById(R.id.lineNumberText);
        TextView brigadeNumber = (TextView) v.findViewById(R.id.brigadeNumberText);
        TextView timeStamp = (TextView) v.findViewById(R.id.timeText);

        String title = marker.getTitle();

        lineNumber.setText(title);

        String snippet = marker.getSnippet();

        String[] detailInfo = snippet.split(",");

        brigadeNumber.setText("Brygada: " + detailInfo[0]);
        timeStamp.setText(detailInfo[1]);
        return v;
    }
}
