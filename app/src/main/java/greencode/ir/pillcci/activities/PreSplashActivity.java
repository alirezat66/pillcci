package greencode.ir.pillcci.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.LoginEvent;
import com.daimajia.androidanimations.library.Techniques;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.model.ConfigSplash;

import greencode.ir.pillcci.R;
import greencode.ir.pillcci.controler.AppDatabase;
import greencode.ir.pillcci.onboarding.OnboardingActivity;
import greencode.ir.pillcci.utils.Constants;
import greencode.ir.pillcci.utils.PreferencesData;
import greencode.ir.pillcci.utils.Utility;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by alireza on 5/13/18.
 */

public class PreSplashActivity extends AwesomeSplash {

    private FirebaseAnalytics mFirebaseAnalytics;

    String[] mPermission = {
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA
    };
    public int REQUEST_CODE_PERMISSION = 48;
    /*@Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_splash);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);







    }
*/
    @Override
    public void initSplash(ConfigSplash configSplash) {


        configSplash.setBackgroundColor(R.color.lightPing);
        configSplash.setAnimCircularRevealDuration(200);
        //any color you want form colors.xml
        //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default
        //Customize Logo
     //   configSplash.setTitleTextSize(20f); //float value
        configSplash.setTitleSplash("نسخه " + Utility.getVersionName());
        configSplash.setTitleTextSize(20f);
        configSplash.setTitleTextColor(R.color.white);

        configSplash.setLogoSplash(R.drawable.ic_splash); //or any other drawable
        configSplash.setAnimLogoSplashDuration(3000); //int ms
        configSplash.setAnimLogoSplashTechnique(Techniques.FadeIn); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)


        /*//Customize Path
        configSplash.setPathSplash(Constants.DROID_LOGO); //set path String
        configSplash.setOriginalHeight(400); //in relation to your svg (path) resource
        configSplash.setOriginalWidth(400); //in relation to your svg (path) resource
        configSplash.setAnimPathStrokeDrawingDuration(3000);
        configSplash.setPathSplashStrokeSize(3); //I advise value be <5
        configSplash.setPathSplashStrokeColor(R.color.accent); //any color you want form colors.xml
        configSplash.setAnimPathFillingDuration(3000);
        configSplash.setPathSplashFillColor(R.color.Wheat); //path object filling color
*/

        //Customize Title


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);



        //provide string to your font located in assets/fonts/

    }

    @Override
    public void animationsFinished() {
        welcomeScreen();
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
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



        if(!PreferencesData.getBoolean(Constants.PREF_FIRST,PreSplashActivity.this)){


            Intent intent = new Intent(this, OnboardingActivity.class);
            startActivity(intent);
            finish();
        }else if(PreferencesData.getBoolean(Constants.PREF_LOGIN,PreSplashActivity.this)){
            Utility.reCalculateManager(this);
            Answers.getInstance().logLogin(new LoginEvent());
            Bundle params = new Bundle();
            params.putString("phoneNumber", AppDatabase.getInMemoryDatabase(this).profileDao().getMyProfile().getPhone()+"");
            mFirebaseAnalytics.logEvent("back_to_app", params);
            Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(PreSplashActivity.this, android.R.anim.fade_in, android.R.anim.fade_out).toBundle();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent,bundle);
            finish();
        }else if(PreferencesData.getBoolean(Constants.PREF_Guess,PreSplashActivity.this)){
            Utility.reCalculateManager(this);
            Answers.getInstance().logLogin(new LoginEvent());
            Bundle params = new Bundle();
            params.putString("phoneNumber", "geuest");
            mFirebaseAnalytics.logEvent("back_to_app", params);

            Intent intent = new Intent(this, MainActivity.class);
            Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(PreSplashActivity.this, android.R.anim.fade_in, android.R.anim.fade_out).toBundle();

            startActivity(intent,bundle);
         //   overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            finish();
        }
        else {
            Intent intent = new Intent(this, LoginActivity.class);
         //   overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(PreSplashActivity.this, android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
            startActivity(intent,bundle);
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
