package greencode.ir.pillcci.utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import androidx.annotation.RequiresApi;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

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

    public static KProgressHUD makeWaiting(String title , String desc, Context context){
        KProgressHUD kProgressHUD=  KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(title)
                .setDetailsLabel(desc)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.8f)
                .setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
        return kProgressHUD;
    }
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
        view.getId();
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public static TimePickerDialog getTimeDialog(OnDateSetListener listener,int color){
       return new TimePickerDialog.Builder()
                .setHourText("")
                .setMinuteText("")
                .setWheelItemTextSize(16)
                .setCancelStringId("انصراف")
                .setThemeColor(color)
                .setTitleStringId("یادآوری کن در ساعت")
                .setSureStringId("انتخاب")
                .setType(Type.HOURS_MINS)
               .setCurrentMillseconds(System.currentTimeMillis())

                .setCallBack(listener)
                .build();
    }

    public static TimePickerDialog getTimeDialogUsage(OnDateSetListener listener,int color){
       return new TimePickerDialog.Builder()
                .setHourText("")
                .setMinuteText("")
                .setWheelItemTextSize(16)
                .setCancelStringId("انصراف")
                .setThemeColor(color)
                .setTitleStringId("")
                .setSureStringId("تایید")
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
                .setCancelStringId("انصراف")
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
        int snoozCount = Utility.getRemindCount();
        int snoozDistance = Utility.getDistance();
        long exteraTime = snoozCount * snoozDistance * 60 * 1000   ;

        long currentTime = System.currentTimeMillis();
        PersianDate date = new PersianDate(currentTime);
        date.setSecond(0);

        List<PillUsage> notExpireNotAlarmed = database.pillUsageDao()
                .getAllNotAlarmNotexpirePillUsage(date.getTime(),exteraTime);
        Utility.appendLog("تعداد داروهایی که باید اسنوز بشوند برابر اند با" + notExpireNotAlarmed.size() );

        List<PillUsage> shouldUpdateUsage = new ArrayList<>();
        for(PillUsage usage : notExpireNotAlarmed){
            Utility.appendLog("داروی اول که باید اسنوز شود " + usage.getPillName() );
            Utility.appendLog("زمان واقعی داروی اول " + PersianCalculater.getYearMonthAndDay(usage.getSetedTime())+" -- " + PersianCalculater.getHourseAndMin(usage.getSetedTime())) ;
            Utility.appendLog("زمان فعلی  " + PersianCalculater.getYearMonthAndDay(date.getTime())+" -- " + PersianCalculater.getHourseAndMin(date.getTime())) ;

            // mikhaym bebinim timi ke bayad toosh zang bokhore keye
            for(int i=1;i<=snoozCount;i++){
                long extra = i * snoozDistance * 60 * 1000;
                Log.d("timeextera ", "extra : "+extra+usage.getUsageTime() +" , current"+ currentTime );
                if(extra+usage.getSetedTime()> currentTime){

                    PersianDate persianDate = new PersianDate(usage.getSetedTime());
                    PersianDate persianDateusage = new PersianDate(usage.getUsageTime());


                    Log.e("setedTime:" ,"usageSetedTime : " + usage.getSetedTime() +"=== "+persianDate.getHour()+":"+persianDate.getMinute());
                    Log.e("setedTime:" ,"usageUsageTime : " + usage.getUsageTime() +"=== "+persianDateusage.getHour()+":"+persianDateusage.getMinute());
                    int min = persianDate.getMinute();
                    Log.e("setedTime:" ,"min : "+ min);

                    min = min +(i*snoozDistance);
                    Log.e("setedTime:" ,"newMin : "+ min);
                    if(min>60){
                        persianDate.setMinute(min-60);
                        int hour = persianDate.getHour();
                        if(hour>23){
                            persianDate.addDay(1);
                            persianDate.setHour(0);
                        }else {
                            persianDate.setHour(hour+1);
                        }
                    }else {
                        persianDate.setMinute(min);
                    }
                    persianDate.setSecond(0);
                    usage.setUsageTime(persianDate.getTime());
                    usage.setSnoozCount(i);
                    usage.setIsSync(0);
                    Log.e("setedtime:","extra min:"+(i*snoozCount)+" , i="+i+", snoozdistance = "+snoozDistance+" new usage time = " + PersianCalculater.getHourseAndMin(persianDate.getTime()));
                    shouldUpdateUsage.add(usage);
                    break;
                }
            }
        }
        if(shouldUpdateUsage.size()>0){
            database.pillUsageDao().update(shouldUpdateUsage);
        }
        PersianDate current = new PersianDate(System.currentTimeMillis());
        current.setSecond(0);
        return database.pillUsageDao().getNearestUsage(current.getTime());
    }
    public static void reCalculateManager(Context context,long timeStamp) {
        PersianDate date = new PersianDate(timeStamp);
        date.setSecond(0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(context, EventReciver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0);
        Log.d("recalculate","current and date is not same");
        Utility.appendLog("recalculate  " +" and time "+ PersianCalculater.getHourseAndMin(date.getTime()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent);
        }else if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT){
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,date.getTime(),pendingIntent);
        }else {
            alarmManager.setExact(AlarmManager.RTC,date.getTime(),pendingIntent);

        }

    }
    public static void reCalculateManager(Context context) {
        List<PillUsage> expendedUsage = DatabaseManager.getAllExpendedPillUsage(context);
        Log.d("restart","yes we get all expected in if");
        DatabaseManager.updateToExpendedMode(context,expendedUsage);
        Utility.appendLog("we are in recalculate manager and we should get nearestPill");
        PillUsage pillUsage = getNearestPill(context);
        if(pillUsage!=null) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent myIntent = new Intent(context, EventReciver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context, 1, myIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pendingIntent);
            PersianDate date = new PersianDate(pillUsage.getUsageTime());
            Log.d("setedTime",PersianCalculater.getHourseAndMin(pillUsage.getUsageTime()));
            date.setSecond(0);
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
        PersianDate current = new PersianDate(System.currentTimeMillis());

        Utility.appendLog("داروی مربوط به  " + pillUsage.getPillName()+" برای تاریخ " +
                PersianCalculater.getYearMonthAndDay(pillUsage.getSetedTime()) +" ساعت "
                + PersianCalculater.getHourseAndMin(pillUsage.getSetedTime())+" برای ساعت "
                + PersianCalculater.getHourseAndMin(pillUsage.getUsageTime()) +" تنظیم شد.");

        if(current.getMinute()==date.getMinute() && current.getShYear()==date.getShYear() &&
                current.getShMonth()==date.getShMonth() && current.getShDay() == date.getShDay() &&
                current.getHour()==date.getHour()){
            Log.d("recalculate","current and date is same");
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+2000, pendingIntent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+2000, pendingIntent);
            }else if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT){
                alarmManager.setExact(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+2000,pendingIntent);
            }else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+2000,pendingIntent);
            }
        }else {
            Log.d("recalculate","current and date is not same");
            Utility.appendLog("recalculate  " + pillUsage.getPillName()+" and time "+ PersianCalculater.getHourseAndMin(date.getTime()));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent);
            }else if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT){
                alarmManager.setExact(AlarmManager.RTC_WAKEUP,date.getTime(),pendingIntent);
            }else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP,date.getTime(),pendingIntent);
            }
        }
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
    }public static Typeface getRegularTypeFaceWithOutNumber(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "isans.ttf");
    }
    public static String convertToPersianDigits(String value) {
        return value.replace("1", "١").replace("2", "٢").replace("3", "٣").replace("4", "۴").replace("5", "۵")
                .replace("6", "۶").replace("7", "٧").replace("8", "٨").replace("9", "٩").replace("0","٠");
    }

    public static String getPhoneNumber(Context context) {
        if(AppDatabase.getInMemoryDatabase(context).profileDao().getMyProfile()!=null){
            return AppDatabase.getInMemoryDatabase(context).profileDao().getMyProfile().getPhone();
        }else {
            return "Guest";
        }

    }
    public static void centrizeAndShow(Toast toast){
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        if( v != null) v.setGravity(Gravity.CENTER);
        toast.show();
    }
    public static String getVersionName(){
        PackageInfo pinfo = null;
        try {
            pinfo = AppController.getContext().getPackageManager().getPackageInfo(AppController.getContext().getPackageName(), 0);
            int versionNumber = pinfo.versionCode;
            String versionName = pinfo.versionName;
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }

    }
    public static void appendLog(String text)
    {
  /*      File logFile = new File("sdcard/log.txt");
        if (!logFile.exists())
        {
            try
            {
                logFile.createNewFile();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try
        {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
    }
    public static int getRemindCount() {
        return  PreferencesData.getInt(Constants.PREF_REMIND_COUNT, 3);
    }

    public static int getDistance() {
        return PreferencesData.getInt(Constants.PREF_DISTANCE, 10);
    }

    public static PersianDate makeNearestTime(PersianDate persianDate) {
        int min = persianDate.getMinute();
        int h = persianDate.getHour();
        if(min+10<60) {
            min = min+10;

        }else {
            h = h+1;
            min = min+10-60;
        }

        int dah = min / 10;
        int yek = min % 10;
        if(yek<=2){
            yek=0;
        }else if(yek>2 && yek <=7){
            yek=5;
        }else {
            yek=0;
            dah +=1;
        }
        if(dah<6){
            min = (dah)*10 + yek;
            persianDate.setMinute(min);
            persianDate.setHour(h);
        }else {
            min =(6-dah)*10 + yek;
            persianDate.setMinute(min);
            persianDate.setHour(h+1);
        }
        return persianDate;
    }

    public static PersianDate getPersianDateWithDistance(int destance, long usageTime) {
        PersianDate persianDate =  new PersianDate(usageTime);
        int min = persianDate.getMinute();
        min = min +destance;
        if(min>60){
            persianDate.setMinute(min-60);
            int hour = persianDate.getHour();
            if(hour>23){
                persianDate.addDay(1);
                persianDate.setHour(0);
            }else {
                persianDate.setHour(hour+1);
            }
        }else {
            persianDate.setMinute(min);
        }
        persianDate.setSecond(0);
        return persianDate;
    }
}
