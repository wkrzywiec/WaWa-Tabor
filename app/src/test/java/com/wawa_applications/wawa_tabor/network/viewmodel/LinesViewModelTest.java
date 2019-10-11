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

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Before
    public void init() {
        //linesViewModel = new LinesViewModel();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whenNoBusesAvailable_thenReturnEmptyList() {
        // when
        LiveData<List<ZTMAPILine>> transportList = linesViewModel.getTransportList();

        // then
        assertEquals(0, transportList.getValue().size());
    }

    @Test
    public void whenTwoBusesAreAvailable_thenReturnListWithTwoBuses() throws InterruptedException {

        TestScheduler testScheduler = new TestScheduler();
        RxJavaPlugins.setComputationSchedulerHandler(scheduler -> testScheduler);

        //given
        mockZTMResults("180", 2);

        //when
        linesViewModel.subscribeBus("180");
        testScheduler.advanceTimeBy(20, TimeUnit.SECONDS);
        LiveData<List<ZTMAPILine>> transportList = linesViewModel.getTransportList();

        //then
        assertEquals(2, transportList.getValue().size());
    }

    private void mockZTMResults(String lineNo, int numberOfBuses) {
        List<ZTMAPILine> lines =  IntStream.rangeClosed(1, numberOfBuses)
                .mapToObj(i -> createZTMResult(lineNo, i))
                .collect(Collectors.toList());

        ZTMAPIResult results = new ZTMAPIResult();
        lines.forEach(line -> results.addLine(line));

        when(mockedZtmService.getBuses(any()))
                .thenReturn((Observable.just(results)));
    }

    private ZTMAPILine createZTMResult(String lineNo, int i) {
        return new ZTMAPILine(
                52.22977,
                21.01178,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                lineNo,
                String.valueOf(i)
        );
    }
}
