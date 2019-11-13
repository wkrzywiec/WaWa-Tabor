package com.wawa_applications.wawa_tabor.viewmodel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import androidx.lifecycle.LiveData;

import com.wawa_applications.wawa_tabor.model.Line;
import com.wawa_applications.wawa_tabor.model.ApiResult;
import com.wawa_applications.wawa_tabor.repository.ZtmApiRepository;

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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LinesViewModelTest {

    @Mock( name = "repository")
    private ZtmApiRepository mockedRepository;

    @InjectMocks
    private LinesViewModel linesViewModel;

    TestScheduler testScheduler;

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        testScheduler = new TestScheduler();
        RxJavaPlugins.setComputationSchedulerHandler(scheduler -> testScheduler);
    }

    @Test
    public void given180Line_whenIndicateLineType_thenReturn1() {
        // given & when
        int lineType = linesViewModel.indicateLineType("180");

        // then
        assertEquals(1, lineType);
    }

    @Test
    public void givenN83Line_whenIndicateLineType_thenReturn1() {
        // given & when
        int lineType = linesViewModel.indicateLineType("N83");

        // then
        assertEquals(1, lineType);
    }

    @Test
    public void given17Line_whenIndicateLineType_thenReturn2() {
        // given & when
        int lineType = linesViewModel.indicateLineType("17");

        // then
        assertEquals(2, lineType);
    }

    @Test
    public void givenABCLine_whenIndicateLineType_thenReturn0() {
        // given & when
        int lineType = linesViewModel.indicateLineType("ABC");

        // then
        assertEquals(0, lineType);
    }

    @Test
    public void givenn83Line_whenIndicateLineType_thenReturn1() {
        // given & when
        int lineType = linesViewModel.indicateLineType("n83");

        // then
        assertEquals(1, lineType);
    }

    @Test
    public void whenSubscribeForLine_thenLineNoUpdated() {
        //given & when
        linesViewModel.subscribeToLine("180", 1);

        //then
        assertEquals("180", linesViewModel.getLineNoLiveData().getValue());
    }

    @Test
    public void whenNoBusesAvailable_thenReturnEmptyList() {
        // when
        LiveData<List<Line>> transportList = linesViewModel.getLineListLiveData();

        // then
        assertEquals(0, transportList.getValue().size());
    }

    @Test
    public void whenTwoBusesAreAvailable_thenReturnListWithTwoBuses() {
        //given
        mockZTMResults("180", 2);

        //when
        linesViewModel.subscribeToLine("180", 1);
        testScheduler.advanceTimeBy(5, TimeUnit.SECONDS);
        LiveData<List<Line>> transportList = linesViewModel.getLineListLiveData();

        //then
       assertEquals(2, transportList.getValue().size());
    }

    @Test
    public void when2TimesTryToGetBuses_thenReceive2DifferentResultSets() {
        //given
        mock2ZTMResults("180", 2, 3);

        //when
        linesViewModel.subscribeToLine("180", 1);

        testScheduler.advanceTimeBy(5, TimeUnit.SECONDS);
        LiveData<List<Line>> transportList1 = linesViewModel.getLineListLiveData();
        assertEquals(2, transportList1.getValue().size());

        testScheduler.advanceTimeBy(5, TimeUnit.SECONDS);
        LiveData<List<Line>> transportList2 = linesViewModel.getLineListLiveData();
        assertEquals(3, transportList2.getValue().size());
    }

    @Test
    public void whenSubscribeForLine_thenIsResultTrue() {
        //given
        mockZTMResults("180", 2);

        //when
        linesViewModel.subscribeToLine("180", 1);

        testScheduler.advanceTimeBy(5, TimeUnit.SECONDS);

        assertTrue(linesViewModel.getIsResult().getValue());
    }

    @Test
    public void whenSubscribeForLine_thenIsResultFalse() {
        //given
        mockZTMResults("ABC", 0);

        //when
        linesViewModel.subscribeToLine("ABC", 1);

        testScheduler.advanceTimeBy(5, TimeUnit.SECONDS);

        assertFalse(linesViewModel.getIsResult().getValue());
    }

    private void mockZTMResults(String lineNo, int numberOfBuses) {
        ApiResult results = createZTMResult(lineNo, numberOfBuses);
        when(mockedRepository.getBuses(any()))
                .thenReturn(Observable.just(results));
    }

    private void mock2ZTMResults(String lineNo, int numberOfBuses, int numberOfBuses2) {
        ApiResult results1 = createZTMResult(lineNo, numberOfBuses);
        ApiResult results2 = createZTMResult(lineNo, numberOfBuses2);
        when(mockedRepository.getBuses(any()))
                .thenReturn(Observable.just(results1), Observable.just(results2));
    }

    private ApiResult createZTMResult(String lineNo, int numberOfBuses) {
        List<Line> lines =  IntStream.rangeClosed(1, numberOfBuses)
                .mapToObj(i -> createZTMLine(lineNo, i))
                .collect(Collectors.toList());

        ApiResult results = new ApiResult();
        lines.forEach(line -> results.addLine(line));
        return results;
    }

    private Line createZTMLine(String lineNo, int i) {
        return new Line(
                52.22977,
                21.01178,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                lineNo,
                String.valueOf(i)
        );
    }
}
