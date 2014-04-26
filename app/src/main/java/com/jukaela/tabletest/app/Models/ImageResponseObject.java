package com.jukaela.tabletest.app.Models;

import android.content.Context;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by jbarrow on 4/25/14.  Yay!
 */

 public class ImageResponseObject {

    public String fullCaption;
    public String title;
    public URL imageUrl;
    Context context;

    public ImageResponseObject(Context c) {
        this.context = c;
    }

    public void setFullCaption (String fc) {
        this.fullCaption = fc;
    }

    public void setTitle (String t) {
        this.title = t;
    }

    public void setImageUrl (String u) throws MalformedURLException {
        this.imageUrl = new URL(u);
    }

    public String getFullCaption() {
        return fullCaption;
    }

//
//    public String getTitle() {
//        return title;
//    }

    public URL getImageUrl() { return imageUrl; }

    public URL getThumbnailUrl() throws MalformedURLException {
        String stringUrl = imageUrl.toString();
        stringUrl = new StringBuilder(stringUrl).insert(stringUrl.length() - 4, "s").toString();

        return new URL(stringUrl);
    }
}
