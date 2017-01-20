package app.miji.com.inventorycheck.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by isse on 19/01/2017.
 */

public class Sales implements Parcelable {
    String date;
    String time;
    String customer;
    String ReferenceNo;
    String location;
    String image;
    List<Item> items;

    public Sales(String date, String time, String customer, String referenceNo, String location, String image, List<Item> items) {
        this.date = date;
        this.time = time;
        this.customer = customer;
        ReferenceNo = referenceNo;
        this.location = location;
        this.image = image;
        this.items = items;
    }

    protected Sales(Parcel in) {
        date = in.readString();
        time = in.readString();
        customer = in.readString();
        ReferenceNo = in.readString();
        location = in.readString();
        image = in.readString();
        items = in.createTypedArrayList(Item.CREATOR);
    }

    public static final Creator<Sales> CREATOR = new Creator<Sales>() {
        @Override
        public Sales createFromParcel(Parcel in) {
            return new Sales(in);
        }

        @Override
        public Sales[] newArray(int size) {
            return new Sales[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(date);
        parcel.writeString(time);
        parcel.writeString(customer);
        parcel.writeString(ReferenceNo);
        parcel.writeString(location);
        parcel.writeString(image);
        parcel.writeTypedList(items);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getReferenceNo() {
        return ReferenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        ReferenceNo = referenceNo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public static Creator<Sales> getCREATOR() {
        return CREATOR;
    }
}
