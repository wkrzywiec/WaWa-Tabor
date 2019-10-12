package com.wawa_applications.wawa_tabor.network.viewmodel;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;

import com.wawa_applications.wawa_tabor.network.retrofit.ZTMAPIService;
import com.wawa_applications.wawa_tabor.network.retrofit.model.ZTMAPILine;
import com.wawa_applications.wawa_tabor.network.retrofit.model.ZTMAPIResult;
import com.wawa_applications.wawa_tabor.viewmodel.LinesViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import io.reactivex.Observable;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.TestScheduler;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LinesViewModelTest {

    @Mock( name = "ztmService")
    private ZTMAPIService mockedZtmService;

    @InjectMocks
    private LinesViewModel linesViewModel;

    TestScheduler testScheduler;

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Before
    public void init() {
        //linesViewModel = new LinesViewModel();
        MockitoAnnotations.initMocks(this);
        testScheduler = new TestScheduler();
        RxJavaPlugins.setComputationSchedulerHandler(scheduler -> testScheduler);
    }

    @Test
    public void whenNoBusesAvailable_thenReturnEmptyList() {
        // when
        LiveData<List<ZTMAPILine>> transportList = linesViewModel.getTransportList();

        // then
        assertEquals(0, transportList.getValue().size());
    }

    @Test
    public void whenTwoBusesAreAvailable_thenReturnListWithTwoBuses() {
        //given
        mockZTMResults("180", 2);

        //when
        linesViewModel.subscribeBus("180");
        testScheduler.advanceTimeBy(15, TimeUnit.SECONDS);
        LiveData<List<ZTMAPILine>> transportList = linesViewModel.getTransportList();

        //then
       assertEquals(2, transportList.getValue().size());
    }

    @Test
    public void when2TimesTryToGetBuses_thenReceive2DifferentResultSets() {
        //given
        mock2ZTMResults("180", 2, 3);

        //when
        linesViewModel.subscribeBus("180");

        testScheduler.advanceTimeBy(15, TimeUnit.SECONDS);
        LiveData<List<ZTMAPILine>> transportList1 = linesViewModel.getTransportList();
        assertEquals(2, transportList1.getValue().size());

        testScheduler.advanceTimeBy(15, TimeUnit.SECONDS);
        LiveData<List<ZTMAPILine>> transportList2 = linesViewModel.getTransportList();
        assertEquals(3, transportList2.getValue().size());


    }

    private void mockZTMResults(String lineNo, int numberOfBuses) {
        ZTMAPIResult results = createZTMResult(lineNo, numberOfBuses);
        when(mockedZtmService.getBuses(any()))
                .thenReturn(Observable.just(results));
    }

    private void mock2ZTMResults(String lineNo, int numberOfBuses, int numberOfBuses2) {
        ZTMAPIResult results1 = createZTMResult(lineNo, numberOfBuses);
        ZTMAPIResult results2 = createZTMResult(lineNo, numberOfBuses2);
        when(mockedZtmService.getBuses(any()))
                .thenReturn(Observable.just(results1), Observable.just(results2));
    }

    private ZTMAPIResult createZTMResult(String lineNo, int numberOfBuses) {
        List<ZTMAPILine> lines =  IntStream.rangeClosed(1, numberOfBuses)
                .mapToObj(i -> createZTMLine(lineNo, i))
                .collect(Collectors.toList());

        ZTMAPIResult results = new ZTMAPIResult();
        lines.forEach(line -> results.addLine(line));
        return results;
    }

    private ZTMAPILine createZTMLine(String lineNo, int i) {
        return new ZTMAPILine(
                52.22977,
                21.01178,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                lineNo,
                String.valueOf(i)
        );
    }
}
