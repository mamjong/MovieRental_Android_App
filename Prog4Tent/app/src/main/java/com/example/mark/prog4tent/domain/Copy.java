package com.example.mark.prog4tent.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mark on 17-6-2017.
 */

public class Copy implements Parcelable {

    private int mData, inventoryId, storeId, rentalId, staffId;
    private boolean rented;
    private String rentalDate;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mData);
    }

    public static final Parcelable.Creator<Copy> CREATOR
            = new Parcelable.Creator<Copy>() {
        public Copy createFromParcel(Parcel in) {
            return new Copy(in);
        }

        public Copy[] newArray(int size) {
            return new Copy[size];
        }
    };

    private Copy(Parcel in) {
        mData = in.readInt();
    }

    public Copy() {
    }

    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getRentalId() {
        return rentalId;
    }

    public void setRentalId(int rentalId) {
        this.rentalId = rentalId;
    }

    public String getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(String rentalDate) {
        this.rentalDate = rentalDate.substring(0, 10);
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public boolean getRented() {
        return rented;
    }

    public void setRented(Boolean rented) {
        this.rented = rented;
    }
}
