package com.girmiti.mobilepos.activity;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.girmiti.mobilepos.R;
import com.girmiti.mobilepos.net.model.ConfirmTransactionDetailsDTO;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.notNullValue;

/**
 * << Add Comments Here >>
 *
 * @author Girmiti Software
 * @date 08-Aug-2017 04:10:00 pm
 * @version 1.0
 */

@RunWith(AndroidJUnit4.class)
public class ReceiptActivityTest {

    @Rule
    public ActivityTestRule<ReceiptActivity> activityTestRule = new ActivityTestRule<>(ReceiptActivity.class);

    @Mock
    Intent intent;

    @Mock
    ConfirmTransactionDetailsDTO confirmTransactionDetailsDTO;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testReceipt() {
        ReceiptActivity activity = activityTestRule.getActivity();
        View toolbarTitleTextView = activity.findViewById(R.id.toolbar_new_left);
        assertThat(toolbarTitleTextView, notNullValue());

        onView(withText(R.string.txn_ref)).check(matches(isDisplayed()));
        onView(withText(R.string.cg_ref)).check(matches(isDisplayed()));
        onView(withText(R.string.terminal)).check(matches(isDisplayed()));
        onView(withText(R.string.merchant_code)).check(matches(isDisplayed()));
        onView(withText(R.string.merchant_name)).check(matches(isDisplayed()));
        onView(withText(R.string.txn_date)).check(matches(isDisplayed()));
        onView(withText(R.string.auth_id)).check(matches(isDisplayed()));
        onView(withText(R.string.status)).check(matches(isDisplayed()));
        onView(withText(R.string.trans_type)).check(matches(isDisplayed()));
        onView(withText(R.string.trans_amount)).check(matches(isDisplayed()));
        onView(withText(R.string.tip_name)).check(matches(isDisplayed()));
        onView(withText(R.string.exp_date)).check(matches(isDisplayed()));

        onView(withId(R.id.txtTranNo)).check(matches(isDisplayed()));
        onView(withId(R.id.txtRefNo)).check(matches(isDisplayed()));
        onView(withId(R.id.txtTerminalId)).check(matches(isDisplayed()));
        onView(withId(R.id.txtMerchantId)).check(matches(isDisplayed()));
        onView(withId(R.id.txtDateTime)).check(matches(isDisplayed()));
        onView(withId(R.id.txtAuthCode)).check(matches(isDisplayed()));
        onView(withId(R.id.txtStatus)).check(matches(isDisplayed()));
        onView(withId(R.id.txtType)).check(matches(isDisplayed()));
        onView(withId(R.id.txtTranAmount)).check(matches(isDisplayed()));
        onView(withId(R.id.txtTip)).check(matches(isDisplayed()));
        onView(withId(R.id.txtTotalAmount)).check(matches(isDisplayed()));
    }
}