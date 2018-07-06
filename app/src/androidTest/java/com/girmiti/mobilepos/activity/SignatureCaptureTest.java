package com.girmiti.mobilepos.activity;

import android.support.test.rule.ActivityTestRule;

import com.girmiti.mobilepos.R;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 10-Aug-2017 10:00:00 am
 * @version 1.0
 */

public class SignatureCaptureTest {

    @Rule
    public ActivityTestRule<SignatureCapture> mActivityRule = new ActivityTestRule<>(SignatureCapture.class);

    @Test
    public void testSignatureCapture() {
        onView(withId(R.id.amount)).check(matches(withText("SUBTOTAL")));
        onView(withId(R.id.amount_value)).check(matches(withText("$ 0.00")));
        onView(withId(R.id.capture)).perform(click());
    }
}