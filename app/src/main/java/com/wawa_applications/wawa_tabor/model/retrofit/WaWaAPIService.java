package com.wawa_applications.wawa_tabor.model.retrofit;

import com.wawa_applications.wawa_tabor.model.retrofit.model.WaWaAPIResult;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WaWaAPIService {

    @GET("api/action/busestrams_get/?type=1")
    Observable<WaWaAPIResult> getBuses(@Query("apikey") String apiKey,
                                                 @Query("resource_id") String resourceId,
                                                 @Query("line") String line);

    @GET("api/action/busestrams_get/?type=2")
    Observable<WaWaAPIResult> getTrams(@Query("apikey") String apiKey,
                                                 @Query("resource_id") String resourceId,
                                                 @Query("line") String line);
}
