package com.wawa_applications.wawa_tabor.network;

import com.wawa_applications.wawa_tabor.model.ApiResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ZtmApiClient {

    String URL = "https://api.um.warszawa.pl";

    @GET("api/action/busestrams_get/?type=1&resource_id=f2e5503e-927d-4ad3-9500-4ab9e55deb59")
    Observable<ApiResult> getBuses(
            @Query("apikey") String apiKey,
            @Query("line") String line);

    @GET("api/action/busestrams_get/?type=2&resource_id=f2e5503e-927d-4ad3-9500-4ab9e55deb59")
    Observable<ApiResult> getTrams(
            @Query("apikey") String apiKey,
            @Query("line") String line);
}
