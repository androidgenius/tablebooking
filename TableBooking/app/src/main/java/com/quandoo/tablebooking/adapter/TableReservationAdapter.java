package com.quandoo.tablebooking.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.quandoo.tablebooking.R;

import java.util.List;

/**
 * Created by gaurav on 31/08/16.
 */

/**
 * Adapter for Table Reservations
 */
public class TableReservationAdapter extends RecyclerView.Adapter<TableReservationAdapter.TableReservationViewHolder> {

    private Activity mContext;
    private int currentSelectionIndex = -1;
    private ItemSelectedListener listener;
    private List<Boolean> reservedList;

    @Override
    public TableReservationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_table_grid_row_item, parent, false);
        return new TableReservationViewHolder(itemView);
    }


    /**
     * Binding view with Reserved Table List data
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final TableReservationViewHolder holder, final int position) {

        final Boolean status = reservedList.get(position);

        holder.tableReservationButton.setText(mContext.getString(R.string.table_no, position + 1));

        //If Already occupied
        if (!status) {
            holder.tableReservationButton.setEnabled(false);
            holder.tableReservationStatusTV.setText("X");

        } else {

            // Non Occupied Tables
            holder.tableReservationButton.setEnabled(true);

            //Currently selected
            if (position == currentSelectionIndex) {
                holder.tableReservationButton.setPressed(true);
                holder.tableReservationStatusTV.setText(R.string.reserve_it);

            } else { // Completely Free table
                holder.tableReservationButton.setPressed(false);
                holder.tableReservationStatusTV.setText((Html.fromHtml("&#x2713;")));
            }
        }

        //Listener to Table reservation on click
        holder.tableReservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (position == currentSelectionIndex) {
                    currentSelectionIndex = -1;
                } else {
                    currentSelectionIndex = position;
                }
                holder.tableReservationButton.setPressed(true);
                holder.tableReservationStatusTV.setText(R.string.reserve_it);

                // Calling delate to reserve the table and update the DB resepectivly
                listener.itemSelected(position);

                //Time to notify the data changes in adapter
                notifyDataSetChanged();

            }
        });


    }

    //Total List count
    @Override
    public int getItemCount() {
        return reservedList.size();
    }


    public interface ItemSelectedListener {
        void itemSelected(int index);
    }


    //Adapter constructor

    /**
     * @param context               Activity Context
     * @param list                  List of  Tables reservation status
     * @param currentSelectionIndex Currenly selected table index  ( default -1)
     * @param listener              Listener to update Activity from adapter
     */
    public TableReservationAdapter(Activity context, List<Boolean> list, int currentSelectionIndex, ItemSelectedListener listener) {

        this.mContext = context;
        this.currentSelectionIndex = currentSelectionIndex;
        this.listener = listener;
        this.reservedList = list;
    }


    /**
     * View holder class for Table Reservation Data
     */
    public class TableReservationViewHolder extends RecyclerView.ViewHolder {

        private Button tableReservationButton;
        private TextView tableReservationStatusTV;


        public TableReservationViewHolder(View view) {
            super(view);
            tableReservationButton = (Button) view.findViewById(R.id.tableRservationBT);
            tableReservationStatusTV = (TextView) view.findViewById(R.id.tableStatusTV);


        }
    }

}



