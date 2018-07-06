package com.girmiti.mobilepos.activity;

import android.support.test.filters.MediumTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.girmiti.mobilepos.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static org.hamcrest.Matchers.notNullValue;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 07-Aug-2017 10:00:00 am
 * @version 1.0
 */

@MediumTest
@RunWith(AndroidJUnit4.class)
public class AboutActivityTest {

    @Rule
    public ActivityTestRule<AboutActivity> mActivityRule = new ActivityTestRule<>(AboutActivity.class);

    @Test
    public void testAbout() throws Exception {
        AboutActivity activity = mActivityRule.getActivity();
        View toolbarTitleTextView = activity.findViewById(R.id.toolbar_new);
        assertThat(toolbarTitleTextView, notNullValue());
        //  View webView = activity.findViewById(R.id.webView);
        // assertThat(webView, notNullValue());
    }
}