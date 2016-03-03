package info.aamulumi.tomate;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * > @ListAdapterMember
 * <p/>
 * Adapter for FragmentMember's ListView
 *
 * @author Florian "Aamu Lumi" Kauder
 *         for the project @Label[i]
 */
public class ListUsersAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<User> users;
    private LayoutInflater mInflater;

    public ListUsersAdapter(Context c, ArrayList<User> al) {
        mContext = c;
        users = al;
        mInflater = (LayoutInflater)
                mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        Log.i("Test", " " + position);
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @SuppressWarnings("unchecked")
    public void updateDatas(ArrayList<User> al) {
        this.users = (ArrayList<User>) al.clone();
        this.notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        Log.i("Test", "Refresh Adapter");
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView != null){
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = mInflater.inflate(R.layout.list_view_users, null);

            viewHolder = new ViewHolder();
            viewHolder.name = (TextView)  convertView.findViewById(R.id.listViewUsersTextViewName);
            viewHolder.email = (TextView) convertView.findViewById(R.id.listViewUsersTextViewEmail);

            convertView.setTag(viewHolder);
        }

        viewHolder.name.setText(users.get(position).getLastName() + users.get(position).getFirstName());
        viewHolder.email.setText(users.get(position).getEmail());

        return convertView;
    }

    static class ViewHolder {
        TextView name;
        TextView email;
    }
}

