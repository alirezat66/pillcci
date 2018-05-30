package greencode.ir.pillcci.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import greencode.ir.pillcci.R;
import greencode.ir.pillcci.adapter.EditEachTimeAdapter;
import greencode.ir.pillcci.objects.EachUsage;

/**
 * Created by alireza on 5/18/18.
 */

public class EachTimeEditPartDialog extends Dialog {

    EachTimeEditPart myInterface;
    Context context;
    RecyclerView container;
    EditText edtStartTime;
    Button btnOk,btnCancel;

    String startTime;
    String unit;
    int count;
    double diffrenceOfUsage;
    double eachTimeUsage;
    EditEachTimeAdapter adapter;

    public EachTimeEditPartDialog(Context context, int startTimeHour, int startTimeMin, int countOfUsagePerDay, double diffrenceOfUsage, String unitUse,double eachTimeUsage) {
        super(context);
        this.context = context;
        String startStr;
        String startMinStr;
        if(startTimeHour<10){
            startStr="0"+startTimeHour;
        }else {
            startStr=startTimeHour+"";
        }
        if(startTimeMin==0){
            startMinStr="00";
        }else {
            startMinStr=""+startTimeMin;
        }
        this.startTime = startStr+":"+startMinStr;
        this.count=countOfUsagePerDay;
        this.unit= unitUse;
        this.diffrenceOfUsage =diffrenceOfUsage;
        this.eachTimeUsage=eachTimeUsage;
    }

    public void setListener(EachTimeEditPart listener) {
        this.myInterface = listener;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        View view = View.inflate(context, R.layout.dialog_each_time_edit_part, null);

        setContentView(view);
        setCancelable(false);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = -1;
        getWindow().setAttributes(params);


        btnCancel = findViewById(R.id.btnCancle);
        btnOk = findViewById(R.id.btnOk);
        edtStartTime = findViewById(R.id.edtStartEvrayDay);
        container = findViewById(R.id.container);
        edtStartTime.setText(startTime);

        ArrayList<EachUsage> list = new ArrayList<>();
        for(int i=0;i<count;i++){
            EachUsage item = new EachUsage(startTime,unit,eachTimeUsage+"");
            list.add(item);
            startTime = calcNewTime(startTime);



           /* TextView text = (TextView) to_add.findViewById(R.id.text);
            text.setText(options[i]);
            text.setTypeface(FontSelector.getBold(getActivity()));*/

        }
        container.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        adapter =new EditEachTimeAdapter(context,list);
        container.setAdapter(adapter);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCurrect=true;
                ArrayList<String>usageCountList = new ArrayList<>();
                ArrayList<String>timeList = new ArrayList<>();
                for(EachUsage eachUsage:adapter.getList()){
                    if(eachUsage.getEachUse().length()==0){
                        isCurrect=false;
                        break;
                    }else {
                        usageCountList.add(Double.parseDouble(eachUsage.getEachUse())+"");
                        timeList.add(eachUsage.getTimeStr());
                    }

                }
                if(isCurrect){
                    myInterface.onSuccess(usageCountList,timeList);
                }else {
                    Toast.makeText(context, "میزان مصرف نمی تواند مقدار خالی داشته باشد.", Toast.LENGTH_LONG).show();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterface.onRejected();
            }
        });




    }

    private String calcNewTime(String startTime) {
        String myTime = startTime;
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Date d = null;
        try {
            d = df.parse(myTime);
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);

            double amount = diffrenceOfUsage*60;


            cal.add(Calendar.MINUTE, (int) amount);
            String newTime = df.format(cal.getTime());
            return newTime;
        } catch (ParseException e) {

            e.printStackTrace();
            return startTime;
        }

    }

    @Override
    public void onBackPressed() {
        myInterface.onRejected();
        super.onBackPressed();
    }
}
