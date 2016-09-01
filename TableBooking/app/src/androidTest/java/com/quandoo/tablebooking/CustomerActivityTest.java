package com.quandoo.tablebooking;

import android.support.test.InstrumentationRegistry;
import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.quandoo.tablebooking.adapter.CustomerAdapter;
import com.quandoo.tablebooking.model.CustomerData;
import com.quandoo.tablebooking.ui.CustomerActivity;

import org.hamcrest.Matchers;

import java.util.List;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.core.deps.guava.base.Predicates.not;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;

/**
 * Created by gaurav on 31/08/16.
 */
public class CustomerActivityTest extends ActivityInstrumentationTestCase2<CustomerActivity> {

    private RecyclerView customerListRV;
    private CustomerAdapter customerAdapter;
    private static final int ADAPTER_COUNT = 21;


    public CustomerActivityTest() {
        super(CustomerActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        customerListRV = (RecyclerView) getActivity().findViewById(R.id.customerRV);
        customerAdapter = (CustomerAdapter) customerListRV.getAdapter();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());

    }


    public void testPreConditions() {
        assertTrue(customerListRV != null);
        assertTrue(customerAdapter != null);
    }


    public void test_searchView_recylerData() {

        onView(withId(R.id.action_search)).perform(click());
        onView(isAssignableFrom(EditText.class)).perform(typeText("abr"), pressImeActionButton());
        onView(withId(R.id.customerRV))
                .check(matches(hasDescendant(withText("Abraham Lincoln"))));

    }


    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
