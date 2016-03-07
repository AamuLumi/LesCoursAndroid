package info.aamulumi.sharedshopping;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
    private ShoppingList sl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        sl = this.getIntent().getExtras().getParcelable(MainActivity.BUNDLE_PARAM_SHOPPING_LIST);
        if (this.getSupportActionBar() != null && sl != null)
            this.getSupportActionBar().setTitle(sl.getName());
    }
}
