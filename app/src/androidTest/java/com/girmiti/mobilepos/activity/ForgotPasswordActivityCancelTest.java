package com.girmiti.mobilepos.activity;

import android.support.test.rule.ActivityTestRule;

import com.girmiti.mobilepos.R;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 07-Aug-2017 03:00:00 pm
 * @version 1.0
 */

public class ForgotPasswordActivityCancelTest {

    @Rule
    public ActivityTestRule<ForgotPasswordActivity> mActivityRule = new ActivityTestRule<>(ForgotPasswordActivity.class);

    @Test
    public void testForgotPasswordCancel() {
        onView(withId(R.id.forgot_pass_cancel)).perform(click());
    }
}