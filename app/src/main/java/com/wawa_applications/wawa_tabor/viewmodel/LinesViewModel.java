package com.wawa_applications.wawa_tabor.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.wawa_applications.wawa_tabor.model.ApiResult;
import com.wawa_applications.wawa_tabor.model.Line;
import com.wawa_applications.wawa_tabor.repository.ZtmApiRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class LinesViewModel extends ViewModel {

    private MutableLiveData<String> lineNoLiveData;
    private MutableLiveData<List<Line>> lineListLiveData;
    private CompositeDisposable compositeDisposable;
    private ZtmApiRepository repository;

    public LinesViewModel() {
        compositeDisposable = new CompositeDisposable();
        repository = new ZtmApiRepository();
    }

    public MutableLiveData<String> getLineNoLiveData() {
        checkIfLineNoIsInitiated();
        return lineNoLiveData;
    }

    public LiveData<List<Line>> getLineListLiveData(){
        checkIfLineInfoListIsInitiated();
        return lineListLiveData;
    }

    public void subscribeBus(String line, int lineType) {

        setLineNoLiveData(line);

        Disposable disposable = Observable.interval(15, TimeUnit.SECONDS)
                .flatMap(n -> getLines(line, lineType))
                .doOnError(error -> Log.d("Error in class " + this.getClass().getName(), error.getMessage()))
                .subscribe(this::handleResult);

        compositeDisposable.add(disposable);
    }

    public void unSubscribeBus() {
        compositeDisposable.dispose();
    }

    private Observable<ApiResult> getLines(String line, int lineType) {
        if (lineType == 1){
            return repository.getBuses(line);
        } else {
            return repository.getTrams(line);
        }
    }

    private void handleResult(ApiResult apiResult){
        checkIfLineInfoListIsInitiated();
        lineListLiveData.postValue(apiResult.getLinesList());
    }

    private void setLineNoLiveData(String line) {
        checkIfLineNoIsInitiated();
        lineNoLiveData.setValue(line);
    }

    private void checkIfLineNoIsInitiated() {
        if (lineNoLiveData == null) {
            lineNoLiveData = new MutableLiveData<>();
        }
    }

    private void checkIfLineInfoListIsInitiated() {
        if (lineListLiveData == null) {
            lineListLiveData = new MutableLiveData<>();
            lineListLiveData.setValue(new ArrayList<>());
        }
    }
}
