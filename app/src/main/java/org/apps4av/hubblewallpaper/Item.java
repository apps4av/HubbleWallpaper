package org.apps4av.hubblewallpaper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zkhan on 1/1/18.
 */

public class Item {
    private String mTitle;
    private String mDate;
    private String mUrl;
    private String mLink;
    private String mDescription;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        mLink = link;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public static String formatDate(String webDate) {
        // convert hubble time to sqlite time
        DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy kk:mm:ss Z");
        try {
            Date result =  df.parse(webDate);
            df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
            return df.format(result);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

}
