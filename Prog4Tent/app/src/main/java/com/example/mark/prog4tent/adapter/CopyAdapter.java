package com.example.mark.prog4tent.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mark.prog4tent.R;
import com.example.mark.prog4tent.domain.Copy;
import java.util.ArrayList;

/**
 * Created by mark on 17-6-2017.
 */

public class CopyAdapter extends ArrayAdapter<Copy> {

    private TextView copyInventoryId, copyStoreId, copyRentalId, copyStaffId, copyRentalDate, copyRented;

    public CopyAdapter(@NonNull Context context, ArrayList<Copy> copies) {
        super(context, 0, copies);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Copy copy = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.copy_lv_item, parent, false);
        }

        copyInventoryId = (TextView) convertView.findViewById(R.id.copyItem_tv_inventoryId);
        copyStoreId = (TextView) convertView.findViewById(R.id.copyItem_tv_storeId);
        copyRentalDate = (TextView) convertView.findViewById(R.id.copyItem_tv_rentalDate);
        copyRented = (TextView) convertView.findViewById(R.id.copyItem_tv_rented);

        copyInventoryId.setText("Inventory: " + copy.getInventoryId());
        copyStoreId.setText("Store: " + copy.getStoreId());

        if (copy.getRented()) {
            copyRented.setText("NOT AVAILABLE");
            copyRented.setTextColor(Color.parseColor("#FF0000"));
            copyRentalDate.setText("Rented on: " + copy.getRentalDate());
            copyRentalDate.setVisibility(View.VISIBLE);
        } else {
            copyRented.setText("AVAILABLE");
            copyRented.setTextColor(Color.parseColor("#2CB63C"));
            copyRentalDate.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }
}
