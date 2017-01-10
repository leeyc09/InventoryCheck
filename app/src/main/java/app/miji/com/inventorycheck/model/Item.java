package app.miji.com.inventorycheck.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Item model class for creating new item or editing
 */

public class Item implements Parcelable{
    String name;
    String qty;
    String unit;
    //TODO add image

    public Item(String name, String qty, String unit) {
        this.name = name;
        this.qty = qty;
        this.unit = unit;
    }

    protected Item(Parcel in) {
        name = in.readString();
        qty = in.readString();
        unit = in.readString();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(qty);
        parcel.writeString(unit);
    }
}
