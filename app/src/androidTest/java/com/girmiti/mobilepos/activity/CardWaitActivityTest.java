package com.girmiti.mobilepos.activity;

import android.support.test.filters.MediumTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.girmiti.mobilepos.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.notNullValue;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 07-Aug-2017 11:00:00 am
 * @version 1.0
 */

@MediumTest
@RunWith(AndroidJUnit4.class)
public class CardWaitActivityTest {

    @Rule
    public ActivityTestRule<CardWaitActivity> activityTestRule = new ActivityTestRule<>(CardWaitActivity.class);

    @Test
    public void testCardWaitActivity() {
        CardWaitActivity activity = activityTestRule.getActivity();
        View toolbarTitleTextView = activity.findViewById(R.id.toolbar_cardwaiting);
        assertThat(toolbarTitleTextView, notNullValue());
        onView(withId(R.id.layoutImg)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.headerlyt)).check(matches(isDisplayed()));
    }
}