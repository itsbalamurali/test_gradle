package com.girmiti.mobilepos.activity;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.girmiti.mobilepos.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.notNullValue;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 08-Aug-2017 10:00:00 am
 * @version 1.0
 */

@RunWith(AndroidJUnit4.class)
public class PaymentOptionActivityNFCTest {

    @Rule
    public ActivityTestRule<PaymentOptionActivity> activityTestRule = new ActivityTestRule<>(PaymentOptionActivity.class);

    @Test
    public void testPaymentOptionNFC() {
        PaymentOptionActivity activity = activityTestRule.getActivity();
        View toolbarTitleTextView = activity.findViewById(R.id.toolbar_new_left);
        assertThat(toolbarTitleTextView, notNullValue());
        onView(withId(R.id.btn_nfc)).perform(click());
        pressBack();
    }
}