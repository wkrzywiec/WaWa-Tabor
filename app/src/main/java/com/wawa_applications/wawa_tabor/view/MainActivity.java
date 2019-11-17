package com.wawa_applications.wawa_tabor.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputLayout;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import com.wawa_applications.wawa_tabor.R;
import com.wawa_applications.wawa_tabor.model.LineType;
import com.wawa_applications.wawa_tabor.viewmodel.LinesViewModel;
import com.wawa_applications.wawa_tabor.viewmodel.LinesViewModelFactory;

import org.osmdroid.config.Configuration;
import org.osmdroid.views.MapView;

public class MainActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_main);

        initMap();

        mLineTextView = findViewById(R.id.edit_query);
        TextInputLayout layout = findViewById(R.id.input_layout);

        setActionListenerForTextView(layout);

        viewModel = ViewModelProviders.of(
                this,
                new LinesViewModelFactory(this.getString(R.string.ztm_api_key)))
                .get(LinesViewModel.class);

        viewModel.getLineListLiveData().observe(this, list -> {
            mapView.getOverlays().clear();
            mapHelper.addLineMarkersOntoMap(list, lineMarkerIcon);
        });

        viewModel.getLineNoLiveData().observe(this, lineNo ->
                mDisplayedLine = lineNo
        );

        viewModel.getIsResult().observe(this, this::handleIsResult);
    }

    public void setDisplayedLine(View view){
        closeKeyboard();
        mDisplayedLine = mLineTextView.getText().toString();
        lineType = viewModel.indicateLineType(mDisplayedLine);
        setLineTypeIcon();
        viewModel.subscribeToLine(mDisplayedLine, lineType);

        Toast toast = DynamicToast.make(this,
                "Wyszukiwanie pojazdów lini: " + mDisplayedLine.toUpperCase(),
                Toast.LENGTH_LONG);

        toast.show();
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
        viewModel.unSubscribeLine();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.unSubscribeLine();
    }

    private void initMap() {
        Context mContext = getApplicationContext();
        Configuration.getInstance().load(mContext, PreferenceManager.getDefaultSharedPreferences(mContext));

        mapView = findViewById(R.id.map);
        mapHelper = new MapHelper(mapView, mContext);
        mapHelper.onCreatePrepareMap();
    }

    private void setActionListenerForTextView(TextInputLayout layout) {
        layout.setEndIconOnClickListener((textView) -> {
            setDisplayedLine(textView);
        });

        mLineTextView.setOnEditorActionListener(
                (textView, actionID, keyEvent) ->
                        changeDisplayedLineTextViewOnAction(textView, actionID));
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void setLineTypeIcon() {
        if (lineType == LineType.BUS.getValue()) {
            lineMarkerIcon = this.getResources().getDrawable(R.drawable.ic_bus);
        } else {
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

    private void handleIsResult(Boolean isResult) {
        if (!isResult){
            Toast toast = DynamicToast.makeWarning(this,
                    "Brak wyszukań dla linii: " + mDisplayedLine.toUpperCase(),
                    Toast.LENGTH_LONG);

            toast.show();
        }
    }
}
