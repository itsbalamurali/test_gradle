package com.girmiti.mobilepos.activity;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.girmiti.mobilepos.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static org.hamcrest.CoreMatchers.notNullValue;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 16-Aug-2017 09:50:00 am
 * @version 1.0
 */

@RunWith(AndroidJUnit4.class)
public class TipActivityTest {

    @Rule
    public ActivityTestRule<TipActivity> activityTestRule = new ActivityTestRule<>(TipActivity.class);

    @Test
    public void testTip() throws Exception {
        TipActivity activity = activityTestRule.getActivity();
        View toolbarTitleTextView = activity.findViewById(R.id.toolbar_new_left);
        assertThat(toolbarTitleTextView, notNullValue());

      //  onView(withId(R.id.totalAmountText)).check(matches(withText("$ 0.00")));

/*        ViewInteraction appCompatEditText3 = onView(
                withId(R.id.totalAmountText) );
        appCompatEditText3.perform(replaceText("$ 5.00"));

        onView(withId(R.id.buttonFirst)).perform(click());

        onView(withId(R.id.proceedBtn)).perform(click());*/
    }
}