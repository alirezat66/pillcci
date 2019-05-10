package greencode.ir.pillcci.controler;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.instabug.library.Instabug;
import com.instabug.library.InstabugColorTheme;
import com.instabug.library.invocation.InstabugInvocationEvent;

import java.util.Locale;

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
        new Instabug.Builder(this, "947553c18003495834b4dd5eb0cad203")
                .setInvocationEvents(InstabugInvocationEvent.SHAKE,InstabugInvocationEvent.NONE)
                .build();
        Instabug.setColorTheme(InstabugColorTheme.InstabugColorThemeLight);
        Instabug.setPrimaryColor(getResources().getColor(R.color.colorPrimary));
        Instabug.setDebugEnabled(true);
        Instabug.setLocale(new Locale("fa"));
        //Settings custom strings to replace instabug's strings

       /* InstabugCustomTextPlaceHolder placeHolder = new InstabugCustomTextPlaceHolder();
        placeHolder.set(InstabugCustomTextPlaceHolder.Key.SHAKE_HINT , "");
        placeHolder.set(InstabugCustomTextPlaceHolder.Key.SWIPE_HINT,"");
        placeHolder.set(InstabugCustomTextPlaceHolder.Key.INVALID_EMAIL_MESSAGE,"لطفا آدرس ایمیل ");
        placeHolder.set(InstabugCustomTextPlaceHolder.Key.INVALID_COMMENT_MESSAGE,"");
        placeHolder.set(InstabugCustomTextPlaceHolder.Key.INVOCATION_HEADER,"");
        placeHolder.set(InstabugCustomTextPlaceHolder.Key.START_CHATS,"");
        placeHolder.set(InstabugCustomTextPlaceHolder.Key.REPORT_BUG,"");
        placeHolder.set(InstabugCustomTextPlaceHolder.Key.REPORT_FEEDBACK,"");
        placeHolder.set(InstabugCustomTextPlaceHolder.Key.EMAIL_FIELD_HINT,"");
        placeHolder.set(InstabugCustomTextPlaceHolder.Key.COMMENT_FIELD_HINT_FOR_BUG_REPORT,"");
        placeHolder.set(InstabugCustomTextPlaceHolder.Key.COMMENT_FIELD_HINT_FOR_FEEDBACK,"");
        placeHolder.set(InstabugCustomTextPlaceHolder.Key.ADD_VOICE_MESSAGE,"");
        placeHolder.set(InstabugCustomTextPlaceHolder.Key.ADD_IMAGE_FROM_GALLERY,"");
        placeHolder.set(InstabugCustomTextPlaceHolder.Key.ADD_EXTRA_SCREENSHOT,"");
        placeHolder.set(InstabugCustomTextPlaceHolder.Key.CONVERSATIONS_LIST_TITLE,"");
        placeHolder.set(InstabugCustomTextPlaceHolder.Key.AUDIO_RECORDING_PERMISSION_DENIED,"");
        placeHolder.set(InstabugCustomTextPlaceHolder.Key.CONVERSATION_TEXT_FIELD_HINT,"");
        placeHolder.set(InstabugCustomTextPlaceHolder.Key.BUG_REPORT_HEADER,"");
        placeHolder.set(InstabugCustomTextPlaceHolder.Key.FEEDBACK_REPORT_HEADER,"");
        placeHolder.set(InstabugCustomTextPlaceHolder.Key.VOICE_MESSAGE_PRESS_AND_HOLD_TO_RECORD,"");
        placeHolder.set(InstabugCustomTextPlaceHolder.Key.VOICE_MESSAGE_RELEASE_TO_ATTACH,"");
        placeHolder.set(InstabugCustomTextPlaceHolder.Key.REPORT_SUCCESSFULLY_SENT,"");
        placeHolder.set(InstabugCustomTextPlaceHolder.Key.SUCCESS_DIALOG_HEADER,"");
        placeHolder.set(InstabugCustomTextPlaceHolder.Key.ADD_VIDEO,"");
        placeHolder.set(InstabugCustomTextPlaceHolder.Key.VIDEO_PLAYER_TITLE,"");
        placeHolder.set(InstabugCustomTextPlaceHolder.Key.FEATURES_REQUEST,"");
        placeHolder.set(InstabugCustomTextPlaceHolder.Key.FEATURES_REQUEST_ADD_FEATURE_TOAST,"");
        placeHolder.set(InstabugCustomTextPlaceHolder.Key.FEATURES_REQUEST_ADD_FEATURE_THANKS_MESSAGE,"");
        placeHolder.set(InstabugCustomTextPlaceHolder.Key.SURVEYS_WELCOME_SCREEN_TITLE,"");
        placeHolder.set(InstabugCustomTextPlaceHolder.Key.SURVEYS_WELCOME_SCREEN_SUBTITLE,"");
        placeHolder.set(InstabugCustomTextPlaceHolder.Key.SURVEYS_WELCOME_SCREEN_BUTTON,"");
        placeHolder.set(InstabugCustomTextPlaceHolder.Key.BETA_WELCOME_MESSAGE_WELCOME_STEP_TITLE,"");
        placeHolder.set(InstabugCustomTextPlaceHolder.Key.BETA_WELCOME_MESSAGE_WELCOME_STEP_CONTENT,"");
        placeHolder.set(InstabugCustomTextPlaceHolder.Key.BETA_WELCOME_MESSAGE_HOW_TO_REPORT_STEP_TITLE,"");

       Instabug.setCustomTextPlaceHolders(placeHolder);
*/
        //setting user attributes
        Instabug.setUserAttribute("USER_TYPE", "instabug user");

        sContext = getApplicationContext();

    }
    public static Context getContext() {
        return sContext;
    }
    private Thread.UncaughtExceptionHandler _unCaughtExceptionHandler =
            new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread thread, Throwable ex) {


                    Utility.reCalculateManager(getApplicationContext());

                }
            };
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }



    public static void setActivityContext(Activity activity, Context context) {
        CurrentActivity = activity;
        CurrentContext = context;
    }

    public static Activity getCurrentActivity() {
        return CurrentActivity;
    }


}