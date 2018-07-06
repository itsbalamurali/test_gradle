package com.girmiti.mobilepos.activity;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.girmiti.mobilepos.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.notNullValue;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 05-Aug-2017 03:00:00 pm
 * @version 1.0
 */

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> activityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void testValidLogin() {
        LoginActivity activity = activityTestRule.getActivity();
        View toolbarTitleTextView = activity.findViewById(R.id.toolbar);
        assertThat(toolbarTitleTextView, notNullValue());
        onView(withId(R.id.logo)).check(matches(isDisplayed()));
        onView(withId(R.id.username)).perform(typeText("johnMark"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("Ipsidy@123!"), closeSoftKeyboard());
        onView(withId(R.id.signin_btn)).perform(click());
    }
}