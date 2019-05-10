package greencode.ir.pillcci.service;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import greencode.ir.pillcci.controler.AppDatabase;
import greencode.ir.pillcci.database.PillUsage;
import greencode.ir.pillcci.utils.DatabaseManager;
import greencode.ir.pillcci.utils.Utility;
import saman.zamani.persiandate.PersianDate;

/**
 * Created by alireza on 5/22/18.
 */


public class EventReciver extends BroadcastReceiver {
       static long a = 0;


    @SuppressLint("WrongConstant")
    @Override
    public void onReceive(final Context context, Intent intent) {
        Utility.appendLog("we are in on reciver  class and a = " +a);
        long b = System.currentTimeMillis()/10000;
        Utility.appendLog("we are in on reciver  class and b = " +b);

        Log.d("alireza",a+"");
        if(b>(a)) {
            a = System.currentTimeMillis()/10000;
            Utility.appendLog("we are in on reciver  class and a = " +a);
            // hameye estefade nashodeha az alan ta 1 daghighe dige
            //dasdasdsad
            final AppDatabase appDatabase = AppDatabase.getInMemoryDatabase(context);
            PersianDate persianDate = new PersianDate(System.currentTimeMillis());

            persianDate.setSecond(0);
            // hameye expended shodeha ro dar miarimo expend mikonim
            List<PillUsage> expendedUsage = DatabaseManager.getAllExpendedPillUsage(context);
            Utility.appendLog("extended size " + expendedUsage.size());
            Log.d("restart", "yes we get all expected in if");
            if(expendedUsage!=null){
                if(expendedUsage.size()>0){
                    DatabaseManager.updateToExpendedMode(context, expendedUsage);
                    Utility.appendLog("persian date triggered " + persianDate.getHour() + ":" + persianDate.getMinute() + ":" + persianDate.getSecond());

                }
            }
             List<PillUsage> allNotUsed = new ArrayList<>(appDatabase.pillUsageDao().getNearestUsedList(persianDate.getTime() + 59999));
            Utility.appendLog("all note used :" + allNotUsed.size());
            if (allNotUsed.size() > 0) {
                startAlarm(context);
                Utility.appendLog("played alarm manager :" + allNotUsed.size());

            } else {
                Utility.appendLog("we are in delay :" + allNotUsed.size());

                startAlarm(context);
            }
        }else {

        }
    }

    private void startAlarm(Context context) {

            Utility.appendLog("we are try to start alarm ");

            Intent alarmIntent = new Intent();
            alarmIntent.setClassName("greencode.ir.pillcci", "greencode.ir.pillcci.activities.ActivityAlarmManager");
            alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Utility.appendLog("we are try to start alarm ");

            context.startActivity(alarmIntent);
            Utility.appendLog("we start alarm ");


    }

}
