package org.apps4av.hubblewallpaper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;

import java.util.LinkedList;

/**
 * Created by zkhan on 1/1/18.
 */

public class StreamHelper {

    public static void insert(Context ctx, LinkedList<Item> items) {

        for (Item i : items) {

            String selection = StreamContract.DATE + "= ?";
            String date = Item.formatDate(i.getDate());
            if(null == date) {
                continue;
            }
            String selectionArg[] = new String[] {date};

            Cursor c = ctx.getContentResolver().query(StreamContract.CONTENT_URI, null, selection, selectionArg, StreamContract.DATE + " DESC");
            if (null != c) {
                if (c.moveToFirst()) {
                    // already in, do not insert
                    return;
                }
            }

            // New image, download
            Bitmap b = BitmapUtils.getBitmapFromURL(i.getUrl());

            if (null != b) {
                date = Item.formatDate(i.getDate());
                if(null != date) {
                    ContentValues values = new ContentValues();
                    values.put(StreamContract.TITLE, i.getTitle());
                    values.put(StreamContract.DATE, date);
                    values.put(StreamContract.URL, i.getUrl());
                    values.put(StreamContract.LINK, i.getLink());
                    values.put(StreamContract.IMAGEBLOB, BitmapUtils.getBytes(b));
                    values.put(StreamContract.DESCRIPTION, i.getDescription());
                    ctx.getContentResolver().insert(StreamContract.CONTENT_URI, values);
                }
            }
        }
    }

    /**
     * Get latest image
     * @param ctx
     * @return
     */
    public static Bitmap get(Context ctx) {
        Cursor c = ctx.getContentResolver().query(StreamContract.CONTENT_URI, null, null, null, StreamContract.DATE + " DESC");
        if (null != c) {
            if (c.moveToFirst()) {
                byte b[] = c.getBlob(c.getColumnIndex(StreamContract.IMAGEBLOB));
                if (b != null) {
                    return BitmapUtils.getImage(b);
                }
            }
        }
        return null;
    }
}
