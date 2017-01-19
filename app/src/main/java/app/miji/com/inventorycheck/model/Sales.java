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
}
