package org.apps4av.hubblewallpaper;

import android.net.Uri;

/**
 * Created by zkhan on 3/13/17.
 */

public class StreamContract {

    public static final String AUTHORITY = "org.apps4av.provider.stream";

    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    public static final String BASE = "stream";

    public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, BASE);

    public static final String TABLE = "stream";

    public static final String ID = "_id";
    public static final String TITLE = "KeyTitle";
    public static final String DATE = "KeyDate";
    public static final String URL = "KeyUrl";
    public static final String LINK = "KeyLink";
    public static final String IMAGEBLOB = "KeyImageBlob";
    public static final String DESCRIPTION = "KeyDescription";
}
