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
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.notNullValue;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 28-July-2017 12:45:00 pm
 * @version 1.0
 */

@RunWith(AndroidJUnit4.class)
public class ChangePasswordActivityTest {

    @Rule
    public ActivityTestRule<ChangePasswordActivity> mActivityRule = new ActivityTestRule<>(ChangePasswordActivity.class);

    @Test
    public void testChangePassword() {
        ChangePasswordActivity activity = mActivityRule.getActivity();
        View toolbarTitleTextView = activity.findViewById(R.id.toolbar_new);
        assertThat(toolbarTitleTextView, notNullValue());
        onView(withId(R.id.existing_pwd_edittxt)).perform(typeText("Asd@12345"), closeSoftKeyboard());
        onView(withId(R.id.new_pwd_edittxt)).perform(typeText("Asd@123456"), closeSoftKeyboard());
        onView(withId(R.id.confirm_new_pwd_edittxt)).perform(typeText("Asd@123456"), closeSoftKeyboard());
        onView(withId(R.id.save_pwd_btn)).perform(click());
    }
}