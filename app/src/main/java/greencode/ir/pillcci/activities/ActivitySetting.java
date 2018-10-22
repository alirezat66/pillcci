package greencode.ir.pillcci.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
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
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
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


    private static final int REQUEST_CODE_PERMISSION = 2;
    String[] mPermission = {
            Manifest.permission.CAMERA
    };
    @BindView(R.id.btnDeleteAllInfo)
    TextView btnDeleteAllInfo;
    @BindView(R.id.btnExit)
    TextView btnExit;
    FirebaseAnalytics firebaseAnalytics;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle params = new Bundle();
        params.putString("phoneNumber", Utility.getPhoneNumber(this));
        firebaseAnalytics.logEvent("setting_open", params);
        edtDistance.setFilters(new InputFilter[]{new InputFilterMinMax(1, 30)});

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    ) {
                lightOn();

            } else {
                Toast.makeText(this, "برای ادامه نیاز به اجازه دسترسی وجود دارد.", Toast.LENGTH_LONG).show();
                toggleLight.setToggleOff(true);
            }
        } else {
            Toast.makeText(this, "برای ادامه نیاز به اجازه دسترسی وجود دارد.", Toast.LENGTH_LONG).show();
            toggleLight.setToggleOff(true);

        }
    }

    @OnClick({R.id.img_back, R.id.btnInsert, R.id.btnDelete,R.id.btnExit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                Utility.hideKeyboard();
                finish();
                break;
            case R.id.btnInsert:
                saveState();
                break;
            case R.id.btnDelete:
                Utility.hideKeyboard();
                finish();
                break;
            case R.id.btnExit:
                PreferencesData.saveBool(Constants.PREF_LOGIN,false);
                AppDatabase database = AppDatabase.getInMemoryDatabase(this);
                database.phoneBookDao().nukeTable();
                database.pillUsageDao().nukeTable();
                database.pillObjectDao().nukeTable();
                database.categoryDao().nukeTable();
                database.profileDao().nukeTable();
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void saveState() {
        if (edtReminderCount.getText().toString().length() == 0 || edtReminderCount.getText().equals("")) {
            Toast.makeText(this, "ورودی تعداد یادآوری صحیح نمی باشد.", Toast.LENGTH_LONG).show();
        }
        if (edtDistance.getText().toString().length() == 0 || edtDistance.getText().toString().equals("0")) {
            Toast.makeText(this, "فاصله زمانی بین یاد آوری صحیح نمی باشد..", Toast.LENGTH_LONG).show();

        } else {
            int reminderCount = Integer.parseInt(edtReminderCount.getText().toString());
            int distance = Integer.parseInt(edtDistance.getText().toString());
            PreferencesData.saveInt(Constants.PREF_REMIND_COUNT, reminderCount);
            PreferencesData.saveInt(Constants.PREF_DISTANCE, distance);
            PreferencesData.saveBool(Constants.PREF_VIBRATE, isVibrate);
            PreferencesData.saveBool(Constants.PREF_LOGHT, isLight);
            Utility.hideKeyboard();
            finish();
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


}
