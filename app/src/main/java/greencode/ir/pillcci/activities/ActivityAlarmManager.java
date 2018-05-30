package greencode.ir.pillcci.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.widget.AppCompatImageView;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.controler.AppDatabase;
import greencode.ir.pillcci.database.PillObject;
import greencode.ir.pillcci.database.PillUsage;
import greencode.ir.pillcci.service.EventReciver;
import greencode.ir.pillcci.utils.BaseActivity;
public class ActivityAlarmManager extends BaseActivity implements View.OnKeyListener {

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
    int snoozCount = 0;
    int state = 0;
    ViewGroup transitionsContainer;
    MediaPlayer mp;
    PillUsage pillUsage;
    @BindView(R.id.desc)
    TextView desc;
    private PowerManager.WakeLock wl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_manager);
        ButterKnife.bind(this);
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Tag");
        wl.acquire();

        AppDatabase appDatabase = AppDatabase.getInMemoryDatabase(this);
        pillUsage = appDatabase.pillUsageDao().getNearestUsed(System.currentTimeMillis());
        ArrayList<PillUsage> list = new ArrayList<>(appDatabase.pillUsageDao().listPillUsage());
        if (pillUsage != null) {


            PillObject pill = appDatabase.pillObjectDao().specialPil(pillUsage.getPillName());
            if (pill.getB64() != null) {
                if (!pill.getB64().equals("")) {
                    byte[] decodedString = Base64.decode(pill.getB64(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    imgLogo.setImageBitmap(decodedByte);
                }
            }

            if (pillUsage.getCatRingtone().length() == 0) {
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                mp = MediaPlayer.create(this, notification);
            } else {
                Uri notification = Uri.parse(pillUsage.getCatRingtone());
                mp = MediaPlayer.create(this, notification);
            }
            mp.setLooping(true);
            mp.start();
            txtMedName.setText(pillUsage.getPillName());
            catName.setText(pillUsage.getCatNme());
            unitText.setText(pillUsage.getUnitAmount() + " " + pillUsage.getUnit());
            if(!pill.getDescription().equals("")){
                desc.setVisibility(View.VISIBLE);
                desc.setText(pill.getDescription());
            }
            snoozCount = pillUsage.getSnoozCount();
            if (pillUsage.getSnoozCount() > 2) {
                btnSnooz.setVisibility(View.GONE);
            }

        }


        transitionsContainer = findViewById(R.id.layout_container);

    }

    @Override
    protected void onStop() {
        try {
            wl.release();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        super.onStop();

    }

    public void stopPlay(){
        if(mp!=null) {

                mp.stop();
                mp.release();
                mp = null;

        }
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
        if (state == 1) {
            // user used pill
            pillUsage.setState(1);
            pillUsage.setUsedTime(System.currentTimeMillis());
        } else if (state == 2) {
            long usageTime = pillUsage.getUsageTime();
            usageTime = usageTime + (10 * 60 * 1000);
            pillUsage.setUsageTime(usageTime);
            pillUsage.setSnoozCount(snoozCount + 1);
        } else {
            pillUsage.setState(2);
        }
        AppDatabase database = AppDatabase.getInMemoryDatabase(this);
        database.pillUsageDao().update(pillUsage);

        PillUsage newPill = database.pillUsageDao().getNearestUsage(System.currentTimeMillis());
        if (newPill != null) {
            startAlarmPillReminder(newPill);
        }

        finish();
    }

    private void startAlarmPillReminder(PillUsage pillUsage) {
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(ActivityAlarmManager.this, EventReciver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, ((System.currentTimeMillis()) + (pillUsage.getUsageTime() - System.currentTimeMillis())), pendingIntent);
    }

    private void showAnimateUsage() {

        btnSnooz.setVisibility(View.GONE);

        btnCancel.setVisibility(View.GONE);

        layoutOk.setVisibility(View.VISIBLE);


    }


    private void showAnimateSnoosz() {

        btnUsage.setVisibility(View.GONE);

        btnCancel.setVisibility(View.GONE);

        layoutOk.setVisibility(View.VISIBLE);

    }

    private void showAnimateCancel() {
        btnUsage.setVisibility(View.GONE);
        btnSnooz.setVisibility(View.GONE);
        layoutOk.setVisibility(View.VISIBLE);
    }

    private void showAnimateReturn() {
        btnUsage.setVisibility(View.VISIBLE);
        if (snoozCount > 2) {
            btnSnooz.setVisibility(View.GONE);
        } else {
            btnSnooz.setVisibility(View.VISIBLE);
        }
        btnCancel.setVisibility(View.VISIBLE);
        layoutOk.setVisibility(View.GONE);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                |WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON|
                WindowManager.LayoutParams.FLAG_FULLSCREEN

               );
    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }
}
