package greencode.ir.pillcci.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alirezaafkar.sundatepicker.DatePicker;
import com.alirezaafkar.sundatepicker.interfaces.DateSetListener;
import com.github.florent37.expansionpanel.ExpansionLayout;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.kevalpatel.ringtonepicker.RingtonePickerDialog;
import com.kevalpatel.ringtonepicker.RingtonePickerListener;
import com.otaliastudios.autocomplete.Autocomplete;
import com.otaliastudios.autocomplete.AutocompleteCallback;
import com.otaliastudios.autocomplete.AutocompletePresenter;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.zcw.togglebutton.ToggleButton;

import org.xdty.preference.colorpicker.ColorPickerDialog;
import org.xdty.preference.colorpicker.ColorPickerSwatch;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import fr.ganfra.materialspinner.MaterialSpinner;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.adapter.CatAutoCompletePresenter;
import greencode.ir.pillcci.adapter.DrAutoCompletePresenter;
import greencode.ir.pillcci.adapter.PillAutoCompletePresenter;
import greencode.ir.pillcci.adapter.ResultAutoCompletePresenter;
import greencode.ir.pillcci.controler.AppDatabase;
import greencode.ir.pillcci.database.Category;
import greencode.ir.pillcci.database.PillObject;
import greencode.ir.pillcci.database.PillUsage;
import greencode.ir.pillcci.dialog.AmountDialog;
import greencode.ir.pillcci.dialog.AmountInterface;
import greencode.ir.pillcci.dialog.ChosePhotoTakerDialog;
import greencode.ir.pillcci.dialog.DayCountDialog;
import greencode.ir.pillcci.dialog.DayCountInterface;
import greencode.ir.pillcci.dialog.DayOfWeekInterface;
import greencode.ir.pillcci.dialog.DaySelectedDialog;
import greencode.ir.pillcci.dialog.FinishDialog;
import greencode.ir.pillcci.dialog.FinishListener;
import greencode.ir.pillcci.dialog.PhotoChoserInterface;
import greencode.ir.pillcci.dialog.TimeIntervalDialog;
import greencode.ir.pillcci.dialog.TimeIntervalInterface;
import greencode.ir.pillcci.objects.EachUsage;
import greencode.ir.pillcci.objects.EndUseFields;
import greencode.ir.pillcci.objects.GeneralFields;
import greencode.ir.pillcci.objects.UsageFields;
import greencode.ir.pillcci.retrofit.SyncController;
import greencode.ir.pillcci.timepicker.TimePickerDialog;
import greencode.ir.pillcci.timepicker.listener.OnDateSetListener;
import greencode.ir.pillcci.utils.BaseActivity;
import greencode.ir.pillcci.utils.CalcTimesAndSaveUsage;
import greencode.ir.pillcci.utils.PersianCalculater;
import greencode.ir.pillcci.utils.Utility;
import me.omidh.liquidradiobutton.LiquidRadioButton;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import saman.zamani.persiandate.PersianDate;

import static greencode.ir.pillcci.utils.CalcTimesAndSaveUsage.makePillUsageInPerAmountMood;
import static greencode.ir.pillcci.utils.CalcTimesAndSaveUsage.makePillUsageInPerCountMood;
import static greencode.ir.pillcci.utils.CalcTimesAndSaveUsage.makePillUsageInPerFreeMood;

public class ActivityEditPill extends BaseActivity implements OnDateSetListener, DateSetListener {
    @BindView(R.id.img_back)
    AppCompatImageView imgBack;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.imgLogo)
    CircleImageView imgLogo;
    @BindView(R.id.iv_camera)
    AppCompatImageView ivCamera;
    @BindView(R.id.lypic)
    FrameLayout lypic;
    @BindView(R.id.txtTitleImage)
    TextView txtTitleImage;
    @BindView(R.id.edtMedName)
    TextInputEditText edtMedName;
    @BindView(R.id.edtUseRes)
    TextInputEditText edtUseRes;
    @BindView(R.id.edtDrName)
    TextInputEditText edtDrName;
    @BindView(R.id.edtcatName)
    TextInputEditText edtcatName;
    @BindView(R.id.edtRing)
    TextInputEditText edtRing;
    @BindView(R.id.edtColor)
    TextInputEditText edtColor;
    @BindView(R.id.toggleVibrate)
    ToggleButton toggleVibrate;
    @BindView(R.id.toggleLight)
    ToggleButton toggleLight;
    @BindView(R.id.btnInsert)
    Button btnInsert;
    @BindView(R.id.btnDelete)
    Button btnDelete;


    String pillName;
    String catName;
    PillObject object;


    @BindView(R.id.radioAll)
    LiquidRadioButton radioAll;
    @BindView(R.id.radioTime)
    LiquidRadioButton radioTime;

    @BindView(R.id.radioCount)
    LiquidRadioButton radioCount;


    @BindView(R.id.toogleReminder)
    ToggleButton toogleReminder;
    @BindView(R.id.edtCountOfPill)
    EditText edtCountOfPill;
    @BindView(R.id.txtUnitReminder)
    TextView txtUnitReminder;
    @BindView(R.id.edtReminderDay)
    EditText edtReminderDay;

    // for general info
    private static final int REQUEST_CODE_PERMISSION = 2;
    private static final int REQUEST_CODE_PERMISSION_Light = 3;
    ChosePhotoTakerDialog dialog;
    String ringTone;
    String ringToneFirst;
    KProgressHUD kProgressHUD;

    int isVibrate;
    int isLight;
    String[] mPermission = {
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA
    };
    @BindView(R.id.txtDaysOfUse)
    TextView txtDaysOfUse;
    @BindView(R.id.txtTypeOfDays)
    TextView txtTypeOfDays;
    @BindView(R.id.reminderDayLayout)
    LinearLayout reminderDayLayout;
    @BindView(R.id.radioEvryDay)
    LiquidRadioButton radioEvryDay;
    @BindView(R.id.radioDaysInterval)
    LiquidRadioButton radioDaysInterval;
    @BindView(R.id.radioSpecificDays)
    LiquidRadioButton radioSpecificDays;
    @BindView(R.id.radiobirthControl)
    LiquidRadioButton radiobirthControl;
    @BindView(R.id.txtStartDate)
    TextView txtStartDate;
    @BindView(R.id.txtStartTime)
    TextView txtStartTime;
    @BindView(R.id.txtStartUsageDate)
    TextView txtStartUsageDate;
    @BindView(R.id.txtStartUsageTime)
    TextView txtStartUsageTime;
    @BindView(R.id.txtPerDay)
    TextView txtPerDay;
    @BindView(R.id.rgOne)
    RadioGroup rgOne;
    @BindView(R.id.txtUseCount)
    TextView txtUseCount;
    @BindView(R.id.txtUsage)
    TextView txtUsage;
    @BindView(R.id.edtEachTime)
    TextInputEditText edtEachTime;
    @BindView(R.id.spinner)
    MaterialSpinner spinner;
    @BindView(R.id.edtDescription)
    TextInputEditText edtDescription;
    @BindView(R.id.txtUseType)
    TextView txtUseType;
    @BindView(R.id.txtUseTypeTitle)
    TextView txtUseTypeTitle;

    @BindView(R.id.remiderBeforLay)
    LinearLayout remiderBeforLay;
    @BindView(R.id.headerIndicator_one)
    AppCompatImageView headerIndicatorOne;
    @BindView(R.id.expansionLayout_one)
    ExpansionLayout expansionLayoutOne;
    @BindView(R.id.headerIndicator)
    AppCompatImageView headerIndicator;
    @BindView(R.id.radio0)
    LiquidRadioButton radio0;
    @BindView(R.id.radio1)
    LiquidRadioButton radio1;
    @BindView(R.id.radio2)
    LiquidRadioButton radio2;
    @BindView(R.id.radio3)
    LiquidRadioButton radio3;
    @BindView(R.id.radio5)
    LiquidRadioButton radio5;
    @BindView(R.id.radio7)
    LiquidRadioButton radio7;
    @BindView(R.id.radio8)
    LiquidRadioButton radio8;
    @BindView(R.id.radio9)
    LiquidRadioButton radio9;

    private int mSelectedColor;
    private int mSelectedColorFirst;
    private Autocomplete pillAutocomplete;
    private Autocomplete resultAutoCompelete;
    private Autocomplete drAutoComplete;
    private Autocomplete catAutoComplete;

    String b64Image = "";
    // finish general info

    // its about usage info
    int dayRepeat = 0;
    int dayRepeatFirst = 0;
    int typeDayUsage = 0;
    int typeDayUsageFirst = 0;
    String usageStartDate = "";
    String usageStartDateFirst = "";
    ArrayList<String> daysOfUsage = new ArrayList<>();
    ArrayList<String> daysOfUsageFirst = new ArrayList<>();
    int countOfUsagePerDay = 0;
    int countOfUsagePerDayFirst = 0;
    double diffrenceOfUsage = 0;
    double diffrenceOfUsageFirst = 0;
    int startTimeHour = 0;
    int startTimeHourFirst = 0;
    int startTimeMin = 0;
    int startTimeMinFirst = 0;
    double countEachUse = 0;
    String unitUse = "";
    String unitUseFirst = "";
    int isRegular = 1;
    int isRegularFirst = 1;
    int totalDay = 1;
    double totalUseAmount = 1;

    long ourStartTimeStamp = 0;
    ArrayList<String> unitsCount = new ArrayList<>();
    ArrayList<String> unitsCountFirst = new ArrayList<>();
    ArrayList<Long> unitTimes = new ArrayList<>();
    ArrayList<Long> unitTimesFirst = new ArrayList<>();


    TimePickerDialog timePickerDialog;
    // its finish usage info


    // its about complitation info
    int type = 1;
    int typeFirst = 1;
    String countOfUseDays = "";
    String amountOfUse = "";
    boolean hasReminder = false;

    //its finish complitstion info

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pil_edit);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        txtTitle.setText("ویرایش دارو");
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.hideKeyboard();
                finish();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.hideKeyboard();
                finish();
            }
        });
        if (bundle != null) {
            pillName = bundle.getString("pillName");
            catName = bundle.getString("catName");
            object = AppDatabase.getInMemoryDatabase(ActivityEditPill.this).pillObjectDao().specialPil(pillName, catName);
            makeGeneralDetail(object);
            makeUsageDetail(object);
            makeCompleteDetail(object);

            ourStartTimeStamp = System.currentTimeMillis();
            edtMedName.requestFocus();
            edtMedName.requestFocus();
            edtDescription.setEnabled(true);
        }
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPill();
                Utility.hideKeyboard();


            }
        });
        Utility.hideKeyboard();
    }

    private void backToLastStateComplete(int lastType) {
        if (lastType == 1) {
            radioAll.setChecked(true);
            txtUseType.setText("مصرف مداوم");
        } else if (lastType == 2) {
            radioTime.setChecked(true);
            txtUseType.setText("مصرف برای " + totalDay + " روز");
        } else if (lastType == 3) {
            radioCount.setChecked(true);

            txtUseType.setText("مصرف به میزان " + totalUseAmount + " " + unitUse);

        }


    }

    private void makeCompleteDetail(PillObject object) {
        type = object.getUseType();
        typeFirst = object.getUseType();
        totalDay = object.getAllUseDays();
        countOfUseDays =object.getAllUseDays()+"";
        totalUseAmount = object.getTotalAmounts();
        amountOfUse = object.getTotalAmounts()+"";
        backToLastStateComplete(type);

        txtUnitReminder.setText(object.getUnitUse());

        if (object.getReminderDays() > 0) {
            hasReminder = true;
            toogleReminder.setToggleOn();


            reminderDayLayout.setVisibility(View.VISIBLE);
            remiderBeforLay.setVisibility(View.GONE);
            edtCountOfPill.setText((int) object.getAllPillCount() + "");
            edtCountOfPill.setEnabled(true);

            edtReminderDay.setText(object.getReminderDays() + "");
            edtReminderDay.setEnabled(true);
        }
        toogleReminder.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    hasReminder = true;
                    reminderDayLayout.setVisibility(View.VISIBLE);
                    remiderBeforLay.setVisibility(View.VISIBLE);
                    edtReminderDay.setEnabled(true);
                    edtCountOfPill.setEnabled(true);

                    if (type == 3) {
                        edtCountOfPill.setText(Utility.getDoubleStringValue(totalUseAmount) + "");
                    }
                    if (type == 2) {
                        double amount = 0;
                        for (String mount : AddMedicianActivity.getUsageFields().getUnitsCount()) {
                            amount += Double.parseDouble(mount);
                        }

                        edtCountOfPill.setText(Utility.getDoubleStringValue(amount * totalDay) + "");
                    }
                } else {
                    hasReminder = false;
                    reminderDayLayout.setVisibility(View.GONE);
                    remiderBeforLay.setVisibility(View.GONE);

                    edtReminderDay.setText("");
                    edtReminderDay.setEnabled(false);
                    edtCountOfPill.setEnabled(false);
                }

            }
        });


        radioAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 1;
                backToLastState(type);
            }
        });
        radioTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDayDialog();
            }
        });

        radioCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAmountDialog();
            }
        });


    }

    private void getAmountDialog() {
        final AmountDialog dialog = new AmountDialog(this, totalUseAmount);
        dialog.setListener(new AmountInterface() {
            @Override
            public void onSuccess(double amount) {
                totalUseAmount = amount;
                type = 3;
                backToLastState(type);
                dialog.dismiss();
            }

            @Override
            public void onCancel() {
                backToLastState(type);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void getDayDialog() {
        final DayCountDialog dialog = new DayCountDialog(this, totalDay, type);
        dialog.setListener(new DayCountInterface() {
            @Override
            public void onSuccess(int days) {
                totalDay = days;
                type = 2;
                backToLastState(type);
                dialog.dismiss();
            }

            @Override
            public void onCancel(int lastType) {

                backToLastState(type);
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    // its about usage info


    private void makeUsageDetail(PillObject object) {

        dayRepeat = object.getRepeatUsageDay();
        dayRepeatFirst = object.getRepeatUsageDay();
        typeDayUsage = object.getTypeOfUsage();
        typeDayUsageFirst = object.getTypeOfUsage();
        usageStartDate = PersianCalculater.getYearMonthAndDay(object.getFirstAlarmTime());
        usageStartDateFirst = PersianCalculater.getYearMonthAndDay(object.getFirstAlarmTime());
        String[] days = object.getDays().split(",");
        for (String day : days) {
            daysOfUsage.add(day);
            daysOfUsageFirst.add(day);

        }
        countOfUsagePerDay = 24 / (int) object.getDiffrenceOfUsage();
        countOfUsagePerDayFirst = 24 / (int) object.getDiffrenceOfUsage();
        diffrenceOfUsage = object.getDiffrenceOfUsage();
        diffrenceOfUsageFirst = object.getDiffrenceOfUsage();

        switch (countOfUsagePerDay) {
            case 1:

                radio0.setChecked(true);
                break;
            case 2:

                radio1.setChecked(true);
                break;
            case 3:

                radio2.setChecked(true);
                break;
            case 4:

                radio5.setChecked(true);
                break;

            case 6:

                radio7.setChecked(true);
                break;

            case 8:

                radio8.setChecked(true);
                break;
            case 12:

                radio8.setChecked(true);
                break;
            case 24:

                radio9.setChecked(true);
                break;


        }
        txtPerDay.setText(countOfUsagePerDay + " نوبت ");
        startTimeHour = object.getStardHour();
        startTimeHourFirst = object.getStardHour();
        startTimeMin = object.getStartMin();
        startTimeMinFirst = object.getStartMin();
        unitUse = object.getUnitUse();
        unitUseFirst = object.getUnitUse();
        isRegular = object.isRegular();
        isRegularFirst = object.isRegular();

        String[] counts = object.getUnitsCount().split(",");
        for (String cou : counts) {
            unitsCount.add(cou);
            unitsCountFirst.add(cou);
        }
        String[] times = object.getUnitTimes().split(",");
        for (String time : times) {
            unitTimes.add(Long.parseLong(time));
            unitTimesFirst.add(Long.parseLong(time));
        }

        txtStartUsageTime.setText(PersianCalculater.getHourseAndMin(object.getFirstAlarmTime()));
        txtStartTime.setText(PersianCalculater.getHourseAndMin(object.getFirstAlarmTime()) + " - ");
        txtStartDate.setText(PersianCalculater.getYearMonthAndDay(object.getFirstAlarmTime()));
        txtStartUsageDate.setText(PersianCalculater.getYearMonthAndDay(object.getFirstAlarmTime()));
        edtDescription.setText(object.getDescription());

        backToLastState(typeDayUsage);

        final String[] ITEMS = {"عدد", "قرص", "کپسول", "سی سی/میلی لیتر", "قاشق چای خوری", "بار", "قاشق غذا خوری",
                "قطره", "پیمانه", "پاف/اسپری", "گرم", "میلی گرم", "قطعه/تکه", "برچسب"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        String[] usage = object.getUnitsCount().split(",");
        if (usage.length > 0) {
            edtEachTime.setText(usage[0]);
            countEachUse = Double.parseDouble(usage[0]);

        }
        edtEachTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {
                    countEachUse = Double.parseDouble(s.toString());
                } catch (NumberFormatException ex) {

                    ex.printStackTrace();
                    countEachUse = 0;

                }
                txtUseCount.setText(countEachUse + "");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        spinner.setAdapter(adapter);
        int pos = 0;
        for (int i = 0; i < ITEMS.length; i++) {
            if (unitUse.equals(ITEMS[i])) {
                pos = i;
                break;
            }
        }
        spinner.setSelection(pos);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                unitUse = ITEMS[position];
                txtUsage.setText(unitUse);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        rgOne.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = rgOne.getCheckedRadioButtonId();

                String title = "";
                switch (selectedId) {
                    case R.id.radio0:
                        countOfUsagePerDay = 1;
                        title = "هر ۲۴ ساعت";
                        diffrenceOfUsage = 24;
                        break;
                    case R.id.radio1:
                        countOfUsagePerDay = 2;
                        title = "هر ۱۲ ساعت";
                        diffrenceOfUsage = 12;
                        break;
                    case R.id.radio2:
                        countOfUsagePerDay = 3;
                        title = "هر ۸ ساعت";
                        diffrenceOfUsage = 8;
                        break;
                    case R.id.radio3:
                        countOfUsagePerDay = 4;
                        title = "هر ۶ ساعت";
                        diffrenceOfUsage = 6;
                        break;

                    case R.id.radio5:
                        countOfUsagePerDay = 6;
                        title = "هر ۴ ساعت";
                        diffrenceOfUsage = 4;
                        break;

                    case R.id.radio7:
                        countOfUsagePerDay = 8;
                        title = "هر ۳ ساعت";
                        diffrenceOfUsage = 3;
                        break;
                    case R.id.radio8:
                        countOfUsagePerDay = 12;
                        title = "هر ۲ ساعت";
                        diffrenceOfUsage = 2;
                        break;
                    case R.id.radio9:
                        countOfUsagePerDay = 24;
                        diffrenceOfUsage = 1;
                        title = "هر ۱ ساعت";

                        break;
                }
                txtPerDay.setText(countOfUsagePerDay + " نوبت ");
            }

        });


    }

    private void backToLastState(int lastType) {
        if (lastType == 1) {
            radioEvryDay.setChecked(true);
            txtTypeOfDays.setText("(هر روز)");
        } else if (lastType == 2) {
            radioDaysInterval.setChecked(true);
            txtTypeOfDays.setText("(هر " + dayRepeat + " روز)");
        } else if (lastType == 3) {
            radioSpecificDays.setChecked(true);
            String dayStr = makeDays(daysOfUsage);
            txtTypeOfDays.setText("( روزهای : " + dayStr + " )");

        } else {
            radiobirthControl.setChecked(true);
            txtTypeOfDays.setText("(چرخه ضد بارداری " + daysOfUsage.get(0) + "-" + daysOfUsage.get(1) + ")");
        }


    }

    private String makeDays(ArrayList<String> days) {
        String dayStr = "";
        for (String day : days) {
            dayStr += day;
            dayStr += ",";
        }
        if (dayStr.length() > 0) {
            dayStr = dayStr.substring(0, dayStr.length() - 1);
        }
        return dayStr;
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        PersianDate persianCalendar = new PersianDate(millseconds);
        startTimeHour = persianCalendar.getHour();
        startTimeMin = persianCalendar.getMinute();
        txtStartUsageTime.setText(PersianCalculater.getHourseAndMin(millseconds));
        txtStartTime.setText(PersianCalculater.getHourseAndMin(millseconds) + " - ");
        //disableAfterThree();
    }

    private void showDialogOne() {
        /*final DayRepeatDialog dialog = new DayRepeatDialog(this, this, getSupportFragmentManager(),
                typeDayUsage, daysOfUsage, dayRepeat, ourStartTimeStamp);
        dialog.setListener(new DayRepeatInterface() {


            @Override
            public void onSuccess(int type, int eachdays, String startDate, String title, ArrayList<String> days, long startTimeStamp) {


                //  disableAfterOne();
                typeDayUsage = type;
                usageStartDate = startDate;
                daysOfUsage = days;
                dayRepeat = eachdays;
                if (type == 1 || type == 2) {
                    isRegular = true;
                    useDays.setText(title + " - شروع از : " + startDate);
                } else if (type == 3) {
                    if (days.size() == 7) {
                        isRegular = true;
                        typeDayUsage = 1;
                        useDays.setText("هر روز - شروع از :" + startDate);
                    } else {
                        isRegular = false;
                        String ourdays = "";


                        for (String day : days) {
                            ourdays += day;
                            ourdays += ",";
                        }
                        if (ourdays.length() > 0) {
                            ourdays = ourdays.substring(0, ourdays.length() - 1);
                        }
                        useDays.setText(title + " " + ourdays + "\n" + "  شروع از :" + startDate);
                    }
                } else if (type == 4) {
                    isRegular = true;

                    useDays.setText(title + " - شروع از : " + startDate);
                }
                usePartDays.setEnabled(true);
                PersianDate date = new PersianDate(startTimeStamp);
                date.setHour(0);
                date.setMinute(0);
                ourStartTimeStamp = date.getTime();
                dialog.dismiss();
            }

            @Override
            public void onRejected() {
                dialog.dismiss();
            }
        });
        dialog.show(getSupportFragmentManager(), dialog.getTag());*/
    }

    private void showDialogTwo() {
        /*final UsageCountDialog dialogUsage = new UsageCountDialog(this, countOfUsagePerDay);
        dialogUsage.setListener(new UsageCountInterface() {
            @Override
            public void onSuccess(int selected, String title, int count, double difrent) {
                diffrenceOfUsage = difrent;
                countOfUsagePerDay = count;
                usePartDays.setText(title);
                edtStartTime.setEnabled(true);
                disablePartFour();
                dialogUsage.dismiss();
            }

            @Override
            public void onRejected() {
                dialogUsage.dismiss();
            }
        });
        dialogUsage.show();*/

    }

    private void showDialogThree() {

        /*timePickerDialog = Utility.getTimeDialog(this, getResources().getColor(R.color.colorPrimary));

        timePickerDialog.show(getSupportFragmentManager(), "انتخاب زمان");*/
       /* final SelectTimeDialog selectTimeDialog = new SelectTimeDialog(getContext());
        selectTimeDialog.setListener(new SelectTimeInterface() {
            @Override
            public void onSuccess(String startDate,int time,int min) {
                startTimeHour  = time;
                startTimeMin=min;
                edtStartTime.setText(startDate);
                selectTimeDialog.dismiss();
                edtUseEachTime.setEnabled(true);
                disableAfterThree();
            }

            @Override
            public void onRejected() {
                selectTimeDialog.dismiss();
            }
        });
        selectTimeDialog.show();*/
    }

    private void showDialogFour() {
       /* final EachTimeDialog eachTimeDialog = new EachTimeDialog(this, unitUse, countEachUse);
        eachTimeDialog.setListener(new EachTimeInterface() {
            @Override
            public void onSuccess(double count, final String unit) {
                countEachUse = count;
                unitUse = unit;
                eachTimeDialog.dismiss();

                final EachTimeEditPartDialog editDialog = new EachTimeEditPartDialog(ActivityEditPill.this, startTimeHour, startTimeMin, countOfUsagePerDay, diffrenceOfUsage, unitUse, countEachUse, getSupportFragmentManager(), ourStartTimeStamp);
                editDialog.setListener(new EachTimeEditPart() {
                    @Override
                    public void onSuccess(ArrayList<EachUsage> usages) {
                        unitsCount = new ArrayList<>();
                        unitTimes = new ArrayList<>();
                        for (EachUsage use : usages) {
                            unitsCount.add(use.getEachUse());
                            unitTimes.add(use.getStartDay());
                        }


                        String temp = unitsCount.get(0);
                        boolean allSame = true;
                        for (String use : unitsCount) {
                            if (!use.equals(temp)) {
                                allSame = false;
                                break;
                            }
                        }

                        if (allSame) {
                            edtUseEachTime.setText(temp + " " + unitUse + " در هر وعده");
                        } else {
                            String finalStr = "به ترتیب ";
                            for (String use : unitsCount) {
                                finalStr += use;
                                finalStr += ",";
                            }
                            finalStr = finalStr.substring(0, finalStr.length() - 1);
                            finalStr = finalStr + " " + unitUse;
                            finalStr += " در ساعت(های): ";
                            for (long timeStr : unitTimes) {
                                finalStr += PersianCalculater.getHourseAndMin(timeStr);
                                finalStr += ",";
                            }
                            finalStr = finalStr.substring(0, finalStr.length() - 1);
                            edtUseEachTime.setText(finalStr);
                        }
                        edtDescription.setEnabled(true);
                        edtDescription.setFocusable(true);
                        String str = edtUseEachTime.getText().toString().replace(".0", "");
                        edtUseEachTime.setText(str);
                        txtUnit.setText(unitUse);
                        txtUnitReminder.setText(unitUse);
                        editDialog.dismiss();

                    }

                    @Override
                    public void onRejected() {
                        editDialog.dismiss();

                    }
                });
                editDialog.show(getSupportFragmentManager(), eachTimeDialog.getTag());


            }

            @Override
            public void onRejected() {
                eachTimeDialog.dismiss();
            }
        });
        eachTimeDialog.show(getSupportFragmentManager(), eachTimeDialog.getTag());*/
    }

    private void disablePartFour() {
       /* edtUseEachTime.setText("");
        countEachUse = 0;
        unitUse = "";*/
    }

    public boolean isChange() {
        if (dayRepeat != dayRepeatFirst) {
            return true;
        } else if (typeDayUsage != typeDayUsageFirst) {
            return true;
        } else if (!usageStartDate.equals(usageStartDateFirst)) {
            return true;
        } else if (!isEquale(daysOfUsage, daysOfUsageFirst)) {
            return true;
        } else if (countOfUsagePerDay != countOfUsagePerDayFirst) {
            return true;
        } else if (diffrenceOfUsage != diffrenceOfUsageFirst) {
            return true;
        } else if (startTimeHour != startTimeHourFirst) {
            return true;
        } else if (startTimeMin != startTimeMinFirst) {
            return true;
        } else if (!unitUse.equals(unitUseFirst)) {
            return true;
        } else if (isRegular != isRegularFirst) {
            return true;
        } else if (!isEquale(unitsCount, unitsCountFirst)) {
            return true;
        } else if (!isEqualeLong(unitTimes, unitTimesFirst)) {
            return true;
        } else if (type != typeFirst) {
            return true;
        } else if (!countOfUseDays.equals(totalDay + "")) {
            return true;
        } else if (!amountOfUse.equals(totalUseAmount + "")) {
            return true;
        } else {
            return false;
        }
       /* if(dayRepeat!=dayRepeatFirst || typeDayUsage!=typeDayUsageFirst||
                !usageStartDate.equals(usageStartDateFirst)||!isEquale(daysOfUsage,daysOfUsageFirst) ||countOfUsagePerDay== countOfUsagePerDayFirst
    ||diffrenceOfUsage!=diffrenceOfUsageFirst||startTimeHour!=startTimeHourFirst||startTimeMin!=startTimeMinFirst||
                unitUse!= unitUseFirst ||isRegular!=isRegularFirst||!isEquale(unitsCount,unitsCountFirst)
                ||!isEqualeLong(unitTimes,unitTimesFirst) ){
            return true;
        }else {
            return false;
        }*/
    }

    public boolean isEquale(ArrayList<String> str1, ArrayList<String> str2) {
        if (str1.size() != str2.size()) {
            return false;
        } else {
            boolean isSame = true;
            for (int i = 0; i < str1.size(); i++) {
                if (!str1.get(i).equals(str2.get(i))) {
                    isSame = false;
                    break;
                }
            }
            return isSame;
        }
    }

    public boolean isEqualeLong(ArrayList<Long> str1, ArrayList<Long> str2) {
        if (str1.size() != str2.size()) {
            return false;
        } else {
            boolean isSame = true;
            for (int i = 0; i < str1.size(); i++) {
                String strOne = str1.get(i) + "";
                String strTwo = str2.get(i) + "";
                if (!strOne.equals(strTwo)) {
                    isSame = false;
                    break;
                }
            }
            return isSame;
        }
    }
    // finish usage


    // its all about general
    private void makeGeneralDetail(PillObject object) {
        if (!object.getB64().equals("")) {

            byte[] decodedString = Base64.decode(object.getB64(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imgLogo.setImageBitmap(decodedByte);
            b64Image = object.getB64();
        }
        imgLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
            }
        });

        edtMedName.setText(pillName);
        edtUseRes.setText(object.getCouseOfUse());
        edtDrName.setText(object.getDrName());
        edtcatName.setText(catName);
        edtcatName.setTextColor(object.getCatColor());
        edtColor.setTextColor(object.getCatColor());
        edtColor.setText("رنگ یادآور");
        mSelectedColor = object.getCatColor();
        mSelectedColorFirst = object.getCatColor();

        edtRing.setText(object.getCatring());
        ringTone = object.getCatring();
        ringToneFirst = object.getCatring();
        if (object.getVibrate() == 1) {
            isVibrate = 1;
            toggleVibrate.setToggleOn();
        }
        if (object.getLight() == 1) {
            isLight = 1;
            toggleLight.setToggleOn();
        }
        setUpCatNameAutoComplete();
        setUpDrNameAutoComplete();
        setUpPillNameAutoCompelet();
        setUpResultAutoCompele();


        edtRing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRingToneDialog();
            }
        });
        edtColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColorDialog();
            }
        });
        toggleLight.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                checkPermissionLight();
            }
        });
    }

    private void setUpResultAutoCompele() {
        float elevation = 6f;
        Drawable backgroundDrawable = new ColorDrawable(Color.WHITE);
        AutocompletePresenter<String> presenter = new ResultAutoCompletePresenter(this);
        AutocompleteCallback<String> callback = new AutocompleteCallback<String>() {
            @Override
            public boolean onPopupItemClicked(Editable editable, String item) {
                editable.clear();
                editable.append(item);
                return true;
            }

            public void onPopupVisibilityChanged(boolean shown) {
            }
        };

        resultAutoCompelete = Autocomplete.<String>on(edtUseRes
        )
                .with(elevation)
                .with(backgroundDrawable)
                .with(presenter)
                .with(callback)
                .build();
    }

    private void setUpPillNameAutoCompelet() {
        float elevation = 6f;
        Drawable backgroundDrawable = new ColorDrawable(Color.WHITE);
        AutocompletePresenter<String> presenter = new PillAutoCompletePresenter(this);
        AutocompleteCallback<String> callback = new AutocompleteCallback<String>() {
            @Override
            public boolean onPopupItemClicked(Editable editable, String item) {
                editable.clear();
                editable.append(item);
                return true;
            }

            public void onPopupVisibilityChanged(boolean shown) {
            }
        };

        pillAutocomplete = Autocomplete.<String>on(edtMedName
        )
                .with(elevation)
                .with(backgroundDrawable)
                .with(presenter)
                .with(callback)
                .build();
    }

    private void setUpCatNameAutoComplete() {
        float elevation = 6f;
        Drawable backgroundDrawable = new ColorDrawable(Color.WHITE);
        AutocompletePresenter<Category> presenter = new CatAutoCompletePresenter(this);
        AutocompleteCallback<Category> callback = new AutocompleteCallback<Category>() {
            @Override
            public boolean onPopupItemClicked(Editable editable, Category item) {

                editable.clear();
                editable.append(item.getName());
                ringTone = item.getRingtone();
                mSelectedColor = item.getColor();
                edtColor.setTextColor(item.getColor());
                return true;


            }


            public void onPopupVisibilityChanged(boolean shown) {
            }
        };

        catAutoComplete = Autocomplete.<Category>on(edtcatName
        )
                .with(elevation)
                .with(backgroundDrawable)
                .with(presenter)
                .with(callback)
                .build();
    }

    private void setUpDrNameAutoComplete() {
        float elevation = 6f;
        Drawable backgroundDrawable = new ColorDrawable(Color.WHITE);
        AutocompletePresenter<String> presenter = new DrAutoCompletePresenter(this);
        AutocompleteCallback<String> callback = new AutocompleteCallback<String>() {
            @Override
            public boolean onPopupItemClicked(Editable editable, String item) {

                editable.clear();
                editable.append(item);
                return true;


            }


            public void onPopupVisibilityChanged(boolean shown) {
            }
        };

        drAutoComplete = Autocomplete.<String>on(edtDrName
        )
                .with(elevation)
                .with(backgroundDrawable)
                .with(presenter)
                .with(callback)
                .build();
    }

    private void checkPermissionLight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                if (ActivityCompat.checkSelfPermission(this, mPermission[2])
                        != PackageManager.PERMISSION_GRANTED) {
                    String[] permissionLight = {mPermission[2]};
                    requestPermissions(
                            permissionLight, REQUEST_CODE_PERMISSION_Light);
                    // If any permission aboe not allowed by user, this condition will execute every tim, else your else part will work
                } else {

                    isLight = 1;
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
        } else {
            isLight = 1;
        }

    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                if (ActivityCompat.checkSelfPermission(this, mPermission[0])
                        != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(this, mPermission[1])
                                != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(this, mPermission[2])
                        != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(
                            mPermission, REQUEST_CODE_PERMISSION);

                    // If any permission aboe not allowed by user, this condition will execute every tim, else your else part will work
                } else {
                    showDialogForImageSelector();

                }
            } catch (Exception e) {

                e.printStackTrace();
            }
        } else {
            showDialogForImageSelector();
        }
    }

    public void showDialogForImageSelector() {
        dialog = new ChosePhotoTakerDialog(this);
        dialog.setListener(new PhotoChoserInterface() {
            @Override
            public void onSuccess(int type) {

                if (type == 1) {
                    EasyImage.openCamera(ActivityEditPill.this, 500);
                    //camera chosemn
                } else {
                    EasyImage.openGallery(ActivityEditPill.this, 600);
                    //gallery chosen
                }
                dialog.dismiss();
            }

            @Override
            public void onRejected() {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    @OnClick({R.id.radioEvryDay, R.id.radioDaysInterval, R.id.radioSpecificDays, R.id.radiobirthControl, R.id.txtStartUsageDate, R.id.txtStartUsageTime})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.radioEvryDay:
                txtTypeOfDays.setText("(هر روز)");
                typeDayUsage = 1;
                dayRepeat = 0;
                isRegular = 1;
                break;
            case R.id.radioDaysInterval:
                showDialogInterval();
                break;
            case R.id.radioSpecificDays:
                showDialogSelectDay();

                break;
            case R.id.radiobirthControl:
                showDialogBirthControl();

                break;
            case R.id.txtStartUsageDate:
                PersianDate now = new PersianDate();
                long justNow = System.currentTimeMillis();
                final Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(justNow);
                new DatePicker.Builder()
                        .id(4)
                        .theme(R.style.DialogTheme)
                        .date(calendar)
                        .build(this)
                        .show(getSupportFragmentManager(), "انتخاب تاریخ شروع");
                break;
            case R.id.txtStartUsageTime:
                timePickerDialog = Utility.getTimeDialog(this, getResources().getColor(R.color.colorPrimary));

                timePickerDialog.show(getSupportFragmentManager(), "انتخاب زمان");
                break;
        }
    }

    private void showDialogBirthControl() {
        Intent intent = new Intent(this, BirthControlActivity.class);
        if (typeDayUsage == 4) {
            intent.putExtra("from", true);
            String days = makeDays(daysOfUsage);
            intent.putExtra("days", days);
            isRegular = 1;
        } else {
            intent.putExtra("from", false);

        }

        startActivityForResult(intent, 2911);

    }
    public  void  changeDate(PersianDate newTime){
        PersianDate date = new PersianDate(System.currentTimeMillis());
        PersianDate ourTime = new PersianDate(ourStartTimeStamp);
        date.setShYear(newTime.getShYear());
        date.setShMonth(newTime.getShMonth());
        date.setShDay(newTime.getShDay());
        date.setHour(ourTime.getHour());
        date.setMinute(ourTime.getMinute());
        ourStartTimeStamp = date.getTime();
        txtStartUsageDate.setText(PersianCalculater.getYearMonthAndDay(ourStartTimeStamp));
        txtStartDate.setText(PersianCalculater.getYearMonthAndDay(ourStartTimeStamp));

    }
    private void showDialogSelectDay() {
        final DaySelectedDialog dialog = new DaySelectedDialog(this, daysOfUsage);
        dialog.setListener(new DayOfWeekInterface() {
            @Override
            public void onSuccess(ArrayList<String> days,PersianDate persianDate) {


                if (days.size() == 7) {
                    radioEvryDay.setChecked(true);
                    typeDayUsage = 1;
                    txtTypeOfDays.setText("(هر روز)");
                    dayRepeat = 0;

                } else {
                    daysOfUsage = days;
                    String dayStr = makeDays(daysOfUsage);
                    dayRepeat = 0;
                    txtTypeOfDays.setText("( روزهای : " + dayStr + " )");
                    typeDayUsage = 3;
                    isRegular = 0;
                }
                dialog.dismiss();

            }

            @Override
            public void onRejected() {
                backToLastState(typeDayUsage);
                dialog.dismiss();

            }
        });

        dialog.show();
    }

    private void showDialogInterval() {

        final TimeIntervalDialog dialog = new TimeIntervalDialog(this, (dayRepeat >= 2 ? dayRepeat : 2), typeDayUsage);
        dialog.setListener(new TimeIntervalInterface() {
            @Override
            public void onSuccess(int days) {
                dayRepeat = days;
                isRegular = 1;
                typeDayUsage = 2;
                txtTypeOfDays.setText("(هر " + days + " روز)");
                dialog.dismiss();
            }

            @Override
            public void onCancel(int lastType) {
                dialog.dismiss();
                backToLastState(lastType);

            }
        });
        dialog.show();
    }

    @Override
    public void onDateSet(int id, @Nullable Calendar calendar, int day, int month, int year) {
        if (isCurrectSelectedDate(year, month, day)) {

            PersianDate date = new PersianDate(System.currentTimeMillis());
            PersianDate ourTime = new PersianDate(ourStartTimeStamp);
            date.setShYear(year);
            date.setShMonth(month);
            date.setShDay(day);
            date.setHour(ourTime.getHour());
            date.setMinute(ourTime.getMinute());
            ourStartTimeStamp = date.getTime();
            txtStartUsageDate.setText(PersianCalculater.getYearMonthAndDay(ourStartTimeStamp));
            txtStartDate.setText(PersianCalculater.getYearMonthAndDay(ourStartTimeStamp));
        } else {
            Toast.makeText(this, "روز انتخاب شده نباید قبل از امروز باشد.", Toast.LENGTH_LONG).show();
        }
    }

    boolean isCurrectSelectedDate(int year, int month, int day) {
        PersianDate nowPersianDate = new PersianDate(System.currentTimeMillis());
        if (year == nowPersianDate.getShYear()) {
            if (month == nowPersianDate.getShMonth()) {
                if (day >= nowPersianDate.getShDay()) {
                    return true;
                } else {
                    return false;
                }
            } else if (month > nowPersianDate.getShMonth()) {
                return true;
            } else {
                return false;
            }
        } else if (year > nowPersianDate.getShYear()) {
            return true;
        } else {
            return false;
        }
    }

    class ConvertToB64 extends AsyncTask<File, String, String> {
        @Override
        protected String doInBackground(File... files) {
            File imageFile = files[0];
            Bitmap bm = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            bm = Utility.resizeBitmap(bm, 800);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//added lines
            bm.recycle();
            bm = null;
//added lines
            byte[] b = baos.toByteArray();
            String b64 = Base64.encodeToString(b, Base64.DEFAULT);
            return b64;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            b64Image = result;

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2910) {
            if (resultCode == RESULT_OK) {


                ArrayList<EachUsage> myList = (ArrayList<EachUsage>) data.getSerializableExtra("mylist");
                unitsCount = new ArrayList<>();
                unitTimes = new ArrayList<>();
                for (EachUsage use : myList) {
                    unitsCount.add(use.getEachUse());
                    unitTimes.add(use.getStartDay());
                }


                String temp = unitsCount.get(0);
                boolean allSame = true;
                for (String use : unitsCount) {
                    if (!use.equals(temp)) {
                        allSame = false;
                        break;
                    }
                }

                String description = edtDescription.getText().toString().replace("\n", " ");
                edtDescription.setText(description);
                new CalcAlarm().execute();
                /*onAction.onSaveButtonTwo(new UsageFields(typeDayUsage, txtStartDate.getText().toString(), dayRepeat, isRegular, daysOfUsage, typeDayUsage,
                        diffrenceOfUsage, countOfUsagePerDay, startTimeHour, startTimeMin, unitUse, unitsCount, unitTimes, description.toString(), ourStartTimeStamp));
*/

            } else {

            }


        } else if (requestCode == 2911) {
            if (resultCode == RESULT_OK) {
                String days = data.getStringExtra("days");
                String[] dayList = days.split(",");
                daysOfUsage = new ArrayList<>();
                dayRepeat = 0;
                for (String day : dayList) {
                    daysOfUsage.add(day);
                }
                typeDayUsage = 4;
                txtTypeOfDays.setText("(چرخه ضد بارداری " + daysOfUsage.get(0) + "-" + daysOfUsage.get(1) + ")");


            } else {
                backToLastState(typeDayUsage);
            }
        } else {


            EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
                @Override
                public void onImagePicked(final File imageFile, EasyImage.ImageSource source, int type) {


                    Picasso.with(ActivityEditPill.this)
                            .load(imageFile)
                            .centerCrop()
                            .resize(100, 100)
                            .into(imgLogo, new Callback() {
                                @Override
                                public void onSuccess() {
                                    new ConvertToB64().execute(imageFile);
                                }

                                @Override
                                public void onError() {

                                }
                            });
                }
            });
        }
    }

    private void setColorDialog() {
        int[] mColors = getResources().getIntArray(R.array.default_rainbow);
        ColorPickerDialog dialog = ColorPickerDialog.newInstance(R.string.color_picker,
                mColors,
                mSelectedColor,
                5, // Number of columns
                ColorPickerDialog.SIZE_SMALL,
                true // True or False to enable or disable the serpentine effect
                //0, // stroke width
                //Color.BLACK // stroke color
        );

        dialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {

            @Override
            public void onColorSelected(int color) {
                mSelectedColor = color;
                edtColor.setTextColor(mSelectedColor);
                edtColor.setText("رنگ یادآور");
                edtcatName.setTextColor(mSelectedColor);

            }

        });

        dialog.show(getFragmentManager(), "color_dialog_test");
    }

    private void setRingToneDialog() {
        RingtonePickerDialog.Builder ringtonePickerBuilder = new RingtonePickerDialog.Builder(this, getSupportFragmentManager());
        ringtonePickerBuilder.setTitle("انتخاب صدای هشدار");
        ringtonePickerBuilder.addRingtoneType(RingtonePickerDialog.Builder.TYPE_RINGTONE);

        ringtonePickerBuilder.setPositiveButtonText("انتخاب");

//set text to display as negative button. (Optional)
        ringtonePickerBuilder.setCancelButtonText("لغو");
        ringtonePickerBuilder.setPlaySampleWhileSelection(true);

        ringtonePickerBuilder.setListener(new RingtonePickerListener() {
            @Override
            public void OnRingtoneSelected(String ringtoneName, Uri ringtoneUri) {

                edtRing.setText(ringtoneName);
                ringTone = ringtoneUri.toString();

                //Do someting with Uri.
            }
        });
        ringtonePickerBuilder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED
                    ) {
                showDialogForImageSelector();

            } else {
                Toast.makeText(this, "برای ادامه،نیاز به اجازه دسترسی وجود دارد.", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == REQUEST_CODE_PERMISSION_Light) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    ) {
                isLight = 1;

            } else {
                isLight = 0;
                toggleLight.setToggleOff();

            }
        } else {
            Toast.makeText(this, "برای ادامه، نیاز به اجازه دسترسی وجود دارد.", Toast.LENGTH_LONG).show();
        }

    }
// its finish of general

    public boolean checkValidationGeneral() {
        if (edtMedName.getText().toString().length() == 0) {
            edtMedName.setError("نام دارو باید وارد شود.");
            edtMedName.requestFocus();
            return false;
        } else {
            if (!pillName.equals(edtMedName.getText().toString())) {
                AppDatabase database = AppDatabase.getInMemoryDatabase(this);
                PillObject checkTwice = database.pillObjectDao().specialPil(edtMedName.getText().toString(), edtcatName.getText().toString());
                if (checkTwice != null) {
                    edtMedName.setError("دارویی با این نام  و با این مصرف کننده ذخیره شده است.");
                    edtMedName.requestFocus();
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }
    }

    public boolean checkValidationComplitation() {
        int totalTime = 0;
        double totalcount = 0;
        int reminderCount = 0;
        int reminderDay = 0;

        if (hasReminder) {
            try {
                reminderDay = 1;
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                reminderDay = 0;
            }

        }else {
            reminderDay = 0;

        }
        if (totalDay > 0) {
            totalTime = totalDay;
        }
        if (totalUseAmount > 0) {
            totalcount = totalUseAmount;
        }

        if (radioTime.isChecked()) {


            if (totalTime <= 0) {
                Toast.makeText(this, "مقدار مصرف در این حالت باید حداقل ۱ روز باشد.", Toast.LENGTH_LONG).show();
                return false;
            }

            ArrayList<String> startAndStop = daysOfUsage;
            int type = typeDayUsage;
            if (type == 4) {
                int startDay = Integer.parseInt(startAndStop.get(0));
                if (totalTime <= startDay) {
                    Toast.makeText(this, "در چرخه ضد بارداری مقدار مصرف باید از تعداد روزهای استفاده چرخه بیشتر باشد. ", Toast.LENGTH_LONG).show();
                    return false;
                }
            }

        }
        if (radioCount.isChecked()) {

            if (totalcount <= 0) {
                Toast.makeText(this, "میزان مصرف در این حالت صحیح نیست.", Toast.LENGTH_LONG).show();
                return false;
            } else {

                double amount = 0;
                for (String s : unitsCount) {
                    try {
                        amount += Double.parseDouble(s);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
                if (amount > totalcount) {
                    Toast.makeText(this, "میزان مصرف وارد شده از مصرف یک روز شما کمتر می باشد.", Toast.LENGTH_LONG).show();
                    return false;
                }
                if (typeDayUsage == 4) {
                    ArrayList<String> startAndStop = daysOfUsage;
                    int startDay = Integer.parseInt(startAndStop.get(0));
                    amount = 0;
                    for (String s : unitsCount) {
                        try {
                            amount += Double.parseDouble(s);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    amount = amount * startDay;
                    if (amount > totalcount) {
                        Toast.makeText(this, "در چرخه ضد بارداری، میزان داروی تجویز شده باید حداقل یک چرخه باشد.", Toast.LENGTH_LONG).show();
                        return false;
                    }
                }


            }

        }

        return true;


    }

    public boolean checkValidationUsage() {
        if (countEachUse == 0) {
            Toast.makeText(this, "وارد کردن همه فیلدها جز فیلد توضیحات اجباری است.", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    public void editPill() {
        if (!isChange()) {
            if (checkValidationGeneral() && checkValidationComplitation()) {
                AppDatabase database = AppDatabase.getInMemoryDatabase(this);
                PillObject pillObject = database.pillObjectDao().specialPil(pillName, catName);
                pillObject.setMidname(edtMedName.getText().toString());
                pillObject.setB64(b64Image);
                pillObject.setCatName(edtcatName.getText().toString());
                pillObject.setCatColor(mSelectedColor);
                pillObject.setCatring(ringTone);
                pillObject.setCouseOfUse(edtUseRes.getText().toString());
                pillObject.setDrName(edtDrName.getText().toString());
                pillObject.setHasLight(isLight);
                pillObject.setHasVibrate(isVibrate);
                pillObject.setDescription(edtDescription.getText().toString());

                // complitation info
                int totalTime = 0;
                double totalcount = 0;
                int reminderCount = 0;
                int reminderDay = 0;


                if (hasReminder) {
                    try {
                        reminderDay = Integer.parseInt(edtReminderDay.getText().toString());
                    } catch (NumberFormatException ex) {
                        ex.printStackTrace();
                        reminderDay = 0;
                    }
                    try {
                        reminderCount = Integer.parseInt(edtCountOfPill.getText().toString());
                    } catch (NumberFormatException ex) {
                        ex.printStackTrace();
                        reminderCount = 0;
                    }

                } else {
                    reminderCount = 0;
                    reminderDay = 0;
                    try {
                        reminderCount = Integer.parseInt(edtCountOfPill.getText().toString());
                    } catch (NumberFormatException ex) {
                        ex.printStackTrace();
                        reminderCount = 0;
                    }
                    if (reminderDay == 0) {
                        Toast.makeText(this, "تعداد روزهای پیش از یادآوری ذکر نشده است.", Toast.LENGTH_LONG).show();

                    }
                    if (reminderCount == 0) {
                        Toast.makeText(this, "تعداد کل داروهای موجود ذکر نشده است.", Toast.LENGTH_LONG).show();

                    }
                }

                pillObject.setReminderDays(reminderDay);
                pillObject.setAllPillCount(reminderCount);

                // finish complitation info


                if (edtcatName.getText().toString().trim().length() == 0) {
                    pillObject.setCatName("");
                }
                pillObject.setSync(0);
                database.pillObjectDao().update(pillObject);

                List<PillUsage> usages = database.pillUsageDao().listSpecialPillUsage(pillName, catName);
                for (PillUsage usage : usages) {
                    usage.setPillName(edtMedName.getText().toString());
                    usage.setCatColor(mSelectedColor);
                    usage.setDrName(edtDrName.getText().toString());
                    usage.setCatNme(edtcatName.getText().toString());
                    usage.setCatRingtone(ringTone);
                    usage.setIsSync(0);

                }

                database.pillUsageDao().update(usages);

                SyncController syncController = new SyncController();
                syncController.checkDataBaseForUpdate();
                Utility.reCalculateManager(this);
                Utility.hideKeyboard();
                finish();


            }


        } else {

            if (checkValidationUsage() && checkValidationComplitation() && checkValidationGeneral()) {
                final FinishDialog dialog = new FinishDialog(this, "از ویرایش اطمینان دارید؟", "شما اطلاعات اصلی دارو را تغییر داده اید. در صورت تایید تمامی یادآوری های تنظیم شده این دارو حذف شده و مجددا تنظیم می شود.");
                dialog.setListener(new FinishListener() {
                    @Override
                    public void onReject() {
                        dialog.dismiss();
                    }

                    @Override
                    public void onSuccess() {
                        dialog.dismiss();
                        showLastPage();
                    }
                });
                dialog.show();
            }
        }
    }

    private void showLastPage() {

        Intent intent = new Intent(this, EachTimeEditPartActivity.class);
        intent.putExtra("startTimeHour", startTimeHour);
        intent.putExtra("startTimeMin", startTimeMin);
        intent.putExtra("countOfUsagePerDay", countOfUsagePerDay);
        intent.putExtra("diffrenceOfUsage", diffrenceOfUsage);
        intent.putExtra("unitUse", unitUse);
        intent.putExtra("eachTimeUsage", countEachUse);
        intent.putExtra("startTimeDate", ourStartTimeStamp);

        startActivityForResult(intent, 2910);


    }

    private GeneralFields getGeneralFileds() {
        if (checkValidationGeneral()) {
            GeneralFields generalFields = new GeneralFields(edtMedName.getText().toString(), b64Image, edtUseRes.getText().toString(),
                    edtDrName.getText().toString(), edtcatName.getText().toString(), mSelectedColor, ringTone, isLight, isVibrate);
            return generalFields;
        } else {
            return null;
        }
    }

    private EndUseFields getEndUseFields() {
        if (checkValidationComplitation()) {
            int totalTime = 0;
            double totalcount = 0;
            int reminderCount = 0;
            int reminderDay = 0;


            if (totalDay > 0) {
                totalTime = totalDay;
            }
            if (totalUseAmount > 0) {
                totalcount = totalUseAmount;
            }

            if (radioTime.isChecked()) {
                ArrayList<String> startAndStop = daysOfUsage;
            }
            if (radioCount.isChecked()) {


                double amount = 0;
                for (String s : unitsCount) {
                    try {
                        amount += Double.parseDouble(s);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }

                if (typeDayUsage == 4) {
                    ArrayList<String> startAndStop = daysOfUsage;

                    int startDay = Integer.parseInt(startAndStop.get(0));
                    amount = 0;
                    for (String s : unitsCount) {
                        try {
                            amount += Double.parseDouble(s);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    amount = amount * startDay;
                }
            }


            if (hasReminder) {
                try {
                    reminderDay = Integer.parseInt(edtReminderDay.getText().toString());
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                    reminderDay = 0;
                }
                try {
                    reminderCount = Integer.parseInt(edtCountOfPill.getText().toString());
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                    reminderCount = 0;
                }
            }
            EndUseFields endUseFields = new EndUseFields(type, totalTime, totalcount, reminderDay, reminderCount);
            return endUseFields;
        } else {
            return null;
        }
    }

    private UsageFields getUsageFields() {
        if (checkValidationUsage()) {
            String description = edtDescription.getText().toString().replace("\n", " ");
            UsageFields usageFields = new UsageFields(typeDayUsage, usageStartDate, dayRepeat, isRegular, daysOfUsage, typeDayUsage,
                    diffrenceOfUsage, countOfUsagePerDay, startTimeHour, startTimeMin, unitUse, unitsCount, unitTimes, description.toString(), ourStartTimeStamp);
            return usageFields;
        } else {
            return null;
        }
    }

    private void editEveryThing() {
        AppDatabase database = AppDatabase.getInMemoryDatabase(this);
        PillObject pillObject = database.pillObjectDao().specialPil(pillName, catName);

        GeneralFields generalFields = getGeneralFileds();
        UsageFields usageFields = getUsageFields();
        EndUseFields endUseFields = getEndUseFields();

        if (generalFields != null && usageFields != null && endUseFields != null) {
            database.pillUsageDao().deleteTempPill(pillName, catName, ourStartTimeStamp);
            List<PillUsage> usages = database.pillUsageDao().listSpecialPillUsage(pillName, catName);
            for (PillUsage usage : usages) {
                usage.setPillName(edtMedName.getText().toString());
                usage.setCatColor(mSelectedColor);
                usage.setCatNme(edtcatName.getText().toString());
                usage.setCatRingtone(ringTone);
                usage.setDrName(edtDrName.getText().toString());
                usage.setIsSync(0);

            }
            database.pillUsageDao().update(usages);
            editPillAndCat(pillObject, generalFields, usageFields, endUseFields);
            saveEditPillUsage(pillObject);

            SyncController sync = new SyncController();
            sync.checkDataBaseForUpdate();
        }


    }

    private void saveEditPillUsage(PillObject pillObject) {


        ArrayList<PillUsage> usageList = new ArrayList<>();
        if (pillObject.getUseType() == 1) {
            ///masrafe modam
            usageList = makePillUsageInPerFreeMood(pillObject, this);

        } else if (pillObject.getUseType() == 2) {
            // masraf bar asase rooz;
            usageList = makePillUsageInPerCountMood(pillObject, this);

        } else if (pillObject.getUseType() == 3) {
            usageList = makePillUsageInPerAmountMood(pillObject,this);
            //masraf bar asase mizane daroo;
        }
        AppDatabase database = AppDatabase.getInMemoryDatabase(this);
        database.pillUsageDao().insertPillList(usageList);
        Utility.reCalculateManager(this);
        Utility.hideKeyboard();
        finish();
    }

    private PillObject editPillAndCat(PillObject pillObject, GeneralFields generalFields, UsageFields usageFields, EndUseFields endUseFields) {
        AppDatabase database = AppDatabase.getInMemoryDatabase(this);
        String catName;


        PillObject lastPill = CalcTimesAndSaveUsage.editPillObject(this, pillObject, generalFields, usageFields, endUseFields);
        return lastPill;
    }

    class CalcAlarm extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            showWaiting();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            editEveryThing();
            return "";
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            disMissWaiting();
            Utility.hideKeyboard();
            finish();

        }

    }

    public void showWaiting() {
        kProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("لطفا منتظر بمانید")
                .setDetailsLabel("پیلچی در حال تنظیم یادآورهاست...")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.8f)
                .setBackgroundColor(getResources().getColor(R.color.colorPrimary))
                .show();
    }

    public void disMissWaiting() {
        if (kProgressHUD != null) {
            kProgressHUD.dismiss();
        }
    }

}
