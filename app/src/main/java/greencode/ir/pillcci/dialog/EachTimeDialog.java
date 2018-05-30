package greencode.ir.pillcci.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import fr.ganfra.materialspinner.MaterialSpinner;
import greencode.ir.pillcci.R;

/**
 * Created by alireza on 5/18/18.
 */

public class EachTimeDialog extends Dialog {

    EachTimeInterface myInterface;
    Context context;
    Button btnCancle;
    Button btnOk;
    TextInputEditText edtEachTime;
    MaterialSpinner spinner;
    public EachTimeDialog(Context context) {
        super(context);
        this.context = context;
    }

    public void setListener(EachTimeInterface listener) {
        this.myInterface = listener;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        View view = View.inflate(context, R.layout.dialog_each_usage, null);
        setContentView(view);

        setCancelable(false);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = -1;
        getWindow().setAttributes(params);
        btnCancle = findViewById(R.id.btnCancle);
        btnOk = findViewById(R.id.btnOk);
        spinner = findViewById(R.id.spinner);

        edtEachTime = findViewById(R.id.edtEachTime);
        final String[] ITEMS = {"عدد", "قرص", "کپسول", "سی سی/میلی لیتر", "قاشق چای خوری","بار","قاشق غذا خوری",
                "قطره","پیمانه","پاف/اسپری","گرم","میلی گرم","قطعه/تکه","برچسب"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtEachTime.getText().toString().length()!=0){
                    if(spinner.getSelectedItemPosition()-1>=0){
                        myInterface.onSuccess(Double.parseDouble(edtEachTime.getText().toString()),ITEMS[spinner.getSelectedItemPosition()-1]);
                    }else {
                        Toast.makeText(context, "واحد انتخاب نشده است.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(context, "مقدار هر بار مصرف ذکر نشده است.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterface.onRejected();
            }
        });

        edtEachTime.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
