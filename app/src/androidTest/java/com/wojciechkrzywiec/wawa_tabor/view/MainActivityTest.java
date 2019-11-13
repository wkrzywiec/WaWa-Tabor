package com.wojciechkrzywiec.wawa_tabor.view;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.wawa_applications.wawa_tabor.R;
import com.wawa_applications.wawa_tabor.view.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void whenSearchForLine_thenShowLoadingToast() {

        //when
        onView(withId(R.id.edit_query))
                .perform(typeText("180"), pressImeActionButton());

        //then
        onView(withText("Wyszukiwanie pojazdów lini: 180"))
                .inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));

    }

    @Test
    public void whenSearchForIncorrectLine_thenShowWarningToast() throws InterruptedException {

        //when
        onView(withId(R.id.edit_query))
                .perform(typeText("ABC"), pressImeActionButton());

        //then
        onView(withText("Wyszukiwanie pojazdów lini: ABC"))
                .inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));

        Thread.sleep(5000);

        onView(withText("Brak wyszukań dla linii: ABC"))
                .inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }
}
