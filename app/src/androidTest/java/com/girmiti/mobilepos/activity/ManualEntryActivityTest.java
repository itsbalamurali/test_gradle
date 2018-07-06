package com.girmiti.mobilepos.activity;

import android.nfc.NfcAdapter;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.girmiti.mobilepos.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.girmiti.mobilepos.R.id.cardHolderName;
import static com.girmiti.mobilepos.R.id.expDateEditText;
import static org.hamcrest.CoreMatchers.notNullValue;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 16-Aug-2017 10:45:00 am
 * @version 1.0
 */

@RunWith(AndroidJUnit4.class)
public class ManualEntryActivityTest {

    @Rule
    public ActivityTestRule<ManualEntryActivity> activityTestRule = new ActivityTestRule<>(ManualEntryActivity.class);

    private NfcAdapter mAdapter;

    @Test
    public void testManualEntry() {
        ManualEntryActivity activity = activityTestRule.getActivity();
        View toolbarTitleTextView = activity.findViewById(R.id.toolbar_new_left);
        assertThat(toolbarTitleTextView, notNullValue());

        onView(withText("Enter Card Details")).check(matches(isDisplayed()));
        onView(withText("Card number")).check(matches(isDisplayed()));
        onView(withText("Exp(YYMM)")).check(matches(isDisplayed()));
        onView(withText("Card Holder Name")).check(matches(isDisplayed()));
        onView(withId(R.id.ImgMc)).check(matches(isDisplayed()));

        onView(withId(R.id.panEditText1)).perform(typeText("5578"));
        onView(withId(R.id.panEditText2)).perform(typeText("9712"));
        onView(withId(R.id.panEditText3)).perform(typeText("3936"));
        onView(withId(R.id.panEditText4)).perform(typeText("6399"), closeSoftKeyboard());
        onView(withId(expDateEditText)).perform(typeText("2003"), closeSoftKeyboard());
        onView(withId(cardHolderName)).perform(typeText("JohnMark"), closeSoftKeyboard());
    }
}