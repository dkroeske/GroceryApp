package com.example.soricosoft.mygrocerylist.Model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import org.threeten.bp.LocalDateTime;


/**
 * Created by Paul de Mast on 26-Oct-17.
 */

public class Grocery implements Parcelable {
    private String name;
    private String quantity;
    private LocalDateTime dateItemAdded;

    private int id;

    public Grocery() {
    }

    public Grocery(String name, String quantity, LocalDateTime dateItemAdded, int id) {
        this.name = name;
        this.quantity = quantity;
        this.dateItemAdded = dateItemAdded;
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || o instanceof Grocery) return false;

        Grocery grocery = (Grocery) o;

        return id == grocery.id;
    }

    @Override
    public String toString() {
        return "Grocery{" +
                "name='" + name + '\'' +
                ", quantity='" + quantity + '\'' +
                ", dateItemAdded=" + dateItemAdded +
                ", id=" + id +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getDateItemAdded() {
        return dateItemAdded;
    }

    public void setDateItemAdded(LocalDateTime dateItemAdded) {
        this.dateItemAdded = dateItemAdded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    protected Grocery(Parcel in) {
        name = in.readString();
        quantity = in.readString();
        id = in.readInt();
        dateItemAdded = LocalDateTime.parse(in.readString());
    }

    public static final Creator<Grocery> CREATOR = new Creator<Grocery>() {
        @Override
        public Grocery createFromParcel(Parcel in) {
            return new Grocery(in);
        }

        @Override
        public Grocery[] newArray(int size) {
            return new Grocery[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(quantity);
        dest.writeInt(id);
        dest.writeString(dateItemAdded.toString());
    }
}
