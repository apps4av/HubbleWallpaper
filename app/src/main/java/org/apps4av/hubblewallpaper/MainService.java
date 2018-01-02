package org.apps4av.hubblewallpaper;

import android.app.Service;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;

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

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public void onDestroy() {
        super.onDestroy();

        mTimer.cancel();
    }

    @Override
    public int onStartCommand (Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        mWallpaperManager
                = WallpaperManager.getInstance(getApplicationContext());


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

            try {
                mWallpaperManager.setBitmap(b);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
