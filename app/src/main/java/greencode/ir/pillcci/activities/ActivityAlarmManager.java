package greencode.ir.pillcci.activities;

import android.Manifest;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PowerManager;
import android.os.VibrationEffect;
import android.os.Vibrator;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.controler.AppDatabase;
import greencode.ir.pillcci.database.PillObject;
import greencode.ir.pillcci.database.PillUsage;
import greencode.ir.pillcci.dialog.CancelDialog;
import greencode.ir.pillcci.dialog.CancelListener;
import greencode.ir.pillcci.service.BroadCastRestart;
import greencode.ir.pillcci.service.SyncService;
import greencode.ir.pillcci.utils.BaseActivity;
import greencode.ir.pillcci.utils.Constants;
import greencode.ir.pillcci.utils.DatabaseManager;
import greencode.ir.pillcci.utils.PersianCalculater;
import greencode.ir.pillcci.utils.PreferencesData;
import greencode.ir.pillcci.utils.Utility;
import saman.zamani.persiandate.PersianDate;

public class ActivityAlarmManager extends BaseActivity implements View.OnKeyListener {

    @BindView(R.id.txtMessage)
    TextView txtMessage;
    @BindView(R.id.cardCat)
    CardView cardCat;
    @BindView(R.id.layout_container)
    LinearLayout layoutContainer;
    @BindView(R.id.txtclock)
    TextView txtclock;
    private Camera mCamera;
    private Camera.Parameters parameters;
    AudioManager mAudioManager;
    @BindView(R.id.imgLogo)
    CircleImageView imgLogo;
    @BindView(R.id.txtMedName)
    TextView txtMedName;
    @BindView(R.id.catImage)
    AppCompatImageView catImage;
    @BindView(R.id.catName)
    TextView catName;
    @BindView(R.id.unitImage)
    AppCompatImageView unitImage;
    @BindView(R.id.unitText)
    TextView unitText;
    @BindView(R.id.layoutInfo)
    LinearLayout layoutInfo;
    @BindView(R.id.btnUsage)
    Button btnUsage;
    @BindView(R.id.btnSnooz)
    Button btnSnooz;
    @BindView(R.id.btnCancel)
    Button btnCancel;
    @BindView(R.id.btnReturn)
    Button btnReturn;
    @BindView(R.id.btnOk)
    Button btnOk;
    @BindView(R.id.layoutOk)
    LinearLayout layoutOk;
    ViewGroup transitionsContainer;
    Ringtone mp;
    Vibrator vibrator;
    @BindView(R.id.desc)
    TextView desc;
    @BindView(R.id.txtRemind)
    TextView txtRemind;

    boolean isStarted = false;


    public static boolean isActive = false;
    private PowerManager.WakeLock wl;
    ArrayList<PillUsage> allNotUsed;// liste hame daroohayi ke timeshoon reside va bayad masraf shan hamin alan.
    CountDownTimer countDownTimer;
    private CameraManager camManager;
    int state = 0;
    String[] mPermission = {
            Manifest.permission.CAMERA
    };
    boolean isFinished = false;
    FirebaseAnalytics firebaseAnalytics;

    public void setFireBase(PersianDate persianDate) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle params = new Bundle();
        params.putString("phoneNumber", Utility.getPhoneNumber(this));
        params.putString("time", PersianCalculater.getYearMonthAndDay(persianDate.getTime()) + "-" + PersianCalculater.getHourseAndMin(persianDate.getTime()));
        firebaseAnalytics.logEvent("alarm_open", params);
        txtclock.setText(PersianCalculater.getHourseAndMin(persianDate.getTime()));

    }

    public void init() {
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        /*PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(SCREEN_BRIGHT_WAKE_LOCK, "My Tag");
        wl.acquire();
        wl.release();*/
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_manager);

        makePageOn();

        Utility.appendLog("we are in alarmmanager ");
        ButterKnife.bind(this);
        isActive = true;
        PersianDate persianDate = new PersianDate(System.currentTimeMillis());
        setFireBase(persianDate);
        Utility.appendLog("we init alarm ");
        init();/// manage powermanager and mAudiomanager
        final AppDatabase appDatabase = AppDatabase.getInMemoryDatabase(this);
        allNotUsed = new ArrayList<>(appDatabase.pillUsageDao().getNearestUsedList(persianDate.getTime() + 59999));
        Log.d("allnot used", allNotUsed.size() + "");
        Log.d("allnot used", (persianDate.getTime() + 59999) + "");
        Intent intent = new Intent(this, SyncService.class);
        intent.setAction(Constants.ACTION_START_FOREGROUND_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startService(intent);
            Log.d("restart", "yes we calculate again and syncservice");
        } else {
            startService(intent);
            Log.d("restart", "yes we calculate again and syncservice");
        }

        if (allNotUsed.size() != 0) {
            makeAlarm();
            Utility.reCalculateManager(this);


            if (countDownTimer == null) {
                // age countdown ba dokme cancel shode bashe dobare misazimesh ke 55 sanie baad ghat kone kolan
                countDownTimer = new CountDownTimer(45000, 5000) { // adjust the milli seconds here

                    public void onTick(long millisUntilFinished) {

                    }

                    public void onFinish() {
                        final int reminderCount = PreferencesData.getInt(Constants.PREF_REMIND_COUNT, 3);
                        isFinished = true;
                        stopPlay();

                        ArrayList<PillUsage> notUsed = new ArrayList<>();
                        for (PillUsage pillUsage : allNotUsed) {
                            notUsed.add(pillUsage);
                        }
                        for (PillUsage pillUsage : notUsed) {

                            if (pillUsage.getSnoozCount() == reminderCount) {
                                // age hanooz snooz dashte bashe snoozesh mikonim
                                pillUsage.setState(2);
                                PersianDate persianDate = new PersianDate(System.currentTimeMillis());
                                pillUsage.setUsedTime(persianDate.getTime());
                                AppDatabase database = AppDatabase.getInMemoryDatabase(ActivityAlarmManager.this);
                                pillUsage.setIsSync(0);
                                database.pillUsageDao().update(pillUsage);
                            } else {
                                long usageTime = pillUsage.getUsageTime();
                                int destance = PreferencesData.getInt(Constants.PREF_DISTANCE, 10);
                                PersianDate persianDate = Utility.getPersianDateWithDistance(destance, usageTime);
                                Log.d("setedTime", "set new time to :" + PersianCalculater.getHourseAndMin(persianDate.getTime()));

                                pillUsage.setUsageTime(persianDate.getTime());
                                pillUsage.setSnoozCount(pillUsage.getSnoozCount() + 1);

                                AppDatabase database = AppDatabase.getInMemoryDatabase(ActivityAlarmManager.this);
                                pillUsage.setIsSync(0);
                                database.pillUsageDao().update(pillUsage);
                                // age na miaim cancelesh mikonim
                            }
                            allNotUsed.remove(0);

                        }
                        Utility.reCalculateManager(ActivityAlarmManager.this);
                        if (android.os.Build.VERSION.SDK_INT >= 21) {
                            finishAndRemoveTask();
                        } else {
                            System.exit(0);
                            finish();
                        }


                    }
                }.start();
            }
            isStarted = true;


        } else {
            Utility.reCalculateManager(this);
            isStarted = true;
            if (android.os.Build.VERSION.SDK_INT >= 21) {
                finishAndRemoveTask();
            } else {
                System.exit(0);
                finish();
            }
        }
    }

    private void makePageOn() {
        KeyguardManager km = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        final KeyguardManager.KeyguardLock kl = km.newKeyguardLock("My_App");
        kl.disableKeyguard();

        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.FULL_WAKE_LOCK, "My_App");
        wl.acquire();
        wl.release();

    }


    private void makeAlarm() {
        Bundle params = new Bundle();
        params.putString("phoneNumber", Utility.getPhoneNumber(this));
        PersianDate persianDate = new PersianDate(System.currentTimeMillis());
        params.putString("time", PersianCalculater.getYearMonthAndDay(persianDate.getTime()) + "-" + PersianCalculater.getHourseAndMin(persianDate.getTime()));
        firebaseAnalytics.logEvent("alarm_ring", params);
        AppDatabase appDatabase = AppDatabase.getInMemoryDatabase(this);
        final PillUsage pillUsage = allNotUsed.get(0);// avalin iteme liste reminder ro migirim
        final PillObject pill = appDatabase.pillObjectDao().specialPil(pillUsage.getPillName(), pillUsage.getCatNme());//daroo ro dar miarim
        int isVibrate = pill.getVibrate();
        int isLight = pill.getLight();
        if (needToRemind(pill)) {
            txtRemind.setVisibility(View.VISIBLE);
        } else {
            txtRemind.setVisibility(View.GONE);

        }
        cardCat.setCardBackgroundColor(pill.getCatColor());
        /// age ax dasht axo neshoonim midim
        showImage(pill);
        // music ro play mikonim
        makeRing(pillUsage);

        // vibration  ro play mikonim
        makeVibration(isVibrate);
        isFinished = false;
        //light flash
        makeFlash(isLight);
        //fieldhaye texti ro minvisim
        makeTextesFeture(pillUsage, pill);

        // age snoozcount tamam shode bashe dige snooz nadare
        final int reminderCount = PreferencesData.getInt(Constants.PREF_REMIND_COUNT, 3);
        Log.e("setedtime", "snoozcount ==" + pillUsage.getSnoozCount() + ", reminderCount==" + reminderCount);
        if (pillUsage.getSnoozCount() >= reminderCount) {
            btnSnooz.setVisibility(View.GONE);
        } else {
            btnSnooz.setVisibility(View.VISIBLE);
        }


        int destance = PreferencesData.getInt(Constants.PREF_DISTANCE, 10);

        btnSnooz.setText("یادآوری " + destance + " دقیقه دیگر");


        btnUsage.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.VISIBLE);
        layoutOk.setVisibility(View.GONE);
        txtMessage.setVisibility(View.GONE);


    }

    private void makeFlash(int isLight) {
        if (isLight == 1) {
            boolean hasFlash = getApplicationContext().getPackageManager()
                    .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
            if (hasFlash) {
                if (ActivityCompat.checkSelfPermission(this, mPermission[0])
                        == PackageManager.PERMISSION_GRANTED) {
                    lightStart(0);

                }

            }
        }

    }

    private void lightStart(int count) {
        if (!isFinished) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                if (count == 40) {
                    return;
                } else {
                    if (count % 2 == 0) {

                        // Usually back camera is at 0 position.
                        try {
                            String cameraId = null; // Usually front camera is at 0 position.
                            if (cameraId == null) {
                                cameraId = camManager.getCameraIdList()[0];
                                camManager.setTorchMode(cameraId, true);
                            } else {
                                camManager.setTorchMode(cameraId, true);
                            }
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }


                        Handler handler = new Handler();
                        final int finalCount = count + 1;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                lightStart(finalCount);
                            }
                        }, 500);
                    } else {
                        try {
                            String cameraId = null; // Usually front camera is at 0 position.

                            if (cameraId == null) {
                                cameraId = camManager.getCameraIdList()[0];
                                camManager.setTorchMode(cameraId, false);
                            } else {
                                camManager.setTorchMode(cameraId, false);
                            }
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }

                        Handler handler = new Handler();
                        final int finalCount = count + 1;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                lightStart(finalCount);
                            }
                        }, 500);
                    }
                }

            } else {
                if (count == 40) {
                    return;
                } else {
                    if (count % 2 == 0) {

                        try {
                            mCamera = Camera.open();
                        }catch (RuntimeException ex){
                            ex.printStackTrace();
                        }
                        parameters = mCamera.getParameters();
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        mCamera.setParameters(parameters);
                        mCamera.startPreview();


                        Handler handler = new Handler();
                        final int finalCount = count + 1;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                lightStart(finalCount);
                            }
                        }, 500);
                    } else {
                        try {
                            mCamera = Camera.open();
                        }catch (RuntimeException ex){
                            ex.printStackTrace();
                        }
                        parameters = mCamera.getParameters();
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        mCamera.setParameters(parameters);
                        mCamera.stopPreview();
                        Handler handler = new Handler();
                        final int finalCount = count + 1;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                lightStart(finalCount);
                            }
                        }, 500);
                    }
                }
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    String cameraId = null; // Usually front camera is at 0 position.

                    if (cameraId == null) {
                        cameraId = camManager.getCameraIdList()[0];
                        camManager.setTorchMode(cameraId, false);
                    } else {
                        camManager.setTorchMode(cameraId, false);
                    }
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            } else {
                mCamera = Camera.open();
                parameters = mCamera.getParameters();
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                mCamera.setParameters(parameters);
                mCamera.stopPreview();
            }
        }

    }

    private boolean needToRemind(PillObject pill) {
        int remindDay = pill.getReminderDays();
        double countOfPill = pill.getAllPillCount();

        if (remindDay == 0 || countOfPill <= 0) {
            return false;
        } else {
            int usagePerDay = pill.getCountOfUsagePerDay();
            //darooye har rooz

            String[] unitAmount = pill.getUnitsCount().split(",");
            if (unitAmount.length == 1) {
                // hameye masrafa andazeye hame
                double unitPerUse = Double.parseDouble(unitAmount[0]);
                if ((unitPerUse * usagePerDay * remindDay) >= countOfPill) {
                    return true;
                } else {
                    return false;
                }
            } else {
                double amountInDay = 0;
                for (String unitA : unitAmount) {
                    double unitPerUse = Double.parseDouble(unitA);
                    amountInDay += unitPerUse;
                }
                if ((amountInDay * remindDay) >= countOfPill) {
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

    private void makeTextesFeture(PillUsage pillUsage, PillObject pill) {
        txtMedName.setText(pillUsage.getPillName() + " " + PersianCalculater.getHourseAndMin(pillUsage.getSetedTime()));
        catName.setText(pillUsage.getCatNme());
        unitText.setText(pillUsage.getUnitAmount() + " " + pillUsage.getUnit());
        if (!pill.getDescription().equals("")) {
            desc.setVisibility(View.VISIBLE);
            desc.setText(pill.getDescription());
        }
        if (pillUsage.getCatNme().equals("")) {
            layoutContainer.setVisibility(View.GONE);
        } else {
            layoutContainer.setVisibility(View.VISIBLE);
        }
    }

    private void makeVibration(int isVibrate) {

        if (isVibrate == 1) {
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


            long[] pattern = {0, 1000, 500,
                    2000, 500,
                    1000, 500,
                    1000, 500,
                    2000, 500,
                    1000, 500,
                    1000, 500,
                    2000, 500,
                    1000, 500,
                    1000, 500,
                    2000, 500};
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(pattern, -1));
            } else {
                //deprecated in API 26
                vibrator.vibrate(pattern, -1);
            }


            // Vibrate for 500 milliseconds

        }
    }

    private void makeRing(PillUsage pillUsage) {
        if (pillUsage.getCatRingtone().length() == 0) {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            mp = RingtoneManager.getRingtone(this, notification);
            ;

        } else {
            Uri notification = Uri.parse(pillUsage.getCatRingtone());
            mp = RingtoneManager.getRingtone(this, notification);

        }
        if (Build.VERSION.SDK_INT >= 21) {
            AudioAttributes aa = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            mp.setAudioAttributes(aa);
        } else {
            mp.setStreamType(AudioManager.STREAM_ALARM);
        }

        mp.play();
    }

    private void showImage(PillObject pill) {
        if (pill.getB64() != null) {
            if (!pill.getB64().equals("")) {
                byte[] decodedString = Base64.decode(pill.getB64(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imgLogo.setImageBitmap(decodedByte);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Utility.appendLog("we are in pause");
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        Utility.appendLog("we are in leave hint");
        stopPlay();
        saveStateAndClose();
    }

    @Override
    protected void onStop() {
        Utility.appendLog("we are in stop");

        /*if(isStarted){
            try {

                Utility.appendLog("we are in stop");
                stopPlay();
                    saveStateAndClose();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        isActive = false;*/
        super.onStop();

    }


    private void saveStateAndClose() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        if (allNotUsed.size() > 0) {
            Bundle params = new Bundle();
            params.putString("phoneNumber", Utility.getPhoneNumber(this));
            PersianDate persianDate = new PersianDate(System.currentTimeMillis());
            params.putString("time", PersianCalculater.getYearMonthAndDay(persianDate.getTime()) + "-" + PersianCalculater.getHourseAndMin(persianDate.getTime()));
            firebaseAnalytics.logEvent("alarm_close_forcly", params);


            final int reminderCount = Utility.getRemindCount();

            for (PillUsage usage : allNotUsed) {
                if (usage.getSnoozCount() < reminderCount) {
                    long usageTime = usage.getUsageTime();

                    int destance = Utility.getDistance();
                    PersianDate date = Utility.getPersianDateWithDistance(destance, usageTime);
                    usage.setUsageTime(date.getTime());
                    usage.setSnoozCount(usage.getSnoozCount() + 1);

                    AppDatabase database = AppDatabase.getInMemoryDatabase(this);
                    usage.setIsSync(0);
                    database.pillUsageDao().update(usage);

                } else {
                    usage.setState(2);
                    AppDatabase database = AppDatabase.getInMemoryDatabase(this);
                    usage.setIsSync(0);
                    database.pillUsageDao().update(usage);
                }
            }
        }
        Utility.reCalculateManager(this);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            finishAndRemoveTask();
        } else {
            System.exit(0);
            finish();
        }
        // stopService(new Intent(this,SyncService.class));

        /*Intent serviceIntent = new Intent(this, SyncService.class);
        serviceIntent.setAction(Constants.ACTION_STOP_FOREGROUND_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            startForegroundService(serviceIntent);
        }else {
            startService(serviceIntent);
        }*/

        Intent broadcastIntent = new Intent(this, BroadCastRestart.class);
        sendBroadcast(broadcastIntent);
    }

    public void stopPlay() {
        if (mp != null) {
            mp.stop();
            mp = null;

        }
        if (vibrator != null) {
            vibrator.cancel();
        }
        if (countDownTimer != null) {
            // count down ro pak mikonim ke dobar cancel ya snooz nashe
            countDownTimer.cancel();
            countDownTimer = null;
        }

        isFinished = true;

    }

    @OnClick({R.id.btnUsage, R.id.btnSnooz, R.id.btnCancel, R.id.btnReturn, R.id.btnOk})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnUsage:
                stopPlay();
                state = 1;
                showAnimateUsage();
                break;
            case R.id.btnSnooz:
                stopPlay();
                state = 2;
                updateOperation();
                break;
            case R.id.btnCancel:
                state = 3;
                stopPlay();
                showAnimateCancel();
                break;
            case R.id.btnReturn:
                showAnimateReturn();
                break;
            case R.id.btnOk:

                updateOperation();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {
            stopPlay();
        }
        if ((keyCode) == KeyEvent.KEYCODE_VOLUME_UP) {
            stopPlay();
        }
        return true;
    }

    private void updateOperation() {
        final PillUsage pillUsage = allNotUsed.get(0);
        if (state == 1) {
            // user used pill
            pillUsage.setState(1);

            PersianDate persianDate = new PersianDate(System.currentTimeMillis());
            pillUsage.setUsedTime(persianDate.getTime());
            PillObject object = AppDatabase.getInMemoryDatabase(this).pillObjectDao().specialPil(pillUsage.getPillName(), pillUsage.getCatNme());
            showNext(pillUsage);
            if (object.getTypeOfUsage() == 4) {
                makeNextInBirth(pillUsage);
            } else {
                makeNextUsage(pillUsage);
            }

        } else if (state == 2) {
            //snoozoo zafe
            long usageTime = pillUsage.getUsageTime();

            int destance = PreferencesData.getInt(Constants.PREF_DISTANCE, 10);

            PersianDate persianDate = Utility.getPersianDateWithDistance(destance, usageTime);

            pillUsage.setUsageTime(persianDate.getTime());
            pillUsage.setSnoozCount(pillUsage.getSnoozCount() + 1);

            showNext(pillUsage);

        } else if (state == 4) {
            //timesh tamam shode


            pillUsage.setState(2);
            PersianDate persianDate = new PersianDate(System.currentTimeMillis());
            pillUsage.setUsedTime(persianDate.getTime());
            PillObject object = AppDatabase.getInMemoryDatabase(this).pillObjectDao().specialPil(pillUsage.getPillName(), pillUsage.getCatNme());

            showNext(pillUsage);
            if (object.getTypeOfUsage() == 4) {
                makeNextInBirth(pillUsage);
            } else {
                makeNextUsage(pillUsage);
            }
        } else if (state == 3) {
            // cancell ro zade

            final AppDatabase database = AppDatabase.getInMemoryDatabase(this);
            PillObject pill = database.pillObjectDao().specialPil(pillUsage.getPillName(), pillUsage.getCatNme());
            if (pill.getUseType() == 1) {
                DatabaseManager.cancelUsage(ActivityAlarmManager.this, pillUsage);
                PersianDate persianDate = new PersianDate(System.currentTimeMillis());
                pillUsage.setUsedTime(persianDate.getTime());
                PillObject object = AppDatabase.getInMemoryDatabase(this).pillObjectDao().specialPil(pillUsage.getPillName(), pillUsage.getCatNme());

                showNext(pillUsage);
                if (object.getTypeOfUsage() == 4) {
                    makeNextInBirth(pillUsage);
                } else {
                    makeNextUsage(pillUsage);
                }
            } else {
                final CancelDialog dialog = new CancelDialog(ActivityAlarmManager.this, pill);
                dialog.setListener(new CancelListener() {
                    @Override
                    public void onReject() {
                        dialog.dismiss();
                    }

                    @Override
                    public void onSuccess(int type) {
                        if (type == 1) {
                            DatabaseManager.cancelUsage(ActivityAlarmManager.this, pillUsage);
                            PersianDate persianDate = new PersianDate(System.currentTimeMillis());
                            pillUsage.setUsedTime(persianDate.getTime());
                            PillObject object = AppDatabase.getInMemoryDatabase(ActivityAlarmManager.this).pillObjectDao().specialPil(pillUsage.getPillName(), pillUsage.getCatNme());
                            showNext(pillUsage);
                            if (object.getTypeOfUsage() == 4) {
                                makeNextInBirth(pillUsage);
                            } else {
                                makeNextUsage(pillUsage);
                            }

                        } else {
                            DatabaseManager.addToEnd(ActivityAlarmManager.this, pillUsage);
                            PersianDate persianDate = new PersianDate(System.currentTimeMillis());
                            pillUsage.setUsedTime(persianDate.getTime());
                            PillObject object = AppDatabase.getInMemoryDatabase(ActivityAlarmManager.this).pillObjectDao().specialPil(pillUsage.getPillName(), pillUsage.getCatNme());
                            showNext(pillUsage);
                            if (object.getTypeOfUsage() == 4) {
                                makeNextInBirth(pillUsage);
                            } else {
                                makeNextUsage(pillUsage);
                            }


                        }
                        dialog.dismiss();

                    }

                });
                dialog.show();
            }
        }


    }

    private void makeNextInBirth(PillUsage pillUsage) {
        // inja mikhaym begim age 2 rooz monde be payane masraf add kon;
        AppDatabase database = AppDatabase.getInMemoryDatabase(this);

        PillObject object = database.pillObjectDao().specialPil(pillUsage.getPillName(), pillUsage.getCatNme());
        List<PillUsage> usages = database.pillUsageDao().getAllNotUsed(pillUsage.getPillName(), pillUsage.getCatNme());
        if (usages.size() / object.getCountOfUsagePerDay() <= 4 * object.getCountOfUsagePerDay()) {
            DatabaseManager.makeNextBirthControl(this, pillUsage);
        }
    }

    private void showNext(PillUsage pillUsage) {
        AppDatabase database = AppDatabase.getInMemoryDatabase(this);
        pillUsage.setIsSync(0);
        database.pillUsageDao().update(pillUsage);
        PillObject pill = database.pillObjectDao().specialPil(pillUsage.getPillName(), pillUsage.getCatNme());
        double pillCount = pill.getAllPillCount();
        pillCount -= Double.parseDouble(pillUsage.getUnitAmount());
        pill.setAllPillCount(pillCount);
        pill.setSync(0);
        database.pillObjectDao().update(pill);
        Utility.reCalculateManager(ActivityAlarmManager.this);
        if (allNotUsed.size() == 1) {
            Utility.appendLog("we try finish activity");
            if (android.os.Build.VERSION.SDK_INT >= 21) {
                finishAndRemoveTask();
            } else {
                System.exit(0);
                finish();
            }
        } else {
            allNotUsed.remove(0);
            makeAlarm();
        }
    }

    private void makeNextUsage(PillUsage pillUsage) {
        DatabaseManager.makeNextUsage(ActivityAlarmManager.this, pillUsage);
    }


    public void goneButtons() {
        btnSnooz.setVisibility(View.GONE);
        btnUsage.setVisibility(View.GONE);
        btnCancel.setVisibility(View.GONE);
        layoutOk.setVisibility(View.VISIBLE);
        txtMessage.setVisibility(View.VISIBLE);

    }

    private void showAnimateUsage() {
        goneButtons();
        txtMessage.setText("دارو را مصرف می\u200Cکنم");
        txtMessage.setTextColor(getResources().getColor(R.color.teal));
    }


    private void showAnimateCancel() {
        goneButtons();
        txtMessage.setText("دارو را مصرف نمی\u200Cکنم");
        txtMessage.setTextColor(getResources().getColor(R.color.colorAccent));
    }

    private void showAnimateReturn() {
        int reminderCount = Utility.getRemindCount();
        PillUsage pillUsage = allNotUsed.get(0);
        btnUsage.setVisibility(View.VISIBLE);
        if (pillUsage.getSnoozCount() >= reminderCount) {
            btnSnooz.setVisibility(View.GONE);
        } else {
            btnSnooz.setVisibility(View.VISIBLE);
        }
        btnCancel.setVisibility(View.VISIBLE);
        layoutOk.setVisibility(View.GONE);
        txtMessage.setVisibility(View.GONE);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_FULLSCREEN

        );
    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    @Override
    protected void onDestroy() {
    /*    Utility.appendLog("we are in on distory");

        Log.e("hilevel", "not used number =" + allNotUsed.size());
        if (allNotUsed.size() > 1) {
            Bundle params = new Bundle();
            params.putString("phoneNumber", Utility.getPhoneNumber(this));
            PersianDate persianDate = new PersianDate(System.currentTimeMillis());
            params.putString("time", PersianCalculater.getYearMonthAndDay(persianDate.getTime()) + "-" + PersianCalculater.getHourseAndMin(persianDate.getTime()));
            firebaseAnalytics.logEvent("alarm_close_forcly", params);


            final int reminderCount = Utility.getRemindCount();

            for (PillUsage usage : allNotUsed) {
                if (usage.getSnoozCount() <= reminderCount) {
                    long usageTime = usage.getUsageTime();

                    int destance = Utility.getDistance();
                    PersianDate date = Utility.getPersianDateWithDistance(destance, usageTime);
                    usage.setUsageTime(date.getTime());
                    usage.setSnoozCount(usage.getSnoozCount() + 1);

                    AppDatabase database = AppDatabase.getInMemoryDatabase(this);
                    usage.setIsSync(0);
                    database.pillUsageDao().update(usage);

                } else {
                    usage.setState(2);
                    AppDatabase database = AppDatabase.getInMemoryDatabase(this);
                    usage.setIsSync(0);
                    database.pillUsageDao().update(usage);
                }
            }
        }
        Utility.reCalculateManager(this);


        // stopService(new Intent(this,SyncService.class));

        *//*Intent serviceIntent = new Intent(this, SyncService.class);
        serviceIntent.setAction(Constants.ACTION_STOP_FOREGROUND_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            startForegroundService(serviceIntent);
        }else {
            startService(serviceIntent);
        }*//*

        Intent broadcastIntent = new Intent(this, BroadCastRestart.class);
        sendBroadcast(broadcastIntent);*/

        super.onDestroy();
    }


}
