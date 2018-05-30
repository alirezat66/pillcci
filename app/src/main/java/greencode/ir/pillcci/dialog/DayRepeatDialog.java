package greencode.ir.pillcci.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.text.InputFilter;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.alirezaafkar.sundatepicker.DatePicker;
import com.alirezaafkar.sundatepicker.interfaces.DateSetListener;

import java.util.ArrayList;
import java.util.Calendar;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.utils.InputFilterMinMax;
import me.omidh.liquidradiobutton.LiquidRadioButton;
import saman.zamani.persiandate.PersianDate;
import saman.zamani.persiandate.PersianDateFormat;

/**
 * Created by alireza on 5/18/18.
 */

public class DayRepeatDialog extends Dialog implements DateSetListener {

    DayRepeatInterface myInterface;
    Context context;
    LiquidRadioButton radioEvryDay;
    TextInputEditText edtStartEvrayDay;
    long startTimeStamp=0;
    LiquidRadioButton radioEachDay;
    TextInputEditText dayRepeat;
    TextInputEditText edtStartEachDay;
    TextInputEditText edtSelectedDay;
    LiquidRadioButton radioEvryWeek;
    Button btnCancel,btnOk;
    ArrayList<String> selectedDay=new ArrayList<>();
    PersianDate nowPersianDate;
    TagContainerLayout tagcontainer;
    android.support.v4.app.FragmentManager supportedFragmentManager;
    int type=0;
    Activity activity;
    public DayRepeatDialog(Context context, Activity activity, android.support.v4.app.FragmentManager manager) {
        super(context);
        this.context = context;
        this.activity = activity;
        supportedFragmentManager = manager;
    }

    public void setListener(DayRepeatInterface listener) {
        this.myInterface = listener;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        View view = View.inflate(context, R.layout.dialog_dayly_usage, null);

        setContentView(view);


        radioEvryDay = findViewById(R.id.radioEvryDay);
        edtStartEvrayDay = findViewById(R.id.edtStartEvrayDay);
        radioEachDay = findViewById(R.id.radioEachDay);
        dayRepeat = findViewById(R.id.dayRepeat);
        dayRepeat.setFilters(new InputFilter[]{ new InputFilterMinMax("2", "60")});
        edtStartEachDay = findViewById(R.id.edtStartEachDay);
        radioEvryWeek = findViewById(R.id.radioEvryWeek);
        edtSelectedDay = findViewById(R.id.edtSelectedDay);
        nowPersianDate = new PersianDate(System.currentTimeMillis());
        PersianDateFormat pdformater1 = new PersianDateFormat("Y/m/d");
        edtStartEachDay.setText(pdformater1.format(nowPersianDate));
        edtStartEvrayDay.setText(pdformater1.format(nowPersianDate));
        startTimeStamp  = nowPersianDate.getTime();
        tagcontainer = findViewById(R.id.tagcontainer);
        dayRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eachDayActive();
            }
        });
        edtStartEachDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eachDayActive();
            }
        });
        edtSelectedDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                everyWeekActive();
            }
        });

        btnCancel= findViewById(R.id.btnCancle);
        btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioEvryDay.isChecked()){
                    if(edtStartEvrayDay.getText().toString().length()!=0) {
                        selectedDay = new ArrayList<>();

                         myInterface.onSuccess(1,0, edtStartEvrayDay.getText().toString(), "هر روز", selectedDay,startTimeStamp);


                    }else {
                        Toast.makeText(context, "تاریخ شروع انتخاب نشده است.", Toast.LENGTH_LONG).show();

                    }
                }else if(radioEachDay.isChecked()){
                    if(edtStartEachDay.getText().toString().length()==0){
                        Toast.makeText(context, "تاریخ شروع انتخاب نشده است.", Toast.LENGTH_LONG).show();
                    }else if(dayRepeat.getText().toString().length()==0){
                        Toast.makeText(context, "توالی روزها انتخاب نشده است.", Toast.LENGTH_LONG).show();
                    }else {
                        selectedDay = new ArrayList<>();
                        myInterface.onSuccess(2, Integer.parseInt(dayRepeat.getText().toString()),edtStartEachDay.getText().toString(), "هر " + dayRepeat.getText().toString()+" روز ", selectedDay,startTimeStamp);
                    }
                }else if(radioEvryWeek.isChecked()){
                    if(selectedDay.size()==0){
                        Toast.makeText(context, "هیچ روزی انتخاب نشده است.", Toast.LENGTH_LONG).show();
                    }else {

                        PersianDate tempPersianCalendar = nowPersianDate;
                        while (!selectedDay.contains(tempPersianCalendar.dayName())/*!tempPersianCalendar.getPersianWeekDayName().equals(selectedDay.get(0).trim())*/){
                            tempPersianCalendar.addDay(1);
                        }

                        String startday = tempPersianCalendar.dayName();
                        int index = selectedDay.indexOf(startday);
                        ArrayList<String> newSelectedDay = new ArrayList<>();
                        for(int i = index;i<selectedDay.size();i++){
                            newSelectedDay.add(selectedDay.get(i));
                        }
                        for(int i=0;i<index;i++){
                            newSelectedDay.add(selectedDay.get(i));
                        }
                        myInterface.onSuccess(3,0,tempPersianCalendar.getShYear()+"/"+(tempPersianCalendar.getShMonth())+"/"+tempPersianCalendar.getShDay(),"روزهای " ,newSelectedDay,tempPersianCalendar.getTime());
                    }
                }else {
                    Toast.makeText(context, "یکی از نحوه های زمان های موجود را انتخاب کنید.", Toast.LENGTH_LONG).show();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterface.onRejected();
            }
        });
        tagcontainer.setOnTagClickListener(new TagView.OnTagClickListener() {

            @Override
            public void onTagClick(int position, String text) {
                // ...
            }

            @Override
            public void onTagLongClick(final int position, String text) {
                // ...
            }

            @Override
            public void onTagCrossClick(int position) {
                selectedDay.remove(position);
                tagcontainer.setTags(selectedDay);
            }
        });        setCancelable(false);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = -1;
        getWindow().setAttributes(params);
        radioEvryDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    everyDayActive();
                }
            }
        });
        radioEachDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    eachDayActive();
                }
            }
        });
        radioEvryWeek.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    everyWeekActive();
                }
            }
        });
        edtSelectedDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                everyWeekActive();
                final DaySelectedDialog dialog = new DaySelectedDialog(context,selectedDay);
                dialog.setListener(new DayOfWeekInterface() {
                    @Override
                    public void onSuccess(ArrayList<String> days) {
                        selectedDay = days;
                        tagcontainer.setTags(days);
                        tagcontainer.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                    }

                    @Override
                    public void onRejected() {
                        dialog.dismiss();

                    }
                });

                dialog.show();
            }
        });
        edtStartEvrayDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                everyDayActive();
                PersianDate now = new PersianDate();
                type = 1;
                long justNow = System.currentTimeMillis();
                final Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(justNow);
                new DatePicker.Builder()
                        .id(1)
                        .theme(R.style.DialogTheme)
                        .date(calendar)
                        .build(DayRepeatDialog.this)
                        .show(supportedFragmentManager, "انتخاب تاریخ شروع");

            }
        });
        edtStartEachDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eachDayActive();
                long justNow = System.currentTimeMillis();
                final Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(justNow);
                new DatePicker.Builder()
                        .id(2)
                        .theme(R.style.DialogTheme)
                        .date(calendar)
                        .build(DayRepeatDialog.this)
                        .show(supportedFragmentManager, "انتخاب تاریخ شروع");
                type = 2;

            }
        });


    }

    private void everyWeekActive() {
        tagcontainer.setVisibility(View.VISIBLE);

        radioEvryDay.setChecked(false);
        radioEachDay.setChecked(false);
        radioEvryWeek.setChecked(true);


    }

    private void eachDayActive() {
        tagcontainer.setVisibility(View.GONE);
        radioEvryDay.setChecked(false);
        radioEvryWeek.setChecked(false);
        radioEachDay.setChecked(true);

    }

    private void everyDayActive() {
        tagcontainer.setVisibility(View.GONE);
        radioEvryDay.setChecked(true);
        radioEachDay.setChecked(false);
        radioEvryWeek.setChecked(false);


    }

    @Override
    public void onBackPressed() {
        myInterface.onRejected();
        super.onBackPressed();
    }

    boolean isCurrectSelectedDate(int year,int month,int day){
        if(year==nowPersianDate.getShYear()){
            if(month==nowPersianDate.getShMonth()){
                if(day>= nowPersianDate.getShDay()){
                    return true;
                }else {
                    return false;
                }
            }else if(month>nowPersianDate.getShMonth()){
                return true;
            }else {
                return false;
            }
        }else if(year>nowPersianDate.getShYear()){
            return true;
        }else {
            return false;
        }
    }



    @Override
    public void onDateSet(int id, @Nullable Calendar calendar, int day, int month, int year) {
        if(isCurrectSelectedDate(year,month,day)) {
            if (id == 1) {
                edtStartEvrayDay.setText(year + "/" + month + "/" + day);
                calendar.set(year,month,day,0,0,0);
                startTimeStamp = calendar.getTimeInMillis();
            } else if (id == 2) {
                edtStartEachDay.setText(year + "/" + month + "/" + day);
                calendar.set(year,month,day,0,0,0);
                startTimeStamp  = calendar.getTimeInMillis();

            }
        }else {
            Toast.makeText(context, "روز انتخاب شده نباید قبل از امروز باشد.", Toast.LENGTH_LONG).show();
        }
    }
}
