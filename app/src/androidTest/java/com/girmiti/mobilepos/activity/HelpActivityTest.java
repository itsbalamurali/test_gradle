package com.girmiti.mobilepos.activity;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.girmiti.mobilepos.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.notNullValue;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 07-Aug-2017 03:00:00 pm
 * @version 1.0
 */

@RunWith(AndroidJUnit4.class)
public class HelpActivityTest {
    @Rule
    public ActivityTestRule<HelpActivity> mActivityRule = new ActivityTestRule<>(HelpActivity.class);

    @Test
    public void testHelp() {

        HelpActivity activity = mActivityRule.getActivity();
        View toolbarTitleTextView = activity.findViewById(R.id.toolbar_new);
        assertThat(toolbarTitleTextView, notNullValue());
        onView(withId(R.id.items)).check(matches(isDisplayed()));
        onData(anything()).inAdapterView(withId(R.id.items)).atPosition(0).perform(click());
    }
}