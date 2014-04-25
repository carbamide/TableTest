package com.jukaela.tabletest.app.Models;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by jbarrow on 4/25/14.
 */

 public class ImageResponseObject {

    public String fullCaption;
    public String title;
    public URL imageUrl;

    public void setFullCaption (String fc) {
        fullCaption = fc;
    }

    public void setTitle (String t) {
        title = t;
    }

    public void setImageUrl (String u) throws MalformedURLException {
        URL url = new URL(u);

        imageUrl = url;
    }

    public String getFullCaption() {
        return fullCaption;
    }

    public String getTitle() {
        return title;
    }

    public URL getImageUrl() { return imageUrl; }
}
