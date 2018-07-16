package greencode.ir.pillcci.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zcw.togglebutton.ToggleButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.activities.AddMedicianActivity;
import greencode.ir.pillcci.objects.EndUseFields;
import greencode.ir.pillcci.objects.UsageFields;
import greencode.ir.pillcci.utils.InputFilterMinMax;
import greencode.ir.pillcci.utils.Utility;
import me.omidh.liquidradiobutton.LiquidRadioButton;

/**
 * Created by alireza on 5/15/18.
 */

public class FragmentComplitationMedic extends Fragment {


    onActionStepThree onAction;

    String unit;
    int type = 1;


    boolean hasReminder = false;
    @BindView(R.id.radioAll)
    LiquidRadioButton radioAll;
    @BindView(R.id.radioTime)
    LiquidRadioButton radioTime;
    @BindView(R.id.totalTimeUseDay)
    TextInputEditText totalTimeUseDay;
    @BindView(R.id.radioCount)
    LiquidRadioButton radioCount;
    @BindView(R.id.edtCountDay)
    TextInputEditText edtCountDay;
    @BindView(R.id.txtUnit)
    TextView txtUnit;
    @BindView(R.id.toogleReminder)
    ToggleButton toogleReminder;
    @BindView(R.id.edtCountOfPill)
    EditText edtCountOfPill;
    @BindView(R.id.txtUnitReminder)
    TextView txtUnitReminder;
    @BindView(R.id.edtReminderDay)
    EditText edtReminderDay;
    @BindView(R.id.reminderLayout)
    LinearLayout reminderLayout;
    @BindView(R.id.btnInsert)
    Button btnInsert;
    @BindView(R.id.btnDelete)
    Button btnDelete;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_medician_step_three, container, false);
        ButterKnife.bind(this, view);
        unit = AddMedicianActivity.getUnit();
        txtUnit.setText(unit);
        txtUnitReminder.setText(unit);
        edtCountDay.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    radioCount.setChecked(true);

                }
            }
        });
        totalTimeUseDay.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    radioTime.setChecked(true);
                }
            }
        });

        radioAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    radioallChecked();
                    Utility.hideKeyboard();
                    totalTimeUseDay.clearFocus();
                    edtCountDay.clearFocus();

                }
            }
        });
        radioTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    radioTimeChecked();
                    totalTimeUseDay.requestFocus();
                }
            }
        });
        radioCount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    radioCountChecked();
                    edtCountDay.requestFocus();
                }
            }
        });
        totalTimeUseDay.setFilters(new InputFilter[]{new InputFilterMinMax(1, 365)});
        edtCountDay.setFilters(new InputFilter[]{new InputFilterMinMax(1, 9999)});
        toogleReminder.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    hasReminder = true;
                    edtReminderDay.setEnabled(true);
                    edtCountOfPill.setEnabled(true);
                    if(edtCountDay.getText().length()!=0){
                        edtCountOfPill.setText(edtCountDay.getText());
                    }
                    if(totalTimeUseDay.getText().length()!=0){
                        double amount =0 ;
                        for(String mount :AddMedicianActivity.getUsageFields().getUnitsCount()){
                            amount+=Double.parseDouble(mount);
                        }
                        
                        edtCountOfPill.setText(Utility.getDoubleStringValue( amount * Integer.parseInt(totalTimeUseDay.getText().toString()))+"");
                    }
                } else {
                    hasReminder = false;
                    edtReminderDay.setText("");
                    edtReminderDay.setEnabled(false);
                    edtCountOfPill.setEnabled(false);
                }
            }
        });
        return view;
    }

    private void radioCountChecked() {

        radioAll.setChecked(false);
        radioTime.setChecked(false);
        totalTimeUseDay.setText("");
        edtCountDay.setText("");
        totalTimeUseDay.setFilters(new InputFilter[]{new InputFilterMinMax(1, 365)});
        edtCountDay.setFilters(new InputFilter[]{new InputFilterMinMax(1, 9999)});
        type = 3;
    }

    private void radioTimeChecked() {
        radioAll.setChecked(false);
        radioCount.setChecked(false);
        totalTimeUseDay.setText("");
        edtCountDay.setText("");
        totalTimeUseDay.setFilters(new InputFilter[]{new InputFilterMinMax(1, 365)});
        edtCountDay.setFilters(new InputFilter[]{new InputFilterMinMax(1, 9999)});
        type = 2;
    }

    private void radioallChecked() {

        radioTime.setChecked(false);
        radioCount.setChecked(false);
        totalTimeUseDay.setText("");
        edtCountDay.setText("");
        totalTimeUseDay.setFilters(new InputFilter[]{new InputFilterMinMax(1, 365)});
        edtCountDay.setFilters(new InputFilter[]{new InputFilterMinMax(1, 9999)});
        type = 1;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAction = (onActionStepThree) context;

    }


    @OnClick({R.id.btnInsert, R.id.btnDelete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnInsert:
                int totalTime = 0;
                double totalcount = 0;
                int reminderCount = 0;
                int reminderDay = 0;


                if (totalTimeUseDay.getText().toString().length() > 0) {
                    totalTime = Integer.parseInt(totalTimeUseDay.getText().toString());
                }
                if (edtCountDay.getText().toString().length() > 0) {
                    totalcount = Double.parseDouble(edtCountDay.getText().toString());
                }

                if (radioTime.isChecked()) {


                    if (totalTime <= 0) {
                        Toast.makeText(getContext(), "مقدار مصرف در این حالت باید حداقل ۱ روز باشد.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    ArrayList<String> startAndStop = AddMedicianActivity.getUsageFields().getDays();
                    int type = AddMedicianActivity.getUsageFields().getTypeOfdayUsage();
                    if(type==4){
                        int startDay = Integer.parseInt(startAndStop.get(0));
                        if(totalTime<=startDay){
                            Toast.makeText(getContext(), "در چرخه ضد بارداری مقدار مصرف باید از تعداد روزهای استفاده چرخه بیشتر باشد. ", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }

                }
                if (radioCount.isChecked()) {

                    if (totalcount <= 0) {
                        Toast.makeText(getContext(), "میزان مصرف در این حالت صحیح نیست.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    else {
                        UsageFields usageFields = AddMedicianActivity.getUsageFields();
                        double amount = 0;
                        for (String s : usageFields.getUnitsCount()) {
                            try {
                                amount += Double.parseDouble(s);
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                        if (amount > totalcount) {
                            Toast.makeText(getContext(), "میزان مصرف وارد شده از مصرف یک روز شما کمتر می باشد.", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if(usageFields.getTypedayUsage()==4){
                            ArrayList<String> startAndStop = AddMedicianActivity.getUsageFields().getDays();
                            int type = AddMedicianActivity.getUsageFields().getTypeOfdayUsage();
                            int startDay = Integer.parseInt(startAndStop.get(0));
                            amount=0;
                            for (String s : usageFields.getUnitsCount()) {
                                try {
                                    amount += Double.parseDouble(s);
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            }
                            amount = amount*startDay;
                            if (amount > totalcount) {
                                Toast.makeText(getContext(), "در چرخه ضد بارداری، میزان داروی تجویز شده باید حداقل یک چرخه باشد.", Toast.LENGTH_LONG).show();
                                return;
                            }
                        }


                    }
                }


                if (hasReminder) {
                    try {
                        reminderDay = Integer.parseInt(edtReminderDay.getText().toString());
                    } catch (NumberFormatException ex) {
                        ex.printStackTrace();
                        reminderDay = 0;
                    }
                    try {
                        reminderCount = Integer.parseInt(edtCountOfPill.getText().toString());
                    } catch (NumberFormatException ex) {
                        ex.printStackTrace();
                        reminderCount = 0;
                    }
                    if (reminderDay == 0) {
                        Toast.makeText(getContext(), "تعداد روزهای پیش از یادآوری ذکر نشده است.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (reminderCount == 0) {
                        Toast.makeText(getContext(), "تعداد کل داروهای موجود ذکر نشده است.", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                onAction.onSaveButtonThree(new EndUseFields(type, totalTime, totalcount, reminderDay, reminderCount));
                break;
            case R.id.btnDelete:
                onAction.onCanceleThree();
                break;

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    public interface onActionStepThree {
        public void onSaveButtonThree(EndUseFields object);

        public void onCanceleThree();
    }

}
