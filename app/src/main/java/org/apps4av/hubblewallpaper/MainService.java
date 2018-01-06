package org.apps4av.hubblewallpaper;

import android.app.Service;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.DisplayMetrics;

/**
 * Created by zkhan on 1/1/18.
 */

public class MainService extends Service {

    private WallpaperManager mWallpaperManager;

    private Bitmap mScreenBitmap;
    private Long mLastPerform;

    private static final long TIMEOUT_MS = 60 * 60 * 1000;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(0);
        mScreenBitmap.recycle();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mLastPerform = System.currentTimeMillis() - TIMEOUT_MS;

        mWallpaperManager
                = WallpaperManager.getInstance(getApplicationContext());

        // find screen size and create bitmap its size
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        double ws = (double)config.screenWidthDp * dm.density;
        double hs = ws * dm.heightPixels / dm.widthPixels;
        mScreenBitmap = Bitmap.createBitmap((int)ws, (int)hs, Bitmap.Config.RGB_565);

        // Download when screen turns on.
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Screen on. Download
                NetworkInfo networkInfo = ((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE)).
                        getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if(!networkInfo.isConnected()) {
                    // Only on WIFI connected
                    return;
                }
                mHandler.removeMessages(0);
                mHandler.sendEmptyMessage(0);
            }
        }, new IntentFilter(Intent.ACTION_SCREEN_ON));

        // start
        mHandler.sendEmptyMessage(0);

    }

    @Override
    public int onStartCommand (Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    /**
     * Does everything in background
     */
    private void performChange() {

        if((System.currentTimeMillis() - mLastPerform) < TIMEOUT_MS) {
            // Do not perform again until some time has passed.
            return;
        }

        StreamHelper.insert(getApplicationContext(), StreamParser.parse(getApplicationContext()));

        // query and set
        Bitmap b = StreamHelper.get(getApplicationContext());
        if(null == b) {
            // default when nothing
            b = BitmapUtils.getDefaultImage(getApplicationContext());
        }

        // clear
        mScreenBitmap.eraseColor(0);

        // find screen size
        double ws = mScreenBitmap.getWidth();
        double hs = mScreenBitmap.getHeight();
        // find image size
        double hi = b.getHeight();
        double wi = b.getWidth();

        // find ratio
        double rs = ws / hs;
        double ri = wi / hi;

        // scale to fit
        int w;
        int h;
        if(rs > ri) {
            w = (int)(wi * hs/hi);
            h = (int)hs;
        }
        else {
            w = (int)ws;
            h = (int)(hi * ws / wi);
        }

        // center
        int top = (int)(hs - h) / 2;
        int left = (int)(ws - w) / 2;

        Canvas c = new Canvas(mScreenBitmap);
        c.drawBitmap(b, new Rect(0, 0, (int)wi, (int)hi), new Rect(left, top, left + w, top + h), new Paint());

        // set
        try {
            mWallpaperManager.setBitmap(mScreenBitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mLastPerform = System.currentTimeMillis();
    }

    // A way to start refresh thread
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    performChange();
                }
            }.start();
        }
    };

}
