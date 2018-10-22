package greencode.ir.pillcci.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alirezaafkar.sundatepicker.DatePicker;
import com.alirezaafkar.sundatepicker.interfaces.DateSetListener;
import com.github.florent37.expansionpanel.ExpansionLayout;
import com.github.florent37.expansionpanel.viewgroup.ExpansionLayoutCollection;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.ganfra.materialspinner.MaterialSpinner;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.activities.AddMedicianActivity;
import greencode.ir.pillcci.activities.BirthControlActivity;
import greencode.ir.pillcci.activities.EachTimeEditPartActivity;
import greencode.ir.pillcci.dialog.DayOfWeekInterface;
import greencode.ir.pillcci.dialog.DaySelectedDialog;
import greencode.ir.pillcci.dialog.EachTimeDialog;
import greencode.ir.pillcci.dialog.EachTimeInterface;
import greencode.ir.pillcci.dialog.TimeIntervalDialog;
import greencode.ir.pillcci.dialog.TimeIntervalInterface;
import greencode.ir.pillcci.dialog.UsageCountDialog;
import greencode.ir.pillcci.dialog.UsageCountInterface;
import greencode.ir.pillcci.objects.EachUsage;
import greencode.ir.pillcci.objects.UsageFields;
import greencode.ir.pillcci.timepicker.TimePickerDialog;
import greencode.ir.pillcci.timepicker.listener.OnDateSetListener;
import greencode.ir.pillcci.utils.PersianCalculater;
import greencode.ir.pillcci.utils.Utility;
import me.omidh.liquidradiobutton.LiquidRadioButton;
import saman.zamani.persiandate.PersianDate;

import static android.app.Activity.RESULT_OK;

/**
 * Created by alireza on 5/15/18.
 */

public class FragmentUsageMedic extends Fragment implements OnDateSetListener, DateSetListener {
    onActionStepTwo onAction;


    int dayRepeat = 0;
    int typeDayUsage = 1;
    String usageStartDate = "";
    ArrayList<String> daysOfUsage = new ArrayList<>();
    int countOfUsagePerDay = 1;
    double diffrenceOfUsage = 24;
    int startTimeHour = 8;
    int startTimeMin = 0;
    double countEachUse = 1;
    String unitUse = "عدد";
    int isRegular = 1;

    long ourStartTimeStamp = 0;
    ArrayList<String> unitsCount;
    ArrayList<Long> unitTimes;
    TimePickerDialog timePickerDialog;
    @BindView(R.id.txtDaysOfUse)
    TextView txtDaysOfUse;
    @BindView(R.id.txtTypeOfDays)
    TextView txtTypeOfDays;
    @BindView(R.id.txtTitleDate)
    TextView txtTitleDate;
    @BindView(R.id.txtStartDate)
    TextView txtStartDate;
    @BindView(R.id.txtStartTime)
    TextView txtStartTime;
    @BindView(R.id.txtStartUsageDate)
    TextView txtStartUsageDate;
    @BindView(R.id.txtStartUsageTime)
    TextView txtStartUsageTime;
    @BindView(R.id.txtTitlePerDay)
    TextView txtTitlePerDay;
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
    @BindView(R.id.btnInsert)
    Button btnInsert;
    @BindView(R.id.btnDelete)
    Button btnDelete;
    @BindView(R.id.radioEvryDay)
    LiquidRadioButton radioEvryDay;
    @BindView(R.id.radioDaysInterval)
    LiquidRadioButton radioDaysInterval;
    @BindView(R.id.radioSpecificDays)
    LiquidRadioButton radioSpecificDays;
    @BindView(R.id.radiobirthControl)
    LiquidRadioButton radiobirthControl;
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
    @BindView(R.id.headerIndicator4)
    AppCompatImageView headerIndicator4;
    @BindView(R.id.expansionLayout4)
    ExpansionLayout expansionLayout4;
    @BindView(R.id.expansionLayout)
    ExpansionLayout expansionLayout;
    @BindView(R.id.expansionLayout3)
    ExpansionLayout expansionLayout3;
    @BindView(R.id.expansionLayout2)
    ExpansionLayout expansionLayout2;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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

                onAction.onSaveButtonTwo(new UsageFields(typeDayUsage, txtStartDate.getText().toString(), dayRepeat, isRegular, daysOfUsage, typeDayUsage,
                        diffrenceOfUsage, countOfUsagePerDay, startTimeHour, startTimeMin, unitUse, unitsCount, unitTimes, description.toString(), ourStartTimeStamp));


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
                PersianDate persianDate = new PersianDate(System.currentTimeMillis());
                changeDate(persianDate);

            } else {
                backToLastState(typeDayUsage);
            }
        }
    }
     ExpansionLayoutCollection expansionsCollection;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_medician_step_two, container, false);
        ButterKnife.bind(this, view);
        Utility.hideKeyboard();

         expansionsCollection = new ExpansionLayoutCollection();
        expansionsCollection.add(expansionLayout);
        expansionsCollection.add(expansionLayout2);
        expansionsCollection.add(expansionLayout3);
        expansionsCollection.add(expansionLayout4);
        expansionsCollection.openOnlyOne(true);
        if (AddMedicianActivity.getUsageFields() != null) {

            setDefualts(AddMedicianActivity.getUsageFields());
        } else {
            setDefualts();
        }
        return view;
    }

    private void setDefualts() {
        long timeStamp = System.currentTimeMillis();
        PersianDate persianDate = new PersianDate(timeStamp);
        persianDate.setHour(8);
        persianDate.setMinute(0);
        persianDate.setSecond(0);
        ourStartTimeStamp = persianDate.getTime();
        startTimeHour = 8;
        startTimeMin = 0;
        txtStartUsageDate.setText(PersianCalculater.getYearMonthAndDay(persianDate.getTime()));
        txtStartUsageTime.setText("8:00");
        txtStartDate.setText(PersianCalculater.getYearMonthAndDay(persianDate.getTime()));
        txtStartTime.setText("8:00" + " - ");

        final String[] ITEMS = {"عدد", "قرص", "کپسول", "سی سی/میلی لیتر", "قاشق چای خوری", "بار", "قاشق غذا خوری",
                "قطره", "پیمانه", "پاف/اسپری", "گرم", "میلی گرم", "قطعه/تکه", "برچسب"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edtEachTime.setText(countEachUse+"");
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
        spinner.setSelection(0);
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
                expansionLayout3.collapse(true);
                txtPerDay.setText(countOfUsagePerDay + " نوبت ");
            }

        });


    }

    private void setDefualts(UsageFields usageFields) {
        ourStartTimeStamp = usageFields.getStartTimeStamp();
        PersianDate persianDate = new PersianDate(ourStartTimeStamp);
        typeDayUsage = usageFields.getTypeOfdayUsage();
        daysOfUsage = usageFields.getDays();
        dayRepeat = usageFields.getRepeatDays();
        backToLastState(typeDayUsage);
        startTimeHour =usageFields.getStartHour();
        startTimeMin = usageFields.getStartMin();
        persianDate.setHour(startTimeHour);
        persianDate.setMinute(startTimeMin);
        ourStartTimeStamp = persianDate.getTime();
        txtStartUsageDate.setText(PersianCalculater.getYearMonthAndDay(persianDate.getTime()));
        txtStartUsageTime.setText(PersianCalculater.getHourseAndMin(ourStartTimeStamp));
        txtStartDate.setText(PersianCalculater.getYearMonthAndDay(ourStartTimeStamp));
        txtStartTime.setText(PersianCalculater.getHourseAndMin(ourStartTimeStamp) + " - ");

        final String[] ITEMS = {"عدد", "قرص", "کپسول", "سی سی/میلی لیتر", "قاشق چای خوری", "بار", "قاشق غذا خوری",
                "قطره", "پیمانه", "پاف/اسپری", "گرم", "میلی گرم", "قطعه/تکه", "برچسب"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        try {
            countEachUse = Double.parseDouble(usageFields.getUnitsCount().get(0));
            edtEachTime.setText(usageFields.getUnitsCount().get(0));

        } catch (NumberFormatException ex) {
            ex.printStackTrace();
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
            if (ITEMS[i].equals(usageFields.getUnitUsage())) {
                pos = i;
                txtUsage.setText(ITEMS[i]);

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

        switch (usageFields.getCountOfUsagePerDay()) {
            case 1:
                countOfUsagePerDay = 1;
                diffrenceOfUsage = 24;
                radio0.setChecked(true);
                break;
            case 2:
                countOfUsagePerDay = 2;
                diffrenceOfUsage = 12;
                radio1.setChecked(true);
                break;
            case 3:
                countOfUsagePerDay = 3;
                diffrenceOfUsage = 8;
                radio2.setChecked(true);
            case 4:
                countOfUsagePerDay = 4;

                diffrenceOfUsage = 6;
                radio5.setChecked(true);
                break;

            case 6:
                countOfUsagePerDay = 6;
                diffrenceOfUsage = 4;
                radio7.setChecked(true);
                break;

            case 8:
                countOfUsagePerDay = 8;
                diffrenceOfUsage = 3;
                radio8.setChecked(true);
                break;
            case 12:
                countOfUsagePerDay = 12;
                diffrenceOfUsage = 2;
                radio8.setChecked(true);
                break;
            case 24:
                countOfUsagePerDay = 24;
                diffrenceOfUsage = 1;
                radio9.setChecked(true);
                break;


        }
        txtPerDay.setText(countOfUsagePerDay + " نوبت ");
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
                expansionLayout3.collapse(true);

            }

        });




    }

    @Override
    public void onStart() {
       /* if (!useDays.getText().toString().equals("")) {
            usePartDays.setEnabled(true);
        }
        if (!usePartDays.getText().toString().equals("")) {
            edtStartTime.setEnabled(true);
        }
        if (!edtStartTime.getText().toString().equals("")) {
            edtUseEachTime.setEnabled(true);
        }*/
        edtDescription.setEnabled(true);
        super.onStart();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAction = (onActionStepTwo) context;


    }


    @OnClick({R.id.btnInsert, R.id.btnDelete, R.id.radioEvryDay, R.id.radioDaysInterval, R.id.radioSpecificDays, R.id.radiobirthControl, R.id.txtStartUsageDate, R.id.txtStartUsageTime})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.useDays:
                showDialogOne();
                break;
            case R.id.usePartDays:
                showDialogTwo();

                break;
            case R.id.edtStartTime:
                showDialogThree();
                break;
            case R.id.edtUseEachTime:
                showDialogFour();
                break;
            case R.id.btnInsert:


                if (countEachUse == 0) {
                    Toast.makeText(getContext(), "تمامی موارد بجز توضیحات را وارد کن.", Toast.LENGTH_LONG).show();
                } else {


                    showLastPage();
                }

                break;
            case R.id.btnDelete:
                onAction.onCanceleTwo();
                break;
            case R.id.radioEvryDay:
                txtTypeOfDays.setText("(هر روز)");
                typeDayUsage = 1;
                dayRepeat = 0;
                isRegular = 1;
                PersianDate persianDate = new PersianDate(System.currentTimeMillis());
                changeDate(persianDate);


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
                        .show(getFragmentManager(), "انتخاب تاریخ شروع");
                break;
            case R.id.txtStartUsageTime:
                timePickerDialog = Utility.getTimeDialog(this, getResources().getColor(R.color.colorPrimary));

                timePickerDialog.show(getFragmentManager(), "انتخاب زمان");
                break;


        }
    }

    private void showLastPage() {

        Intent intent = new Intent(getContext(), EachTimeEditPartActivity.class);
        intent.putExtra("startTimeHour", startTimeHour);
        intent.putExtra("startTimeMin", startTimeMin);
        intent.putExtra("countOfUsagePerDay", countOfUsagePerDay);
        intent.putExtra("diffrenceOfUsage", diffrenceOfUsage);
        intent.putExtra("unitUse", unitUse);
        intent.putExtra("eachTimeUsage", countEachUse);
        intent.putExtra("startTimeDate", ourStartTimeStamp);

        startActivityForResult(intent, 2910);


    }

    private void showDialogBirthControl() {
        Intent intent = new Intent(getContext(), BirthControlActivity.class);
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

    private void showDialogSelectDay() {
        final DaySelectedDialog dialog = new DaySelectedDialog(getContext(), daysOfUsage);
        dialog.setListener(new DayOfWeekInterface() {
            @Override
            public void onSuccess(ArrayList<String> days,PersianDate persianDate) {


                changeDate(persianDate);
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

    private void showDialogInterval() {

        final TimeIntervalDialog dialog = new TimeIntervalDialog(getContext(), (dayRepeat >= 2 ? dayRepeat : 2), typeDayUsage);
        dialog.setListener(new TimeIntervalInterface() {
            @Override
            public void onSuccess(int days) {
                dayRepeat = days;
                isRegular = 1;
                typeDayUsage = 2;
                txtTypeOfDays.setText("(هر " + days + " روز)");
                PersianDate persianDate = new PersianDate(System.currentTimeMillis());
                changeDate(persianDate);
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

    private void backToLastState(int lastType) {
        if (lastType == 1) {
            radioEvryDay.setChecked(true);
            txtTypeOfDays.setText(")هر روز)");
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

    private void showDialogFour() {
        final EachTimeDialog eachTimeDialog = new EachTimeDialog(getContext(), unitUse, countEachUse);
        eachTimeDialog.setListener(new EachTimeInterface() {
            @Override
            public void onSuccess(double count, String unit) {
                countEachUse = count;
                unitUse = unit;
                eachTimeDialog.dismiss();


/*

                final EachTimeEditPartDialog editDialog = new EachTimeEditPartDialog(getContext(),startTimeHour,startTimeMin,countOfUsagePerDay,diffrenceOfUsage,unitUse,countEachUse,getFragmentManager(),ourStartTimeStamp);
                editDialog.setListener(new EachTimeEditPart() {
                    @Override
                    public void onSuccess(ArrayList<EachUsage> usages) {
                        unitsCount = new ArrayList<>();
                        unitTimes = new ArrayList<>();
                        for(EachUsage use:usages){
                            unitsCount.add(use.getEachUse());
                            unitTimes.add(use.getStartDay());
                        }


                        String temp = unitsCount.get(0);
                        boolean allSame = true;
                        for(String use : unitsCount){
                            if(!use.equals(temp)){
                                allSame = false;
                                break;
                            }
                        }

                        if(allSame){
                            edtUseEachTime.setText(temp+" "+unitUse+" در هر وعده");
                        }else {
                            String finalStr = "به ترتیب ";
                            for(String use : unitsCount){
                                finalStr+=use;
                                finalStr+=",";
                            }
                            finalStr = finalStr.substring(0,finalStr.length()-1);
                            finalStr=finalStr+" "+unitUse;
                            finalStr+=" در ساعت(های): ";
                            for(long timeStr:unitTimes){
                                finalStr+=PersianCalculater.getHourseAndMin(timeStr);
                                finalStr+=",";
                            }
                            finalStr=finalStr.substring(0,finalStr.length()-1);
                            edtUseEachTime.setText(finalStr);
                        }
                        edtDescription.setEnabled(true);
                        edtDescription.setFocusable(true);
                        String str = edtUseEachTime.getText().toString().replace(".0","");
                        edtUseEachTime.setText(str);
                        editDialog.dismiss();

                    }

                    @Override
                    public void onRejected() {
                        editDialog.dismiss();

                    }
                });
                editDialog.show(getActivity().getSupportFragmentManager(), eachTimeDialog.getTag());
*/


            }

            @Override
            public void onRejected() {
                eachTimeDialog.dismiss();
            }
        });
        eachTimeDialog.show(getActivity().getSupportFragmentManager(), eachTimeDialog.getTag());
    }

    private void showDialogThree() {

        timePickerDialog = Utility.getTimeDialog(this, getResources().getColor(R.color.colorPrimary));

        timePickerDialog.show(getFragmentManager(), "انتخاب زمان");
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

    private void showDialogTwo() {
        final UsageCountDialog dialogUsage = new UsageCountDialog(getContext(), countOfUsagePerDay);
        dialogUsage.setListener(new UsageCountInterface() {
            @Override
            public void onSuccess(int selected, String title, int count, double difrent) {
                diffrenceOfUsage = difrent;
                countOfUsagePerDay = count;
               /* usePartDays.setText(title);
                edtStartTime.setEnabled(true);*/
                //  disablePartFour();
                dialogUsage.dismiss();
            }

            @Override
            public void onRejected() {
                dialogUsage.dismiss();
            }
        });
        dialogUsage.show();

    }

    private void showDialogOne() {
       /* final DayRepeatDialog dialog = new DayRepeatDialog(getContext(), getActivity(), getActivity().getSupportFragmentManager(),
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
        dialog.show(getActivity().getSupportFragmentManager(), dialog.getTag());*/
    }


    private void disablePartFour() {
        /*edtUseEachTime.setText("");
        countEachUse = 0;
        unitUse = "";*/
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        PersianDate persianCalendar = new PersianDate(millseconds);
        PersianDate ourTime = new PersianDate(ourStartTimeStamp);
        ourTime.setHour(persianCalendar.getHour());
        ourTime.setMinute(persianCalendar.getMinute());
        startTimeHour = persianCalendar.getHour();
        startTimeMin = persianCalendar.getMinute();
        txtStartTime.setText(PersianCalculater.getHourseAndMin(millseconds) + " - ");
        txtStartUsageTime.setText(PersianCalculater.getHourseAndMin(millseconds));
/*
        edtStartTime.setText(PersianCalculater.getHourseAndMin(millseconds));
*/
        //disableAfterThree();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

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
            Toast.makeText(getContext(), "روز انتخاب شده نباید قبل از امروز باشد.", Toast.LENGTH_LONG).show();
        }
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

    public interface onActionStepTwo {
        public void onSaveButtonTwo(UsageFields usageFields);

        public void onCanceleTwo();
    }

}
