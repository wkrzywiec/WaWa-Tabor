package com.wawa_applications.wawa_tabor.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wawa_applications.wawa_tabor.R;
import com.wawa_applications.wawa_tabor.viewmodel.LinesViewModel;

import org.osmdroid.config.Configuration;
import org.osmdroid.views.MapView;

public class LinesActivity extends AppCompatActivity {

    private MapView mapView;
    private MapHelper mapHelper;
    private Drawable lineMarkerIcon;

    private int lineType;
    private String mDisplayedLine;
    private EditText mLineTextView;
    private LinesViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context mContext = getApplicationContext();
        Configuration.getInstance().load(mContext, PreferenceManager.getDefaultSharedPreferences(mContext));

        chooseLineActivityLayout();

        mapView = findViewById(R.id.map);
        mapHelper = new MapHelper(mapView, mContext);
        mapHelper.onCreatePrepareMap();

        mLineTextView = findViewById(R.id.edit_query);
        mLineTextView.setOnEditorActionListener(
                (textView, actionID, keyEvent) ->
                        changeDisplayedLineTextViewOnAction(textView, actionID));

        viewModel = ViewModelProviders.of(this).get(LinesViewModel.class);
        viewModel.getLineListLiveData().observe(this, list -> {
           mapView.getOverlays().clear();
           mapHelper.addLineMarkersOntoMap(list, lineMarkerIcon);
        });

        viewModel.getLineNoLiveData().observe(this, lineNo ->
           mDisplayedLine = lineNo
        );
    }

    @Override
    public void onResume(){
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        viewModel.unSubscribeBus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.unSubscribeBus();
    }

    public void setDisplayedLine(View view){
        mDisplayedLine = mLineTextView.getText().toString();
        mDisplayedLine.toUpperCase();
        viewModel.subscribeBus(mDisplayedLine, lineType);

        Toast toast = Toast.makeText(this, "Pobieranie danych dla lini: " + mDisplayedLine, Toast.LENGTH_LONG);
        toast.show();
    }

    private void chooseLineActivityLayout() {
        lineType = getIntent().getIntExtra(getString(R.string.line_type), 1);
        if (lineType == 1) {
            setContentView(R.layout.activity_buses);
            lineMarkerIcon = this.getResources().getDrawable(R.drawable.ic_bus);
        } else {
            setContentView(R.layout.activity_trams);
            setTitle(R.string.title_activity_trams);
            lineMarkerIcon = this.getResources().getDrawable(R.drawable.ic_tram);
        }
    }

    private boolean changeDisplayedLineTextViewOnAction(TextView textView, int actionID) {
        if (actionID == EditorInfo.IME_ACTION_DONE) {
            setDisplayedLine(textView);
            return true;
        }
        return false;
    }
}

