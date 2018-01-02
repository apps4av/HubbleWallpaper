package org.apps4av.hubblewallpaper;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by zkhan on 1/1/18.
 */

public class MainActivity extends Activity {

    private StreamCursorAdapter mAdapter;
    public static final int STREAM_LOADER_ID = 1478;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = new Intent(getApplicationContext(), MainService.class);
        getApplicationContext().startService(i);
        setContentView(R.layout.activity_main);

        mAdapter = new StreamCursorAdapter(this, null, 0);

        // Find list and bind to adapter
        ListView lv = (ListView) findViewById(R.id.activity_main_image_list);
        lv.setAdapter(mAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textViewLink = (TextView) view.findViewById(R.id.list_past_text_view_link);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(textViewLink.getText().toString()));
                startActivity(i);
            }
        });

        getLoaderManager().initLoader(STREAM_LOADER_ID,
                new Bundle(), mStreamLoader);
    }


    // Defines the asynchronous callback for the data loader
    private LoaderManager.LoaderCallbacks<Cursor> mStreamLoader =
            new LoaderManager.LoaderCallbacks<Cursor>() {
                // Create and return the actual cursor loader for the data
                @Override
                public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                    // Define the columns to retrieve
                    // Construct the loader
                    CursorLoader cursorLoader = new CursorLoader(MainActivity.this,
                            StreamContract.CONTENT_URI, // URI
                            null, // projection fields
                            null, // the selection criteria
                            null, // the selection args
                            StreamContract.DATE + " DESC " // the sort order
                    );
                    // Return the loader for use
                    return cursorLoader;
                }

                // When the system finishes retrieving the Cursor through the CursorLoader,
                // a call to the onLoadFinished() method takes place.
                @Override
                public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                    // The swapCursor() method assigns the new Cursor to the adapter
                    mAdapter.swapCursor(cursor);
                }

                // This method is triggered when the loader is being reset
                // and the loader data is no longer available. Called if the data
                // in the provider changes and the Cursor becomes stale.
                @Override
                public void onLoaderReset(Loader<Cursor> loader) {
                    // Clear the Cursor we were using with another call to the swapCursor()
                    mAdapter.swapCursor(null);
                }
            };
}
