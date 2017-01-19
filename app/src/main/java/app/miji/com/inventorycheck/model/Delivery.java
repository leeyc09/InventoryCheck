package app.miji.com.inventorycheck.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Delivery implements Parcelable {
    String date;
    String location;
    String deliveryMan;
    String referenceNo;
    String image;
    List<Item> items;

    public Delivery(String date, String location, String deliveryMan, String referenceNo, String image, List<Item> items) {
        this.date = date;
        this.location = location;
        this.deliveryMan = deliveryMan;
        this.referenceNo = referenceNo;
        this.image = image;
        this.items = items;
    }

    protected Delivery(Parcel in) {
        date = in.readString();
        location = in.readString();
        deliveryMan = in.readString();
        referenceNo = in.readString();
        image = in.readString();
        items = in.createTypedArrayList(Item.CREATOR);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(date);
        parcel.writeString(location);
        parcel.writeString(deliveryMan);
        parcel.writeString(referenceNo);
        parcel.writeString(image);
        parcel.writeTypedList(items);
    }


}
