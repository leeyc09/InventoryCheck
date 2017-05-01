package app.miji.com.inventorycheck.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {

    private String productCode;
    private String barcode;
    private String name;
    private String description;
    private String price;
    private String lowStock;
    private String notes;
    private String image;
    private Float quantity;
    private String unit;

    /**Empty Constructor for Firebase**/
    public Product(){
        // Default constructor required for calls to DataSnapshot.getValue(Product.class)
    }

    public Product(String productCode, String barcode, String name, String description, String price, String lowStock, String notes, String image, Float quantity,String unit) {
        this.productCode = productCode;
        this.barcode = barcode;
        this.name = name;
        this.description = description;
        this.price = price;
        this.lowStock = lowStock;
        this.notes = notes;
        this.image = image;
        this.quantity = quantity;
        this.unit = unit;
    }

    protected Product(Parcel in) {
        productCode = in.readString();
        barcode = in.readString();
        name = in.readString();
        description = in.readString();
        price = in.readString();
        lowStock = in.readString();
        notes = in.readString();
        image = in.readString();
        quantity = in.readFloat();
        unit = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLowStock() {
        return lowStock;
    }

    public void setLowStock(String lowStock) {
        this.lowStock = lowStock;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
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
        parcel.writeString(productCode);
        parcel.writeString(barcode);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(price);
        parcel.writeString(lowStock);
        parcel.writeString(notes);
        parcel.writeString(image);
        parcel.writeFloat(quantity);
        parcel.writeString(unit);
    }
}
