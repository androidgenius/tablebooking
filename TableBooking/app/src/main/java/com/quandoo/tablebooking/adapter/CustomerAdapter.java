package com.quandoo.tablebooking.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quandoo.tablebooking.R;
import com.quandoo.tablebooking.model.CustomerData;

import java.util.List;

/**
 * Created by gaurav on 30/08/16.
 */

/**
 * Adapter to hold the customer values
 */
public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {


    private Activity mContext;
    private List<CustomerData> customerList;
    private ItemSelectedListener listener;


    public interface ItemSelectedListener {
        void itemSelected(String customerName);
    }


    public CustomerAdapter(Activity context, List<CustomerData> list, ItemSelectedListener listener) {
        this.mContext = context;
        this.customerList = list;
        this.listener = listener;
    }

    @Override
    public CustomerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_customer_row_item, parent, false);
        return new CustomerViewHolder(itemView);

    }


    /**
     * Binding view with Customer List Data
     * @param holder hodler
     * @param position position of adapter
     */
    @Override
    public void onBindViewHolder(final CustomerViewHolder holder, int position) {

        final CustomerData customerData = customerList.get(position);
        holder.custNameTV.setText(customerData.customerFirstName + " " + customerData.customerLastName);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.itemSelected(holder.custNameTV.getText().toString());
            }
        });


    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    /**
     * View holder class for Customer Data
     */
    public class CustomerViewHolder extends RecyclerView.ViewHolder {

        private TextView custNameTV;


        public CustomerViewHolder(View view) {
            super(view);
            custNameTV = (TextView) view.findViewById(R.id.customerName);

        }
    }


}