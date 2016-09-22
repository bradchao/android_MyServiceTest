package brad.tw.myservicetest;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

public class MyService extends Service {
    private MediaPlayer mp;

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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mp.start();

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
    }
}
