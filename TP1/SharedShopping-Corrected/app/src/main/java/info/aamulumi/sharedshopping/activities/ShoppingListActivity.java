package info.aamulumi.sharedshopping.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import info.aamulumi.sharedshopping.R;
import info.aamulumi.sharedshopping.adapters.ShoppingListItemsAdapter;
import info.aamulumi.sharedshopping.models.ShoppingList;
import info.aamulumi.sharedshopping.models.ShoppingListItem;
import info.aamulumi.sharedshopping.network.APIConnection;

/**
 * @author Florian "Aamu Lumi" Kauder
 */

/*
 * TODO 1 - Create layout for the activity
 * TODO 2 - Create View and Adapter for the ShoppingList
 * TODO 3 - Create methods in APIConnection to update DB when updating the UI
 * TODO 4 - Link UI updates with DB updates
 * TODO 5 - Upgrade the app :)
 */

public class ShoppingListActivity extends AppCompatActivity {
    public static final String BUNDLE_PARAM_SHOPPING_LIST = "shoppingList";
    public static final int ADD_SHOPPING_LIST_ITEM = 1;

    private ShoppingList sl;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        sl = this.getIntent().getExtras().getParcelable(BUNDLE_PARAM_SHOPPING_LIST);
        if (this.getSupportActionBar() != null && sl != null)
            this.getSupportActionBar().setTitle(sl.getName());

        lv = (ListView) findViewById(R.id.shoppingListActivityListViewItems);
        lv.setAdapter(new ShoppingListItemsAdapter(this, sl));
        this.registerForContextMenu(lv);
    }

    // Options Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_shopping_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.activityShoppingListAdd:
                callAddShoppingListItemActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Context Menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_shopping_list_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.activityShoppingListRemove:
                new RemoveItemTask().execute(sl.getItems().get(info.position));
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void callAddShoppingListItemActivity() {
        Intent i = new Intent(ShoppingListActivity.this, AddShoppingListItemActivity.class);
        i.putExtra(AddShoppingListItemActivity.BUNDLE_PARAM_SHOPPING_LIST_NAME, sl.getName());
        startActivityForResult(i, ADD_SHOPPING_LIST_ITEM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK)
            switch(requestCode) {
                case ADD_SHOPPING_LIST_ITEM:
                    addShoppingListItem((ShoppingListItem)data.getExtras().getParcelable(
                            AddShoppingListItemActivity.BUNDLE_PARAM_SHOPPING_LIST_ITEM));
                    break;
                default:
                    super.onActivityResult(requestCode, resultCode, data);
            }
    }

    private void addShoppingListItem(ShoppingListItem sli) {
        ((ShoppingListItemsAdapter)lv.getAdapter()).addItem(sli);
    }

    private void removeShoppingListItem(ShoppingListItem sli) {
        ((ShoppingListItemsAdapter)lv.getAdapter()).removeItem(sli);
    }

    private class RemoveItemTask extends AsyncTask<ShoppingListItem, Integer, Boolean> {
        private ShoppingListItem sli;

        public RemoveItemTask(){
            sli = null;
        }

        @Override
        protected Boolean doInBackground(ShoppingListItem... params) {
            sli = params[0];
            return APIConnection.deleteShoppingListItem(sl.getName(), params[0].getName());
        }

        @Override
        protected void onPostExecute(Boolean isDeleted) {
            if (isDeleted){
                removeShoppingListItem(sli);
            }
            else {
                Toast.makeText(ShoppingListActivity.this, "Erreur lors de la suppression de l'article",
                        Toast.LENGTH_LONG);
            }
        }
    }
}
