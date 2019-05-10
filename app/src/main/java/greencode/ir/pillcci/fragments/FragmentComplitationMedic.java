package greencode.ir.pillcci.fragments;

import android.app.Service;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import greencode.ir.pillcci.utils.SoftKeyboard;
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
    @BindView(R.id.topLayout)
    LinearLayout topLayout;
    @BindView(R.id.root)
    ScrollView root;
    @BindView(R.id.ly_one)
    LinearLayout lyOne;
    @BindView(R.id.ly_second)
    LinearLayout lySecond;
    @BindView(R.id.txtDay)
    TextView txtDay;
    @BindView(R.id.txt_count)
    TextView txtCount;
    @BindView(R.id.txt_type)
    TextView txtType;


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
        checkKeyBoard();
        if (AddMedicianActivity.getUsageFields().getTypeOfdayUsage() == 4) {
            radioTime.setActivated(false);
            radioTime.setFocusable(false);
            radioCount.setVisibility(View.GONE);
            radioTime.setVisibility(View.GONE);
            radioTime.setEnabled(false);
            radioCount.setActivated(false);
            radioCount.setFocusable(false);
            radioCount.setEnabled(false);
            lySecond.setVisibility(View.GONE);
            lyOne.setVisibility(View.GONE);
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
                    if (type == 2) {
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
        txtType.setText(AddMedicianActivity.getUnit());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        txtType.setText(AddMedicianActivity.getUnit());
        radioAll.setChecked(true);
        disableSecond();
        disableOne();
        totalUseAmount = 1;
        totalDay = 1;
        UsageFields usageFields = AddMedicianActivity.getUsageFields();
        double amount = 0;
        for (String s : usageFields.getUnitsCount()) {
            try {
                amount += Double.parseDouble(s);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        totalUseAmount = amount;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAction = (onActionStepThree) context;

    }


    @OnClick({R.id.btnInsert, R.id.btnDelete, R.id.radioAll, R.id.radioTime, R.id.radioCount,R.id.ly_one,R.id.ly_second})
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
                disableSecond();
                disableOne();
                break;
            case R.id.ly_second:
                if(radioTime.isChecked())
                {

                    enableSecond();
                    disableOne();
                    getAmountDialog();
                }

                break;
                case R.id.ly_one:
                    if(radioTime.isChecked()) {
                        enableOne();
                        disableSecond();
                        getDayDialog();
                    }
                break;
            case R.id.radioTime:
                enableOne();
                disableSecond();
                //  getDayDialog();
                break;

        }
    }

    private void disableSecond() {
        txtCount.setText("----");
        int childCount = lySecond.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View v = lySecond.getChildAt(i);
            if (v instanceof TextView) {
                ((TextView) v).setTextColor(getActivity().getResources().getColor(R.color.gray));
            }
        }
    }
    private void disableOne() {
        txtDay.setText("----");
        totalDay = 1;
        int childCount = lyOne.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View v = lyOne.getChildAt(i);
            if (v instanceof TextView) {
                ((TextView) v).setTextColor(getActivity().getResources().getColor(R.color.gray));
            }
        }

    }

    private void enableOne() {
        type = 2;
        int childCount = lyOne.getChildCount();
        txtDay.setText("1");
        txtCount.setText("----");
        totalDay = 1;

        for (int i = 0; i < childCount; i++) {
            View v = lyOne.getChildAt(i);
            if (v instanceof TextView) {
                ((TextView) v).setTextColor(getActivity().getResources().getColor(R.color.black));
            }
        }

    }
    private void enableSecond() {
        type = 3;
        txtCount.setText(totalUseAmount+"");
        int childCount = lySecond.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View v = lySecond.getChildAt(i);
            if (v instanceof TextView) {
                ((TextView) v).setTextColor(getActivity().getResources().getColor(R.color.black));
            }
        }
    }


    private void insertDrug() {


        if (type == 2) {


            if (totalDay == 0) {
                Toast toast = Toast.makeText(getContext(), "مقدار مصرف در این حالت باید حداقل ۱ روز باشد.", Toast.LENGTH_LONG);
                Utility.centrizeAndShow(toast);
                return;
            }

            ArrayList<String> startAndStop = AddMedicianActivity.getUsageFields().getDays();
            int loctype = AddMedicianActivity.getUsageFields().getTypeOfdayUsage();
            if (loctype == 4) {
                int startDay = Integer.parseInt(startAndStop.get(0));
                if (totalDay <= startDay) {
                    Toast toast = Toast.makeText(getContext(), "در چرخه ضد بارداری مقدار مصرف باید از تعداد روزهای استفاده چرخه بیشتر باشد. ", Toast.LENGTH_LONG);
                    Utility.centrizeAndShow(toast);
                    return;
                }
            }

        }
        if (type==3) {


            if (totalUseAmount <= 0) {
                Toast toast = Toast.makeText(getContext(), "میزان مصرف در این حالت صحیح نیست.", Toast.LENGTH_LONG);
                Utility.centrizeAndShow(toast);
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
                    Toast toast = Toast.makeText(getContext(), "مقدار وارد شده از مصرفِ یک روز این دارو کمتر است.", Toast.LENGTH_LONG);
                    Utility.centrizeAndShow(toast);
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
                        Toast toast = Toast.makeText(getContext(), "در چرخه ضد بارداری، میزان داروی تجویز شده باید حداقل یک چرخه باشد.", Toast.LENGTH_LONG);
                        Utility.centrizeAndShow(toast);
                        return;
                    }
                }


            }
        }


        int reminderDay = 1;
        double reminderCount = 0;
        if (hasReminder) {
            try {
                reminderDay = 1;
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                reminderDay = 0;
            }
            try {
                reminderCount = Double.parseDouble(edtCountOfPill.getText().toString());
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                reminderCount = 0;
            }
            if (reminderDay == 0) {
                Toast toast = Toast.makeText(getContext(), "تعداد روزهای پیش از یادآوری ذکر نشده است.", Toast.LENGTH_LONG);
                Utility.centrizeAndShow(toast);
                return;
            }
            if (reminderCount == 0) {
                Toast toast = Toast.makeText(getContext(), "موجودی دارو را وارد کن!", Toast.LENGTH_LONG);
                Utility.centrizeAndShow(toast);
                return;
            }
        } else {
            reminderDay = 0;
            reminderCount = 0;
        }
        onAction.onSaveButtonThree(new EndUseFields(type, totalDay, totalUseAmount, reminderDay, reminderCount));
    }

    private void getAmountDialog() {
        final AmountDialog dialog = new AmountDialog(getContext(), totalUseAmount, unit);
        dialog.setListener(new AmountInterface() {
            @Override
            public void onSuccess(double amount) {

                totalUseAmount = amount;
                type = 3;
                txtCount.setText(totalUseAmount+"");
                backToLastState(type);
                dialog.dismiss();

            }

            @Override
            public void onCancel() {

                type = 3;
                txtCount.setText(totalUseAmount+"");
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
                txtDay.setText(totalDay+"");
                txtCount.setText("----");
                backToLastState(type);
                dialog.dismiss();

            }

            @Override
            public void onCancel(int lastType) {


                type = 2;
                txtDay.setText(totalDay+"");
                txtCount.setText("----");
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
           // radioAll.setChecked(true);
            txtUseType.setText("مصرف همیشگی");
        } else if (lastType == 2) {
           // radioTime.setChecked(true);
            txtUseType.setText("" + totalDay + " روز");
        } else if (lastType == 3) {
          //  radioCount.setChecked(true);

            txtUseType.setText("" + totalUseAmount + " " + unit);

        }


    }

    void checkKeyBoard() {
        SoftKeyboard softKeyboard;
        InputMethodManager im = (InputMethodManager) getContext().getSystemService(Service.INPUT_METHOD_SERVICE);
        /// this is for hide and show keyboard
        softKeyboard = new SoftKeyboard(root, im);
        softKeyboard.setSoftKeyboardCallback(new SoftKeyboard.SoftKeyboardChanged() {

            @Override
            public void onSoftKeyboardHide() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        topLayout.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onSoftKeyboardShow() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        topLayout.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

}
