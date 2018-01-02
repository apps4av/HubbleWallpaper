package org.apps4av.hubblewallpaper;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by zkhan on 1/1/18.
 */

class StreamCursorAdapter extends CursorAdapter {


    private LayoutInflater mCursorInflater;

    // Default constructor
    public StreamCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        mCursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

    }

    public void bindView(View view, Context context, Cursor cursor) {
        TextView textViewTitle = (TextView) view.findViewById(R.id.list_past_text_view_title);
        String title = cursor.getString(cursor.getColumnIndex(StreamContract.TITLE));
        textViewTitle.setText(title);

        TextView textViewDesc = (TextView) view.findViewById(R.id.list_past_text_view_description);
        String desc = cursor.getString(cursor.getColumnIndex(StreamContract.DESCRIPTION)).trim();
        textViewDesc.setText(desc);

        TextView textViewLink = (TextView) view.findViewById(R.id.list_past_text_view_link);
        String link = cursor.getString(cursor.getColumnIndex(StreamContract.LINK)).trim();
        textViewLink.setText(link);

        ImageView imageViewImage = (ImageView) view.findViewById(R.id.list_past_image_view_image);
        Bitmap b = BitmapUtils.getImage(cursor.getBlob(cursor.getColumnIndex(StreamContract.IMAGEBLOB)));
        imageViewImage.setImageBitmap(b);

        TextView textViewDate = (TextView) view.findViewById(R.id.list_past_text_view_date);
        String date = cursor.getString(cursor.getColumnIndex(StreamContract.DATE));
        textViewDate.setText(date);
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mCursorInflater.inflate(R.layout.list_past, parent, false);
    }
}
