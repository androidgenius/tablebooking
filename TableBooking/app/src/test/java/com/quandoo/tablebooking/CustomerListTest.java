package com.quandoo.tablebooking;

import com.quandoo.tablebooking.test.CustomerListActivityUnderTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by gaurav on 01/09/16.
 */

@RunWith(MockitoJUnitRunner.class)
public class CustomerListTest {


    @Mock
    private CustomerListActivityUnderTest.CustomerTestListener listener;
    private CustomerListActivityUnderTest customerTest;
    private static final String MOCK_STRING = "QUANDOO";


    @org.junit.Before
    public void setUp() throws Exception {
        customerTest = new CustomerListActivityUnderTest(listener);
    }

    @Test
    public void verifyCustomerList() throws Exception {

        when(listener.getSearchString()).thenReturn(MOCK_STRING);
        customerTest.performSearchNameUnitTest();
        assertThat(listener.getSearchString(), is(MOCK_STRING));


    }
}
