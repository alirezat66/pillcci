package greencode.ir.pillcci.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import greencode.ir.pillcci.R;
import greencode.ir.pillcci.controler.AppDatabase;
import greencode.ir.pillcci.database.PhoneBook;
import greencode.ir.pillcci.database.PillUsage;
import greencode.ir.pillcci.onboarding.OnboardingActivity;
import greencode.ir.pillcci.service.EventReciver;
import greencode.ir.pillcci.utils.BaseActivity;
import greencode.ir.pillcci.utils.Constants;
import greencode.ir.pillcci.utils.PreferencesData;

/**
 * Created by alireza on 5/13/18.
 */

public class PreSplashActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_splash);
        AppDatabase appDatabase = AppDatabase.getInMemoryDatabase(this);
       /* PillUsage pillUsage = appDatabase.pillUsageDao().getNearestUsed(System.currentTimeMillis());
        if(pillUsage!=null){
            startAlarmPillReminder(pillUsage);
        }*/

       if(appDatabase.phoneBookDao().listOfPhone().size()==0){
           appDatabase.phoneBookDao().insertPhone(new PhoneBook("اورژانس","","","115","",true));
           appDatabase.phoneBookDao().insertPhone(new PhoneBook("اطلاعات دارویی","","","1490","",true));
       }
        if(!PreferencesData.getBoolean(Constants.PREF_FIRST,this)){
            Intent intent = new Intent(this, OnboardingActivity.class);
            startActivity(intent);
            finish();
        }else if(PreferencesData.getBoolean(Constants.PREF_LOGIN,this)){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }



    }
    private void startAlarmPillReminder(PillUsage pillUsage) {
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent=new Intent(PreSplashActivity.this, EventReciver.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,0,myIntent,0);


        alarmManager.set(AlarmManager.RTC_WAKEUP,((System.currentTimeMillis())+(pillUsage.getUsageTime()-System.currentTimeMillis())) , pendingIntent);
    }
}
