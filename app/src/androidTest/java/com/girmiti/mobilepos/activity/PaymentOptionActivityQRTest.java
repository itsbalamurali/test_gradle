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
 * @date 08-Aug-2017 02:00:00 pm
 * @version 1.0
 */

@RunWith(AndroidJUnit4.class)
public class PaymentOptionActivityQRTest {

    @Rule
    public ActivityTestRule<PaymentOptionActivity> activityTestRule = new ActivityTestRule<>(PaymentOptionActivity.class);

    @Test
    public void testPaymentOptionQR() {
        PaymentOptionActivity activity = activityTestRule.getActivity();
        View toolbarTitleTextView = activity.findViewById(R.id.toolbar_new_left);
        assertThat(toolbarTitleTextView, notNullValue());
        onView(withId(R.id.readQrCode)).perform(click());
        pressBack();
    }
}