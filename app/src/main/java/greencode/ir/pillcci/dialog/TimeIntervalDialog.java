package greencode.ir.pillcci.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import greencode.ir.pillcci.R;

/**
 * Created by alireza on 5/18/18.
 */

public class TimeIntervalDialog extends Dialog {

    TimeIntervalInterface myInterface;
    Context context;
    int days;
    int lastType;
    @BindView(R.id.addBtn)
    LinearLayout addBtn;
    @BindView(R.id.txtDays)
    TextView txtDays;
    @BindView(R.id.minusBtn)
    LinearLayout minusBtn;
    @BindView(R.id.btnOk)
    Button btnOk;
    @BindView(R.id.btnCancel)
    Button btnCancel;

    public TimeIntervalDialog(Context context, int days,int lastType) {
        super(context);
        this.context = context;
        this.days = days;
        this.lastType = lastType;
    }

    public void setListener(TimeIntervalInterface listener) {
        this.myInterface = listener;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        View view = View.inflate(context, R.layout.dialog_day_interval, null);
        ButterKnife.bind(this, view);
        setContentView(view);
        setCancelable(false);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = -1;
        getWindow().setAttributes(params);
        txtDays.setText(days+"");





    }


    @Override
    public void onBackPressed() {
        myInterface.onCancel(lastType);
        super.onBackPressed();
    }

    @OnClick({R.id.addBtn, R.id.minusBtn, R.id.btnOk, R.id.btnCancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addBtn:
                days++;
                txtDays.setText(days+"");
                break;
            case R.id.minusBtn:
                if(days>2){
                    days--;
                    txtDays.setText(days+"");
                }
                break;
            case R.id.btnOk:
                 myInterface.onSuccess(days);
                break;
            case R.id.btnCancel:
                myInterface.onCancel(lastType);
                break;
        }
    }
}
