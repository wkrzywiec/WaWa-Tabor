package com.wawa_applications.wawa_tabor.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.wawa_applications.wawa_tabor.network.retrofit.ZTMAPIService;
import com.wawa_applications.wawa_tabor.network.retrofit.model.ZTMAPILine;
import com.wawa_applications.wawa_tabor.network.retrofit.model.ZTMAPIResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class LinesViewModel extends ViewModel {

    private MutableLiveData<String> lineNo;
    private MutableLiveData<List<ZTMAPILine>> transportList;
    private ZTMAPIService ztmService;
    CompositeDisposable compositeDisposable;

    public LinesViewModel() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.giantbomb.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        ztmService = retrofit.create(ZTMAPIService.class);

        compositeDisposable = new CompositeDisposable();
    }

    public MutableLiveData<String> getLineNo() {

        if (lineNo == null){
            lineNo = new MutableLiveData<String>();
        }
        return lineNo;
    }

    public LiveData<List<ZTMAPILine>> getTransportList(){

        if (transportList == null){
            createMutableLiveData();
        }
        return transportList;
    }

    public void subscribeBus(String line) {

        Disposable disposable = Observable.interval(15, TimeUnit.SECONDS)
                .flatMap(n -> ztmService.getBuses(line))
                .subscribe(ztmapiResult -> handleResult(ztmapiResult));

        compositeDisposable.add(disposable);
    }

    public void unSubscribeBus() {
        compositeDisposable.dispose();
    }

    private void handleResult(ZTMAPIResult ztmapiResult){
        if (transportList == null){
            createMutableLiveData();
        }
        transportList.setValue(ztmapiResult.getLinesList());
    }

    private void createMutableLiveData() {
        transportList = new MutableLiveData<>();
        transportList.setValue(new ArrayList<ZTMAPILine>());
    }
}
