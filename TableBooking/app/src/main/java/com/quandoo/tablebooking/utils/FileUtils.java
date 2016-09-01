package com.quandoo.tablebooking.utils;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.quandoo.tablebooking.application.QuandooApplication;
import com.quandoo.tablebooking.model.CustomerData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by gaurav on 30/08/16.
 */
public class FileUtils {


    /**
     * To read customer data from Json
     * inside Asset folder
     * @return customerModelList
     */
    public static List<CustomerData> getCustomerListFromFile() {
        List<CustomerData> customerModelList = null;
        String json = "";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(QuandooApplication.getAppContext()
                            .getAssets().open("customer_data.json")));
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                json += mLine;
            }
            customerModelList = new GsonBuilder().create().fromJson(json,
                    new TypeToken<List<CustomerData>>() {
                    }.getType());
        } catch (IOException e) {
            QLog.e("Exception ", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    QLog.e("Exception ", e);
                }
            }
        }
        return customerModelList;
    }

    /**
     * To get table ocucpnacy data from file
     * inside Asset folder
     * @return tableArray : Array of Table occupancy status
     */
    public static boolean[] getTableDataFromFile() {
        boolean[] tableArray = null;
        String json = "";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(QuandooApplication.getAppContext()
                            .getAssets().open("table_data.json")));
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                json += mLine;
            }
            tableArray = new GsonBuilder().create().fromJson(json,
                    new TypeToken<boolean[]>() {
                    }.getType());
        } catch (IOException e) {
            QLog.e("Exception ", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    QLog.e("Exception ", e);
                }
            }
        }
        return tableArray;
    }


}
