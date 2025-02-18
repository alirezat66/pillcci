package greencode.ir.pillcci.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import greencode.ir.pillcci.R;

/**
 * Created by alireza on 5/18/18.
 */

public class AmountDialog extends Dialog {

    AmountInterface myInterface;
    Context context;
    double amount;

    String unit;
    @BindView(R.id.btnOk)
    Button btnOk;
    @BindView(R.id.btnCancel)
    Button btnCancel;
    @BindView(R.id.edtAmount)
    EditText edtAmount;
    @BindView(R.id.txt_unit)
    TextView txtUnit;

    public AmountDialog(Context context, double amount,String unit) {
        super(context);
        this.context = context;
        this.amount = amount;
        this.unit = unit;
    }

    public void setListener(AmountInterface listener) {
        this.myInterface = listener;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        View view = View.inflate(context, R.layout.dialog_usage_amount, null);
        ButterKnife.bind(this, view);
        setContentView(view);
        setCancelable(false);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = -1;
        getWindow().setAttributes(params);
        edtAmount.setText(amount + "");
        txtUnit.setText(unit);


    }


    @Override
    public void onBackPressed() {
        myInterface.onCancel();
        super.onBackPressed();
    }

    @OnClick({R.id.btnOk, R.id.btnCancel})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnOk:
                try {
                    amount = Double.parseDouble(edtAmount.getText().toString());
                    myInterface.onSuccess(amount);

                } catch (NumberFormatException ex) {
                    myInterface.onCancel();
                }
                edtAmount.setVisibility(View.GONE);
                break;
            case R.id.btnCancel:
                myInterface.onCancel();
                edtAmount.setVisibility(View.GONE);

                break;
        }
    }
}
