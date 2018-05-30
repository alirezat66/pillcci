package greencode.ir.pillcci.service;

/**
 * Created by alireza on 5/29/18.
 */

public class ControlServices
{



 /*   private AlarmManager alarmManager;
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
        initialized = true;
          int FIVE_SECONDS = 300000;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Context context = getApplicationContext();
                AppDatabase database = AppDatabase.getInMemoryDatabase(context);
                PillUsage pillUsage= database.pillUsageDao().getNearestUsage(System.currentTimeMillis());
                Intent myIntent=new Intent(getApplicationContext(), EventReciver.class);
                PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),0,myIntent,0);
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+(pillUsage.getUsageTime()-System.currentTimeMillis()) , pendingIntent);
            }
        },FIVE_SECONDS);

    }










    private void finishService() {
        initialized = false;
    }






    private void restartService() {

        Intent intent = new Intent(this, ControlServices.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);
        PersianDate date = new PersianDate();
        long now = date.getTime();
        getAlarmManager().set(AlarmManager.RTC_WAKEUP, now + 5000, pendingIntent);

    }


    private AlarmManager getAlarmManager() {
        return alarmManager != null ? alarmManager : (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    }
*/

}
