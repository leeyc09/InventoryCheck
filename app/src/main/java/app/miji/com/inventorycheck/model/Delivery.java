package app.miji.com.inventorycheck.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Delivery implements Parcelable {
    String date;
    String time;
    String location;
    String deliveryMan;
    String referenceNo;
    String image;
    List<Item> items;

    /*Required Empty constructor for Firebase*/
    public Delivery() {
        // Default constructor required for calls to DataSnapshot.getValue(Delivery.class)
    }

    public Delivery(String date, String time, String location, String deliveryMan, String referenceNo, String image, List<Item> items) {
        this.date = date;
        this.time = time;
        this.location = location;
        this.deliveryMan = deliveryMan;
        this.referenceNo = referenceNo;
        this.image = image;
        this.items = items;
    }

    protected Delivery(Parcel in) {
        date = in.readString();
        time = in.readString();
        location = in.readString();
        deliveryMan = in.readString();
        referenceNo = in.readString();
        image = in.readString();
        items = in.createTypedArrayList(Item.CREATOR);
    }

    public static final Creator<Delivery> CREATOR = new Creator<Delivery>() {
        @Override
        public Delivery createFromParcel(Parcel in) {
            return new Delivery(in);
        }

        @Override
        public Delivery[] newArray(int size) {
            return new Delivery[size];
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDeliveryMan() {
        return deliveryMan;
    }

    public void setDeliveryMan(String deliveryMan) {
        this.deliveryMan = deliveryMan;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(date);
        parcel.writeString(time);
        parcel.writeString(location);
        parcel.writeString(deliveryMan);
        parcel.writeString(referenceNo);
        parcel.writeString(image);
        parcel.writeTypedList(items);
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("date", date);
        result.put("time", time);
        result.put("location", location);
        result.put("deliveryMan", deliveryMan);
        result.put("referenceNo", referenceNo);
        result.put("image", image);
        result.put("items", items);

        return result;
    }
}
