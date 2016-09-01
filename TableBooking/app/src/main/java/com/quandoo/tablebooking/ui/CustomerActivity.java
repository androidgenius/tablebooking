package com.quandoo.tablebooking.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.quandoo.tablebooking.Conf;
import com.quandoo.tablebooking.R;
import com.quandoo.tablebooking.adapter.CustomerAdapter;
import com.quandoo.tablebooking.model.CustomerData;
import com.quandoo.tablebooking.receiver.TableClearAlarmReciever;
import com.quandoo.tablebooking.test.CustomerListActivityUnderTest;
import com.quandoo.tablebooking.utils.FileUtils;
import com.quandoo.tablebooking.utils.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * Launcher Activity to List the the customer data and initilization of DB and AlarmManager
 */
public class CustomerActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, CustomerAdapter.ItemSelectedListener, CustomerListActivityUnderTest.CustomerTestListener {

    private RecyclerView customerRV;
    private Toolbar mToolbar;
    private List<CustomerData> customerModelList;
    private CustomerAdapter customerAdapter;
    private AlarmManager manager;
    private PendingIntent pendingIntent;
    private SearchView searchView;
    private String mSearchString;
    private String SEARCH_KEY = "SEARCH_KEY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //DB initialisation
        initDB();

        // Setting Alarm
        initAlarm();

        //Setting Tool Bar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        customerRV = (RecyclerView) findViewById(R.id.customerRV);
        setSupportActionBar(mToolbar);

        // If Onconfiguration changes
        if (savedInstanceState != null) {
            mSearchString = savedInstanceState.getString(SEARCH_KEY);
        }

        //Reading customer specific data from asset folder
        customerModelList = FileUtils.getCustomerListFromFile();

        //Setting Layout manager to recycler view
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        customerRV.setLayoutManager(mLayoutManager);
        customerRV.setItemAnimator(new DefaultItemAnimator());

        //setting Adapter
        customerAdapter = new CustomerAdapter(this, customerModelList, this);
        customerRV.setAdapter(customerAdapter);


    }

    /**
     * Database Initialization
     */
    private void initDB() {
        SQLiteHelper databaseHelper = new SQLiteHelper(this);
        if (databaseHelper.getReservedTablesStatus().size() == 0)
            databaseHelper.insertTableReservationStatus(FileUtils.getTableDataFromFile());
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    /**
     * Delegate to be called on, when user has changed something
     *
     * @param query
     * @return
     */
    @Override
    public boolean onQueryTextChange(String query) {

        //Original adapter reset
        if (query.isEmpty())
            customerRV.setAdapter(new CustomerAdapter(this, customerModelList, this));
        else //Setting adapter with relevant searced data
            customerRV.setAdapter(new CustomerAdapter(this, filter(customerModelList, query), this));

        return true;

    }


    /**
     * For inflating the menu options
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);


        //focus the SearchView
        if (mSearchString != null && !mSearchString.isEmpty()) {
            searchView.onActionViewExpanded();
            searchView.setQuery(mSearchString, true);
            searchView.clearFocus();
        }



        return true;
    }


    /**
     * Filters the list based in input provided in Search View in Toolbar
     *
     * @param originalCustomerList Customerdata List
     * @param query                Search String
     * @return
     */
    private List<CustomerData> filter(List<CustomerData> originalCustomerList, String query) {
        query = query.toLowerCase();

        final List<CustomerData> filteredCustomerList = new ArrayList<>();
        for (CustomerData customer : originalCustomerList) {
            if (customer.customerLastName.toLowerCase().startsWith(query.toLowerCase()) ||
                    customer.customerFirstName.toLowerCase().startsWith(query.toLowerCase())) {
                filteredCustomerList.add(customer);
            }
        }
        return filteredCustomerList;
    }


    @Override
    public void itemSelected(String customerName) {

        Intent intent = new Intent(this, TableReservationActivity.class);
        intent.putExtra(TableReservationActivity.KEY_CUSTOMER_NAME, customerName);
        startActivity(intent);
    }


    /**
     * AlarmManager Initialization
     */
    private void initAlarm() {

        initClearTableAlarm();
        cancelAlarm();
        startAlarm();

    }


    private void initClearTableAlarm() {

        Intent alarmIntent = new Intent(this, TableClearAlarmReciever.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
    }


    /**
     * For cancelling alarm if any
     */
    private void cancelAlarm() {
        if (manager != null) {
            manager.cancel(pendingIntent);
        }
    }


    /**
     * Setting Alarm
     */
    private void startAlarm() {
        manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //Getting interval duration from conf file
        int interval = Conf.remove_reservation_interval;
        //Set repeating alarm
        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + interval, interval, pendingIntent);
    }


    //Mock the Junit test Case
    @Override
    public String getSearchString() {
        return searchView.getQuery().toString();
    }


    // To save state of search view
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mSearchString = searchView.getQuery().toString();
        outState.putString(SEARCH_KEY, mSearchString);
    }

}
