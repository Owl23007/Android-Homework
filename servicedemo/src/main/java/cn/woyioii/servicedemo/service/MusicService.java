package cn.woyioii.servicedemo.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

public class MusicService extends Service {
    private MediaPlayer mediaPlayer;

    public void init(String filePath) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(filePath);
                mediaPlayer.prepare();       // 准备，否则无法播放
            } catch (IOException e) {
                Log.e("MusicService", "Error setting data source or preparing MediaPlayer", e);
                throw new RuntimeException(e);
            }
            mediaPlayer.setLooping(true);
        }
    }

    public void start() {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(0);      // 播放器从头开始
            mediaPlayer.start();            // 播放
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();          // 停止播放
            mediaPlayer.release();     // 回收
            mediaPlayer = null;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("flag", "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("flag", "onStartCommand");
        if (intent != null) {
            String filePath = intent.getStringExtra("filePath");
            if (filePath != null) {
                init(filePath);
                start();    // 播放音乐
            } else {
                Log.e("MusicService", "No file path provided in the intent");
            }
        } else {
            Log.e("MusicService", "Intent is null");
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("flag", "onDestroy");
        stop();    // 结束播放
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("flag", "onBind");
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
