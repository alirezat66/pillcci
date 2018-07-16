package greencode.ir.pillcci.utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import greencode.ir.pillcci.R;
import greencode.ir.pillcci.controler.AppController;
import greencode.ir.pillcci.controler.AppDatabase;
import greencode.ir.pillcci.database.PillUsage;
import greencode.ir.pillcci.service.EventReciver;
import greencode.ir.pillcci.timepicker.TimePickerDialog;
import greencode.ir.pillcci.timepicker.data.Type;
import greencode.ir.pillcci.timepicker.listener.OnDateSetListener;
import saman.zamani.persiandate.PersianDate;

/**
 * Created by alireza on 5/5/18.
 */

public class Utility {
    public static String getDoubleStringValue(double value){

            int count = (int)value;

            if(value-count==0) {
                return count+"";
            }else {
                return value+"";
            }
    }
    public static void freeMemory() {
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }
    public static void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) AppController.getCurrentActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = AppController.getCurrentActivity().getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(AppController.getCurrentActivity());
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public static TimePickerDialog getTimeDialog(OnDateSetListener listener,int color){
       return new TimePickerDialog.Builder()
                .setHourText("")
                .setMinuteText("")
                .setWheelItemTextSize(16)
                .setCancelStringId("لغو")
                .setThemeColor(color)
                .setTitleStringId("یادآوری کن در ساعت")
                .setSureStringId("انتخاب")
                .setType(Type.HOURS_MINS)
               .setCurrentMillseconds(System.currentTimeMillis())

                .setCallBack(listener)
                .build();
    }
    public static Drawable getDrawable(Context context,int id){
        Drawable drawable;
        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            drawable = context.getResources().getDrawable(id, context.getTheme());
        } else {
            drawable = VectorDrawableCompat.create(context.getResources(), id, context.getTheme());
        }
        return drawable;
    }
    public static TimePickerDialog getTimeDialog(OnDateSetListener listener, int color, long timeusage){
       return new TimePickerDialog.Builder()
                .setHourText("")
                .setMinuteText("")
                .setWheelItemTextSize(16)
                .setCancelStringId("لغو")
                .setThemeColor(color)
                .setTitleStringId("یادآوری کن در ساعت")
                .setSureStringId("انتخاب")
                .setType(Type.HOURS_MINS)
               .setCurrentMillseconds(timeusage)

                .setCallBack(listener)
                .build();
    }

    public static Bitmap resizeBitmap(final Bitmap temp, final int size) {
        if (size > 0) {
            int width = temp.getWidth();
            int height = temp.getHeight();
            float ratioBitmap = (float) width / (float) height;
            int finalWidth = size;
            int finalHeight = size;
            if (ratioBitmap < 1) {
                finalWidth = (int) ((float) size * ratioBitmap);
            } else {
                finalHeight = (int) ((float) size / ratioBitmap);
            }
            return Bitmap.createScaledBitmap(temp, finalWidth, finalHeight, true);
        } else {
            return temp;
        }
    }
    public static void fadeOutAnimation(final boolean isFadeIn, Context context, final View view) {
        Animation fade;
        if(isFadeIn){
            fade = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        }else {
            fade = AnimationUtils.loadAnimation(context, R.anim.fade_out);
        }
        view.setAnimation(fade);

        fade.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                if(isFadeIn){
                    view.setVisibility(View.VISIBLE);

                }else {
                    view.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        view.startAnimation(fade);
    }


    public static PillUsage getNearestPill(Context context) {
        AppDatabase database  = AppDatabase.getInMemoryDatabase(context);
        return database.pillUsageDao().getNearestUsage(System.currentTimeMillis());
    }

    public static void reCalculateManager(Context context) {


        PillUsage pillUsage = getNearestPill(context);

        if(pillUsage!=null) {
            PersianDate date = new PersianDate(pillUsage.getUsageTime());
            date.setSecond(0);
            ReadAndWrite.appendLog("we set time for  pill usage " +ReadAndWrite.getHMS(date));

            startAlarmPillReminder(pillUsage, context);
        }else {
            // cancelesh mikonim
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent myIntent = new Intent(context, EventReciver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context, 1, myIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.cancel(pendingIntent);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static void startAlarmPillReminder(PillUsage pillUsage, Context context) {

        PersianDate date = new PersianDate(pillUsage.getUsageTime());
        date.setSecond(0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(context, EventReciver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent);
        long reminder = (date.getTime() - System.currentTimeMillis());
        reminder/=1000;
        ReadAndWrite.appendLog("we set alarm manager at  " + reminder +" second ");
        ReadAndWrite.appendLog("our usage is   " + date.getHour()+":"+date.getMinute()+":"+date.getSecond());

    }
    public static void setKeyboardFocus(final EditText primaryTextField) {
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                primaryTextField.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
                primaryTextField.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP , 0, 0, 0));
            }
        }, 100);
    }
    public static Typeface getRegularTypeFace(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "iransansmobilefanum.ttf");
    }
    public static String convertToPersianDigits(String value) {
        return value.replace("1", "١").replace("2", "٢").replace("3", "٣").replace("4", "۴").replace("5", "۵")
                .replace("6", "۶").replace("7", "٧").replace("8", "٨").replace("9", "٩").replace("0","٠");
    }

}
