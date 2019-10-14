package com.wawa_applications.wawa_tabor.network;



import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;

import io.reactivex.Observable;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class LinesCoordinatesRepositoryIntegrationTest {

//    LinesRepository repository;
//
//    @Before
//    public void setUp() {
//        repository = new LinesCoordinatesRepository();
//    }
//
//    @Test
//    public void givenCorrectBusNo_whenCallForBus_thenReceiveResponse() throws IOException {
//
//        Observable<ApiResult> ztmapiResultObservablebuses = repository.getBuses("131");
//
//        assertNotNull(ztmapiResultObservablebuses);
//        assertTrue(ztmapiResultObservablebuses.blockingFirst()
//                    .getLinesList().size() > 0);
//    }
//
//    @Test
//    public void givenInCorrectBusNo_whenCallForBus_thenGetNoLines(){
//        //given & when & then
//        Observable<ApiResult> busesObservable = repository.getBuses("Ac2");
//        assertTrue(busesObservable.blockingFirst().getLinesList().size() == 0);
//    }
//
//    @Test
//    public void givenCorrectTramNo_whenCallForTram_thenReceiveResponse() throws IOException {
//
//        Observable<ApiResult> ztmapiResultObservableTrams = repository.getTrams("17");
//
//        assertNotNull(ztmapiResultObservableTrams);
//        assertTrue(ztmapiResultObservableTrams.blockingFirst()
//                .getLinesList().size() > 0);
//    }
//
//    @Test
//    public void givenInCorrectTramNo_whenCallForTrams_thenGetNoLines(){
//        //given & when & then
//        Observable<ApiResult> tramsObservable = repository.getBuses("Ac2");
//        assertTrue(tramsObservable.blockingFirst().getLinesList().size() == 0);
//    }
}
