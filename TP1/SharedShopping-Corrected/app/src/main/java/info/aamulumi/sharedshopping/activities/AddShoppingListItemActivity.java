package info.aamulumi.sharedshopping.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import info.aamulumi.sharedshopping.R;
import info.aamulumi.sharedshopping.models.ShoppingList;
import info.aamulumi.sharedshopping.models.ShoppingListItem;
import info.aamulumi.sharedshopping.network.APIConnection;

public class AddShoppingListItemActivity extends AppCompatActivity {
    public static final String BUNDLE_PARAM_SHOPPING_LIST_NAME = "shoppingListName";
    public static final String BUNDLE_PARAM_SHOPPING_LIST_ITEM = "shoppingListItem";

    private EditText name, price, quantity;
    private Button bCreate;
    private String shoppingListName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shopping_list_item);

        setTitle(getString(R.string.addShoppingListItemActivityTitle));
        shoppingListName = this.getIntent().getExtras().getString(BUNDLE_PARAM_SHOPPING_LIST_NAME);

        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().setHomeButtonEnabled(true);
            this.getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        name = (EditText) findViewById(R.id.addShoppingListItemActivityEditTextName);
        price = (EditText) findViewById(R.id.addShoppingListItemActivityEditTextPrice);
        quantity = (EditText) findViewById(R.id.addShoppingListItemActivityEditTextQuantity);
        bCreate = (Button) findViewById(R.id.addShoppingListItemActivityButtonCreate);

        bCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText() != null && !name.getText().equals(""))
                    new AddItemTask(name.getText().toString(), quantity.getText().toString(),
                            price.getText().toString()).execute();
            }
        });
    }

    private class AddItemTask extends AsyncTask<Void, Integer, ShoppingList> {
        private String name, quantity, price;

        public AddItemTask(String name, String quantity, String price) {
            this.name = name;
            this.quantity = quantity;
            this.price = price;
        }

        @Override
        protected ShoppingList doInBackground(Void... params) {
            return APIConnection.createShoppingListItem(shoppingListName, name, quantity, price);
        }

        @Override
        protected void onPostExecute(ShoppingList shoppingList) {
            Intent returnIntent = new Intent();
            if (shoppingList != null) {
                for (ShoppingListItem sli : shoppingList.getItems()){
                    if (sli.getName().equals(name)){
                        returnIntent.putExtra(BUNDLE_PARAM_SHOPPING_LIST_ITEM, sli);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                }
            }
            else
            Toast.makeText(AddShoppingListItemActivity.this, "Erreur lors de la création de l'article",
                    Toast.LENGTH_LONG).show();
        }
    }
}

