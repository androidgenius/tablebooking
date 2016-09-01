package com.quandoo.tablebooking.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.content.LocalBroadcastManager;

import com.quandoo.tablebooking.application.QuandooApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaurav on 31/08/16.
 */


/**
 * Db helper class
 */
public class SQLiteHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    //DB Name
    private static final String DATABASE_NAME = "QUANDO_DB";

    //Table Name
    private static final String TABLE_RESERVATIONS = "reservation";

    //Column Names
    private static final String KEY_ID = "id";
    private static final String KEY_STATUS = "reserv_status";


    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_RESERVATIONS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_STATUS + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }


    /**
     * To Insert Data in DB
     *
     * @param tables
     */
    public void insertTableReservationStatus(boolean[] tables) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (boolean status : tables) {
            ContentValues values = new ContentValues();
            values.put(KEY_STATUS, status + "");
            db.insert(TABLE_RESERVATIONS, null, values);
        }
        db.close();
    }

    /**
     * To Get Data from DB
     *
     * @return Boolean List of Table status whether ther are reserved of Free
     */

    public List<Boolean> getReservedTablesStatus() {

        List<Boolean> tableList = new ArrayList<Boolean>();

        String selectQuery = "SELECT  * FROM " + TABLE_RESERVATIONS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                tableList.add(new Boolean(cursor.getString(1)));
            } while (cursor.moveToNext());
        }

        db.close();
        return tableList;
    }


    /**
     * To Update Table reservation status in DB
     *
     * @param index      Index of Table
     * @param isReserved - Status of Reservation
     */
    public void updateReservedTableStatus(int index, boolean isReserved) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_STATUS, isReserved + "");
        db.update(TABLE_RESERVATIONS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(index + 1)});

        db.close();
    }

    /**
     * To Clear out the rservation status of all the tables in DB
     */
    public void clearAllTableReservations() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_STATUS, true + "");
        db.update(TABLE_RESERVATIONS, values, null, null);
        db.close();
        sendMessage();
    }


    /**
     * @param db         database
     * @param oldVersion oldVersion
     * @param newVersion newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVATIONS);
        onCreate(db);
    }


    // Send an Intent with an action named "custom-event-name". The Intent sent should
// be received by the ReceiverActivity.
    private void sendMessage() {
        QLog.d("Sender Broadcasting message");
        Intent intent = new Intent("clear-table-reservation");
        intent.putExtra("message", "Cleared Reservations Successfully");
        LocalBroadcastManager.getInstance(QuandooApplication.getAppContext()).sendBroadcast(intent);
    }


}