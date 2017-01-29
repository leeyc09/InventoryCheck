package app.miji.com.inventorycheck.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by isse on 29/01/2017.
 */

public class Location implements Parcelable {

    String name;

    public Location(String name) {
        this.name = name;
    }

    protected Location(Parcel in) {
        name = in.readString();
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
    }
}
