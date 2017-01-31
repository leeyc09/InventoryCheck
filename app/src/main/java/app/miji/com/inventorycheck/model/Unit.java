package app.miji.com.inventorycheck.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by isse on 31/01/2017.
 */

public class Unit implements Parcelable {
    String name;

    public Unit() {
        // Default constructor required for calls to DataSnapshot.getValue(Unit.class)
    }

    public Unit(String name) {
        this.name = name;
    }

    protected Unit(Parcel in) {
        name = in.readString();
    }

    public static final Creator<Unit> CREATOR = new Creator<Unit>() {
        @Override
        public Unit createFromParcel(Parcel in) {
            return new Unit(in);
        }

        @Override
        public Unit[] newArray(int size) {
            return new Unit[size];
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
