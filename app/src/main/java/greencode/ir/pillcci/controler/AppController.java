package greencode.ir.pillcci.controler;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;



import greencode.ir.pillcci.R;
import greencode.ir.pillcci.database.PillUsage;
import greencode.ir.pillcci.service.EventReciver;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class AppController extends MultiDexApplication {


    private static final String TAG = AppController.class.getSimpleName();

    public Bitmap cropped = null;
    private Thread.UncaughtExceptionHandler defaultUEH;
    private static Activity CurrentActivity = null;
    private static Context CurrentContext;



    @Override
    public void onCreate() {
        
        // The following line triggers the initialization of ACRA
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("iransansmobilefanum.ttf").setFontAttrId(R.attr.fontPath).build());
        MultiDex.install(this);
       /* defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        // setup handler for uncaught exception
        Thread.setDefaultUncaughtExceptionHandler(_unCaughtExceptionHandler);*/

    }
    private Thread.UncaughtExceptionHandler _unCaughtExceptionHandler =
            new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread thread, Throwable ex) {



                    AppDatabase database = AppDatabase.getInMemoryDatabase(getApplicationContext());
                    PillUsage pillUsage= database.pillUsageDao().getNearestUsage(System.currentTimeMillis());
                    if(pillUsage!=null) {
                        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                        Intent myIntent = new Intent(getApplicationContext(), EventReciver.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, myIntent, 0);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (pillUsage.getUsageTime() - System.currentTimeMillis()), pendingIntent);

                    }
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