package info.aamulumi.sharedshopping;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Florian "Aamu Lumi" Kauder
 */
public class ShoppingListItem implements Parcelable{

    private String name;
    private int quantity;
    private double price;

    public ShoppingListItem(String name, int quantity, double price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // Parcelable

    protected ShoppingListItem(Parcel in) {
        name = in.readString();
        quantity = in.readInt();
        price = in.readDouble();
    }

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(quantity);
        dest.writeDouble(price);
    }

    public static final Parcelable.Creator<ShoppingListItem> CREATOR =
            new Parcelable.Creator<ShoppingListItem>() {
        @Override
        public ShoppingListItem createFromParcel(Parcel in) {
            return new ShoppingListItem(in);
        }

        @Override
        public ShoppingListItem[] newArray(int size) {
            return new ShoppingListItem[size];
        }
    };
}
