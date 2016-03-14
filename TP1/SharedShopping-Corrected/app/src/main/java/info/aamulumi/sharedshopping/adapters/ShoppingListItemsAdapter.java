package info.aamulumi.sharedshopping.adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import info.aamulumi.sharedshopping.R;
import info.aamulumi.sharedshopping.models.ShoppingList;
import info.aamulumi.sharedshopping.models.ShoppingListItem;
import info.aamulumi.sharedshopping.network.APIConnection;

/**
 * > @ListAdapterMember
 * <p/>
 * Adapter for ShoppingListItem's ListView
 *
 * @author Florian "Aamu Lumi" Kauder
 */
public class ShoppingListItemsAdapter extends BaseAdapter{

    private ShoppingList sl;
    private LayoutInflater mInflater;

    public ShoppingListItemsAdapter(Context c, ShoppingList sl) {
        this.sl = sl;
        mInflater = (LayoutInflater)
                c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(ShoppingListItem item){
        this.sl.getItems().add(item);
        super.notifyDataSetChanged();
    }

    public void removeItem(ShoppingListItem item){
        this.sl.getItems().remove(item);
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return sl.getItems().size();
    }

    @Override
    public Object getItem(int position) {
        return sl.getItems().get(position);
    }

    @Override
    public long getItemId(int position) {
        return sl.getItems().get(position).hashCode();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView != null){
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = mInflater.inflate(R.layout.list_adapter_shopping_list_items, null);

            // Needed to activate context menu on each item
            convertView.setLongClickable(true);

            viewHolder = new ViewHolder();
            viewHolder.name = (TextView)  convertView.findViewById(R.id.listAdapterShoppingListItemsTextViewItem);
            viewHolder.price = (TextView) convertView.findViewById(R.id.listAdapterShoppingListItemsTextViewPrice);
            viewHolder.quantity = (TextView) convertView.findViewById(R.id.listAdapterShoppingListItemsTextViewQuantity);
            viewHolder.add = (Button)convertView.findViewById(R.id.listAdapterShoppingListItemsButtonAdd);
            viewHolder.remove = (Button)convertView.findViewById(R.id.listAdapterShoppingListItemsButtonRemove);

            convertView.setTag(viewHolder);
        }

        viewHolder.name.setText(sl.getItems().get(position).getName());
        viewHolder.price.setText(String.valueOf(sl.getItems().get(position).getPrice()));
        viewHolder.quantity.setText(String.valueOf(sl.getItems().get(position).getQuantity()));

        viewHolder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShoppingListItem current = sl.getItems().get(position);
                current.setQuantity(current.getQuantity() + 1);
                new EditItemTask().execute(position);
            }
        });

        viewHolder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShoppingListItem current = sl.getItems().get(position);
                current.setQuantity(current.getQuantity()-1);
                new EditItemTask().execute(position);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView name;
        TextView price;
        TextView quantity;
        Button add;
        Button remove;
    }

    private class EditItemTask extends AsyncTask<Integer,Integer,ShoppingList> {

        @Override
        protected ShoppingList doInBackground(Integer... params) {
            ShoppingListItem current = sl.getItems().get(params[0]);

            return APIConnection.editShoppingListItem(sl.getName(), current.getName(),
                    String.valueOf(current.getQuantity()), String.valueOf(current.getPrice()));
        }

        @Override
        protected void onPostExecute(ShoppingList shoppingList) {
            if (shoppingList != null)
                ShoppingListItemsAdapter.this.notifyDataSetChanged();
            else
                Log.i("Error", "Not found");
        }
    }
}



