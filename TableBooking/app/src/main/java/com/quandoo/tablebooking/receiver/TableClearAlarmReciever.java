package com.quandoo.tablebooking.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.quandoo.tablebooking.utils.QLog;
import com.quandoo.tablebooking.utils.SQLiteHelper;

/**
 * Created by gaurav on 31/08/16.
 */

/**
 * Receiver to update the database for Table reservation status
 */
public class TableClearAlarmReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent arg1) {
        QLog.d("Received broadcast to clear the table reservation");
        SQLiteHelper databaseHelper = new SQLiteHelper(context);
        databaseHelper.clearAllTableReservations();
    }
}

