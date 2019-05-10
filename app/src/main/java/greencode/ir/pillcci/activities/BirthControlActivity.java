package greencode.ir.pillcci.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputEditText;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.utils.BaseActivity;
import greencode.ir.pillcci.utils.Utility;

public class BirthControlActivity extends BaseActivity  {


    @BindView(R.id.edtBirthlUse)
    TextInputEditText edtBirthlUse;
    @BindView(R.id.edtBirthStop)
    TextInputEditText edtBirthStop;
    @BindView(R.id.edtPastDay)
    TextInputEditText edtPastDay;
    @BindView(R.id.btnOk)
    Button btnOk;
    @BindView(R.id.btnCancle)
    Button btnCancle;
    ArrayList<String> selectedDay = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_birth_control);
        ButterKnife.bind(this);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            boolean isLastBirth = bundle.getBoolean("from");
            if(isLastBirth) {
                String days = bundle.getString("days");

                String[] daylist = days.split(",");
                if (daylist.length > 0) {
                    for (String day : daylist) {
                        selectedDay.add(day);
                    }
                }
                edtBirthlUse.setText(selectedDay.get(0));
                edtBirthStop.setText(selectedDay.get(1));
                if(selectedDay.size()>1)
                    edtPastDay.setText(selectedDay.get(2));
            }

        }





    }








    @OnClick({R.id.btnOk, R.id.btnCancle})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnOk:
                Intent intent = getIntent();
                if(edtBirthlUse.getText().toString().length()==0){
                    Toast toast = Toast.makeText(this, "تعداد روزهای مصرف مشخص نشده است.", Toast.LENGTH_LONG);
                    Utility.centrizeAndShow(toast);
                }else if(edtBirthStop.getText().length()==0){
                    Toast toast = Toast.makeText(this, "تعداد روزهای توقف مصرف مشخص نشده است.", Toast.LENGTH_LONG);
                    Utility.centrizeAndShow(toast);

                }else {
                    String passtDays = edtPastDay.getText().toString().equals("") ? "0" : edtPastDay.getText().toString();
                    int past = Integer.parseInt(passtDays);
                    int use = Integer.parseInt(edtBirthlUse.getText().toString());
                    if (past >= use) {
                        Toast toast = Toast.makeText(this, "تعداد روزهای سپری شده نباید از روزهای مصرف بیشتر باشد.", Toast.LENGTH_LONG);
                        Utility.centrizeAndShow(toast);
                        return;
                    } else {

                        selectedDay = new ArrayList<>();
                        selectedDay.add(edtBirthlUse.getText().toString());
                        selectedDay.add(edtBirthStop.getText().toString());
                        String passDays = edtPastDay.getText().toString().equals("")?"0":edtPastDay.getText().toString();
                        selectedDay.add(passDays);

                        String days = "";
                        for(String day : selectedDay){
                            days += day;
                            days += ",";
                        }
                        if(days.length()>0){
                            days = days.substring(0,days.length()-1);
                        }
                         intent.putExtra("days",days);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }



                break;
            case R.id.btnCancle:
                 intent = getIntent();
                setResult(RESULT_CANCELED, intent);
                finish();
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Utility.hideKeyboard();
    }
}
