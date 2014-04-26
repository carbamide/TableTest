package com.jukaela.tabletest.app.AsyncTasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.jukaela.tabletest.app.Misc.MemoryCache;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.lang.ref.WeakReference;
import java.net.URL;

/**
 * Created by jbarrow on 4/25/14.  Yay!
 */

 class ImageLoaderTask extends AsyncTask<URL, Void, Bitmap> {
    MemoryCache memoryCache = new MemoryCache();

    private final WeakReference<ImageView> imageViewReference;

    public ImageLoaderTask(ImageView imageView) {
        imageViewReference = new WeakReference<ImageView>(imageView);
    }

    @Override
    // Actual download method, run in the task thread
    protected Bitmap doInBackground(URL... params) {
        // params comes from the execute() call: params[0] is the url.
        Bitmap bitmap = memoryCache.get(params[0].toString());

        if (bitmap  != null) {
            Log.i("TableTest", "Using bitmap from memory cache");

            return bitmap;
        }
        else {
            Log.i("TableTest", "Attempting to download image.");

            return getSmallImage(params[0]);
        }
    }

    @Override
    // Once the image is downloaded, associates it to the imageView
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }

        ImageView imageView = imageViewReference.get();

        if (imageView != null) {
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    private Bitmap getSmallImage(URL imageUrl) {
        try {
            HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();

            connection.setDoInput(true);
            connection.connect();

            InputStream input = connection.getInputStream();

            Bitmap bitmap = BitmapFactory.decodeStream(input);

            memoryCache.put(imageUrl.toString(), bitmap);

            return bitmap;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
