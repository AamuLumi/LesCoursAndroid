package info.aamulumi.sharedshopping.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * @author Florian "Aamu Lumi" Kauder
 */
public class ShoppingList implements Parcelable{
    private String name;
    private ArrayList<ShoppingListItem> items;

    public ShoppingList(String name, ArrayList<ShoppingListItem> items) {
        this.name = name;
        if (items != null)
            this.items = (ArrayList<ShoppingListItem>)items.clone();
        else
            this.items = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ShoppingListItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<ShoppingListItem> items) {
        this.items = items;
    }

    // Parcelable

    protected ShoppingList(Parcel in) {
        items = new ArrayList<>();
        name = in.readString();
        in.readTypedList(items, ShoppingListItem.CREATOR);
    }

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeTypedList(items);
    }

    public static final Parcelable.Creator<ShoppingList> CREATOR =
            new Parcelable.Creator<ShoppingList>() {
        @Override
        public ShoppingList createFromParcel(Parcel in) {
            return new ShoppingList(in);
        }

        @Override
        public ShoppingList[] newArray(int size) {
            return new ShoppingList[size];
        }
    };
}
