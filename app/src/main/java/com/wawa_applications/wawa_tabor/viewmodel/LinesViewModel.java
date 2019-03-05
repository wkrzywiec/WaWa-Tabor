package com.wawa_applications.wawa_tabor.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.wawa_applications.wawa_tabor.network.repository.LinesCoordinatesRepository;
import com.wawa_applications.wawa_tabor.network.repository.LinesRepository;
import com.wawa_applications.wawa_tabor.network.retrofit.model.ZTMAPILine;
import com.wawa_applications.wawa_tabor.network.retrofit.model.ZTMAPIResult;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LinesViewModel extends ViewModel {

    private MutableLiveData<String> lineNo;
    private MutableLiveData<List<ZTMAPILine>> transportList;

    private LinesRepository repository;
    private Scheduler scheduler;

    public LinesViewModel() {
        repository = new LinesCoordinatesRepository();
    }

    public MutableLiveData<String> getLineNo() {

        if (lineNo == null){
            lineNo = new MutableLiveData<String>();
        }
        return lineNo;
    }

    public LiveData<List<ZTMAPILine>> getTransportList(){

        if (transportList == null){
            transportList = new MutableLiveData<List<ZTMAPILine>>();
        }
        return transportList;
    }

    public void subscribeBus(String line) {

        scheduler = Schedulers.from(Executors.newSingleThreadExecutor());

        Observable.interval(15, TimeUnit.SECONDS)
                .flatMap(n -> repository.getBuses(line)
                        .retry(10)
                        .subscribeOn(scheduler))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ztmapiResult -> handleResult(ztmapiResult));
    }

    private void handleResult(ZTMAPIResult ztmapiResult){
        transportList.setValue(ztmapiResult.getLinesList());
    }
}
