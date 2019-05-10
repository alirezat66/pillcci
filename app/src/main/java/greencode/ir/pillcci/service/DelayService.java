package greencode.ir.pillcci.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Timer;

import greencode.ir.pillcci.controler.AppDatabase;
import greencode.ir.pillcci.database.PillUsage;
import greencode.ir.pillcci.utils.PersianCalculater;
import greencode.ir.pillcci.utils.Utility;

public class DelayService extends Service {
    public Context context;
    static  final int Updateinterval=60000;
    int count = 0;
    private Timer timer=new Timer();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = this;
        super.onStartCommand(intent,flags,startId);
        AppDatabase database = AppDatabase.getInMemoryDatabase(this);
        Utility.appendLog("we are in delay service");
        List<PillUsage> usages = database.pillUsageDao().getAllNotUsed(System.currentTimeMillis());
        if(usages.size()>0){
            Utility.appendLog("we have usage in : " + PersianCalculater.getHourseAndMin(usages.get(0).getUsageTime()));
            Utility.reCalculateManager(this,usages.get(0).getUsageTime());
            Utility.appendLog("we set new alarm and closed");
        }
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Utility.appendLog("stop delay service");


    }




}
