package org.apps4av.hubblewallpaper;

import android.app.Service;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.IBinder;
import android.util.DisplayMetrics;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zkhan on 1/1/18.
 */

public class MainService extends Service {

    private WallpaperManager mWallpaperManager;
    private Timer mTimer;

    // update time
    private static final int ONE_HOUR = 60 * 60 * 1000;

    private Bitmap mScreenBitmap;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public void onDestroy() {
        super.onDestroy();
        mScreenBitmap.recycle();

        mTimer.cancel();
    }

    @Override
    public int onStartCommand (Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        mWallpaperManager
                = WallpaperManager.getInstance(getApplicationContext());

        // find screen size and create bitmap its size
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        double ws = (double)config.screenWidthDp * dm.density;
        double hs = ws * dm.heightPixels / dm.widthPixels;
        mScreenBitmap = Bitmap.createBitmap((int)ws, (int)hs, Bitmap.Config.RGB_565);


        // start
        WallpaperTask task = new WallpaperTask();
        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(task, 0, ONE_HOUR);


        return START_STICKY;
    }


    private class WallpaperTask extends TimerTask {
        public void run() {
            // download / parse

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
        }
    }

}
