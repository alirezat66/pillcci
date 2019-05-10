package greencode.ir.pillcci.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import greencode.ir.pillcci.R;
import greencode.ir.pillcci.database.PillUsage;
import greencode.ir.pillcci.timepicker.TimePickerDialog;
import greencode.ir.pillcci.timepicker.listener.OnDateSetListener;
import greencode.ir.pillcci.utils.PersianCalculater;
import greencode.ir.pillcci.utils.Utility;
import me.omidh.liquidradiobutton.LiquidRadioButton;
import saman.zamani.persiandate.PersianDate;

/**
 * Created by alireza on 5/18/18.
 */

public class DialogUsagePicker extends Dialog implements OnDateSetListener {

    TimeUsageInterface myInterface;
    Context context;

    int type = 0;
    Activity activity;
    LiquidRadioButton radioNow;
    LiquidRadioButton radioSetTime;
    LiquidRadioButton radioTimeUsage;
    Button btnCancle;
    Button btnOk;
    long setedTime;
    TimePickerDialog dialog;
    PillUsage usage;
    FragmentManager supportedFragmentManager;

    long selectedTime=0;
    public DialogUsagePicker(Context context, Activity activity, FragmentManager manager, long setedTime, PillUsage usage) {
        super(context);
        this.context = context;
        this.activity = activity;
        this.setedTime = setedTime;
        this.supportedFragmentManager = manager;
        this.usage = usage;
    }

    public void setListener(TimeUsageInterface listener) {
        this.myInterface = listener;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        View view = View.inflate(context, R.layout.dialog_set_use_type, null);
        setContentView(view);
        WindowManager.LayoutParams params = getWindow().getAttributes();

        params.width = -1;
        getWindow().setAttributes(params);


        radioNow=findViewById(R.id.radioNow);
        radioSetTime=findViewById(R.id.radioSetTime);
        radioTimeUsage=findViewById(R.id.radioTimeUsage);
        btnCancle=findViewById(R.id.btnCancle);
        btnOk=findViewById(R.id.btnOk);
        radioSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = Utility.getTimeDialogUsage(DialogUsagePicker.this,context.getResources().getColor(R.color.colorPrimary));
                dialog.show(supportedFragmentManager,"انتخاب زمان");
            }
        });
      /*  radioSetTime.setOnCheckedChangeListener(new LiquidRadioButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    dialog = Utility.getTimeDialog(DialogUsagePicker.this,context.getResources().getColor(R.color.colorPrimary));
                    dialog.show(supportedFragmentManager,"انتخاب زمان");
                }
            }
        });*/

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterface.onRejected();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioNow.isChecked()){
                    myInterface.onSuccess(System.currentTimeMillis());
                }else if(radioTimeUsage.isChecked()){
                  myInterface.onSuccess(usage.getUsageTime());
                }else if(radioSetTime.isChecked()){
                        myInterface.onSuccess(selectedTime);
                }else {
                    Toast toast = Toast.makeText(context, "لطفا یکی از گزینه ها را انتخاب کنید.", Toast.LENGTH_LONG);
                    Utility.centrizeAndShow(toast);

                }
            }
        });


    }


    @Override
    public void onBackPressed() {
        myInterface.onRejected();
        super.onBackPressed();
    }


    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        PersianDate persianCalendar = new PersianDate(millseconds);
        PersianDate persianDate = new PersianDate(setedTime);
        persianDate.setHour(persianCalendar.getHour());
        persianDate.setMinute(persianCalendar.getMinute());
        persianDate.setSecond(0);
        selectedTime=persianDate.getTime();
        radioSetTime.setText("در ساعت ("+ PersianCalculater.getHourseAndMin(millseconds)+")");
    }


}
