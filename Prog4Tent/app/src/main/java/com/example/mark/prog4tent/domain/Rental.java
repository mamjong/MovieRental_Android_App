package com.example.mark.prog4tent.domain;

/**
 * Created by Mika Krooswijk on 15-6-2017.
 */

public class Rental {
    String rental_id, rental_date, inventory_id, return_date, last_update, description, your_release, title;

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
        this.rental_date = rental_date;
    }

    public String getInventory_id() {
        return inventory_id;
    }

    public void setInventory_id(String inventory_id) {
        this.inventory_id = inventory_id;
    }

    public String getReturn_date() {
        return return_date;
    }

    public void setReturn_date(String return_date) {
        this.return_date = return_date;
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

    @Override
    public String toString() {
        return "Rental{" +
                "rental_id='" + rental_id + '\'' +
                ", rental_date='" + rental_date + '\'' +
                ", inventory_id='" + inventory_id + '\'' +
                ", return_date='" + return_date + '\'' +
                ", last_update='" + last_update + '\'' +
                ", description='" + description + '\'' +
                ", your_release='" + your_release + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
