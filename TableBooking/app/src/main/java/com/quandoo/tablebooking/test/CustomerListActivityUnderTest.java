package com.quandoo.tablebooking.test;

/**
 * Created by gaurav on 01/09/16.
 */

/**
 * Junit Test case Performer
 */
public class CustomerListActivityUnderTest {


    private CustomerTestListener customerTestListener;

    public CustomerListActivityUnderTest(CustomerTestListener customerSearchView) {
        this.customerTestListener = customerSearchView;
    }


    public void performSearchNameUnitTest() {
        customerTestListener.getSearchString();

    }

    public interface CustomerTestListener {
        String getSearchString();

    }

}
