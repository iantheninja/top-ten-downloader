package com.iantheninja.top10downloader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
private TextView tvXml;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvXml = (TextView) findViewById(R.id.tvXml);
        DownloadData downloadData = new DownloadData();
        downloadData.execute("http://ax.itunes.apple.com/WebObjects/MZStore.woa/wpa/MRSS/newreleases/limit=10/rss.xml");
        //execute method is inbuilt into the async task and starts executing the task

        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private class DownloadData extends AsyncTask<String, Void, String> {
        private String fileContents;
        @Override
        protected String doInBackground(String... params) {
            fileContents = downloadXmlFile(params[0]);
            if(fileContents==null){
                Log.d("DownloadData", "Error Downloading");
            }
            return fileContents;
        }

        @Override
        protected void onPostExecute(String result) {//used when you need to enter info into the UI/layout
            super.onPostExecute(result);
            Log.d("DownloadData", "Result is: " + result);
            tvXml.setText(fileContents);
        }

        private String downloadXmlFile(String urlPath) {
            StringBuilder tempBuffer = new StringBuilder();
            try {
                URL url = new URL(urlPath);//stores the urlPath in url variable..tells java that it is a URL
                HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //attempts to open a HTTP connection
                int response = connection.getResponseCode(); //response to show what response code is returned
                Log.d("DownloadData", "Error "+response); // for programmer to see what the response is
                InputStream inputStream = connection.getInputStream(); // opens an input stream for the data in the url
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream); // reads the data in the url

                //the download operation occurs below
                int charRead;
                char[] inputBuffer = new char[500];
                while(true){
                    charRead = inputStreamReader.read(inputBuffer);
                    if(charRead <= 0){
                        break;
                    }
                    tempBuffer.append(String.copyValueOf(inputBuffer, 0, charRead));
                }

                return tempBuffer.toString();

            }catch (IOException e){
                Log.d("DownloadData", "IO exception reading data: "+e.getMessage());
            }catch (SecurityException e){
                Log.d("DownloadData", "Security exception " + e.getMessage());
            }
            return null;
        }
    }
}
