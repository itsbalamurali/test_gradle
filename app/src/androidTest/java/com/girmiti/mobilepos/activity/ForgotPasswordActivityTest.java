package com.girmiti.mobilepos.activity;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.girmiti.mobilepos.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.notNullValue;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 05-Aug-2017 09:30:00 am
 * @version 1.0
 */

@RunWith(AndroidJUnit4.class)
public class ForgotPasswordActivityTest {

    private String userName;

    @Rule
    public ActivityTestRule<ForgotPasswordActivity> mActivityRule = new ActivityTestRule<>(ForgotPasswordActivity.class);

    @Before
    public void initVariables() {
        userName = "johnMark";
    }

    @Test
    public void testForgotPassword() {
        ForgotPasswordActivity activity = mActivityRule.getActivity();
        View toolbarTitleTextView = activity.findViewById(R.id.toolbar_new);
        assertThat(toolbarTitleTextView, notNullValue());
        onView(withId(R.id.email)).perform(typeText(userName));
        closeSoftKeyboard();
    }
}