package brad.tw.myservicetest;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {
    private MediaPlayer mp;
    private Timer timer;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        timer = new Timer();
        timer.schedule(new BadTask(),0,1000);

        mp = MediaPlayer.create(this, R.raw.brad);
        Log.d("brad", "len: " + mp.getDuration());

        Intent it = new Intent("bradmp3");
        it.putExtra("len", mp.getDuration());
        sendBroadcast(it);

//        try {
//            mp.prepare();
//        } catch (IOException e) {
//            Log.d("brad", "e1");
//        }
    }

    private class MyTask extends TimerTask {
        @Override
        public void run() {
            if (mp != null && mp.isPlaying()){
                Intent it = new Intent("bradmp3");
                it.putExtra("now", mp.getCurrentPosition());
                sendBroadcast(it);
            }
        }
    }

    private class BadTask extends TimerTask {
        @Override
        public void run() {
            Log.d("brad", "OKOK");
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int skip = intent.getIntExtra("skip", -1);
        if (skip == -1) {
            mp.start();
            timer.schedule(new MyTask(), 0, 500);
        }else{
            mp.seekTo(skip);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mp != null){
            if (mp.isPlaying()){
                mp.stop();
            }
            mp.release();
            mp = null;
        }
        if (timer != null){
            timer.purge();
            timer.cancel();
            timer = null;
        }
    }
}
