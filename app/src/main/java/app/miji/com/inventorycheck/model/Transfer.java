package app.miji.com.inventorycheck.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;


public class Transfer implements Parcelable {
    String date;
    String time;
    String transferID;
    String fromLocation;
    String toLocation;
    List<Item> items;

    public Transfer(String date, String time, String transferID, String fromLocation, String toLocation, List<Item> items) {
        this.date = date;
        this.time = time;
        this.transferID = transferID;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.items = items;
    }

    protected Transfer(Parcel in) {
        date = in.readString();
        time = in.readString();
        transferID = in.readString();
        fromLocation = in.readString();
        toLocation = in.readString();
        items = in.createTypedArrayList(Item.CREATOR);
    }

    public static final Creator<Transfer> CREATOR = new Creator<Transfer>() {
        @Override
        public Transfer createFromParcel(Parcel in) {
            return new Transfer(in);
        }

        @Override
        public Transfer[] newArray(int size) {
            return new Transfer[size];
        }
    };

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

    public String getTransferID() {
        return transferID;
    }

    public void setTransferID(String transferID) {
        this.transferID = transferID;
    }

    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(date);
        parcel.writeString(time);
        parcel.writeString(transferID);
        parcel.writeString(fromLocation);
        parcel.writeString(toLocation);
        parcel.writeTypedList(items);
    }
}
