package greencode.ir.pillcci.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatDelegate;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ss.bottomnavigation.BottomNavigation;
import com.ss.bottomnavigation.TabItem;
import com.ss.bottomnavigation.events.OnSelectedItemChangeListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.fragments.FragmentHistory;
import greencode.ir.pillcci.fragments.FragmentMore;
import greencode.ir.pillcci.fragments.FragmentPills;
import greencode.ir.pillcci.fragments.FragmentToday;
import greencode.ir.pillcci.utils.BaseActivity;
import greencode.ir.pillcci.utils.Utility;

public class MainActivity extends BaseActivity /*implements MultiDatePickerDialog.OnDateSetListener*/{
    boolean doubleBackToExitPressedOnce = false;

    @BindView(R.id.tab_home)
    TabItem tabHome;
    @BindView(R.id.tab_images)
    TabItem tabImages;
    @BindView(R.id.tab_camera)
    TabItem tabCamera;
    @BindView(R.id.tab_products)
    TabItem tabProducts;
    @BindView(R.id.bottom_navigation)
    BottomNavigation bottomNavigation;
    @BindView(R.id.container)
    RelativeLayout container;
    private ArrayList<Fragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);



        addFragmentList();
        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragmentList.get(3)).commit();

        //  spaceTabLayout.initialize(viewPager, getSupportFragmentManager(), fragmentList, savedInstanceState);
       /* Intent service = new Intent(this, ControlServices.class);
        startService(service);*/
        bottomNavigation.setTypeface(Utility.getRegularTypeFace(this));
        bottomNavigation.setOnSelectedItemChangeListener(new OnSelectedItemChangeListener() {
            @Override
            public void onSelectedItemChanged(int itemId) {
                switch (itemId){
                    case R.id.tab_home:
                        fragmentManager.beginTransaction().replace(R.id.container,fragmentList.get(3)).commit();
                        break;
                    case R.id.tab_images:
                        fragmentManager.beginTransaction().replace(R.id.container,fragmentList.get(2)).commit();
                        break;
                    case R.id.tab_camera:
                        fragmentManager.beginTransaction().replace(R.id.container,fragmentList.get(1)).commit();
                        break;
                    case R.id.tab_products:
                        fragmentManager.beginTransaction().replace(R.id.container,fragmentList.get(0)).commit();
                        break;

                }
            }
        });



       /* PersianCalendar[] pc = new PersianCalendar[30];
        ArrayList<PersianCalendar>calendars=new ArrayList<>();
        PersianDate date = new PersianDate(System.currentTimeMillis());
        PersianCalendar calendar = new PersianCalendar();
        calendar.setTimeInMillis(date.getTime());
        for (int i = 0; i < pc.length; i++) {
            calendars.add(calendar);
            date.addDay(1);
            calendar =  new PersianCalendar();
            calendar.setTimeInMillis(date.getTime());

        }

        Toast.makeText(this, calendars.get(0).getPersianShortDate(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, calendars.get(calendars.size()-1).getPersianShortDate(), Toast.LENGTH_SHORT).show();
        MultiDatePickerDialog mdpd = MultiDatePickerDialog.newInstance(MainActivity.this, null);
        mdpd.setMinDate(calendars.get(0));
        mdpd.setMaxDate(calendars.get(calendars.size()-1));
        mdpd.show(getFragmentManager(), "انتخاب چندتایی");*/
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


        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "برای خروج کلید بازگشت را دوبار فشار بده.", Toast.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 4000);
    }
}
