package com.quandoo.tablebooking.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gaurav on 30/08/16.
 */

/**
 * Pojo for customer Data, Making it simple
 */
public class CustomerData {

    public final String customerFirstName;
    public final String customerLastName;
    public final int id;

    public CustomerData(String customerFirstName, String customerLastName, int id) {
        this.customerFirstName = customerFirstName;
        this.customerLastName = customerLastName;
        this.id = id;
    }


}

