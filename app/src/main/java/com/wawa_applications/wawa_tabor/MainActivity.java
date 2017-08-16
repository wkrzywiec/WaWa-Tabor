package com.wawa_applications.wawa_tabor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.wawa_applications.wawa_tabor.R;

/**
 *
 * This application is using data provided by Miasto Stołeczne Warszawa (Cityo of Warsaw),
 * that are obtained from the website http://api.um.warszawa.pl
 *
 * The first release of these data to the public was on 31st March 2017.
 *
*/
public class MainActivity extends AppCompatActivity {


    private int busId = 1;
    private int tramId = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public void openBusesActivity(View v){
        Intent linesActivityIntent = new Intent (MainActivity.this, LinesActivity.class);
        linesActivityIntent.putExtra(getString(R.string.line_type), busId);
        startActivity(linesActivityIntent);
    }

    public void openTramsActivity(View v){
        Intent linesActivityIntent = new Intent (MainActivity.this, LinesActivity.class);
        linesActivityIntent.putExtra(getString(R.string.line_type), tramId);
        startActivity(linesActivityIntent);
    }
}
