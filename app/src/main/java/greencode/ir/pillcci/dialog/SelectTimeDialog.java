package greencode.ir.pillcci.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mahfa.dnswitch.DayNightSwitch;
import com.mahfa.dnswitch.DayNightSwitchListener;
import com.nex3z.togglebuttongroup.SingleSelectToggleGroup;
import com.nex3z.togglebuttongroup.button.CircularToggle;

import greencode.ir.pillcci.R;

/**
 * Created by alireza on 5/18/18.
 */

public class SelectTimeDialog extends BottomSheetDialog {

    SelectTimeInterface myInterface;
    Context context;
    DayNightSwitch dayNightSwitch;
    LinearLayout root;
    TextView txtTitle;
    Button btnOk,btnCancel;
    SingleSelectToggleGroup groupChoices;
    SingleSelectToggleGroup groupChoicesMin;
    TextView txtInfo;
    public SelectTimeDialog(Context context) {
        super(context);
        this.context = context;
    }

    public void setListener(SelectTimeInterface listener) {
        this.myInterface = listener;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        View view = View.inflate(context, R.layout.dialog_set_time, null);

        setContentView(view);
        setCancelable(false);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = -1;
        getWindow().setAttributes(params);

        dayNightSwitch = findViewById(R.id.day_night_switch);
        root = findViewById(R.id.root);
        txtTitle = findViewById(R.id.txtTitle);
        txtInfo = findViewById(R.id.txtInfo);
        groupChoices = findViewById(R.id.group_choices);
        groupChoicesMin = findViewById(R.id.group_choices_min);
        btnCancel = findViewById(R.id.btnCancel);
        btnOk = findViewById(R.id.btnOk);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterface.onRejected();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String day="";
                String time="";
                int intTime=0;
                int intMin = 0;
                if(dayNightSwitch.isNight()){
                    day="شب";
                    switch (groupChoices.getCheckedId()){
                        case R.id.choice_6:
                            time="18";
                            intTime =18;
                            break;
                        case R.id.choice_7:
                            time="19";
                            intTime =19;

                            break;
                        case R.id.choice_8:
                            time="20";
                            intTime =20;

                            break;
                        case R.id.choice_9:
                            time="21";
                            intTime =21;

                            break;
                        case R.id.choice_10:
                            time="22";
                            intTime =22;

                            break;
                        case R.id.choice_11:
                            time="23";
                            intTime =23;

                            break;
                        case R.id.choice_12:
                            time="24";
                            intTime =0;

                            break;
                        case R.id.choice_13:
                            time="1";
                            intTime =1;

                            break;
                        case R.id.choice_14:
                            time="2";
                            intTime =2;

                            break;
                        case R.id.choice_15:
                            time="3";
                            intTime =3;

                            break;
                        case R.id.choice_16:
                            time="4";
                            intTime =4;

                            break;
                        case R.id.choice_17:
                            time="5";
                            intTime =5;

                            break;

                    }
                }else {
                    day="روز";
                    switch (groupChoices.getCheckedId()) {
                        case R.id.choice_6:
                            time = "06";
                            intTime =6;

                            break;
                        case R.id.choice_7:
                            time = "07";
                            intTime =7;

                            break;
                        case R.id.choice_8:
                            time = "08";
                            intTime =8;

                            break;
                        case R.id.choice_9:
                            time = "09";
                            intTime =9;

                            break;
                        case R.id.choice_10:
                            time = "10";
                            intTime =10;

                            break;
                        case R.id.choice_11:
                            time = "11";
                            intTime =11;

                            break;
                        case R.id.choice_12:
                            time = "12";
                            intTime =12;

                            break;
                        case R.id.choice_13:
                            time = "13";
                            intTime =13;

                            break;
                        case R.id.choice_14:
                            time = "14";
                            intTime =14;

                            break;
                        case R.id.choice_15:
                            time = "15";
                            intTime =15;

                            break;
                        case R.id.choice_16:
                            time = "16";
                            intTime =16;

                            break;
                        case R.id.choice_17:
                            time = "17";
                            intTime =17;

                            break;
                    }


                }

                String min = "";
                switch (groupChoicesMin.getCheckedId()){
                    case R.id.choice_00:
                        min=":00";
                        intMin = 0;
                        break;
                    case R.id.choice_015:
                        min=":15";
                        intMin = 15;
                        break;
                    case R.id.choice_030:
                        min=":30";
                        intMin = 30;
                        break;
                    case R.id.choice_045:
                        min=":45";
                        intMin = 45;
                        break;

                }



                myInterface.onSuccess(time+min+" "+day,intTime,intMin);
            }
        });
        dayNightSwitch.setListener(new DayNightSwitchListener() {
            @Override
            public void onSwitch(boolean is_night) {

                if (is_night) {
                    root.setBackgroundColor(context.getResources().getColor(R.color.black));
                    txtTitle.setTextColor(context.getResources().getColor(R.color.white));
                    txtInfo.setTextColor(context.getResources().getColor(R.color.white));
                    txtTitle.setText("شب(از ساعت ۱۸  تا ۵ )");
                    txtInfo.setText("برای انتخاب ساعت های روز؛ روی ماه کلیک کنید تا شب شود.");
                    for( int i = 0; i < groupChoices.getChildCount(); i++ ) {

                        if (groupChoices.getChildAt(i) instanceof com.nex3z.togglebuttongroup.button.CircularToggle) {
                            ((CircularToggle) groupChoices.getChildAt(i)).setTextColor(context.getResources().getColor(R.color.white));
                            if(i+18<=24){
                                ((CircularToggle) groupChoices.getChildAt(i)).setText((i+18)+"");
                            }else {
                                ((CircularToggle) groupChoices.getChildAt(i)).setText(((i+18)-24)+"");

                            }
                        }
                    }
                    for( int i = 0; i < groupChoicesMin.getChildCount(); i++ ) {

                        if (groupChoicesMin.getChildAt(i) instanceof com.nex3z.togglebuttongroup.button.CircularToggle)
                            ((CircularToggle) groupChoicesMin.getChildAt(i)).setTextColor(context.getResources().getColor(R.color.white));
                    }

                } else {
                    root.setBackgroundColor(context.getResources().getColor(R.color.white));
                    txtTitle.setTextColor(context.getResources().getColor(R.color.grayText));
                    txtInfo.setTextColor(context.getResources().getColor(R.color.white));
                    txtTitle.setText("روز(از ساعت ۶ تا ۱۷)");
                    txtInfo.setText("برای انتخاب ساعت های شب؛ روی خورشید کلیک کنید تا شب شود.");

                    for( int i = 0; i < groupChoices.getChildCount(); i++ ) {

                        if (groupChoices.getChildAt(i) instanceof com.nex3z.togglebuttongroup.button.CircularToggle) {
                            ((CircularToggle) groupChoices.getChildAt(i)).setTextColor(context.getResources().getColor(R.color.black));
                            ((CircularToggle) groupChoices.getChildAt(i)).setText((i+6)+"");
                        }
                    }
                    for( int i = 0; i < groupChoicesMin.getChildCount(); i++ ) {

                        if (groupChoicesMin.getChildAt(i) instanceof com.nex3z.togglebuttongroup.button.CircularToggle) {
                            ((CircularToggle) groupChoicesMin.getChildAt(i)).setTextColor(context.getResources().getColor(R.color.black));
                        }
                    }

                }


            }
        });
    }

    @Override
    public void onBackPressed() {
        myInterface.onRejected();
        super.onBackPressed();
    }
}
