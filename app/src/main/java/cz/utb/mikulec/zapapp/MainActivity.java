package cz.utb.mikulec.zapapp;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import cz.utb.mikulec.zapapp.sql.MyDbHelper;

public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener,
        HistoryFragment.OnListFragmentInteractionListener, SwipeRefreshLayout.OnRefreshListener {

    private TextView mTextMessage;
    private SwipeRefreshLayout swipeLayout;
    private int fragmentNumber;
    private Fragment fragment;
    private boolean networkVysledek = false;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            //LinearLayout mainContainer = (LinearLayout) findViewById(R.id.main_container);
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    changeFragment(0);
                    return true;
                case R.id.navigation_history:
                    changeFragment(1);
                    onRefresh();
                    return true;
                case R.id.navigation_notifications:
                    changeFragment(2);
                    return true;
            }
            return false;
        }
    };


    TextView tvVstup;

    @Override
    public void onRefresh() {

        HistoryFragment historyFragment = (HistoryFragment) fragment;
        if (historyFragment != null) {
            historyFragment.refreshData();
        }

        swipeLayout.setRefreshing(false);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {


    }

    @Override
    public void onListFragmentInteraction(Firma item) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeLayout = findViewById(R.id.swiperefresh);
        swipeLayout.setOnRefreshListener(this);

        tvVstup = findViewById(R.id.vstup);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        changeFragment(0);

    }

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    public void download(View v) {

        Log.i("download:", "start");
        new DownloadXml().execute(getApplicationContext());

    }


    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }


    private void changeFragment(int position) {


        if (position == 0) {
            fragment = new MainFragment();
        } else if (position == 1) {
            fragment = new HistoryFragment();
        } else {
            fragment = new AboutFragment();
        }

        fragmentNumber = position;
        setSwipeGestureState();
        getFragmentManager().beginTransaction().replace(
                R.id.main_container, fragment)
                .commit();
    }

    private void setSwipeGestureState() {
        if (fragmentNumber == 1) {
            swipeLayout.setEnabled(true);
        } else {
            swipeLayout.setEnabled(false);
        }
    }


    public void zobrazitFirmu() {



        //List<RssItem> list = new ArrayList<RssItem>();
        XmlPullParser parser = Xml.newPullParser();
        InputStream stream = null;
        int pocet_zaznamu = 0;
        String text = "";
        String t;

        Firma firma = new Firma();

        try {
            // auto-detect the encoding from the stream
            //stream = new URL(rssFeed).openConnection().getInputStream();


            File file = new File(getApplicationContext().getFilesDir(), "vystup.xml");
            //Stream stream =  Files.lines(Paths.get(file.getAbsolutePath()));


            //File fl = new File(filePath);
            FileInputStream fin = new FileInputStream(file);
            String ret = convertStreamToString(fin);
            //Make sure you close all streams.
            fin.close();
            //return ret;

            EditText et = findViewById(R.id.editText);
            et.setText(ret);


            File initialFile = new File(file.getAbsolutePath());
            stream = new FileInputStream(initialFile);
            parser.setInput(stream, null);

            int eventType = parser.getEventType();
            boolean done = false;
            //RssItem item = null;
            while (eventType != XmlPullParser.END_DOCUMENT && !done) {
                String name = null;
                name = parser.getName();

                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        //meny.clear();
                        //listItems.clear();
                        break;
                    case XmlPullParser.START_TAG:

                        Log.i("tag:", name);

                        if (name.equalsIgnoreCase("Obchodni_firma")) {
                            //firma.setKod(parser.getAttributeValue(0));
                        }


                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        Log.i("text:", parser.getText());
                        break;

                    case XmlPullParser.END_TAG:

                        //Log.i("endtag:", name);
                        if (name.equalsIgnoreCase("Pocet_zaznamu")) {
                            pocet_zaznamu = Integer.parseInt(text);
                        } else if (name.equalsIgnoreCase("Obchodni_firma")) {
                            firma.setNazev(text);
                        } else if (name.equalsIgnoreCase("Nazev_ulice")) {
                            firma.setUlice(text);
                        } else if (name.equalsIgnoreCase("Cislo_domovni")) {
                            firma.setCisloPopisne(Integer.parseInt(text));
                        } else if (name.equalsIgnoreCase("Nazev_obce")) {
                            firma.setMesto(text);
                        } else if (name.equalsIgnoreCase("PSC")) {
                            firma.setPSC(Integer.parseInt(text));
                        } else if (name.equalsIgnoreCase("ICO")) {
                            firma.setIC(Integer.parseInt(text));
                        }
                        break;
                }
                eventType = parser.next();


            }


            //adapter.notify();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (pocet_zaznamu == 0) {
            Toast.makeText(getApplicationContext(), "Neplatné zadání - nenalezeny žádné výsledky", Toast.LENGTH_SHORT).show();
        } else {


            ((LinearLayout) findViewById(R.id.layout_vysledek)).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.tv_nazev)).setText(firma.getNazev());
            ((TextView) findViewById(R.id.tv_ulice)).setText(firma.getUlice() + " " + firma.getCisloPopisne());
            ((TextView) findViewById(R.id.tv_mesto)).setText(firma.getPSC() + " " + firma.getMesto());


            MyDbHelper myDbHelper = new MyDbHelper(getApplicationContext());
            SQLiteDatabase db = myDbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("IC", firma.getIC());
            values.put("nazev", firma.getNazev());
            values.put("ulice", firma.getUlice());
            values.put("cp", firma.getCisloPopisne());
            values.put("mesto", firma.getMesto());

            db.insert("history", null, values);


        }


    }


    class DownloadXml extends AsyncTask<Context, String, String> {

        Context context;


        @Override
        protected String doInBackground(Context... contexts) {

            context = contexts[0];

            try {
                //URL url = new URL("http://www.cnb.cz/cs/financni_trhy/devizovy_trh/kurzy_devizoveho_trhu/denni_kurz.xml");

                tvVstup = findViewById(R.id.vstup);
                //Log.i("URL:", "http://wwwinfo.mfcr.cz/cgi-bin/ares/darv_std.cgi?max_pocet=1&ico=" + tvVstup.getText());

                //String urlAddress = "http://wwwinfo.mfcr.cz/cgi-bin/ares/darv_std.cgi?max_pocet=1&ico=" + tvVstup.getText();
                String urlAddress = "http://test2.mikulec.name/ares.php?ico=" + tvVstup.getText();
                URL url = new URL(urlAddress);


                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //set up some things on the connection
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);
                //and connect!
                urlConnection.connect();
                //set the path where we want to save the file
                //in this case, going to save it on the root directory of the
                //sd card.

                //File SDCardRoot = new File("/sdcard/" + "ZapApp/");
                File SDCardRoot = new File("");

                //create a new file, specifying the path, and the filename

                //which we want to save the file as.

                File file = new File(contexts[0].getFilesDir(), "vystup.xml");

                //this will be used to write the downloaded data into the file we created
                Log.w("MyClassName", file.toString());

                FileOutputStream fileOutput = new FileOutputStream(file);

                //this will be used in reading the data from the internet

                InputStream inputStream = urlConnection.getInputStream();

                Log.w("MyClassName", "This is a warning");


                //this is the total size of the file

                int totalSize = urlConnection.getContentLength();

                Log.w("Filesize: ", Integer.toString(totalSize));

                //variable to store total downloaded bytes

                int downloadedSize = 0;

                //create a buffer...

                byte[] buffer = new byte[1024];

                int bufferLength = 0; //used to store a temporary size of the buffer

                //now, read through the input buffer and write the contents to the file

                while ((bufferLength = inputStream.read(buffer)) > 0)

                {

                    //add the data in the buffer to the file in the file output stream (the file on the sd card

                    fileOutput.write(buffer, 0, bufferLength);

                    //add up the size so we know how much is downloaded

                    downloadedSize += bufferLength;

                    int progress = (int) (downloadedSize * 100 / totalSize);

                    //this is where you would do something to report the prgress, like this maybe

                    //updateProgress(downloadedSize, totalSize);

                }

                //close the output stream when done

                Log.w("Filesize: ", Long.toString(file.length()));
                fileOutput.close();

                networkVysledek = true;
            } catch (IOException e) {
                e.printStackTrace();
                networkVysledek = false;
            }

            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Activity activity = (Activity) context;
            //activity.zobrazitFirmu();
            if (networkVysledek) {
                zobrazitFirmu();
            } else {
                Toast.makeText(context, "Error. Probably network error. Check your internet connection", Toast.LENGTH_SHORT).show();
            }
        }


    }


}
