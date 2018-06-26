package a123.vaidya.nihal.foodcrunchserver.Service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.widget.Toast;

import java.io.IOException;

import a123.vaidya.nihal.foodcrunchserver.R;

public class BackgroundSoundService extends Service {
    private static final String TAG = null;
    MediaPlayer mPlayer;

    public IBinder onBind(Intent arg0) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        String url  ="http://programmerguru.com/android-tutorial/wp-content/uploads/2013/04/hosannatelugu.mp3";
//        mPlayer = new MediaPlayer();
//
//        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//
//        try {
//
//            mPlayer.setDataSource(url);
//
//        } catch (IllegalArgumentException e) {
//
//            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
//
//        } catch (SecurityException e) {
//
//            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
//
//        } catch (IllegalStateException e) {
//
//            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
//
//        } catch (IOException e) {
//
//            e.printStackTrace();
//
//        }
//
//        try {
//
//            mPlayer.prepare();
//
//        } catch (IllegalStateException e) {
//
//            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
//
//        } catch (IOException e) {
//
//            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
//
//        }
//
//        mPlayer.start();
//    }

        String url = "http://programmerguru.com/android-tutorial/wp-content/uploads/2013/04/hosannatelugu.mp3";
        mPlayer = MediaPlayer.create(this, Uri.parse(url));
        mPlayer.setLooping(true); // Set looping
        mPlayer.setVolume(100, 100);

    }

    @SuppressLint("WrongConstant")
    public int onStartCommand(Intent intent, int flags, int startId) {
        mPlayer.start();
        return 1;
    }

    public void onStart(Intent intent, int startId) {
        // TO DO
    }

    public IBinder onUnBind(Intent arg0) {
        // TO DO Auto-generated method
        return null;
    }

    public void onStop() {

    }

    public void onPause() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlayer.stop();
        mPlayer.release();
    }
}