package greencode.ir.pillcci.service;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.WindowManager;

import greencode.ir.pillcci.utils.Utility;
import saman.zamani.persiandate.PersianDate;

/**
 * Created by alireza on 5/22/18.
 */

public class EventReciver extends BroadcastReceiver {


    @SuppressLint("WrongConstant")
    @Override
    public void onReceive(final Context context, Intent intent) {


        Log.d("hiLevel","we are in receiver and time is "+System.currentTimeMillis());

        PersianDate date = new PersianDate(System.currentTimeMillis());

        Intent alarmIntent = new Intent();
        alarmIntent.setClassName("greencode.ir.pillcci", "greencode.ir.pillcci.activities.ActivityAlarmManager");
        alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        alarmIntent.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED +
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD +
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON +
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        context.startActivity(alarmIntent);

        Utility.reCalculateManager(context);
/*        final android.os.Handler handler = new android.os.Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!ActivityAlarmManager.isInThisPage()){
                    Intent alarmIntent = new Intent();
                    alarmIntent.setClassName("greencode.ir.pillcci", "greencode.ir.pillcci.activities.ActivityAlarmManager");
                    alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    alarmIntent.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED +
                            WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD +
                            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON +
                            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
                    context.startActivity(alarmIntent);
                }
        }
        }, 5000);*/







    }
}
