package greencode.ir.pillcci.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import greencode.ir.pillcci.R;

/**
 * Created by alireza on 5/18/18.
 */

public class SexDialog extends Dialog {

    SexInterface myInterface;
    Context context;
    RadioGroup rgSex;
    Button btnCancle;
    Button btnOk;
    RadioButton rbMale,rbFemaile,rbMore;
    int nowSex;
    public SexDialog(Context context,int nowSex) {
        super(context);
        this.context = context;
        this.nowSex = nowSex;
    }

    public void setListener(SexInterface listener) {
        this.myInterface = listener;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        View view = View.inflate(context, R.layout.dialog_sex, null);
        setContentView(view);
        setCancelable(false);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = -1;
        getWindow().setAttributes(params);
        rbMale =findViewById(R.id.radioMale);
        rbFemaile =findViewById(R.id.radioFemale);
        rbMore =findViewById(R.id.radioMore);
        rgSex = findViewById(R.id.rgSex);
        btnCancle = findViewById(R.id.btnCancle);
        btnOk = findViewById(R.id.btnOk);

        if(nowSex==1)
        {
            rbMale.setChecked(true);
        }else if(nowSex==2){
            rbFemaile.setChecked(true);
        }else if(nowSex==3){
            rbMore.setChecked(true);
        }
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterface.Rejected();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rbMale.isChecked()){
                    myInterface.Success(1);
                }else if(rbFemaile.isChecked()){
                    myInterface.Success(2);
                }else if(rbMore.isChecked()){
                    myInterface.Success(3);
                }else {
                    myInterface.Success(0);
                }
            }
        });


    }


    @Override
    public void onBackPressed() {
        myInterface.Rejected();
        super.onBackPressed();
    }
}
