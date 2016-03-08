package GestureViewer.com.GestureViewer;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private LogStringAdaptor adaptor = null;
    private ArrayList<String> logarray = null;
    private ListView listView;
    private Handler mHandler = new Handler();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readDmesg();
                Snackbar.make(view, "Refleshing...", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });

        listView = (ListView) findViewById(R.id.list_log);
        logarray = new ArrayList<String>();
        adaptor = new LogStringAdaptor(this, R.id.txtLogString, logarray);
        listView.setAdapter(adaptor);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Object listItem = listView.getItemAtPosition(position);

                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                String text = ((String) ((TextView) view).getText());
                builder.setMessage(text);
                builder.show();
            }
        });

        readDmesg();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    protected void readDmesg() {
        boolean running = false;
        Process logprocess = null;
        BufferedReader reader = null;
        String logString;

        try {
            Log.e("a22460", "a22460 dmesg");
            logprocess = Runtime.getRuntime().exec("dmesg");
        } catch (IOException e) {
            Log.e("a22460", "a22460 dmesg error");
            e.printStackTrace();
        }

        try {
            reader = new BufferedReader(new InputStreamReader(
                    logprocess.getInputStream()), 512);
            Log.e("a22460", "a22460 dmesg reader");
            running = true;
        } catch (IllegalArgumentException e) {
            Log.e("a22460", "a22460 dmesg reader error");
            e.printStackTrace();
        }

        adaptor.clear();

        try {
            while (running) {
                logString = reader.readLine();
                Log.e("a22460", "a22460 dmesg logString" + logString);

                if (logString != null) {
                    //if (logString.contains("#v")) {
                    // Affinity
                    //logString = logString.substring(45);
                    // Clark
                    logString = logString.substring(45);
                        adaptor.add(logString);
                    //}
                } else {
                    // read out all dmesg,stop
                    Log.d("a22460", "a22460 Logview - stop");
                    running = false;

                    listView.setSelection(adaptor.getCount() - 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            running = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://motorola.com.com.motorola.a22460.GestureViewer/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://motorola.com.com.motorola.a22460.GestureViewer/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


    private int getLogColor(String type) {
        int color = Color.BLUE;

        if (type.equals("D")) {
            color = Color.rgb(0, 0, 200);
        } else if (type.equals("W")) {
            color = Color.rgb(128, 0, 0);
        } else if (type.equals("E")) {
            color = Color.rgb(255, 0, 0);
        } else if (type.equals("I")) {
            color = Color.rgb(0, 128, 0);
        }

        return color;
    }

    private class LogStringAdaptor extends ArrayAdapter<String> {
        private List<String> objects = null;

        public LogStringAdaptor(Context context, int textviewid, List<String> object) {
            super(context, textviewid, object);

            this.objects = object;
        }

        @Override
        public int getCount() {
            return ((null != objects) ? objects.size() : 0);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public String getItem(int position) {
            return ((null != objects) ? objects.get(position) : null);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;

            if (null == view) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = vi.inflate(R.layout.logitem, null);
            }

            String data = objects.get(position);

            if (null != data) {
                TextView textview = (TextView) view.findViewById(R.id.txtLogString);
                String type = data.substring(7, 8);
                String line = data.substring(0, 5) + data.substring(9);

                textview.setText(line);
                textview.setTextColor(getLogColor(type));
            }

            return view;
        }
    }
}
