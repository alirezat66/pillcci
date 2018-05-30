package greencode.ir.pillcci.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.iwgang.countdownview.CountdownView;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.activities.AddMedicianActivity;
import greencode.ir.pillcci.adapter.TodayUsageAdapter;
import greencode.ir.pillcci.controler.AppDatabase;
import greencode.ir.pillcci.database.PillUsage;
import greencode.ir.pillcci.dialog.DialogUsagePicker;
import greencode.ir.pillcci.dialog.TimeUsageInterface;
import saman.zamani.persiandate.PersianDate;

/**
 * Created by alireza on 5/27/18.
 */

public class FragmentToday extends Fragment implements OnDateSetListener,TodayUsageAdapter.UsageInterface {


    @BindView(R.id.countOfDay)
    TextView countOfDay;
    @BindView(R.id.countOfUsed)
    TextView countOfUsed;
    @BindView(R.id.txtNextRemind)
    TextView txtNextRemind;
    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.txtToday)
    TextView txtToday;
    @BindView(R.id.fabBtn)
    FloatingActionButton fabBtn;
    TodayUsageAdapter adapter;
    @BindView(R.id.difrenceLayout)
    LinearLayout difrenceLayout;
    @BindView(R.id.countDown)
    CountdownView countDown;

    boolean pageExist=false;
    TimePickerDialog mDialogHourMinute;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today, container, false);
        ButterKnife.bind(this, view);
        pageExist=true;
        return view;
    }

    @Override
    public void onStart() {
        if(pageExist){
            makePage();
        }
        super.onStart();
    }
   public void makePage(){
        long todayTime = System.currentTimeMillis();
       Date date = new Date(System.currentTimeMillis());
         PersianDate persianDate = new PersianDate(System.currentTimeMillis());
         txtToday.setText(persianDate.dayName()+" "+persianDate.getShDay()+" " +persianDate.monthName());
        long startDay = getStartOfDay(date).getTime();
        long endDay = getEndOfDay(date).getTime();
        AppDatabase database = AppDatabase.getInMemoryDatabase(getContext());
        ArrayList<PillUsage> allUsage = new ArrayList<>(database.pillUsageDao().listPillUsage());
        ArrayList<PillUsage> usages = new ArrayList<>(database.pillUsageDao().listPillToday(startDay, endDay));

        if (usages != null) {

            countOfDay.setText(usages.size() + "");

            if (usages.size() > 0) {
                adapter = new TodayUsageAdapter(getContext(), usages,this);
                list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                list.setAdapter(adapter);
                countOfUsed.setText(adapter.getUsedUsageCount() + "");
            } else {
                countOfUsed.setText("0");

            }
        } else {
            countOfDay.setText("0");
            countOfUsed.setText("0");
        }

        PillUsage pillUsage = database.pillUsageDao().getNearestUsage(System.currentTimeMillis());
        if (pillUsage != null) {
            long dif = pillUsage.getUsageTime() - System.currentTimeMillis();
            countDown.start(dif);
            countDown.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                @Override
                public void onEnd(CountdownView cv) {
                    AppDatabase database1 = AppDatabase.getInMemoryDatabase(getContext());
                    PillUsage pillUsage = database1.pillUsageDao().getNearestUsage(System.currentTimeMillis());
                    if (pillUsage != null) {
                        long dif = pillUsage.getUsageTime() - System.currentTimeMillis();
                        countDown.start(dif);
                    }else {
                        difrenceLayout.setVisibility(View.GONE);
                    }
                }
            });


        } else {
            difrenceLayout.setVisibility(View.GONE);
        }
    }
    @OnClick(R.id.fabBtn)
    public void onClick() {

        Intent intent = new Intent(getContext(), AddMedicianActivity.class);
        startActivity(intent);
    }

    private Date getStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        calendar.set(year, month, day, 0, 0, 0);
        return calendar.getTime();
    }

    private Date getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        calendar.set(year, month, day, 23, 59, 59);
        return calendar.getTime();
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        PersianDate calendar = new PersianDate(millseconds);

        Toast.makeText(getContext(), calendar.getShYear()+"", Toast.LENGTH_LONG).show();
    }

    @Override
    public void usageAct(final PillUsage item) {
        final DialogUsagePicker dialog = new DialogUsagePicker(getContext(),getActivity(),getFragmentManager(),item.getUsageTime());
        dialog.setListener(new TimeUsageInterface() {
            @Override
            public void onSuccess(long timeUsage) {
                AppDatabase database = AppDatabase.getInMemoryDatabase(getContext());
                item.setUsedTime(timeUsage);
                item.setState(1);
                database.pillUsageDao().update(item);

                dialog.dismiss();
                makePage();

            }

            @Override
            public void onRejected() {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void cancelAct(PillUsage item) {

    }

    @Override
    public void jumpAct(PillUsage item) {

    }



}
