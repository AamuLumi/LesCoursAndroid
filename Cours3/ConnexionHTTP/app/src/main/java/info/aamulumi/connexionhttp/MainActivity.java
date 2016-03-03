package info.aamulumi.connexionhttp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    private Button b;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b = (Button) findViewById(R.id.mainActivityButtonRefresh);
        tv = (TextView) findViewById(R.id.mainActivityTextViewData);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DataLoader().execute();
            }
        });
    }

    private class DataLoader extends AsyncTask<Void, Integer, String>{

        @Override
        protected String doInBackground(Void... params) {
            StringBuffer sb = new StringBuffer();
            HttpURLConnection connection = null;

            try {
                URL url = new URL("http://192.168.43.127:9980/users");
                connection = (HttpURLConnection) url.openConnection();

                Scanner s = new Scanner(new BufferedInputStream(connection.getInputStream()));
                while (s.hasNextLine()) {
                    sb.append(s.nextLine());
                }
            }
            catch (Exception e){
                Log.e("Network", "Error during accessing server");
                e.printStackTrace();
            }
            finally {
                if (connection != null)
                    connection.disconnect();
            }

            return sb.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            tv.setText(s);
        }
    }

}
