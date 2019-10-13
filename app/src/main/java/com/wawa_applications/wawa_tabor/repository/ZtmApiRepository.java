package com.wawa_applications.wawa_tabor.repository;

import com.wawa_applications.wawa_tabor.model.ApiResult;
import com.wawa_applications.wawa_tabor.network.retrofit.ZtmApiRetrofitService;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ZtmApiRepository {

    private ZtmApiRetrofitService ztmService;

    public ZtmApiRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ZtmApiRetrofitService.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        ztmService = retrofit.create(ZtmApiRetrofitService.class);
    }

    public Observable<ApiResult> getBuses(String lineNo) {
        return ztmService.getBuses(lineNo);
    }

    public Observable<ApiResult> getTrams(String lineNo) {
        return ztmService.getTrams(lineNo);
    }
}
