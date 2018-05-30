package greencode.ir.pillcci.service;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.WindowManager;

/**
 * Created by alireza on 5/22/18.
 */

public class EventReciver extends BroadcastReceiver {


    @SuppressLint("WrongConstant")
    @Override
    public void onReceive(Context context, Intent intent) {


        Intent alarmIntent = new Intent();
        alarmIntent.setClassName("greencode.ir.pillcci", "greencode.ir.pillcci.activities.ActivityAlarmManager");
        alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        alarmIntent.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED +
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD +
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON +
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        context.startActivity(alarmIntent);


        /*Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle("پیلچی");
        builder.setContentText("شما باید داروی خودتان را بخورید. برای دیدن جزیات کلیک کنید.");
        builder.setSmallIcon(R.drawable.ic_add_white_24dp);

        Intent notifyIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //to be able to launch your activity from the notification
        builder.setContentIntent(pendingIntent);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(uri);
        Notification notificationCompat = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(1, notificationCompat);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent=new Intent(context, EventReciver.class);
        PendingIntent newpendingIntent=PendingIntent.getBroadcast(context,0,myIntent,0);
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        AppDatabase database = AppDatabase.getInMemoryDatabase(context);

        PillUsage pillUsage = database.pillUsageDao().getNearestUsage(System.currentTimeMillis()+(45*60*6000));
        cal.setTimeInMillis(pillUsage.getUsageTime()-(45*60*6000));
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+(cal.getTimeInMillis()-System.currentTimeMillis()) , pendingIntent);

*/
        /* Intent intent1 = new Intent(context, NotificationManagerService.class);
        context.startService(intent1);*/
    }
}
