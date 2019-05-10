package greencode.ir.pillcci.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.alirezaafkar.sundatepicker.DatePicker;
import com.alirezaafkar.sundatepicker.interfaces.DateSetListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.isapanah.awesomespinner.AwesomeSpinner;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.utils.BaseActivity;
import greencode.ir.pillcci.utils.DatabaseManager;
import greencode.ir.pillcci.utils.PersianCalculater;
import greencode.ir.pillcci.utils.Utility;
import saman.zamani.persiandate.PersianDate;

/**
 * Created by alireza on 7/2/18.
 */

public class FilterActivity extends BaseActivity implements DateSetListener{
    @BindView(R.id.img_back)
    AppCompatImageView imgBack;
    @BindView(R.id.title)
    TextView txtTitle;

    @BindView(R.id.pill_spinner)
    AwesomeSpinner pillSpinner;
    @BindView(R.id.cat_spinner)
    AwesomeSpinner catSpinner;
    @BindView(R.id.edtStartDate)
    EditText edtStartDate;
    @BindView(R.id.edtFinishDate)
    EditText edtFinishDate;
    @BindView(R.id.btnInsert)
    Button btnInsert;
    @BindView(R.id.btnDelete)
    Button btnDelete;
    ArrayAdapter<String> pilAdapter;
    ArrayAdapter<String> catAdapter;
    ArrayList<String> midName = new ArrayList<>();
    ArrayList<String> catNames = new ArrayList<>();

    FirebaseAnalytics firebaseAnalytics;
    int sYear=0;
    int sMonth=0;
    int sDay=0;
    int fYear=0;
    int fMonth=0;
    int fDay=0;

    String pillName="";
    String catName ="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_activity);
        ButterKnife.bind(this);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle params = new Bundle();
        params.putString("phoneNumber", Utility.getPhoneNumber(this));
        firebaseAnalytics.logEvent("filter_open", params);
        midName = DatabaseManager.getAllMidNames(this);

        catNames = DatabaseManager.getAllCatNames(this);

        for(int i = 0 ; i <catNames.size(); i++){
            if(catNames.get(i).equals("")){
                catNames.set(i,"خودم");
                break;
            }
        }

        int countKhodam = 0;
        int lastPosKhodam = -1;
        for(int i = 0; i<catNames.size();i++){
            if(catNames.get(i).equals("خودم")){
                countKhodam++;
                lastPosKhodam = i;
            }
        }
        if(countKhodam==2){
            catNames.remove(lastPosKhodam);
        }

        pilAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, midName);
        catAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, catNames);
        txtTitle.setText("فیلتر کردن گزارش");
        pillSpinner.setAdapter(pilAdapter);
        catSpinner.setAdapter(catAdapter);
        pillSpinner.setOnSpinnerItemClickListener(new AwesomeSpinner.onSpinnerItemClickListener<String>() {
            @Override
            public void onItemSelected(int position, String itemAtPosition) {
                //TODO YOUR ACTIONS
            }
        });
        catSpinner.setOnSpinnerItemClickListener(new AwesomeSpinner.onSpinnerItemClickListener<String>() {
            @Override
            public void onItemSelected(int position, String itemAtPosition) {
                //TODO YOUR ACTIONS
            }
        });
    }


    @SuppressLint("InvalidAnalyticsName")
    @OnClick({R.id.img_back, R.id.edtStartDate, R.id.edtFinishDate, R.id.btnInsert, R.id.btnDelete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.edtStartDate:
                long justNow = System.currentTimeMillis();
                final Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(justNow);
                PersianDate date = new PersianDate(System.currentTimeMillis());
                new DatePicker.Builder()
                        .id(1)

                        .minDate(date.getShYear(),1,1)
                        .maxDate(date.getShYear(),date.getShMonth(),30)
                        .showYearFirst(false)
                        .closeYearAutomatically(true)
                        .theme(R.style.DialogTheme)
                        .date(calendar)
                        .build(FilterActivity.this)
                        .show(getSupportFragmentManager(), "شروع مجدد");

                break;
            case R.id.edtFinishDate:
                 justNow = System.currentTimeMillis();
                 final Calendar calendar2 = Calendar.getInstance();
                calendar2.setTimeInMillis(justNow);
                 date = new PersianDate(System.currentTimeMillis());
                new DatePicker.Builder()
                        .id(2)
                        .minDate(date.getShYear(),1,1)
                        .maxDate(date.getShYear(),date.getShMonth(),30)
                        .showYearFirst(false)
                        .closeYearAutomatically(true)
                        .theme(R.style.DialogTheme)
                        .date(calendar2)
                        .build(FilterActivity.this)
                        .show(getSupportFragmentManager(), "پایان مصرف");
                break;
            case R.id.btnInsert:
                if(pillSpinner.isSelected()){
                    if(pillSpinner.getSelectedItemPosition()==0){
                        pillName="";
                    }else {
                        pillName=pillSpinner.getSelectedItem();

                    }
                }else {
                    pillName="";
                }
                if(catSpinner.isSelected()){
                    if(catSpinner.getSelectedItemPosition()==0){
                        catName="";
                    }else {
                        catName=catSpinner.getSelectedItem();
                    }
                }else {
                    catName="";
                }

                Bundle params = new Bundle();
                String par1,par2,par3,par4;
                if(sYear==0){
                    par1="no";
                }else {
                    par1="yes";
                }
                if(fYear==0){
                    par2="no";
                }else {
                    par2="yes";
                }
                if(pillName.equals("")){
                    par3 ="no";
                }else {
                    par3 ="yes";
                }
                if(catName.equals("")){
                    par4="no";
                }else {
                    par4="yes";
                }
                params.putString("phoneNumber", Utility.getPhoneNumber(this));
                String finalPar = "A_"+par1+"_start"+"&"+par2+"_fin&"+par3+"_name&"+par4+"_cat";
                firebaseAnalytics.logEvent(finalPar, params);


                Intent intent = getIntent();
                intent.putExtra("sYear", sYear);
                intent.putExtra("sMonth", sMonth);
                intent.putExtra("sDay", sDay);
                intent.putExtra("fYear", fYear);
                intent.putExtra("fMonth", fMonth);
                intent.putExtra("fDay", fDay);
                intent.putExtra("pilName", pillName);
                intent.putExtra("catName", catName);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.btnDelete:
                finish();
                break;
        }
    }

    @Override
    public void onDateSet(int id, @Nullable Calendar calendar, int day, int month, int year) {
        if(id==1){
            sDay=day;
            sMonth=month;
            sYear=year;
            PersianDate date = new PersianDate();
            date.setShYear(sYear);
            date.setShMonth(sMonth);
            date.setShDay(sDay);
            edtStartDate.setText(date.getShYear()+"/"+PersianCalculater.getMonthAndDay(date.getTime()));
        }else {
            fDay=day;
            fMonth=month;
            fYear=year;
            PersianDate date = new PersianDate();
            date.setShYear(fYear);
            date.setShMonth(fMonth);
            date.setShDay(fDay);
            edtFinishDate.setText(date.getShYear()+"/"+PersianCalculater.getMonthAndDay(date.getTime()));


        }
    }
}
