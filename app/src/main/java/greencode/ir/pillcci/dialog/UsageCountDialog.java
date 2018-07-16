package greencode.ir.pillcci.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import greencode.ir.pillcci.R;

/**
 * Created by alireza on 5/18/18.
 */

public class UsageCountDialog extends BottomSheetDialog {

    UsageCountInterface myInterface;
    Context context;
    RadioGroup rgOne;
    Button btnCancle;
    Button btnOk;
    int finalSelected=0;
    int count=0;
    double diffrence=0;

    public UsageCountDialog(Context context, int countOfUsagePerDay) {
        super(context);
        this.context = context;
        this.finalSelected = countOfUsagePerDay;
    }

    public void setListener(UsageCountInterface listener) {
        this.myInterface = listener;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        View view = View.inflate(context, R.layout.dialog_usage_count, null);
        setContentView(view);
        setCancelable(false);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = -1;
        getWindow().setAttributes(params);
        rgOne = findViewById(R.id.rgOne);
        btnCancle = findViewById(R.id.btnCancle);
        btnOk = findViewById(R.id.btnOk);

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterface.onRejected();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedId = rgOne.getCheckedRadioButtonId();

                String title="";
                switch (selectedId){
                    case R.id.radio0:
                        finalSelected=1;
                        count=1;
                        title="هر ۲۴ ساعت";
                        diffrence=24;
                        break;
                    case R.id.radio1:
                        finalSelected=2;
                        count=2;
                        title="هر ۱۲ ساعت";
                        diffrence=12;
                        break;
                    case R.id.radio2:
                        finalSelected=3;
                        count=3;
                        title="هر ۸ ساعت";
                        diffrence=8;
                        break;
                    case R.id.radio3:
                        finalSelected=4;
                        count=4;
                        title="هر ۶ ساعت";
                        diffrence=6;
                        break;

                    case R.id.radio5:
                        finalSelected=6;
                        count=6;
                        title="هر ۴ ساعت";
                        diffrence=4;
                        break;

                    case R.id.radio7:
                        finalSelected=8;
                        count=8;
                        title="هر ۳ ساعت";
                        diffrence=3;
                        break;
                    case R.id.radio8:
                        count=12;
                        finalSelected=9;
                        title="هر ۲ ساعت";
                        diffrence=2;
                        break;
                    case R.id.radio9:
                        finalSelected=10;
                        count=24;
                        diffrence=1;
                        title="هر ۱ ساعت";

                        break;
                    default:
                        finalSelected=-1;
                        break;
                }
                if(finalSelected!=-1) {
                    myInterface.onSuccess(finalSelected,title,count,diffrence);
                }else {
                    Toast.makeText(context, "لطفا یک مورد را انتخاب کنید.", Toast.LENGTH_LONG).show();
                }
            }
        });


        selectSwitch();
    }

    private void selectSwitch() {

        switch (finalSelected){
            case 0:
                rgOne.check(R.id.radio0);
                break;
            case 1:
                rgOne.check(R.id.radio0);
                break;
            case 2:
                rgOne.check(R.id.radio1);

                break;
            case 3:
                rgOne.check(R.id.radio2);
                break;

            case 4:
                rgOne.check(R.id.radio3);
                break;

            case 6:
                rgOne.check(R.id.radio5);
                break;
            case 8:
                rgOne.check(R.id.radio7);
                break;
            case 12:
                rgOne.check(R.id.radio8);
                break;

            case 24:
                rgOne.check(R.id.radio9);
                break;
            default:
                finalSelected=-1;
                break;
        }
    }


    @Override
    public void onBackPressed() {
        myInterface.onRejected();
        super.onBackPressed();
    }
}
