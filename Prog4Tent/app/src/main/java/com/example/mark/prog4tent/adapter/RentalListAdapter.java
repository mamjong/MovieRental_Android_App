package com.example.mark.prog4tent.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mark.prog4tent.R;
import com.example.mark.prog4tent.domain.Rental;

import java.util.ArrayList;

/**
 * Created by Mika Krooswijk on 15-6-2017.
 */

public class RentalListAdapter extends ArrayAdapter<Rental> {
    public RentalListAdapter(@NonNull Context context, @LayoutRes int resource, ArrayList<Rental> list) {
        super(context, resource, list);
    }

    public View getView(int position, View convertview, ViewGroup parent) {
            Rental rental = getItem(position);

            if (convertview == null) {
                convertview = LayoutInflater.from(getContext()).inflate(R.layout.rental_lv_item, parent, false);
            }

            TextView titleTextView = (TextView) convertview.findViewById(R.id.rental_item_title_tv);
            titleTextView.setText(rental.getTitle());

            TextView rentalDateTextView = (TextView) convertview.findViewById(R.id.rental_rental_date_tv);
            rentalDateTextView.setText(rental.getRental_date());

            TextView returnDateTextView = (TextView) convertview.findViewById(R.id.rental_return_date_tv);
            returnDateTextView.setText(rental.getReturn_date());

            return convertview;
    }
}
