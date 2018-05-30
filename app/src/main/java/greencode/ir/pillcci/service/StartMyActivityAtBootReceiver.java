package greencode.ir.pillcci.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import greencode.ir.pillcci.controler.AppDatabase;
import greencode.ir.pillcci.database.PillUsage;

/**
 * Created by alireza on 5/29/18.
 */

public class StartMyActivityAtBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            AppDatabase database = AppDatabase.getInMemoryDatabase(context);

            PillUsage pillUsage= database.pillUsageDao().getNearestUsage(System.currentTimeMillis());
            if(pillUsage!=null) {
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Intent myIntent = new Intent(context, EventReciver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0);
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (pillUsage.getUsageTime() - System.currentTimeMillis()), pendingIntent);

            }
        }
    }
}
