package greencode.ir.pillcci.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

import butterknife.BindView;
import greencode.ir.pillcci.R;

/**
 * Created by alireza on 5/18/18.
 */

public class DaySelectedDialog extends Dialog {

    DayOfWeekInterface myInterface;
    Context context;
    Button btnCancle;
    Button btnOk;
    LinearLayout root;
    ArrayList<String> selectedDay;
    AppCompatCheckBox chShanbe;
    AppCompatCheckBox chYekShanbe;
    AppCompatCheckBox chDoShanbe;
    AppCompatCheckBox chSeShanbe;
    AppCompatCheckBox chCharShanbe;
    AppCompatCheckBox chPanjShanbe;
    AppCompatCheckBox chJome;
    public static final String[] persianWeekDays = {"\u0634\u0646\u0628\u0647", // Shanbeh
            "\u06cc\u06a9\u200c\u0634\u0646\u0628\u0647", // Yekshanbeh
            "\u062f\u0648\u0634\u0646\u0628\u0647", // Doshanbeh
            "\u0633\u0647\u200c\u0634\u0646\u0628\u0647", // Sehshanbeh
            "\u0686\u0647\u0627\u0631\u0634\u0646\u0628\u0647", // Chaharshanbeh
            "\u067e\u0646\u062c\u200c\u0634\u0646\u0628\u0647", // Panjshanbeh
            "\u062c\u0645\u0639\u0647" // jome
    };


    public DaySelectedDialog(Context context, ArrayList<String> selectedDay) {
        super(context);
        this.context = context;
        this.selectedDay = selectedDay;
    }

    public void setListener(DayOfWeekInterface listener) {
        this.myInterface = listener;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        View view = View.inflate(context, R.layout.dialog_day_selected, null);
        setContentView(view);
        setCancelable(false);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = -1;
        getWindow().setAttributes(params);
        btnCancle = findViewById(R.id.btnCancle);
        btnOk = findViewById(R.id.btnOk);
        root = findViewById(R.id.root);

        chShanbe = findViewById(R.id.chShanbe);
        chYekShanbe = findViewById(R.id.chYekShanbe);
        chDoShanbe = findViewById(R.id.chDoShanbe);
        chSeShanbe = findViewById(R.id.chSeShanbe);
        chCharShanbe = findViewById(R.id.chCharShanbe);
        chPanjShanbe = findViewById(R.id.chPanjShanbe);
        chJome = findViewById(R.id.chJome);
        chShanbe.setText(persianWeekDays[0]);
        chYekShanbe.setText(persianWeekDays[1]);
        chDoShanbe.setText(persianWeekDays[2]);
        chSeShanbe.setText(persianWeekDays[3]);
        chCharShanbe.setText(persianWeekDays[4]);
        chPanjShanbe.setText(persianWeekDays[5]);
        chJome.setText(persianWeekDays[6]);
        for (String day : selectedDay) {
            if (day.equals(persianWeekDays[0])) {
                chShanbe.setChecked(true);
            }
            if (day.equals(persianWeekDays[1])) {
                chYekShanbe.setChecked(true);
            }
            if (day.equals(persianWeekDays[2])) {
                chDoShanbe.setChecked(true);
            }
            if (day.equals(persianWeekDays[3])) {
                chSeShanbe.setChecked(true);
            }
            if (day.equals(persianWeekDays[4])) {
                chCharShanbe.setChecked(true);
            }
            if (day.equals(persianWeekDays[5])) {
                chPanjShanbe.setChecked(true);
            }
            if (day.equals(persianWeekDays[6])) {
                chJome.setChecked(true);
            }
        }


        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterface.onRejected();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<String> daySelected = new ArrayList<>();
                for (int i = 0; i < root.getChildCount(); i++) {

                    if (root.getChildAt(i) instanceof AppCompatCheckBox) {

                        if (((AppCompatCheckBox) root.getChildAt(i)).isChecked()) {
                            daySelected.add(((AppCompatCheckBox) root.getChildAt(i)).getText().toString());
                        }
                    }
                }
                myInterface.onSuccess(daySelected);
            }
        });


    }


    @Override
    public void onBackPressed() {
        myInterface.onRejected();
        super.onBackPressed();
    }
}
