package greencode.ir.pillcci.activities;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.github.florent37.tutoshowcase.TutoShowcase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.JsonObject;
import com.ss.bottomnavigation.BottomNavigation;
import com.ss.bottomnavigation.TabItem;
import com.ss.bottomnavigation.events.OnSelectedItemChangeListener;

import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.controler.AppDatabase;
import greencode.ir.pillcci.fragments.FragmentHistory;
import greencode.ir.pillcci.fragments.FragmentMore;
import greencode.ir.pillcci.fragments.FragmentPills;
import greencode.ir.pillcci.fragments.FragmentToday;
import greencode.ir.pillcci.retrofit.CallerService;
import greencode.ir.pillcci.retrofit.ServerListener;
import greencode.ir.pillcci.utils.BaseActivity;
import greencode.ir.pillcci.utils.Constants;
import greencode.ir.pillcci.utils.PreferencesData;
import greencode.ir.pillcci.utils.Utility;

public class MainActivity extends BaseActivity implements ServerListener /*implements MultiDatePickerDialog.OnDateSetListener*/{
    boolean doubleBackToExitPressedOnce = false;
    int lastPage=0;
    int currentPage=0;
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
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);



        addFragmentList();
         fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter, R.anim.enter);
        transaction.replace(R.id.container,  fragmentList.get(3) ).commit();





       // fragmentManager.beginTransaction().replace(R.id.container, fragmentList.get(3)).commit();

        //  spaceTabLayout.initialize(viewPager, getSupportFragmentManager(), fragmentList, savedInstanceState);
       /* Intent service = new Intent(this, ControlServices.class);
        startService(service);*/
        bottomNavigation.setTypeface(Utility.getRegularTypeFace(this));
        bottomNavigation.setOnSelectedItemChangeListener(new OnSelectedItemChangeListener() {
            @Override
            public void onSelectedItemChanged(int itemId) {
                switch (itemId){
                    case R.id.tab_home:
                        currentPage = 3;
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        if(currentPage<=lastPage) {
                            transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                            transaction.replace(R.id.container, fragmentList.get(3)).commit();
                        }else {
                            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                            transaction.replace(R.id.container, fragmentList.get(3)).commit();
                        }
                        lastPage = 3;
                       // fragmentManager.beginTransaction().replace(R.id.container,fragmentList.get(3)).commit();
                        break;
                    case R.id.tab_images:
                        currentPage = 2;
                        transaction = fragmentManager.beginTransaction();
                        if(currentPage<=lastPage) {
                            transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                            transaction.replace(R.id.container, fragmentList.get(2)).commit();
                        }else {
                            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                            transaction.replace(R.id.container, fragmentList.get(2)).commit();
                        }
                        lastPage =2;
                       // fragmentManager.beginTransaction().replace(R.id.container,fragmentList.get(2)).commit();
                        break;
                    case R.id.tab_camera:
                        currentPage = 1;
                         transaction = fragmentManager.beginTransaction();
                         if(currentPage<=lastPage) {
                             transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                             transaction.replace(R.id.container, fragmentList.get(1)).commit();
                         }else {
                             transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                             transaction.replace(R.id.container, fragmentList.get(1)).commit();
                         }
                         lastPage = 1;
                     //   fragmentManager.beginTransaction().replace(R.id.container,fragmentList.get(1)).commit();
                        break;
                    case R.id.tab_products:
                         currentPage =0;
                         transaction = fragmentManager.beginTransaction();
                        if(currentPage<=lastPage) {
                             transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                             transaction.replace(R.id.container, fragmentList.get(0)).commit();
                         }else {
                            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                             transaction.replace(R.id.container, fragmentList.get(0)).commit();
                         }
                         lastPage = 0;
                       // fragmentManager.beginTransaction().replace(R.id.container,fragmentList.get(0)).commit();
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


       if(PreferencesData.getBoolean("firstHome",true)){
           firstItem();
       }
        if(!PreferencesData.getBoolean("tokenSeted",false)){
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w("hilevel", "getInstanceId failed", task.getException());
                                return;
                            }
                            // Get new Instance ID token
                            String token = task.getResult().getToken();
                            // Log and toast
                            String msg = getString(R.string.msg_token_fmt, token);

                            if(!PreferencesData.getBoolean(Constants.PREF_Guess,false)){
                                AppDatabase database = AppDatabase.getInMemoryDatabase(MainActivity.this);
                                String userId = database.profileDao().getMyProfile().getMyId();
                                CallerService.updateToken(MainActivity.this,userId,token);
                                PreferencesData.saveString(Constants.Pref_Token,token);
                            }



                            Log.d("hilevel", msg);
                        }
                    });
        }


    }


    private void addFragmentList() {

        //add the fragments you want to display in a List
        fragmentList = new ArrayList<>();
        fragmentList.add(new FragmentMore());
        fragmentList.add(new FragmentHistory());
        fragmentList.add(new FragmentPills());
        fragmentList.add(new FragmentToday());


    }


    private void firstItem(){

        TutoShowcase.from(this)
                .setContentView(R.layout.tuto_showcase_tuto_home).onClickContentView(R.id.root, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
            })
                .withDismissView(R.id.btn_ok)
                .setListener(new TutoShowcase.Listener() {
                    @Override
                    public void onDismissed() {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                sabadGuid();
                            }
                        },200);
                    }
                })
                .setFitsSystemWindows(true)
                .on(tabHome)
                .addCircle().withBorder()
                .show();
         }

    private void sabadGuid() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        transaction.replace(R.id.container,  fragmentList.get(2) ).commit();
        tabImages.setSelected(true);


        TutoShowcase.from(this)
                .setContentView(R.layout.tuto_showcase_tuto_sabad).onClickContentView(R.id.root, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
             })
                .withDismissView(R.id.btn_ok)
                .setListener(new TutoShowcase.Listener() {
                    @Override
                    public void onDismissed() {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                history();
                            }
                        },200);
                    }
                })
                .setFitsSystemWindows(true)
                .on(tabImages)
                .addCircle().withBorder()
                .show();
    }
    private void moreGuide() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        tabProducts.setSelected(true);
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        transaction.replace(R.id.container,  fragmentList.get(0) ).commit();

        TutoShowcase.from(this)
                .setContentView(R.layout.tuto_showcase_tuto_setting).
                onClickContentView(R.id.root, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        PreferencesData.saveBool("firstHome",false);
                        tabHome.setSelected(true);
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        transaction.replace(R.id.container,  fragmentList.get(3) ).commit();                            }
                },500);
            }
        })
                .withDismissView(R.id.btn_ok)
                .setListener(new TutoShowcase.Listener() {
                    @Override
                    public void onDismissed() {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                PreferencesData.saveBool("firstHome",false);
                                tabHome.setSelected(true);
                                FragmentTransaction transaction = fragmentManager.beginTransaction();
                                transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                                transaction.replace(R.id.container,  fragmentList.get(3) ).commit();                            }
                        },500);
                    }
                })
                .setFitsSystemWindows(true)
                .on(tabProducts)
                .addCircle().withBorder()
                .show();





    }

    private void history() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        transaction.replace(R.id.container,  fragmentList.get(1) ).commit();
        tabCamera.setSelected(true);

        TutoShowcase.from(this)
                .setContentView(R.layout.tuto_showcase_tuto_history).
                onClickContentView(R.id.root, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                })
                .withDismissView(R.id.btn_ok)
                .setListener(new TutoShowcase.Listener() {
                    @Override
                    public void onDismissed() {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                moreGuide();
                            }
                        },200);
                    }
                })
                .setFitsSystemWindows(true)
                .on(tabCamera)
                .addCircle().withBorder()
                .show();
    }

    private void finishToasCase() {
        PreferencesData.saveBool("firstHome",false);
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
        Toast toast  = Toast.makeText(this, "برای خروج کلید بازگشت را دوبار فشار بده!", Toast.LENGTH_LONG);
        Utility.centrizeAndShow(toast);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 4000);
    }

    @Override
    public void onFailure(int i, String str) {
        PreferencesData.saveBool("tokenSeted",false);
    }

    @Override
    public void onSuccess(int i, JsonObject jsonObject) throws JSONException {
        PreferencesData.saveBool("tokenSeted",true);
    }
}
