package com.girmiti.mobilepos.activity;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.girmiti.mobilepos.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 12-Aug-2017 01:45:00 pm
 * @version 1.0
 */

@RunWith(AndroidJUnit4.class)
public class SplashScreenActivityTest {

    @Rule
    public ActivityTestRule<SplashScreenActivity> activityTestRule = new ActivityTestRule<>(SplashScreenActivity.class);

    @Test
    public void testSplashScreen() {
        SplashScreenActivity activity = activityTestRule.getActivity();
        onView(withId(R.id.splash)).check(matches(isDisplayed()));
        onView(withId(R.id.powered)).check(matches(isDisplayed()));
        onView(withId(R.id.copyrights)).check(matches(withText("\u00a92017 Ipsidy. All Rights Reserved")));
    }
}