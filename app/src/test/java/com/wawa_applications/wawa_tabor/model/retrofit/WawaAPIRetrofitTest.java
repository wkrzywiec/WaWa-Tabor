package com.wawa_applications.wawa_tabor.model.retrofit;

import com.google.gson.Gson;
import com.wawa_applications.wawa_tabor.model.retrofit.model.WaWaAPILine;
import com.wawa_applications.wawa_tabor.model.retrofit.model.WaWaAPIResult;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;

import io.reactivex.Observable;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static junit.framework.Assert.assertEquals;

@RunWith(JUnit4.class)
public class WawaAPIRetrofitTest {

    private final MockWebServer server = new MockWebServer();
    private Gson gson;
    private WaWaAPIService service;

    private final String apiKey = "key";
    private final String resourceId = "resourceId";

    @Before
    public void setUp(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(server.url("/"))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        service = retrofit.create(WaWaAPIService.class);
        gson = new Gson();
    }

    @Test
    public void givenCorrectBusNo_whenCallRetrofitMethod_ThenGetCorrectResponse(){
        //given
        WaWaAPILine exampleLine =   new WaWaAPILine(52.20676, 21.037996, "2018-10-17 05:52:50:", "131", "11");
        WaWaAPIResult exampleResult = new WaWaAPIResult();
        exampleResult.addLine(exampleLine);
        server.enqueue(new MockResponse().setBody(gson.toJson(exampleResult)));
        //when
        Observable<WaWaAPIResult> busObservable = service.getBuses(apiKey, resourceId,"131");
        WaWaAPIResult result =  busObservable.blockingFirst();
        //then
        assertEquals( exampleResult,result);
    }

    @Test
    public void givenInCorrectBusNo_whenCallRetrofit_ThenGetEmptyResponse(){
        //given
        String errorResponse = "{ \"result\": [] }";
        server.enqueue(new MockResponse().setBody(errorResponse));
        //when
        Observable<WaWaAPIResult> busObservable = service.getBuses(apiKey, resourceId,"ABC");
        WaWaAPIResult result =  busObservable.blockingFirst();
        //then
        assertEquals(new ArrayList<WaWaAPILine>(),result.getLinesList());
    }

    @Test
    public void givenCorrectTramNo_whenCallRetrofit_ThenGetOkResponse(){
        //given
        WaWaAPILine exampleLine =   new WaWaAPILine(52.20676, 21.037996, "2018-10-17 05:52:50:", "11", "12");
        WaWaAPIResult exampleResult = new WaWaAPIResult();
        exampleResult.addLine(exampleLine);
        server.enqueue(new MockResponse().setBody(gson.toJson(exampleResult)));
        //when
        Observable<WaWaAPIResult> tramObservable = service.getTrams(apiKey, resourceId, "11");
        WaWaAPIResult result =  tramObservable.blockingFirst();
        //then
        assertEquals( exampleResult,result);
    }

    @Test
    public void givenInCorrectTramNo_whenCallRetrofit_ThenGetEmptyResponse(){
        //given
        String errorResponse = "{ \"result\": [] }";
        server.enqueue(new MockResponse().setBody(errorResponse));
        //when
        Observable<WaWaAPIResult> busObservable = service.getTrams(apiKey, resourceId,"ABC");
        WaWaAPIResult result =  busObservable.blockingFirst();
        //then
        assertEquals(new ArrayList<WaWaAPILine>(),result.getLinesList());
    }
}
