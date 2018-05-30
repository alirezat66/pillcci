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
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.activities.AddMedicianActivity;
import greencode.ir.pillcci.objects.EndUseFields;
import greencode.ir.pillcci.objects.UsageFields;
import greencode.ir.pillcci.utils.InputFilterMinMax;

/**
 * Created by alireza on 5/15/18.
 */

public class FragmentComplitationMedic extends Fragment {


    onActionStepThree onAction;
    @BindView(R.id.radioAll)
    me.omidh.liquidradiobutton.LiquidRadioButton radioAll;
    @BindView(R.id.radioTime)
    me.omidh.liquidradiobutton.LiquidRadioButton radioTime;
    @BindView(R.id.totalTimeUseDay)
    TextInputEditText totalTimeUseDay;
    @BindView(R.id.radioCount)
    me.omidh.liquidradiobutton.LiquidRadioButton radioCount;
    @BindView(R.id.edtCountDay)
    TextInputEditText edtCountDay;
    @BindView(R.id.countOfHave)
    TextInputEditText countOfHave;
    @BindView(R.id.beforReminding)
    TextInputEditText beforReminding;
    @BindView(R.id.edtUseRes)
    TextInputEditText edtUseRes;
    @BindView(R.id.btnInsert)
    Button btnInsert;
    @BindView(R.id.btnDelete)
    Button btnDelete;
    @BindView(R.id.txtUnit)
    TextView txtUnit;
    String unit;
    int type=1;
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
        radioAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    radioallChecked();
                }
            }
        });
        radioTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if (isChecked) {
                    radioTimeChecked();
                }
            }
        });
        radioCount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    radioCountChecked();
                }
            }
        });
        totalTimeUseDay.setFilters(new InputFilter[]{ new InputFilterMinMax(1, 365)});
        edtUseRes.setFilters(new InputFilter[]{new InputFilterMinMax(1,9999)});
        return view;
    }

    private void radioCountChecked() {
        radioAll.setChecked(false);
        radioTime.setChecked(false);
        totalTimeUseDay.setText("");
        edtCountDay.setText("");
        totalTimeUseDay.setFilters(new InputFilter[]{ new InputFilterMinMax(1, 365)});
        edtCountDay.setFilters(new InputFilter[]{new InputFilterMinMax(1,9999)});
        totalTimeUseDay.setEnabled(false);
        edtCountDay.setEnabled(true);
        type=3;
    }

    private void radioTimeChecked() {
        radioAll.setChecked(false);
        radioCount.setChecked(false);
        totalTimeUseDay.setText("");
        edtCountDay.setText("");
        totalTimeUseDay.setFilters(new InputFilter[]{ new InputFilterMinMax(1, 365)});
        edtCountDay.setFilters(new InputFilter[]{new InputFilterMinMax(1,9999)});
        totalTimeUseDay.setEnabled(true);
        edtCountDay.setEnabled(false);
        type=2;
    }

    private void radioallChecked() {
        radioTime.setChecked(false);
        radioCount.setChecked(false);
        totalTimeUseDay.setText("");
        edtCountDay.setText("");
        totalTimeUseDay.setEnabled(false);
        edtCountDay.setEnabled(false);
        totalTimeUseDay.setFilters(new InputFilter[]{ new InputFilterMinMax(1, 365)});
        edtCountDay.setFilters(new InputFilter[]{new InputFilterMinMax(1,9999)});
        type=1;
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
                int totalTime =0;
                double totalcount =0;



                if(totalTimeUseDay.getText().toString().length()>0) {
                    totalTime = Integer.parseInt(totalTimeUseDay.getText().toString());
                }
                if(edtCountDay.getText().toString().length()>0){
                    totalcount = Double.parseDouble(edtCountDay.getText().toString());
                }

                if(radioTime.isChecked()){
                    if(totalTime<=0){
                        Toast.makeText(getContext(), "مقدار مصرف در این حالت باید حداقل ۱ روز باشد.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if(radioCount.isChecked()){
                    if(totalcount<=0){
                        Toast.makeText(getContext(), "میزان مصرف در این حالت صحیح نیست.", Toast.LENGTH_SHORT).show();
                    }else {
                        UsageFields usageFields = AddMedicianActivity.getUsageFields();
                        double amount =0;
                        for(String s : usageFields.getUnitsCount()){
                            try {
                                amount+=Double.parseDouble(s);
                            }catch (NumberFormatException e){
                                e.printStackTrace();
                            }
                        }
                        if(amount> totalcount){
                            Toast.makeText(getContext(), "میزان مصرف وارد شده از مصرف یک روز شما کمتر می باشد.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
                onAction.onSaveButtonThree(new EndUseFields(type,totalTime,totalcount));
                break;
            case R.id.btnDelete:
                onAction.onCanceleThree();
                break;

        }
    }



    public interface onActionStepThree {
        public void onSaveButtonThree(EndUseFields object);

        public void onCanceleThree();
    }

}
