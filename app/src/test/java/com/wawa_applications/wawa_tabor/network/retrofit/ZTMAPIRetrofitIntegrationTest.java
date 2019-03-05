package com.wawa_applications.wawa_tabor.network.retrofit;

import com.wawa_applications.wawa_tabor.network.retrofit.model.ZTMAPIResult;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static junit.framework.Assert.assertTrue;

@RunWith(JUnit4.class)
public class ZTMAPIRetrofitIntegrationTest {

    private ZTMAPIService service;

    @Before
    public void setUp() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ZTMAPIService.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(provideOkHttpClient())
                .build();
        service = retrofit.create(ZTMAPIService.class);
    }

    @Test
    public void givenCorrectBusNo_whenCallAPI_thenReceiveResponse() throws IOException{

        Observable<ZTMAPIResult> resultObservable = service.getBuses("131");

        ZTMAPIResult result = resultObservable.blockingFirst();
        assertTrue(result.getLinesList().size() > 0);
    }

    @Test
    public void givenInCorrectBusNo_whenCallAPI_thenReceiveEmptyResponse() throws IOException{

        Observable<ZTMAPIResult> resultObservable = service.getBuses("ABC");

        ZTMAPIResult result = resultObservable.blockingFirst();
        assertTrue(result.getLinesList().size() == 0);
    }

    @Test
    public void givenCorrectTramNo_whenCallAPI_thenReceiveResponse() throws IOException{

        Observable<ZTMAPIResult> resultObservable = service.getTrams("18");

        ZTMAPIResult result = resultObservable.blockingFirst();
        assertTrue(result.getLinesList().size() > 0);
    }

    @Test
    public void givenInCorrectTramNo_whenCallAPI_thenReceiveEmptyResponse() throws IOException{

        Observable<ZTMAPIResult> resultObservable = service.getTrams( "ABC");

        ZTMAPIResult result = resultObservable.blockingFirst();
        assertTrue(result.getLinesList().size() == 0);
    }

    private OkHttpClient provideOkHttpClient() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient().newBuilder()
                .connectTimeout(500, TimeUnit.MILLISECONDS)
                .readTimeout(500,TimeUnit.MILLISECONDS)
                .addInterceptor(logging)
                .build();
    }
}
