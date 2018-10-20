package com.wawa_applications.wawa_tabor.model.retrofit;

import com.wawa_applications.wawa_tabor.model.retrofit.model.WaWaAPIResult;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static junit.framework.Assert.assertTrue;

@RunWith(JUnit4.class)
public class WaWaAPIRetrofitIntegrationTest {

    private WaWaAPIService service;
    private final String API_KEY = "YOUR API KEY";
    private final String RESOURCE_ID = "f2e5503e-927d-4ad3-9500-4ab9e55deb59";

    @Before
    public void setUp() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.um.warszawa.pl")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(provideOkHttpClient())
                .build();
        service = retrofit.create(WaWaAPIService.class);
    }

    @Test
    public void givenCorrectBusNo_whenCallAPI_thenReceiveResponse(){

        Observable<WaWaAPIResult> resultObservable = service.getBuses(API_KEY, RESOURCE_ID, "131");

        WaWaAPIResult result = resultObservable.blockingFirst();
        assertTrue(result.getLinesList().size() > 0);
    }

    @Test
    public void givenInCorrectBusNo_whenCallAPI_thenReceiveEmptyResponse(){

        Observable<WaWaAPIResult> resultObservable = service.getBuses(API_KEY, RESOURCE_ID, "ABC");

        WaWaAPIResult result = resultObservable.blockingFirst();
        assertTrue(result.getLinesList().size() == 0);
    }

    @Test
    public void givenCorrectTramNo_whenCallAPI_thenReceiveResponse(){

        Observable<WaWaAPIResult> resultObservable = service.getTrams(API_KEY, RESOURCE_ID, "18");

        WaWaAPIResult result = resultObservable.blockingFirst();
        assertTrue(result.getLinesList().size() > 0);
    }

    @Test
    public void givenInCorrectTramNo_whenCallAPI_thenReceiveEmptyResponse(){

        Observable<WaWaAPIResult> resultObservable = service.getTrams(API_KEY, RESOURCE_ID, "ABC");

        WaWaAPIResult result = resultObservable.blockingFirst();
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
