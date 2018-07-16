package greencode.ir.pillcci.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alirezaafkar.sundatepicker.DatePicker;
import com.alirezaafkar.sundatepicker.interfaces.DateSetListener;
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
import saman.zamani.persiandate.PersianDate;

/**
 * Created by alireza on 7/2/18.
 */

public class FilterActivity extends BaseActivity implements DateSetListener{
    @BindView(R.id.img_back)
    AppCompatImageView imgBack;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
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
        midName = DatabaseManager.getAllMidNames(this);
        catNames = DatabaseManager.getAllCatNames(this);
        pilAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, midName);
        catAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, catNames);

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

                        .minYear(date.getShYear())
                        .maxYear(date.getShYear())
                        .maxMonth(date.getShMonth())
                        .showYearFirst(false)
                        .closeYearAutomatically(true)
                        .theme(R.style.DialogTheme)
                        .date(calendar)
                        .build(FilterActivity.this)
                        .show(getSupportFragmentManager(), "شروع مصرف");

                break;
            case R.id.edtFinishDate:
                 justNow = System.currentTimeMillis();
                 final Calendar calendar2 = Calendar.getInstance();
                calendar2.setTimeInMillis(justNow);
                 date = new PersianDate(System.currentTimeMillis());
                new DatePicker.Builder()
                        .id(2)

                        .minYear(date.getShYear())
                        .maxYear(date.getShYear())
                        .maxMonth(date.getShMonth())
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
