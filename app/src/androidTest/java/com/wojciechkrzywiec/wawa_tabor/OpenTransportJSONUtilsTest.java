package com.wojciechkrzywiec.wawa_tabor;

import android.content.ContentValues;
import android.support.test.runner.AndroidJUnit4;

import com.wawa_applications.wawa_tabor.data.NetworkUtils;
import com.wawa_applications.wawa_tabor.data.OpenTransportJSONUtils;

import junit.framework.Assert;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Wojtek Krzywiec on 31/07/2017.
 */
@RunWith(AndroidJUnit4.class)
public class OpenTransportJSONUtilsTest {


    @Test
    public void given_HttpStringRespond_When_PrepareContentValuesArray_Then_CheckNumber(){

        //Given
        URL httpUrl = NetworkUtils.getURL(1, "131");
        int count = 0;
        ContentValues[] expectedValues;
        try {
            String httpRespond = NetworkUtils.getRespondFromHttp(httpUrl);

            Pattern pattern = Pattern.compile("Time");
            Matcher matcher = pattern.matcher(httpRespond);
            while (matcher.find()) {
                count++;
            }

            //When
            expectedValues = OpenTransportJSONUtils.getTransportContentValuesFromJson(httpRespond);

            //Then
            Assert.assertEquals("Number of buses are from Http respond and in ContentValues are not matching",
                    count, expectedValues.length);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e){
            e.printStackTrace();
        }




    }

}
