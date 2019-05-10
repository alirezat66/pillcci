package greencode.ir.pillcci.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.adapter.EditEachTimeAdapter;
import greencode.ir.pillcci.objects.EachUsage;
import greencode.ir.pillcci.timepicker.TimePickerDialog;
import greencode.ir.pillcci.timepicker.listener.OnDateSetListener;
import greencode.ir.pillcci.utils.KeyboardUtil;
import greencode.ir.pillcci.utils.PersianCalculater;
import greencode.ir.pillcci.utils.Utility;
import saman.zamani.persiandate.PersianDate;

/**
 * Created by alireza on 5/18/18.
 */

@SuppressLint("ValidFragment")
public class EachTimeEditPartDialog extends BottomSheetDialogFragment implements EditEachTimeAdapter.SelectListener, OnDateSetListener {

    EachTimeEditPart myInterface;
    Context context;

    String startTime;
    String unit;
    int count;
    double diffrenceOfUsage;
    double eachTimeUsage;
    EditEachTimeAdapter adapter;
    PersianDate nowPersianDate;
    PersianDate minPersianDate;
    PersianDate maxPersianDate;

    FragmentManager supportedFragmentManager;
    EachUsage data;
    long startTimeDate;
    @BindView(R.id.edtStartEvrayDay)
    TextInputEditText edtStartEvrayDay;
    @BindView(R.id.container)
    RecyclerView recyclerView;
    @BindView(R.id.btnCancle)
    Button btnCancle;
    @BindView(R.id.btnOk)
    Button btnOk;
    Unbinder unbinder;

    @SuppressLint("ValidFragment")
    public EachTimeEditPartDialog(Context context, int startTimeHour, int startTimeMin, int countOfUsagePerDay, double diffrenceOfUsage, String unitUse, double eachTimeUsage, FragmentManager manager, long startTimeDate) {
        this.context = context;
        String startStr;
        String startMinStr;
        this.supportedFragmentManager = manager;
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
        this.startTime = startStr + ":" + startMinStr;
        this.count = countOfUsagePerDay;
        this.unit = unitUse;
        this.diffrenceOfUsage = diffrenceOfUsage;
        this.eachTimeUsage = eachTimeUsage;
        this.startTimeDate = startTimeDate;
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
    public void setListener(EachTimeEditPart listener) {
        this.myInterface = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(context, R.layout.dialog_each_time_edit_part, null);
        ButterKnife.bind(this, view);
        edtStartEvrayDay.setText(startTime);

        ArrayList<EachUsage> list = new ArrayList<>();
        list = makeAllTimes(count, startTime, startTimeDate, diffrenceOfUsage);
        edtStartEvrayDay.setText(calculateFirst(list));
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        adapter = new EditEachTimeAdapter(context, list, EachTimeEditPartDialog.this);
        recyclerView.setAdapter(adapter);
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
                    myInterface.onSuccess(adapter.getList());
                } else {
                    Toast toast = Toast.makeText(context, "میزان مصرف نمی‌تواند مقدار خالی داشته باشد.", Toast.LENGTH_LONG);
                    Utility.centrizeAndShow(toast);
                }
            }
        });
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterface.onRejected();
            }
        });

        new KeyboardUtil(getActivity(),view);

        return view;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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


   /* @Override
    public void selectTime(EachUsage eachUsage) {
        data = eachUsage;

        nowPersianDate = new PersianDate(System.currentTimeMillis());

        String[] times = eachUsage.getTimeStr().split(":");
        int hour = Integer.parseInt(times[0]);
        int min = Integer.parseInt(times[1]);
        nowPersianDate.setHour(hour);
        nowPersianDate.setMinute(min);


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
                .setCallBack(EachTimeEditPartDialog.this)
                .build();
        dialog.show(supportedFragmentManager, "انتخاب زمان");
    }
*/
    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {

        PersianDate selectedDate = new PersianDate(data.getStartDay());
        PersianDate selectedHMDate = new PersianDate(millseconds);
        selectedDate.setHour(selectedHMDate.getHour());
        selectedDate.setMinute(selectedHMDate.getMinute());
        if (selectedDate.getTime() < startTimeDate) {
            Toast toast = Toast.makeText(context, "زمان انتخاب شده نباید قبل از زمان شروع باشد.", Toast.LENGTH_SHORT);
            Utility.centrizeAndShow(toast);
        } else {
            adapter.updatePilTime(data, selectedDate);
            edtStartEvrayDay.setText(calculateFirst(adapter.getList()));
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void selectTime(EachUsage eachUsage, int position) {

    }
}
