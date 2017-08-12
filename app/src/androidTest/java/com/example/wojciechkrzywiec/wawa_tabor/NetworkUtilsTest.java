package com.example.wojciechkrzywiec.wawa_tabor;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.wojciechkrzywiec.wawa_tabor.data.NetworkUtils;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URL;

/**
 * Created by Wojtek Krzywiec on 25/07/2017.
 */
@RunWith(AndroidJUnit4.class)
public class NetworkUtilsTest {

    @Test
    public void given_NetworkUtils_When_FetchAllBussesOfOneLineUrl_Then_UrlCreated(){
        String expectedUrlString ="https://api.um.warszawa.pl/api/action/busestrams_get/?resource_id=f2e5503e-927d-4ad3-9500-4ab9e55deb59&apikey=89abea05-01e5-4726-8cfb-2fcc5e31c364&type=1&line=131";
        String actualUrlString = NetworkUtils.getURL(1, "131").toString();

        Assert.assertEquals(expectedUrlString, actualUrlString);
    }

    @Test
    public void given_NetworkUtils_When_FetchAllTramsOfOneLineUrl_Then_UrlCreated(){

        String expectedUrlString = "https://api.um.warszawa.pl/api/action/busestrams_get/?resource_id=f2e5503e-927d-4ad3-9500-4ab9e55deb59&apikey=89abea05-01e5-4726-8cfb-2fcc5e31c364&type=2&line=33";
        String actualUrlString = NetworkUtils.getURL(2, "33").toString();

        Assert.assertEquals(expectedUrlString, actualUrlString);
    }

    @Test
    public void given_NetworkUtils_When_FetchOtherLineTypeUrl_Then_ReceiveNullUrl(){
        URL actualUrlString = NetworkUtils.getURL(3, "180");

        Assert.assertNull(actualUrlString);
    }

    @Test
    public void given_NetworkUtils_When_FetchNullLine_Then_ReceiveNullUrl(){
        URL actualUrlString = NetworkUtils.getURL(2, null);

        Assert.assertNull(actualUrlString);
    }

}
