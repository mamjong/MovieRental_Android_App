package com.example.mark.prog4tent.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Mika Krooswijk on 15-6-2017.
 */

public class Rental implements Serializable {
    String rental_id;
    String rental_date;
    String inventory_id;
    String last_update;
    String description;
    String your_release;
    String title;
    String staffId;
    String customerId;
    private int mData;



    @Override
    public String toString() {
        return "Rental{" +
                "rental_id='" + rental_id + '\'' +
                ", rental_date='" + rental_date + '\'' +
                ", inventory_id='" + inventory_id + '\'' +
                ", last_update='" + last_update + '\'' +
                ", description='" + description + '\'' +
                ", your_release='" + your_release + '\'' +
                ", title='" + title + '\'' +
                ", staffId='" + staffId + '\'' +
                ", mData=" + mData +
                '}';
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Rental() {
    }

    public String getRental_id() {

        return rental_id;
    }

    public void setRental_id(String rental_id) {
        this.rental_id = rental_id;
    }

    public String getRental_date() {
        return rental_date;
    }

    public void setRental_date(String rental_date) {
        this.rental_date = rental_date.substring(0, 10);
    }

    public String getInventory_id() {
        return inventory_id;
    }

    public void setInventory_id(String inventory_id) {
        this.inventory_id = inventory_id;
    }

    public String getLast_update() {
        return last_update;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getYour_release() {
        return your_release;
    }

    public void setYour_release(String your_release) {
        this.your_release = your_release;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public int getmData() {
        return mData;
    }

    public void setmData(int mData) {
        this.mData = mData;
    }
}
