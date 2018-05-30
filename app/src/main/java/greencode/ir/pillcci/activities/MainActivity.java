package greencode.ir.pillcci.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.long1.spacetablayout.SpaceTabLayout;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.adapter.UsageAdapter;
import greencode.ir.pillcci.controler.AppDatabase;
import greencode.ir.pillcci.database.PillUsage;
import greencode.ir.pillcci.fragments.FragmentHistory;
import greencode.ir.pillcci.fragments.FragmentMore;
import greencode.ir.pillcci.fragments.FragmentPills;
import greencode.ir.pillcci.fragments.FragmentToday;
import greencode.ir.pillcci.utils.BaseActivity;
public class MainActivity extends BaseActivity implements OnDateSetListener {
    private static final String LAST_JOB_ID = "LAST_JOB_ID";
    TimePickerDialog mDialogHourMinute;

    @BindView(R.id.fabBtn)
    FloatingActionButton fabBtn;
    int count = 0;
    ArrayList<PillUsage> usages = new ArrayList<>();
    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.emptyLayout)
    TextView emptyLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.spaceTabLayout)
    SpaceTabLayout spaceTabLayout;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.toolBar)
    Toolbar toolBar;

    private ArrayList<Fragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        addFragmentList();
        txtTitle.setText("امروز ");
        spaceTabLayout.initialize(viewPager, getSupportFragmentManager(), fragmentList, savedInstanceState);
        spaceTabLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                int currentPosition = spaceTabLayout.getCurrentPosition();
                if(currentPosition==3){
                    txtTitle.setText("امروز ");
                }else if(currentPosition==2){
                    txtTitle.setText("سبد دارو");
                }else if(currentPosition==1){
                    txtTitle.setText("تاریخچه مصرف");
                }else {
                    txtTitle.setText("بیشتر");
                }
            }
        });
       /* Intent service = new Intent(this, ControlServices.class);
        startService(service);*/

        spaceTabLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplication(), "" + spaceTabLayout.getCurrentPosition(), Toast.LENGTH_SHORT).show();
            }
        });


       /* AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, EventReciver.class);
        intent.setAction("");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0,intent,0);
        manager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+10000,pendingIntent);
*/
    }

    private void addFragmentList() {

        //add the fragments you want to display in a List
        fragmentList = new ArrayList<>();
        fragmentList.add(new FragmentMore());
        fragmentList.add(new FragmentHistory());
        fragmentList.add(new FragmentPills());
        fragmentList.add(new FragmentToday());


    }

    @Override
    protected void onResume() {


     /*   PersianCalendar calendar = new PersianCalendar();

        calendar.setPersianDate(1397, 2, 1);
        calendar.set(PersianCalendar.HOUR_OF_DAY, 13);
        calendar.set(PersianCalendar.MINUTE, 0);
        calendar.set(PersianCalendar.SECOND, 0);

        long startTime = calendar.getTimeInMillis();
        PersianCalendar cal2 = new PersianCalendar(startTime);
        cal2.setTimeZone(TimeZone.getTimeZone("UTC"));*/


        AppDatabase database = AppDatabase.getInMemoryDatabase(this);
        usages = new ArrayList<>(database.pillUsageDao().listPillUsage());
        if (usages.size() > 0) {
            emptyLayout.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);
            list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            list.setAdapter(new UsageAdapter(this, usages));
        } else {
            emptyLayout.setVisibility(View.VISIBLE);
            list.setVisibility(View.GONE);
        }
        super.onResume();
    }

    @OnClick(R.id.fabBtn)
    public void onClick() {
        /*Intent intent = new Intent(this, AddMedicianActivity.class);
        startActivity(intent);*/


    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
       /* PersianCalendar calendar = new PersianCalendar(millseconds);

        Toast.makeText(this, calendar.getPersianShortDateTime(), Toast.LENGTH_LONG).show();*/
    }


    /*@Override
    public void onDateSet(int id, @Nullable Calendar calendar, int day, int month, int year) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        btnTaghvim.setText(year+"/"+month+"/"+day);
        //
        // calendar.HOUR=15;
        calendar.set(Calendar.HOUR_OF_DAY ,15);
        calendar.set(Calendar.MINUTE , 25);

        PersianCalendar calendar1 = new PersianCalendar(calendar.getTimeInMillis());
        txt5.setText(format.format(calendar.getTime()));
        txt6.setText(calendar1.getPersianShortDateTime());

        long destance = System.currentTimeMillis()-calendar1.getTimeInMillis();

        txt7.setText(destance+"---"+(destance/1000)+"----"+(destance/1000/60));
    }*/
}
