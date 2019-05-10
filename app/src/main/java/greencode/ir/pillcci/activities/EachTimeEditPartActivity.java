package greencode.ir.pillcci.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.transition.Explode;
import android.transition.Transition;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.adapter.EditEachTimeAdapter;
import greencode.ir.pillcci.objects.EachUsage;
import greencode.ir.pillcci.timepicker.TimePickerDialog;
import greencode.ir.pillcci.timepicker.data.Type;
import greencode.ir.pillcci.timepicker.listener.OnDateSetListener;
import greencode.ir.pillcci.utils.BaseActivity;
import greencode.ir.pillcci.utils.KeyboardUtil;
import greencode.ir.pillcci.utils.PersianCalculater;
import greencode.ir.pillcci.utils.Utility;
import saman.zamani.persiandate.PersianDate;

public class EachTimeEditPartActivity extends BaseActivity implements EditEachTimeAdapter.SelectListener, OnDateSetListener {

    @BindView(R.id.container)
    RecyclerView container;

    @BindView(R.id.btnCancle)
    Button btnCancle;
    @BindView(R.id.btnOk)
    Button btnOk;


    String startTime;
    String unit;
    int count;
    double diffrenceOfUsage;
    double eachTimeUsage;
    EditEachTimeAdapter adapter;
    PersianDate nowPersianDate;
    EachUsage data;
    long startTimeDate;
    String startStr;
    String startMinStr;

    int startTimeHour;
    int startTimeMin;
    int countOfUsagePerDay;
    String unitUse;
    @BindView(R.id.img_back)
    ImageView imgBack;
    int selectedPosition;
    FirebaseAnalytics firebaseAnalytics;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_each_time_edit_part);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setupWindowAnimations();
        }
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            startTimeHour = bundle.getInt("startTimeHour");
            startTimeMin = bundle.getInt("startTimeMin");
            countOfUsagePerDay = bundle.getInt("countOfUsagePerDay");
            diffrenceOfUsage = bundle.getDouble("diffrenceOfUsage");
            unitUse = bundle.getString("unitUse");
            eachTimeUsage = bundle.getDouble("eachTimeUsage");
            startTimeDate = bundle.getLong("startTimeDate");
            if(startTimeHour==24){
                startTimeHour=0;
            }
            if (startTimeHour < 10) {
                startStr = "0" + startTimeHour;
            } else {
                startStr = startTimeHour + "";
            }
            if (startTimeMin == 0) {
                startMinStr = "00";
            } else {
                startMinStr = "" + startTimeMin;
            }
            startTime = startStr + ":" + startMinStr;
        }
        ArrayList<EachUsage> list = new ArrayList<>();


        startTime = startStr + ":" + startMinStr;
        count = countOfUsagePerDay;
        this.unit = unitUse;
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                setResult(RESULT_CANCELED, intent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                }else {
                    finish();
                }
            }
        });
        list = makeAllTimes(count, startTime, startTimeDate, diffrenceOfUsage);
        container.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new EditEachTimeAdapter(this, list, EachTimeEditPartActivity.this);
        container.setAdapter(adapter);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCurrect = true;
                for (EachUsage eachUsage : adapter.getList()) {
                    if (eachUsage.getEachUse().length() == 0) {
                        isCurrect = false;
                        break;
                    }

                }
                if (isCurrect) {
                    Intent intent = getIntent();
                    intent.putExtra("mylist", adapter.getList());
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast toast = Toast.makeText(EachTimeEditPartActivity.this, "میزان مصرف نمی‌تواند مقدار خالی داشته باشد.", Toast.LENGTH_LONG);
                    Utility.centrizeAndShow(toast);
                }
            }
        });
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });

        new KeyboardUtil(this, container);


    }

    private ArrayList<EachUsage> makeAllTimes(int count, String startTime, long startTimeDate, double diffrenceOfUsage) {
        ArrayList<EachUsage> list = new ArrayList<>();
        long thisStartTimeDate = startTimeDate;
        String thisStartTime = startTime;
        for (int i = 0; i < count; i++) {

            PersianDate selectedStartDate = new PersianDate(thisStartTimeDate);
            PersianDate currentPersianDate = new PersianDate(System.currentTimeMillis());

            String[] hoursAndMin = thisStartTime.split(":");
            int hour = Integer.parseInt(hoursAndMin[0]);
            int min = Integer.parseInt(hoursAndMin[1]);
            selectedStartDate.setHour(hour);
            selectedStartDate.setMinute(min);
            selectedStartDate.setSecond(0);

            if (selectedStartDate.getTime() < currentPersianDate.getTime()) {
                selectedStartDate.addDay(1);
            }


            EachUsage item = new EachUsage(thisStartTime, unit, eachTimeUsage + "", selectedStartDate.getTime(), (i == 0 ? true : false));
            list.add(item);
            if (selectedStartDate.getHour() + (int) diffrenceOfUsage >= 24) {
                selectedStartDate.addDay(1);
                selectedStartDate.setHour((selectedStartDate.getHour() + (int) diffrenceOfUsage) - 24);
                selectedStartDate.setMinute(selectedStartDate.getMinute());
                selectedStartDate.setSecond(0);
                thisStartTimeDate = selectedStartDate.getTime();
                thisStartTime = PersianCalculater.getHourseAndMin(selectedStartDate.getTime());
                // raftim to rooze baad
            } else {
                selectedStartDate.setHour(selectedStartDate.getHour() + (int) diffrenceOfUsage);
                selectedStartDate.setMinute(selectedStartDate.getMinute());
                selectedStartDate.setSecond(0);
                thisStartTime = PersianCalculater.getHourseAndMin(selectedStartDate.getTime());
                thisStartTimeDate = selectedStartDate.getTime();

                // emroozim
            }

        }

        this.startTimeDate = list.get(0).getStartDay();
        return list;
    }

    public String calculateFirst(ArrayList<EachUsage> list) {
        return PersianCalculater.getHourseAndMin(list.get(0).getStartDay());
    }


    @Override
    public void selectTime(EachUsage eachUsage,int position) {
        data = eachUsage;
        selectedPosition = position;
        nowPersianDate = new PersianDate(data.getStartDay());

        /*String[] times = eachUsage.getTimeStr().split(":");
        int hour = Integer.parseInt(times[0]);
        int min = Integer.parseInt(times[1]);
        nowPersianDate.setHour(hour);
        nowPersianDate.setMinute(min);*/


        TimePickerDialog dialog = new TimePickerDialog.Builder()
                .setHourText("")
                .setMinuteText("")
                .setWheelItemTextSize(16)
                .setCancelStringId("انصراف")
                .setThemeColor(R.color.colorPrimary)
                .setTitleStringId("انتخاب زمان")
                .setSureStringId("انتخاب")
                .setType(Type.HOURS_MINS)
                .setCurrentMillseconds(nowPersianDate.getTime())
                .setCallBack(EachTimeEditPartActivity.this)
                .build();
        dialog.show(getSupportFragmentManager(), "انتخاب زمان");
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {

        // agar position bozorgtar 1 bood nabayad az 0 kochiktar bashe va nabayad az 2 bozorgtar bashe
        // agar position akharin position bood nabayad az ghablish koochiktar bashe

        PersianDate pSelected  = new PersianDate(data.getStartDay());
        PersianDate pickedTime = new PersianDate(millseconds);
        pSelected.setHour(pickedTime.getHour());
        pSelected.setMinute(pickedTime.getMinute());
        if(adapter.getItemCount()-1 >= selectedPosition+1 ){
            // badi darim


            PersianDate pPrevious = new PersianDate(adapter.getSelectedUsage(selectedPosition+1).getStartDay());

            if(pSelected.getTime()>= adapter.getSelectedUsage(selectedPosition+1).getStartDay()){
                Toast toast = Toast.makeText(this, "زمان انتخابی باید بین نوبت‌های قبلی و بعدی مصرف باشد.", Toast.LENGTH_LONG);
                Utility.centrizeAndShow(toast);
                return;
            }

        }
        if(selectedPosition-1>-1){
            // ghabli darim

            PersianDate pPrevious = new PersianDate(adapter.getSelectedUsage(selectedPosition-1).getStartDay());

            if(pSelected.getTime()<= adapter.getSelectedUsage(selectedPosition-1).getStartDay()){
                Toast toast =Toast.makeText(this, "زمان انتخابی باید بین نوبت‌های قبلی و بعدی مصرف باشد.", Toast.LENGTH_LONG);
                Utility.centrizeAndShow(toast);
                return;
            }
        }

        Bundle params = new Bundle();
        params.putString("phoneNumber", Utility.getPhoneNumber(this));
        firebaseAnalytics.logEvent("change_time", params);
        PersianDate selectedDate = new PersianDate(data.getStartDay());
        PersianDate selectedHMDate = new PersianDate(millseconds);
        selectedDate.setHour(selectedHMDate.getHour());
        selectedDate.setMinute(selectedHMDate.getMinute());
        if (selectedDate.getTime() < startTimeDate) {
            Toast toast = Toast.makeText(this, "زمان انتخابی باید بین نوبت‌های قبلی و بعدی مصرف باشد.", Toast.LENGTH_SHORT);
            Utility.centrizeAndShow(toast);
        } else {
            adapter.updatePilTime(data, selectedDate);
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations() {
        Transition transition;


        transition = buildEnterTransition();

        getWindow().setEnterTransition(transition);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private Transition buildEnterTransition() {
        Explode enterTransition = new Explode();
        enterTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        return enterTransition;
    }

}
