package org.apps4av.hubblewallpaper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zkhan on 3/13/17.
 */

public class StreamDatabaseHelper extends SQLiteOpenHelper {

    private static final String DBNAME = "hubble_stream.db";

    public StreamDatabaseHelper(Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create = "CREATE TABLE IF NOT EXISTS " + StreamContract.TABLE + "(" +
                StreamContract.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                StreamContract.TITLE + " TEXT, " +
                StreamContract.DATE + " DATETIME, " +
                StreamContract.URL + " TEXT, " +
                StreamContract.LINK + " TEXT, " +
                StreamContract.IMAGEBLOB + " BLOB, " +
                StreamContract.DESCRIPTION + " TEXT, " +
                "UNIQUE(" + StreamContract.DATE + "))";
        db.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

}