package greencode.ir.pillcci.service;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.JobIntentService;

import java.util.Timer;
import java.util.TimerTask;

import greencode.ir.pillcci.retrofit.SyncController;
import greencode.ir.pillcci.utils.PersianCalculater;
import greencode.ir.pillcci.utils.Utility;

public class SyncService  extends JobIntentService {
    public Context context;
    static  final int Updateinterval=300000;
    private Timer timer=new Timer();
    public static final int JOB_ID = 1;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = this;
        super.onStartCommand(intent,flags,startId);

        return START_STICKY;
    }

    private void ReapetCheck(){
        try {
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {

                    SyncController syncController = new SyncController();
                    syncController.checkDataBaseForUpdate();
                }
            }, 0, Updateinterval);
        }catch (Exception ex){
             ex.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, SyncService.class, JOB_ID, work);
    }
    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        ReapetCheck();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {


        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1,
                restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);


        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartServicePendingIntent);

        Utility.appendLog("alarm service serve for :" + PersianCalculater.getHourseAndMin(SystemClock.elapsedRealtime()+1000));
/*        ReadAndWriteInfile2 r2 = new ReadAndWriteInfile2(context);
        r2.writeStringAsFile("finished");
        r2.writeStringAsFile(getBaseContext().toString());*/

           // startService(new Intent(getBaseContext(), SyncService.class));

        //     r2.writeStringAsFile("start again");
        super.onTaskRemoved(rootIntent);


    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


}
