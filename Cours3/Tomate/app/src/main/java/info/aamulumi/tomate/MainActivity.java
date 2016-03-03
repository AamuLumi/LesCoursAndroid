package info.aamulumi.tomate;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView lv;
    private Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView) findViewById(R.id.mainAcvitityListViewUsers);
        b = (Button) findViewById(R.id.mainActivityButtonRefresh);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UsersLodaer().execute();
            }
        });
    }

    public void prepareListView(ArrayList<User> al){
        lv.setAdapter(new ListUsersAdapter(this.getApplicationContext(), al));
    }

    private class UsersLodaer extends AsyncTask<Void, Integer, ArrayList>{

        @Override
        protected ArrayList doInBackground(Void... params) {
            return APIConnection.getUsers();
        }

        @Override
        protected void onPostExecute(ArrayList arrayList) {
            prepareListView(arrayList);
        }
    }

}
