package com.example.guesscelebrity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

    private URL url;
    private HttpURLConnection urlConnection = null;
    private InputStream in;

    @Override
    protected Bitmap doInBackground(String... urls) {
        try {
            url = new URL(urls[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            in = urlConnection.getInputStream();

            return BitmapFactory.decodeStream(in);

        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
