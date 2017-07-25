package com.example.wojciechkrzywiec.wawa_tabor;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.wojciechkrzywiec.wawa_tabor.data.NetworkUtils;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Wojtek Krzywiec on 25/07/2017.
 */
@RunWith(AndroidJUnit4.class)
public class NetworkUtilsTest {

    private Context appContext = InstrumentationRegistry.getTargetContext();

    @Test
    public void given_NetworkUtils_When_FetchAllBussesUrl_ThenUrlCreated(){
        String expectedUrlString ="https://api.um.warszawa.pl/api/action/busestrams_get/?resource_id=f2e5503e-927d-4ad3-9500-4ab9e55deb59&apikey=89abea05-01e5-4726-8cfb-2fcc5e31c364&type=1";
        String actualUrlString = NetworkUtils.getURL(appContext).toString();

        Assert.assertEquals(expectedUrlString, actualUrlString);
    }
}
