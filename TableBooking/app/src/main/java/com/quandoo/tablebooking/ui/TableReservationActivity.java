package com.quandoo.tablebooking.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.quandoo.tablebooking.R;
import com.quandoo.tablebooking.adapter.TableReservationAdapter;
import com.quandoo.tablebooking.application.QuandooApplication;
import com.quandoo.tablebooking.utils.QLog;
import com.quandoo.tablebooking.utils.SQLiteHelper;

import java.util.List;

/**
 * Activity to represent the Table Receycler view and their reservation accordingly
 */
public class TableReservationActivity extends AppCompatActivity implements TableReservationAdapter.ItemSelectedListener {

    private int currentIndex = -1;
    private String customerName;
    private RecyclerView tableReservationRV;
    //Span count for Grid
    private int changedSpanCount = 4;
    //Recyler Adapter for Table
    private TableReservationAdapter tableReservationAdapter;
    private List<Boolean> tabelStatusList;
    public final static String KEY_CUSTOMER_NAME = "customer_name";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_reservation);
        setToolBar();


        //On configuration changes
        if (savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt("currentIndex");
            customerName = savedInstanceState.getString("customerName");
        } else {
            customerName = getIntent().getStringExtra(KEY_CUSTOMER_NAME);
        }

        //Setting title
        setTitle(customerName);

        tabelStatusList = new SQLiteHelper(this).getReservedTablesStatus();

        tableReservationRV = (RecyclerView) findViewById(R.id.TableReservationRV);


        //Setting Recyler Layout Manager
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), changedSpanCount);
        tableReservationRV.setLayoutManager(mLayoutManager);
        tableReservationRV.setItemAnimator(new DefaultItemAnimator());
        tableReservationAdapter = new TableReservationAdapter(this, tabelStatusList, currentIndex, this);
        tableReservationRV.setAdapter(tableReservationAdapter);

        // We are registering an observer (mMessageReceiver) to receive Intents
        // with actions named "clear-table-reservation".
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("clear-table-reservation"));

    }

    // Our handler for received Intents. This will be called whenever an Intent
    // with an action named "clear-table-reservation" is broadcasted.
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent, as of now, No use
            String message = intent.getStringExtra("message");
            QLog.d("Receiver Got message: " + message);
            //Time to notify the adapter after changes in DB
            notifyRecyclerViewAfterChange();
            Toast.makeText(TableReservationActivity.this, "Auto scheduled clearing Reservations", Toast.LENGTH_LONG).show();


        }
    };


    /**
     * To Notify the Recyler View after changes in Database
     */
    private void notifyRecyclerViewAfterChange() {

        if (tableReservationRV != null && tableReservationAdapter != null) {
            tabelStatusList.clear();
            tabelStatusList.addAll(new SQLiteHelper(QuandooApplication.getAppContext()).getReservedTablesStatus());
            tableReservationAdapter.notifyDataSetChanged();

        }
    }

    /**
     * To set Tool bar
     */
    private void setToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
            return true;
        }

        //If user has pressed to book table for this customer
        if (id == R.id.action_user) {

            if (currentIndex != -1) {

                //To Update DB to reserve the table and then notify adapter
                new SQLiteHelper(this).updateReservedTableStatus(currentIndex, false);
                final String msg = getString(R.string.reservation_successfull, customerName);
                notifyRecyclerViewAfterChange();
                Toast.makeText(TableReservationActivity.this, msg, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(TableReservationActivity.this, R.string.select_free_table, Toast.LENGTH_LONG).show();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentIndex", currentIndex);
        outState.putString("customerName", customerName);
    }


    @Override
    public void itemSelected(int index) {
        currentIndex = index;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_submit, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }


}
