package greencode.ir.pillcci.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.isapanah.awesomespinner.AwesomeSpinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.utils.KeyboardUtil;
import greencode.ir.pillcci.utils.Utility;

/**
 * Created by alireza on 5/18/18.
 */

@SuppressLint("ValidFragment")
public class EachTimeDialog extends BottomSheetDialogFragment {

    EachTimeInterface myInterface;
    Context context;


    @BindView(R.id.edtEachTime)
    TextInputEditText edtEachTime;
    @BindView(R.id.spinner)
    AwesomeSpinner spinner;
    @BindView(R.id.btnCancle)
    Button btnCancle;
    @BindView(R.id.btnOk)
    Button btnOk;
    double countOfEachUse=0;
    String unitUse="";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new BottomSheetDialog(getContext()) {

            @Override
            public void onBackPressed() {
                myInterface.onRejected();
            }
        };
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(context, R.layout.dialog_each_usage, null);
        ButterKnife.bind(this, view);

        final String[] ITEMS = {"عدد", "قرص", "کپسول", "سی سی/میلی لیتر", "قاشق چای خوری", "بار", "قاشق غذا خوری",
                "قطره", "پیمانه", "پاف/اسپری", "گرم", "میلی گرم", "قطعه/تکه", "برچسب"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        int position = -1;
        if(!unitUse.equals("")){
            for(int i = 0 ; i<ITEMS.length;i++){
                if(ITEMS[i].equals(unitUse)){
                    position = i;
                    break;
                }
            }
        }
        if(position!=-1){
            spinner.setSelection(position+1);
        }
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtEachTime.getText().toString().length() != 0) {
                    if (spinner.getSelectedItemPosition() - 1 >= 0) {
                        myInterface.onSuccess(Double.parseDouble(edtEachTime.getText().toString()), ITEMS[spinner.getSelectedItemPosition() - 1]);
                    } else {
                        Toast toast = Toast.makeText(context, "واحد انتخاب نشده است.", Toast.LENGTH_LONG);
                        Utility.centrizeAndShow(toast);
                    }
                } else {
                    Toast toast = Toast.makeText(context, "دوز مصرف را وارد کن.", Toast.LENGTH_LONG);
                    Utility.centrizeAndShow(toast);
                }
            }
        });
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterface.onRejected();
            }
        });
        if(countOfEachUse!=0){
            edtEachTime.setText(countOfEachUse+"");
        }

        new KeyboardUtil(getActivity(),view);


        return view;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
             setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);


    }

    public EachTimeDialog(Context context,String unitUse,double countOfEachUse) {

        this.context = context;
        this.unitUse=unitUse;
        this.countOfEachUse=countOfEachUse;
    }

    public void setListener(EachTimeInterface listener) {
        this.myInterface = listener;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
