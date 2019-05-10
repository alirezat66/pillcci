package greencode.ir.pillcci.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.google.android.material.textfield.TextInputEditText;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.widget.AppCompatImageView;
import android.text.InputFilter;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.Visibility;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.kevalpatel.ringtonepicker.RingtonePickerDialog;
import com.kevalpatel.ringtonepicker.RingtonePickerListener;
import com.zcw.togglebutton.ToggleButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.controler.AppDatabase;
import greencode.ir.pillcci.utils.BaseActivity;
import greencode.ir.pillcci.utils.Constants;
import greencode.ir.pillcci.utils.InputFilterMinMax;
import greencode.ir.pillcci.utils.PreferencesData;
import greencode.ir.pillcci.utils.Utility;

/**
 * Created by alireza on 6/2/18.
 */

public class ActivitySetting extends BaseActivity {
    @BindView(R.id.img_back)
    AppCompatImageView imgBack;
    @BindView(R.id.title)
    TextView txtTitle;

    @BindView(R.id.edtReminderCount)
    TextInputEditText edtReminderCount;
    @BindView(R.id.edtDistance)
    TextInputEditText edtDistance;
    @BindView(R.id.btnInsert)
    Button btnInsert;
    @BindView(R.id.btnDelete)
    Button btnDelete;
    @BindView(R.id.toggleVibrate)
    ToggleButton toggleVibrate;
    @BindView(R.id.toggleLight)
    ToggleButton toggleLight;

    boolean isVibrate = false;
    boolean isLight = false;

    String ringTone ;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String[] mPermission = {
            Manifest.permission.CAMERA
    };
    @BindView(R.id.btnDeleteAllInfo)
    TextView btnDeleteAllInfo;
    @BindView(R.id.btnExit)
    TextView btnExit;
    FirebaseAnalytics firebaseAnalytics;
    @BindView(R.id.edtRing)
    TextInputEditText edtRing;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setupWindowAnimations();
        }
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle params = new Bundle();
        params.putString("phoneNumber", Utility.getPhoneNumber(this));
        firebaseAnalytics.logEvent("setting_open", params);
        edtDistance.setFilters(new InputFilter[]{new InputFilterMinMax(1, 30)});
        ringTone = PreferencesData.getString(Constants.PREF_RingTone, RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_RINGTONE).toString());
        edtRing.setText(ringTone);
        txtTitle.setText("تنظیمات");
        int reminderCount = PreferencesData.getInt(Constants.PREF_REMIND_COUNT, 3);
        int distance = PreferencesData.getInt(Constants.PREF_DISTANCE, 10);
        edtReminderCount.setText(reminderCount + "");
        edtDistance.setText(distance + "");
        isVibrate = PreferencesData.getBoolean(Constants.PREF_VIBRATE);
        isLight = PreferencesData.getBoolean(Constants.PREF_LOGHT);

        toggleVibrate.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                isVibrate = on;
            }
        });
        toggleLight.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    checkPermission();
                } else {
                    isLight = false;
                }
            }
        });
        if (isVibrate)
            toggleVibrate.setToggleOn();
        if (isLight)
            toggleLight.setToggleOn();


    }
    private void setRingToneDialog() {
        RingtonePickerDialog.Builder ringtonePickerBuilder = new RingtonePickerDialog.Builder(this,getSupportFragmentManager());
        ringtonePickerBuilder.setTitle("انتخاب صدای هشدار");
        ringtonePickerBuilder.addRingtoneType(RingtonePickerDialog.Builder.TYPE_RINGTONE);

        ringtonePickerBuilder.setPositiveButtonText("انتخاب");

//set text to display as negative button. (Optional)
        ringtonePickerBuilder.setCancelButtonText("لغو");
        ringtonePickerBuilder.setPlaySampleWhileSelection(true);

        ringtonePickerBuilder.setListener(new RingtonePickerListener() {
            @Override
            public void OnRingtoneSelected(String ringtoneName, Uri ringtoneUri) {
                if (ringtoneUri != null) {
                    edtRing.setText(ringtoneName);
                    ringTone = ringtoneUri.toString();

                }
                //Do someting with Uri.
            }
        });
        ringtonePickerBuilder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    ) {
                lightOn();

            } else {
                Toast toast = Toast.makeText(this, "برای ادامه نیاز به اجازه دسترسی وجود دارد.", Toast.LENGTH_LONG);
                Utility.centrizeAndShow(toast);

                toggleLight.setToggleOff(true);
            }
        } else {
           Toast toast =  Toast.makeText(this, "برای ادامه نیاز به اجازه دسترسی وجود دارد.", Toast.LENGTH_LONG);
            Utility.centrizeAndShow(toast);

            toggleLight.setToggleOff(true);

        }
    }

    @OnClick({R.id.img_back, R.id.btnInsert, R.id.btnDelete, R.id.btnExit,R.id.edtRing})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                Utility.hideKeyboard();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    finish();
                }
                break;
            case R.id.btnInsert:
                saveState();
                break;
            case R.id.btnDelete:
                Utility.hideKeyboard();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    finish();
                }
                break;
            case R.id.btnExit:
                PreferencesData.saveBool(Constants.PREF_LOGIN, false);
                AppDatabase database = AppDatabase.getInMemoryDatabase(this);
                database.phoneBookDao().nukeTable();
                database.pillUsageDao().nukeTable();
                database.pillObjectDao().nukeTable();
                database.categoryDao().nukeTable();
                database.profileDao().nukeTable();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    finish();
                }
                break;
            case R.id.edtRing:
                setRingToneDialog();
                break;
        }
    }

    private void saveState() {
        if (edtReminderCount.getText().toString().length() == 0 || edtReminderCount.getText().equals("")) {
            Toast toast = Toast.makeText(this, "ورودی تعداد یادآوری صحیح نیست.", Toast.LENGTH_LONG);
            Utility.centrizeAndShow(toast);
        }
        if (edtDistance.getText().toString().length() == 0 || edtDistance.getText().toString().equals("0")) {
            Toast toast = Toast.makeText(this, "فاصله زمانی بین یاد آوری صحیح نیست.", Toast.LENGTH_LONG);
            Utility.centrizeAndShow(toast);

        } else {
            int reminderCount = Integer.parseInt(edtReminderCount.getText().toString());
            int distance = Integer.parseInt(edtDistance.getText().toString());
            PreferencesData.saveInt(Constants.PREF_REMIND_COUNT, reminderCount);
            PreferencesData.saveInt(Constants.PREF_DISTANCE, distance);
            PreferencesData.saveBool(Constants.PREF_VIBRATE, isVibrate);
            PreferencesData.saveBool(Constants.PREF_LOGHT, isLight);
            PreferencesData.saveString(Constants.PREF_RingTone,ringTone);
            Utility.hideKeyboard();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAfterTransition();
            } else {
                finish();
            }
        }

    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                if (ActivityCompat.checkSelfPermission(this, mPermission[0])
                        != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(
                            mPermission, REQUEST_CODE_PERMISSION);

                    // If any permission aboe not allowed by user, this condition will execute every tim, else your else part will work
                } else {
                    lightOn();

                }
            } catch (Exception e) {

                e.printStackTrace();
            }
        } else {
            lightOn();
        }
    }

    private void lightOn() {
        isLight = true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations() {
        Transition transition;
        transition = buildEnterTransition();

        getWindow().setEnterTransition(transition);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private Visibility buildEnterTransition() {
        Slide enterTransition = new Slide();
        enterTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        enterTransition.setSlideEdge(Gravity.RIGHT);
        return enterTransition;
    }

}
