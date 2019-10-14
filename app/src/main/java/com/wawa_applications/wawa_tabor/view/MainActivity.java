package com.wawa_applications.wawa_tabor.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wawa_applications.wawa_tabor.R;

public class MainActivity extends AppCompatActivity {


    private Button mBusesButton;
    private int busId = 1;
    private int tramId = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBusesButton = (Button) findViewById(R.id.buses_button);
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
