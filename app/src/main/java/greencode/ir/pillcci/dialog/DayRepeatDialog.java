package greencode.ir.pillcci.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.alirezaafkar.sundatepicker.DatePicker;
import com.alirezaafkar.sundatepicker.interfaces.DateSetListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.controler.AppController;
import greencode.ir.pillcci.utils.KeyboardUtil;
import greencode.ir.pillcci.utils.PersianCalculater;
import greencode.ir.pillcci.utils.Utility;
import me.omidh.liquidradiobutton.LiquidRadioButton;
import saman.zamani.persiandate.PersianDate;

/**
 * Created by alireza on 5/18/18.
 */

@SuppressLint("ValidFragment")
public class DayRepeatDialog extends BottomSheetDialogFragment implements DateSetListener {

    DayRepeatInterface myInterface;
    Context context;
    long startTimeStamp = 0;
    int dayEachRepeat=0;
    ArrayList<String> selectedDay = new ArrayList<>();
    PersianDate nowPersianDate;
    FragmentManager supportedFragmentManager;
    int type = 0;
    Activity activity;
    @BindView(R.id.radioEvryDay)
    LiquidRadioButton radioEvryDay;
    @BindView(R.id.txtStartEveryDay)
    TextView txtStartEveryDay;
    @BindView(R.id.edtStartEvrayDay)
    TextInputEditText edtStartEvrayDay;
    @BindView(R.id.lyStartEveryDay)
    RelativeLayout lyStartEveryDay;
    @BindView(R.id.radioEachDay)
    LiquidRadioButton radioEachDay;
    @BindView(R.id.dayRepeat)
    TextInputEditText dayRepeat;
    @BindView(R.id.edtStartEachDay)
    EditText edtStartEachDay;
    @BindView(R.id.lyStartEachDay)
    LinearLayout lyStartEachDay;
    @BindView(R.id.radioEvryWeek)
    LiquidRadioButton radioEvryWeek;
    @BindView(R.id.edtSelectedDay)
    TextInputEditText edtSelectedDay;
    @BindView(R.id.tagcontainer)
    TagContainerLayout tagcontainer;
    @BindView(R.id.radioBirth)
    LiquidRadioButton radioBirth;
    @BindView(R.id.edtBirthlUse)
    TextInputEditText edtBirthlUse;
    @BindView(R.id.edtBirthStop)
    TextInputEditText edtBirthStop;
    @BindView(R.id.edtStartBirth)
    TextInputEditText edtStartBirth;
    @BindView(R.id.edtPastDay)
    TextInputEditText edtPastDay;
    @BindView(R.id.btnCancle)
    Button btnCancle;
    @BindView(R.id.btnOk)
    Button btnOk;
    @BindView(R.id.lyBirth)
    LinearLayout lyBirth;


    public DayRepeatDialog(Context context, Activity activity, FragmentManager manager,
                           int typeDayUsage, ArrayList<String> daysOfUsage, int dayRepeat, long ourStartTimeStamp) {
        this.context = context;
        this.activity = activity;
        supportedFragmentManager = manager;
        this.startTimeStamp= ourStartTimeStamp;
        this.type = typeDayUsage;
        this.selectedDay = daysOfUsage;
        this.dayEachRepeat  =dayRepeat;


    }

    public void hideKeyboar(){
        InputMethodManager imm = (InputMethodManager) AppController.getCurrentActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = dayRepeat;
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(AppController.getCurrentActivity());
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new BottomSheetDialog(getContext()) {

            @Override
            public void onBackPressed() {
                myInterface.onRejected();
            }
        };
        return dialog;
    }

    public void setListener(DayRepeatInterface listener) {
        this.myInterface = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(context, R.layout.dialog_dayly_usage, null);
        ButterKnife.bind(this, view);


        nowPersianDate = new PersianDate(System.currentTimeMillis());
        startTimeStamp =(startTimeStamp>0?startTimeStamp:System.currentTimeMillis());
        setCancelable(false);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert();
            }
        });

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterface.onRejected();
            }
        });
        dayRepeat.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    radioEachDay.setChecked(true);
                }
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
            public void onSelectedTagDrag(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {
                selectedDay.remove(position);
                tagcontainer.setTags(selectedDay);
            }
        });

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
                    dayRepeat.requestFocus();
                }else {
                    hideKeyboar();
                    dayRepeat.clearFocus();
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
        radioBirth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    birthActive();



                }
            }
        });
        edtSelectedDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                everyWeekActive();
                final DaySelectedDialog dialog = new DaySelectedDialog(context, selectedDay);
                dialog.setListener(new DayOfWeekInterface() {
                    @Override
                    public void onSuccess(ArrayList<String> days,PersianDate persianDate) {
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
        edtStartBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersianDate now = new PersianDate();
                type =4;
                long justNow = System.currentTimeMillis();
                final Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(justNow);
                new DatePicker.Builder()
                        .id(4)
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
        new KeyboardUtil(getActivity(),view);
        makeLastState();

        return view;
    }

    private void makeLastState() {
        setTodayToStarters();
        if(this.type==1 || type==0){
            radioEvryDay.setChecked(true);
        }else if(this.type==2) {
            radioEachDay.setChecked(true);
        }else if(this.type==3){
            radioEvryWeek.setChecked(true);
            tagcontainer.setTags(selectedDay);
        }else {
            radioBirth.setChecked(true);
            edtBirthlUse.setText(selectedDay.get(0));
            edtBirthStop.setText(selectedDay.get(1));
            if(selectedDay.size()>1)
                edtPastDay.setText(selectedDay.get(2));
        }
        dayRepeat.setText(dayEachRepeat==0?"":dayEachRepeat+"");
    }

    private void setTodayToStarters() {
        String today = PersianCalculater.getYearMonthAndDay(startTimeStamp);
        edtStartEvrayDay.setText(today);
        edtStartEachDay.setText(today);
        edtStartBirth.setText(today);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   //     setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);


    }

    private void insert() {
        if (radioEvryDay.isChecked()) {
            if (edtStartEvrayDay.getText().toString().length() != 0) {
                selectedDay = new ArrayList<>();

                myInterface.onSuccess(1, 0, edtStartEvrayDay.getText().toString(), "هر روز", selectedDay, startTimeStamp);


            } else {
                Toast toast  =Toast.makeText(context, "تاریخ شروع انتخاب نشده است.", Toast.LENGTH_LONG);
                Utility.centrizeAndShow(toast);

            }
        } else if (radioEachDay.isChecked()) {
            if (edtStartEachDay.getText().toString().length() == 0) {
                Toast toast = Toast.makeText(context, "تاریخ شروع انتخاب نشده است.", Toast.LENGTH_LONG);
                Utility.centrizeAndShow(toast);
            } else if (dayRepeat.getText().toString().length() == 0) {
                Toast toast = Toast.makeText(context, "توالی روزها انتخاب نشده است.", Toast.LENGTH_LONG);
                Utility.centrizeAndShow(toast);
            } else {
                selectedDay = new ArrayList<>();
                myInterface.onSuccess(2, Integer.parseInt(dayRepeat.getText().toString()), edtStartEachDay.getText().toString(), "هر " + dayRepeat.getText().toString() + " روز ", selectedDay, startTimeStamp);
            }
        } else if (radioEvryWeek.isChecked()) {
            if (selectedDay.size() == 0) {
                Toast toast = Toast.makeText(context, "هیچ روزی انتخاب نشده است.", Toast.LENGTH_LONG);
                Utility.centrizeAndShow(toast);
            } else {

                PersianDate tempPersianCalendar = nowPersianDate;
                while (!selectedDay.contains(tempPersianCalendar.dayName())/*!tempPersianCalendar.getPersianWeekDayName().equals(selectedDay.get(0).trim())*/) {
                    tempPersianCalendar.addDay(1);
                }

                String startday = tempPersianCalendar.dayName();
                int index = selectedDay.indexOf(startday);
                ArrayList<String> newSelectedDay = new ArrayList<>();
                for (int i = index; i < selectedDay.size(); i++) {
                    newSelectedDay.add(selectedDay.get(i));
                }
                for (int i = 0; i < index; i++) {
                    newSelectedDay.add(selectedDay.get(i));
                }
                myInterface.onSuccess(3, 0, PersianCalculater.getYearMonthAndDay(tempPersianCalendar.getTime()), "روزهای ", newSelectedDay, tempPersianCalendar.getTime());
            }
        } else if(radioBirth.isChecked()){
            if(edtBirthlUse.getText().toString().length()==0){
                Toast toast = Toast.makeText(context, "تعداد روزهای مصرف مشخص نشده است.", Toast.LENGTH_LONG);
                Utility.centrizeAndShow(toast);
            }else if(edtBirthStop.getText().length()==0){
                Toast toast = Toast.makeText(context, "تعداد روزهای توقف مصرف مشخص نشده است.", Toast.LENGTH_LONG);
                Utility.centrizeAndShow(toast);

            }else {
                String passtDays = edtPastDay.getText().toString().equals("")?"0":edtPastDay.getText().toString();
                int past = Integer.parseInt(passtDays);
                int use  = Integer.parseInt(edtBirthlUse.getText().toString());
                if(past>=use){
                    Toast toast = Toast.makeText(context, "تعداد روزهای سپری شده نباید از روزهای چرخه بیشتر باشد.", Toast.LENGTH_SHORT);
                    Utility.centrizeAndShow(toast);
                    return;
                }
                selectedDay=new ArrayList<>();
                selectedDay.add(edtBirthlUse.getText().toString());
                selectedDay.add(edtBirthStop.getText().toString());
                selectedDay.add(edtPastDay.getText().toString());
                myInterface.onSuccess(4, 0, edtStartBirth.getText().toString(), "چرخه ضد بارداری", selectedDay, startTimeStamp);

            }
        }
        else {
           Toast toast = Toast.makeText(context, "یکی از نحوه های زمان های موجود را انتخاب کنید.", Toast.LENGTH_LONG);
           Utility.centrizeAndShow(toast);
        }
    }


    private void everyWeekActive() {
        edtSelectedDay.setVisibility(View.VISIBLE);
        tagcontainer.setVisibility(View.VISIBLE);
        lyStartEachDay.setVisibility(View.GONE);
        lyStartEveryDay.setVisibility(View.GONE);
        lyBirth.setVisibility(View.GONE);
        radioEvryDay.setChecked(false);
        radioEachDay.setChecked(false);
        radioBirth.setChecked(false);


    }

    private void birthActive() {
        lyBirth.setVisibility(View.VISIBLE);
        lyStartEveryDay.setVisibility(View.GONE);
        lyStartEachDay.setVisibility(View.GONE);
        tagcontainer.setVisibility(View.GONE);
        edtSelectedDay.setVisibility(View.GONE);
        radioEvryDay.setChecked(false);
        radioEachDay.setChecked(false);
        radioEvryWeek.setChecked(false);


    }

    private void eachDayActive() {
      lyStartEveryDay.setVisibility(View.GONE);
        edtSelectedDay.setVisibility(View.GONE);
        lyStartEachDay.setVisibility(View.VISIBLE);
        tagcontainer.setVisibility(View.GONE);
        lyBirth.setVisibility(View.GONE);
        radioEvryDay.setChecked(false);
        radioEvryWeek.setChecked(false);
        radioBirth.setChecked(false);


    }

    private void everyDayActive() {
        lyStartEveryDay.setVisibility(View.VISIBLE);
        edtSelectedDay.setVisibility(View.GONE);
        lyStartEachDay.setVisibility(View.GONE);
        tagcontainer.setVisibility(View.GONE);
        lyBirth.setVisibility(View.GONE);
        radioEachDay.setChecked(false);
        radioEvryWeek.setChecked(false);
        radioBirth.setChecked(false);


    }



    boolean isCurrectSelectedDate(int year, int month, int day) {
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


    @Override
    public void onDateSet(int id, @Nullable Calendar calendar, int day, int month, int year) {
        if (isCurrectSelectedDate(year, month, day)) {
            if (id == 1) {
                edtStartEvrayDay.setText(PersianCalculater.getYearMonthAndDay(year,month,day));

            } else if (id == 2) {
                edtStartEachDay.setText(PersianCalculater.getYearMonthAndDay(year,month,day));
            }else if(id==4){
                edtStartBirth.setText(PersianCalculater.getYearMonthAndDay(year,month,day));
            }
            PersianDate date = new PersianDate(System.currentTimeMillis());
            date.setShYear(year);
            date.setShMonth(month);
            date.setShDay(day);
            startTimeStamp = date.getTime();
        } else {
           Toast toast = Toast.makeText(context, "روز انتخاب شده نباید قبل از امروز باشد.", Toast.LENGTH_LONG);
           Utility.centrizeAndShow(toast);
        }
    }

}
