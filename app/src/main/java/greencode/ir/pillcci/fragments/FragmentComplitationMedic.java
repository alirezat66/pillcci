package greencode.ir.pillcci.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import greencode.ir.pillcci.dialog.AmountDialog;
import greencode.ir.pillcci.dialog.AmountInterface;
import greencode.ir.pillcci.dialog.DayCountDialog;
import greencode.ir.pillcci.dialog.DayCountInterface;
import greencode.ir.pillcci.objects.EndUseFields;
import greencode.ir.pillcci.objects.UsageFields;
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

    @BindView(R.id.radioCount)
    LiquidRadioButton radioCount;

    @BindView(R.id.toogleReminder)
    ToggleButton toogleReminder;
    boolean isChecked = false;
    @BindView(R.id.edtCountOfPill)
    EditText edtCountOfPill;
    @BindView(R.id.txtUnitReminder)
    TextView txtUnitReminder;
    @BindView(R.id.edtReminderDay)
    EditText edtReminderDay;
    @BindView(R.id.reminderLayout)
    LinearLayout reminderLayout;
    @BindView(R.id.remiderBeforLay)
    LinearLayout remiderBeforLay;
    @BindView(R.id.btnInsert)
    Button btnInsert;
    @BindView(R.id.btnDelete)
    Button btnDelete;
    @BindView(R.id.reminderDayLayout)
    LinearLayout reminderDayLayout;
    @BindView(R.id.txtUseType)
    TextView txtUseType;

    int totalDay = 1;
    double totalUseAmount = 1;
    @BindView(R.id.txtUseTypeTitle)
    TextView txtUseTypeTitle;


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
        txtUnitReminder.setText(unit);
        if(AddMedicianActivity.getUsageFields().getTypeOfdayUsage()==4){
            radioTime.setActivated(false);
            radioTime.setFocusable(false);
            radioCount.setVisibility(View.GONE);
            radioTime.setVisibility(View.GONE);
            radioTime.setEnabled(false);
            radioCount.setActivated(false);
            radioCount.setFocusable(false);
            radioCount.setEnabled(false);
        }

        toogleReminder.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    hasReminder = true;
                    reminderDayLayout.setVisibility(View.VISIBLE);
                 //   remiderBeforLay.setVisibility(View.VISIBLE);
                    edtReminderDay.setEnabled(true);
                    edtCountOfPill.setEnabled(true);

                    if (type == 3) {
                        edtCountOfPill.setText(Utility.getDoubleStringValue(totalUseAmount) + "");
                    }
                    if (type==2) {
                        double amount = 0;
                        for (String mount : AddMedicianActivity.getUsageFields().getUnitsCount()) {
                            amount += Double.parseDouble(mount);
                        }

                        edtCountOfPill.setText(Utility.getDoubleStringValue(amount * totalDay) + "");
                    }
                } else {
                    hasReminder = false;
                    reminderDayLayout.setVisibility(View.GONE);
                    remiderBeforLay.setVisibility(View.GONE);

                    edtReminderDay.setText("");
                    edtReminderDay.setEnabled(false);
                    edtCountOfPill.setEnabled(false);
                }
            }
        });
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAction = (onActionStepThree) context;

    }


    @OnClick({R.id.btnInsert, R.id.btnDelete, R.id.radioAll, R.id.radioTime, R.id.radioCount})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnInsert:
                insertDrug();
                break;
            case R.id.btnDelete:
                onAction.onCanceleThree();
                break;
            case R.id.radioAll:
                type = 1;
                backToLastState(type);
                break;
            case R.id.radioCount:
                getAmountDialog();
                break;
            case R.id.radioTime:
                getDayDialog();
                break;

        }
    }

    private void insertDrug() {


        if (type==2) {


            if (totalDay == 0) {
                Toast.makeText(getContext(), "مقدار مصرف در این حالت باید حداقل ۱ روز باشد.", Toast.LENGTH_LONG).show();
                return;
            }

            ArrayList<String> startAndStop = AddMedicianActivity.getUsageFields().getDays();
            int loctype = AddMedicianActivity.getUsageFields().getTypeOfdayUsage();
            if (loctype == 4) {
                int startDay = Integer.parseInt(startAndStop.get(0));
                if (totalDay <= startDay) {
                    Toast.makeText(getContext(), "در چرخه ضد بارداری مقدار مصرف باید از تعداد روزهای استفاده چرخه بیشتر باشد. ", Toast.LENGTH_LONG).show();
                    return;
                }
            }

        }
        if (radioCount.isChecked()) {

            if (totalUseAmount <= 0) {
                Toast.makeText(getContext(), "میزان مصرف در این حالت صحیح نیست.", Toast.LENGTH_LONG).show();
                return;
            } else {
                UsageFields usageFields = AddMedicianActivity.getUsageFields();
                double amount = 0;
                for (String s : usageFields.getUnitsCount()) {
                    try {
                        amount += Double.parseDouble(s);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
                if (amount > totalUseAmount) {
                    Toast.makeText(getContext(), "میزان مصرف وارد شده از مصرف یک روز شما کمتر می باشد.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (usageFields.getTypedayUsage() == 4) {
                    ArrayList<String> startAndStop = AddMedicianActivity.getUsageFields().getDays();
                    int type = AddMedicianActivity.getUsageFields().getTypeOfdayUsage();
                    int startDay = Integer.parseInt(startAndStop.get(0));
                    amount = 0;
                    for (String s : usageFields.getUnitsCount()) {
                        try {
                            amount += Double.parseDouble(s);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    amount = amount * startDay;
                    if (amount > totalUseAmount) {
                        Toast.makeText(getContext(), "در چرخه ضد بارداری، میزان داروی تجویز شده باید حداقل یک چرخه باشد.", Toast.LENGTH_LONG).show();
                        return;
                    }
                }


            }
        }



        int  reminderDay = 1;
        int reminderCount = 0;
        if (hasReminder) {
            try {
                reminderDay = 1;
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
        }else {
            reminderDay = 0;
            reminderCount =0 ;
        }
        onAction.onSaveButtonThree(new EndUseFields(type, totalDay, totalUseAmount, reminderDay, reminderCount));
    }

    private void getAmountDialog() {
        final AmountDialog dialog = new AmountDialog(getContext(), totalUseAmount);
        dialog.setListener(new AmountInterface() {
            @Override
            public void onSuccess(double amount) {
                totalUseAmount = amount;
                type = 3;
                backToLastState(type);
                dialog.dismiss();
            }

            @Override
            public void onCancel() {
                backToLastState(type);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void getDayDialog() {
        final DayCountDialog dialog = new DayCountDialog(getContext(), totalDay, type);
        dialog.setListener(new DayCountInterface() {
            @Override
            public void onSuccess(int days) {
                totalDay = days;
                type = 2;
                backToLastState(type);
                dialog.dismiss();
            }

            @Override
            public void onCancel(int lastType) {

                backToLastState(type);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }




    public interface onActionStepThree {
        public void onSaveButtonThree(EndUseFields object);

        public void onCanceleThree();
    }

    private void backToLastState(int lastType) {
        if (lastType == 1) {
            radioAll.setChecked(true);
            txtUseType.setText("مصرف مداوم");
        } else if (lastType == 2) {
            radioTime.setChecked(true);
            txtUseType.setText("مصرف برای " + totalDay + " روز");
        } else if (lastType == 3) {
            radioCount.setChecked(true);

            txtUseType.setText("مصرف به میزان " + totalUseAmount + " " + unit);

        }


    }

}
