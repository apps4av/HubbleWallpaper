package org.apps4av.hubblewallpaper;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by zkhan on 3/10/17.
 */

public class StreamProvider extends ContentProvider {

    private StreamDatabaseHelper mDatabaseHelper;

    public static final int STREAMS = 300;
    public static final int STREAMS_ID = 301;

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/rv-streams";

    private static final UriMatcher mURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    static {
        mURIMatcher.addURI(StreamContract.AUTHORITY, StreamContract.BASE, STREAMS);
        mURIMatcher.addURI(StreamContract.AUTHORITY, StreamContract.BASE + "/#", STREAMS_ID);
    }


    @Override
    public String getType(Uri uri) {
        int uriType = mURIMatcher.match(uri);
        switch (uriType) {
            case STREAMS:
                return CONTENT_TYPE;
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        int uriType = mURIMatcher.match(uri);
        switch (uriType) {
            case STREAMS:
                // no filter
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }

        long id = mDatabaseHelper.getWritableDatabase().insert(
                StreamContract.TABLE,
                null,
                values);

        Uri itemUri = ContentUris.withAppendedId(uri, id);
        getContext().getContentResolver().notifyChange(uri, null);
        return itemUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(StreamContract.TABLE);

        int uriType = mURIMatcher.match(uri);
        switch (uriType) {
            case STREAMS:
                // no filter
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }


        try {
            Cursor cursor = queryBuilder.query(mDatabaseHelper.getReadableDatabase(),
                    projection, selection, selectionArgs, null, null, sortOrder);
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
        }
        catch (Exception e) {
            // Something wrong, missing or deleted database from download
            resetDatabase();
        }
        return null;
    }


    @Override
    public boolean onCreate() {
        mDatabaseHelper = new StreamDatabaseHelper(getContext());
        return true;
    }

    /**
     * Sync database on folder change, deleted database, new database, and other conditions
     */
    public void resetDatabase() {
        mDatabaseHelper.close();
        onCreate();
    }

}
