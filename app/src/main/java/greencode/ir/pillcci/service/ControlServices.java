package greencode.ir.pillcci.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by alireza on 5/29/18.
 */

public class ControlServices extends Service
{



    private boolean initialized;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!initialized) {
            initializeService();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        restartService();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        restartService();
    }

    private void initializeService() {
      /*  initialized = true;
          int Therthy_SECONDS = 60000;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Context context = getApplicationContext();


            }
        },FIVE_SECONDS);*/

    }










    private void finishService() {
        initialized = false;
    }






    private void restartService() {


    }





}
