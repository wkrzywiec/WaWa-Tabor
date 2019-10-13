package com.wawa_applications.wawa_tabor.network.retrofit;

import com.wawa_applications.wawa_tabor.model.ApiResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ZTMAPIService {

    String URL = "https://api.um.warszawa.pl";

    @GET("api/action/busestrams_get/?type=1&resource_id=f2e5503e-927d-4ad3-9500-4ab9e55deb59&apikey=89abea05-01e5-4726-8cfb-2fcc5e31c364")
    Observable<ApiResult> getBuses(@Query("line") String line);

    @GET("api/action/busestrams_get/?type=2&resource_id=f2e5503e-927d-4ad3-9500-4ab9e55deb59&apikey=89abea05-01e5-4726-8cfb-2fcc5e31c364")
    Observable<ApiResult> getTrams(@Query("line") String line);
}
