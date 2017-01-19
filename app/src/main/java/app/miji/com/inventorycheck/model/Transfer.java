package app.miji.com.inventorycheck.model;

import android.os.Parcel;
import android.os.Parcelable;


public class Transfer implements Parcelable {
    String date;
    String transferID;
    String fromLocation;
    String toLocation;

    public Transfer(String date, String transferID, String fromLocation, String toLocation) {
        this.date = date;
        this.transferID = transferID;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
    }

    protected Transfer(Parcel in) {
        date = in.readString();
        transferID = in.readString();
        fromLocation = in.readString();
        toLocation = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(date);
        parcel.writeString(transferID);
        parcel.writeString(fromLocation);
        parcel.writeString(toLocation);
    }
}
