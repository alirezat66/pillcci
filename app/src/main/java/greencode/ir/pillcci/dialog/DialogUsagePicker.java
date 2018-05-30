package greencode.ir.pillcci.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import greencode.ir.pillcci.R;
import greencode.ir.pillcci.utils.Utility;
import me.omidh.liquidradiobutton.LiquidRadioButton;
import saman.zamani.persiandate.PersianDate;

/**
 * Created by alireza on 5/18/18.
 */

public class DialogUsagePicker extends BottomSheetDialog implements OnDateSetListener {

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

    android.support.v4.app.FragmentManager supportedFragmentManager;

    long selectedTime=0;
    public DialogUsagePicker(Context context, Activity activity,android.support.v4.app.FragmentManager manager,long setedTime) {
        super(context);
        this.context = context;
        this.activity = activity;
        this.setedTime = setedTime;
        this.supportedFragmentManager = manager;
    }

    public void setListener(TimeUsageInterface listener) {
        this.myInterface = listener;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        View view = View.inflate(context, R.layout.dialog_set_use_type, null);
        setContentView(view);

        radioNow=findViewById(R.id.radioNow);
        radioSetTime=findViewById(R.id.radioSetTime);
        radioTimeUsage=findViewById(R.id.radioTimeUsage);
        btnCancle=findViewById(R.id.btnCancle);
        btnOk=findViewById(R.id.btnOk);
        radioSetTime.setOnCheckedChangeListener(new LiquidRadioButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    dialog = Utility.getTimeDialog(DialogUsagePicker.this,context.getResources().getColor(R.color.colorPrimary));
                    dialog.show(supportedFragmentManager,"انتخاب زمان");
                }
            }
        });

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
                    myInterface.onSuccess(setedTime);
                }else {
                        myInterface.onSuccess(selectedTime);
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
        selectedTime=millseconds;
        radioSetTime.setText("تنظیم ساعت ("+persianCalendar.getHour()+":"+persianCalendar.getMinute()+")");
    }


}
