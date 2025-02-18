package greencode.ir.pillcci.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.isapanah.awesomespinner.AwesomeSpinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import greencode.ir.pillcci.R;

/**
 * Created by alireza on 5/18/18.
 */

public class DayCountDialog extends Dialog {

    DayCountInterface myInterface;
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
    @BindView(R.id.dayspinner)
    AwesomeSpinner dayspinner;
    List<String> lsDays ;
    List<Integer>lsIntDays;
    public DayCountDialog(Context context, int days, int lastType) {
        super(context);
        this.context = context;
        this.days = days;
        this.lastType = lastType;
    }

    public void setListener(DayCountInterface listener) {
        this.myInterface = listener;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        View view = View.inflate(context, R.layout.dialog_usage_day, null);
        ButterKnife.bind(this, view);
        setContentView(view);
        setCancelable(false);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = -1;
        getWindow().setAttributes(params);
        txtDays.setText(days + "");
        lsDays = new ArrayList<>();
        lsIntDays = new ArrayList<>();
        for(int i = 1 ; i<366;i++){
            lsIntDays.add(i);
            lsDays.add(i+"");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, lsDays);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dayspinner.setAdapter(adapter);
        dayspinner.setOnSpinnerItemClickListener(new AwesomeSpinner.onSpinnerItemClickListener<String>() {
            @Override
            public void onItemSelected(int position, String s) {
                days = lsIntDays.get(position);

            }
        });

        dayspinner.setSelection(0);
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
                txtDays.setText(days + "");
                break;
            case R.id.minusBtn:
                if (days > 1) {
                    days--;
                    txtDays.setText(days + "");
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
