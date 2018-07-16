package greencode.ir.pillcci.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import greencode.ir.pillcci.R;
import me.omidh.liquidradiobutton.LiquidRadioButton;

/**
 * Created by alireza on 5/18/18.
 */

public class BloodDialog extends Dialog {

    BloodInterface myInterface;
    Context context;


    int nowBlood;
    @BindView(R.id.radioAPlus)
    LiquidRadioButton radioAPlus;
    @BindView(R.id.radioAMinus)
    LiquidRadioButton radioAMinus;
    @BindView(R.id.radioBPlus)
    LiquidRadioButton radioBPlus;
    @BindView(R.id.radioBMinus)
    LiquidRadioButton radioBMinus;
    @BindView(R.id.radioABPlus)
    LiquidRadioButton radioABPlus;
    @BindView(R.id.radioABMinus)
    LiquidRadioButton radioABMinus;
    @BindView(R.id.radioOplus)
    LiquidRadioButton radioOplus;
    @BindView(R.id.radioOMinus)
    LiquidRadioButton radioOMinus;
    @BindView(R.id.rgSex)
    RadioGroup rgSex;
    @BindView(R.id.btnCancle)
    Button btnCancle;
    @BindView(R.id.btnOk)
    Button btnOk;


    public BloodDialog(Context context, int nowSex) {
        super(context);
        this.context = context;
        this.nowBlood = nowSex;
    }

    public void setListener(BloodInterface listener) {
        this.myInterface = listener;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        View view = View.inflate(context, R.layout.dialog_blood, null);
        ButterKnife.bind(this, view);
        setContentView(view);
        setCancelable(false);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = -1;
        getWindow().setAttributes(params);

        rgSex = findViewById(R.id.rgSex);
        btnCancle = findViewById(R.id.btnCancle);
        btnOk = findViewById(R.id.btnOk);

        if (nowBlood == 1) {
            radioAPlus.setChecked(true);
        } else if (nowBlood == 2) {
            radioAMinus.setChecked(true);
        } else if (nowBlood == 3) {
            radioBPlus.setChecked(true);
        }else if (nowBlood == 4) {
            radioBMinus.setChecked(true);
        }else   if (nowBlood == 5) {
            radioABPlus.setChecked(true);
        } else if (nowBlood == 6) {
            radioABMinus.setChecked(true);
        } else if (nowBlood == 7) {
            radioOplus.setChecked(true);
        }else if (nowBlood == 8) {
            radioOMinus.setChecked(true);
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
                if (radioAPlus.isChecked()) {
                    myInterface.Success(1);
                } else if (radioAMinus.isChecked()) {
                    myInterface.Success(2);
                } else if (radioBPlus.isChecked()) {
                    myInterface.Success(3);
                } else if(radioBMinus.isChecked()){
                    myInterface.Success(4);
                }else if (radioABPlus.isChecked()) {
                    myInterface.Success(5);
                } else if (radioABMinus.isChecked()) {
                    myInterface.Success(6);
                } else if (radioOplus.isChecked()) {
                    myInterface.Success(7);
                } else if(radioOMinus.isChecked()){
                    myInterface.Success(8);
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
