package greencode.ir.pillcci.controler;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;

import greencode.ir.pillcci.R;
import greencode.ir.pillcci.utils.Utility;
import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class AppController extends MultiDexApplication {

    private static final String TAG = AppController.class.getSimpleName();

    public Bitmap cropped = null;
    private Thread.UncaughtExceptionHandler defaultUEH;
    private static Activity CurrentActivity = null;
    private static Context CurrentContext;
    private static Context sContext;



    @Override
    public void onCreate() {
        
        // The following line triggers the initialization of ACRA
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("iransansmobilefanum.ttf").setFontAttrId(R.attr.fontPath).build());
        MultiDex.install(this);
        Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics()).debuggable(true)
                .build();

        final Fabric fabric2 = new Fabric.Builder(this)
                .kits(new Answers())
                .debuggable(true)
                .build();
        Fabric.with(fabric);
        Fabric.with(fabric2);
        sContext = getApplicationContext();

       /* defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        // setup handler for uncaught exception
        Thread.setDefaultUncaughtExceptionHandler(_unCaughtExceptionHandler);*/

    }
    public static Context getContext() {
        return sContext;
    }
    private Thread.UncaughtExceptionHandler _unCaughtExceptionHandler =
            new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread thread, Throwable ex) {


                    Utility.reCalculateManager(getApplicationContext());
                   /* // here I do logging of exception to a db
                    Log.e("error",ex.toString());
                    PendingIntent myActivity = PendingIntent.getActivity(getApplicationContext(),
                            192837, new Intent(getApplicationContext(), SplashActivity.class),
                            PendingIntent.FLAG_ONE_SHOT);
                    AlarmManager alarmManager;
                    alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                            10000, myActivity);
                    System.exit(2);

                    // re-throw critical exception further to the os (important)
                    defaultUEH.uncaughtException(thread, ex);*/
                }
            };
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
    public static void setCurrentContext(Context mCurrentContext) {
        AppController.CurrentContext = mCurrentContext;


    }

    public static void setActivityContext(Activity activity, Context context) {
        CurrentActivity = activity;
        CurrentContext = context;
    }

    public static Activity getCurrentActivity() {
        return CurrentActivity;
    }

    public static Context getCurrentContext() {
        return CurrentContext;
    }
}