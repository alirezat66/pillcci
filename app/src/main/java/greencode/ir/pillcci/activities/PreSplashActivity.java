package greencode.ir.pillcci.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.LoginEvent;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

import greencode.ir.pillcci.R;
import greencode.ir.pillcci.controler.AppDatabase;
import greencode.ir.pillcci.database.PhoneBook;
import greencode.ir.pillcci.database.PillUsage;
import greencode.ir.pillcci.onboarding.OnboardingActivity;
import greencode.ir.pillcci.utils.BaseActivity;
import greencode.ir.pillcci.utils.Constants;
import greencode.ir.pillcci.utils.DatabaseManager;
import greencode.ir.pillcci.utils.PreferencesData;
import greencode.ir.pillcci.utils.Utility;

/**
 * Created by alireza on 5/13/18.
 */

public class PreSplashActivity extends BaseActivity {

    private FirebaseAnalytics mFirebaseAnalytics;

    String[] mPermission = {
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA
    };
    public int REQUEST_CODE_PERMISSION = 48;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_splash);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                welcomeScreen();
            }
        }, 4000);






    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED&&
                    grantResults[2]==PackageManager.PERMISSION_GRANTED
                    ) {

            } else {
                checkPermission();
            }
        } else {
           checkPermission();
        }
    }
    private void welcomeScreen() {
        AppDatabase appDatabase = AppDatabase.getInMemoryDatabase(this);
       /* PillUsage pillUsage = appDatabase.pillUsageDao().getNearestUsed(System.currentTimeMillis());
        if(pillUsage!=null){
            startAlarmPillReminder(pillUsage);
        }*/
        Utility.reCalculateManager(this);
        if(appDatabase.phoneBookDao().listOfPhone().size()==0){
            appDatabase.phoneBookDao().insertPhone(new PhoneBook("اورژانس","","","115","",true));
            appDatabase.phoneBookDao().insertPhone(new PhoneBook("اطلاعات دارویی","","","1490","",true));
        }


        if(!PreferencesData.getBoolean(Constants.PREF_FIRST,this)){


            Intent intent = new Intent(this, OnboardingActivity.class);
            startActivity(intent);
            finish();
        }else if(PreferencesData.getBoolean(Constants.PREF_LOGIN,this)){
            List<PillUsage>expendedUsage = DatabaseManager.getAllExpendedPillUsage(this);
            DatabaseManager.updateToExpendedMode(this,expendedUsage);
            Answers.getInstance().logLogin(new LoginEvent());
            Bundle params = new Bundle();
            params.putString("phoneNumber", AppDatabase.getInMemoryDatabase(this).profileDao().getMyProfile().getPhone());
            mFirebaseAnalytics.logEvent("back_to_app", params);

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                if (ActivityCompat.checkSelfPermission(PreSplashActivity.this, mPermission[0])
                        != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(PreSplashActivity.this, mPermission[1])
                                != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(PreSplashActivity.this, mPermission[2])
                        != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(
                            mPermission, REQUEST_CODE_PERMISSION);

                    // If any permission aboe not allowed by user, this condition will execute every tim, else your else part will work
                } else {
                    welcomeScreen();

                }
            } catch (Exception e) {

                e.printStackTrace();
            }
        } else {
            welcomeScreen();
        }
    }


}
