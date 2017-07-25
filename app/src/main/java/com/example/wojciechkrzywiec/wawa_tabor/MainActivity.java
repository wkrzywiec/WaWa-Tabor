package com.example.wojciechkrzywiec.wawa_tabor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    private Button mBusesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBusesButton = (Button) findViewById(R.id.buses_button);
    }


    public void openBusesActivity(View v){
        Intent busesActivityIntent = new Intent (MainActivity.this, BusesActivity.class);
        startActivity(busesActivityIntent);
    }
}
