package info.aamulumi.sharedshopping;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author Florian "Aamu Lumi" Kauder
 */
public class MainActivity extends AppCompatActivity {

    public static String BUNDLE_PARAM_SHOPPING_LIST = "shoppingList";

    private Button buttonCreate, buttonFind;
    private GetShoppingListNameDialogFragment dialog;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonCreate = (Button) findViewById(R.id.mainActivityButtonCreate);
        buttonFind = (Button) findViewById(R.id.mainActivityButtonFind);

        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new GetShoppingListNameDialogFragment(new CreateListTask());
                dialog.show(getSupportFragmentManager(), "dialog");
            }
        });

        buttonFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new GetShoppingListNameDialogFragment(new GetListTask());
                dialog.show(getSupportFragmentManager(), "dialog");
            }
        });
    }

    /**
     * Load a ShoppingListActivity with a specific ShoppingList
     *
     * @param sl - ShoppingList to load
     */
    public void loadShoppingListActivity(ShoppingList sl) {
        Intent i = new Intent(MainActivity.this, ShoppingListActivity.class);
        i.putExtra(BUNDLE_PARAM_SHOPPING_LIST, sl);
        startActivity(i);
    }

    /**
     * Dialog with an EditText to get the ShoppingList name
     */
    public class GetShoppingListNameDialogFragment extends DialogFragment {
        private AsyncTask<String, Integer, ShoppingList> onPositive;

        public GetShoppingListNameDialogFragment(AsyncTask onPositive) {
            this.onPositive = onPositive;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = getActivity().getLayoutInflater();
            View v = inflater.inflate(R.layout.activity_main_dialog_get_name, null);
            final EditText e = (EditText) v.findViewById(R.id.activityMainDialogEditText);

            builder.setView(v)
                    .setPositiveButton(R.string.mainActivityDialogYes,
                            new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    })
                    .setNegativeButton(R.string.mainActivityDialogNo,
                            new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();

            // Cheat to avoid auto-dismiss when button is clicked
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
                    new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (e.getText().equals(null) || e.getText().toString().equals(""))
                        Toast.makeText(getContext(), "Veuillez rentrer un nom", Toast.LENGTH_LONG)
                                .show();
                    else {
                        dismiss();
                        onPositive.execute(e.getText().toString());
                    }
                }
            });

            return dialog;
        }
    }

    /**
     * Task to create a new list in DB
     */
    private class CreateListTask extends AsyncTask<String, Integer, ShoppingList> {
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setTitle(R.string.mainActivityTaskCreateProgressMessage);
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        @Override
        protected ShoppingList doInBackground(String... params) {
            return APIConnection.createShoppingList(params[0]);
        }

        @Override
        protected void onPostExecute(ShoppingList shoppingList) {
            progressDialog.dismiss();

            if (shoppingList != null) {
                Toast.makeText(getApplicationContext(), R.string.mainActivityTaskCreateSuccess,
                        Toast.LENGTH_SHORT).show();
                loadShoppingListActivity(shoppingList);
            } else {
                Toast.makeText(getApplicationContext(), R.string.mainActivityTaskCreateFailed,
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Task to load a list from DB
     */
    private class GetListTask extends AsyncTask<String, Integer, ShoppingList> {
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setTitle(R.string.mainActivityTaskFindProgressMessage);
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        @Override
        protected ShoppingList doInBackground(String... params) {
            return APIConnection.getShoppingList(params[0]);
        }

        @Override
        protected void onPostExecute(ShoppingList shoppingList) {
            progressDialog.dismiss();

            if (shoppingList != null) {
                loadShoppingListActivity(shoppingList);
            } else {
                Toast.makeText(getApplicationContext(), R.string.mainActivityTaskFindFailed,
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}
