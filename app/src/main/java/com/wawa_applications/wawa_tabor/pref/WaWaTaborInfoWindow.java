package com.wawa_applications.wawa_tabor.pref;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.wawa_applications.wawa_tabor.R;

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

        TextView lineNumberTextView = (TextView) v.findViewById(R.id.lineNumberText);
        TextView brigadeNumberTextView = (TextView) v.findViewById(R.id.brigadeNumberText);
        TextView timeStampTextView = (TextView) v.findViewById(R.id.timeText);

        String title = marker.getTitle();

        lineNumberTextView.setText(title);

        if (checkHurriesTransport(title))
            lineNumberTextView.setTextColor(ContextCompat.getColor(mContext, R.color.hurry_transport_color));


        String snippet = marker.getSnippet();

        String[] detailInfo = snippet.split(",");

        brigadeNumberTextView.setText("Brygada: " + detailInfo[0]);
        timeStampTextView.setText(detailInfo[1]);
        return v;
    }

    private boolean checkHurriesTransport(String lineString){

        try{
            int lineInteger = Integer.parseInt(lineString);

            if (lineInteger >= 400 && lineInteger <= 699) {
                return true;
            }

            return false;

        } catch (NumberFormatException e) {
            return false;
        }

    }
}
