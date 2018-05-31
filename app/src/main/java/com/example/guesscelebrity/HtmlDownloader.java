package com.example.guesscelebrity;

import android.os.AsyncTask;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HtmlDownloader extends AsyncTask<String, Void, String> {
    private String html = "";
    private URL url;
    private HttpURLConnection urlConnection = null;

    @Override
    protected String doInBackground(String... urls) {
        try {
            url = new URL(urls[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);
            int data = reader.read();

            while(data != -1){
                char current = (char) data;
                html += current;
                data = reader.read();
            }

            return html;

        } catch (Exception e) {
            e.printStackTrace();
            return "failed";
        }
    }
}