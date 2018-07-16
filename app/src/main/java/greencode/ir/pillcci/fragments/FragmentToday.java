package greencode.ir.pillcci.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alirezaafkar.sundatepicker.DatePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.activities.AddMedicianActivity;
import greencode.ir.pillcci.adapter.TodayUsageAdapter;
import greencode.ir.pillcci.controler.AppDatabase;
import greencode.ir.pillcci.database.PillObject;
import greencode.ir.pillcci.database.PillUsage;
import greencode.ir.pillcci.dialog.CancelDialog;
import greencode.ir.pillcci.dialog.CancelEditDialog;
import greencode.ir.pillcci.dialog.CancelListener;
import greencode.ir.pillcci.dialog.DialogUsagePicker;
import greencode.ir.pillcci.dialog.FinishDialog;
import greencode.ir.pillcci.dialog.FinishListener;
import greencode.ir.pillcci.dialog.TimeUsageInterface;
import greencode.ir.pillcci.dialog.YesNoDialog;
import greencode.ir.pillcci.dialog.YesNoListener;
import greencode.ir.pillcci.timepicker.TimePickerDialog;
import greencode.ir.pillcci.timepicker.listener.OnDateSetListener;
import greencode.ir.pillcci.utils.DatabaseManager;
import greencode.ir.pillcci.utils.DividerDecorator;
import greencode.ir.pillcci.utils.PersianCalculater;
import greencode.ir.pillcci.utils.Utility;
import saman.zamani.persiandate.PersianDate;

/**
 * Created by alireza on 5/27/18.
 */

public class FragmentToday extends Fragment implements OnDateSetListener,TodayUsageAdapter.UsageInterface ,com.alirezaafkar.sundatepicker.interfaces.DateSetListener {


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
    @BindView(R.id.imgSearch)
    ImageView imgSearch;
    @BindView(R.id.imgClose)
    ImageView imgClose;
    @BindView(R.id.edtSearch)
    EditText edtSearch;
    CountDownTimer countDownTimer;

    boolean pageExist=false;
    TimePickerDialog mDialogHourMinute;
    boolean fromDatePicker  =false;
    PersianDate selectedDay;
    TimePickerDialog timePickerDialog;
    DatePicker datePicker;
    PillUsage jumperItem;

    private static final String FORMAT = "%02d:%02d";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_today, container, false);
        ButterKnife.bind(this, view);
        pageExist=true;
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visibleSearch();
            }
        });
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goneSearch();
            }
        });
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utility.hideKeyboard();
                          //invisibleSearch();
                        }
                    });

                return true;
                }else {
                    return false;
                }
            }
        });
        txtToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              showTimeDialo();
            }
        });

        return view;
    }

    private void showTimeDialo() {
        fromDatePicker  =true;
        long justNow = System.currentTimeMillis();
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(justNow);
        new DatePicker.Builder()
                .id(1)

                .minYear(selectedDay.getShYear())
                .showYearFirst(false)
                .closeYearAutomatically(true)
                .theme(R.style.DialogTheme)
                .date(calendar)
                .build( FragmentToday.this)
                .show(getActivity().getSupportFragmentManager(), "انتخاب زمان مصرف دارو");
    }

    public void filter(String s){
        if(adapter!=null) {
            ArrayList<PillUsage> usages = adapter.getFullList();
            ArrayList<PillUsage> filteredList = new ArrayList<>();
            if (usages.size() > 0) {
                for (PillUsage usage : usages) {
                    if (usage.getPillName().startsWith(s)) {
                        filteredList.add(usage);
                    }
                }
                adapter.filter(filteredList);
            }
        }
    }
    public void visibleSearch() {
        txtToday.setVisibility(View.GONE);
        imgSearch.setVisibility(View.GONE);
        edtSearch.setVisibility(View.VISIBLE);
        imgClose.setVisibility(View.VISIBLE);
        Utility.setKeyboardFocus(edtSearch);
    }
    public void invisibleSearch(){
        txtToday.setVisibility(View.VISIBLE);
        imgSearch.setVisibility(View.VISIBLE);
        edtSearch.setVisibility(View.GONE);
        imgClose.setVisibility(View.GONE);
        Utility.hideKeyboard();
    }
    public void goneSearch(){
        txtToday.setVisibility(View.VISIBLE);
        imgSearch.setVisibility(View.VISIBLE);
        edtSearch.setVisibility(View.GONE);
        imgClose.setVisibility(View.GONE);
        edtSearch.setText("");
        Utility.hideKeyboard();
        adapter.filter(adapter.getFullList());

    }
    @Override
    public void onStart() {

        super.onStart();
}
   public void makePage(){
       Date date = new Date(System.currentTimeMillis());
       selectedDay = new PersianDate(System.currentTimeMillis());
       txtToday.setText("امروز "+selectedDay.dayName()+" "+selectedDay.getShDay()+" " +selectedDay.monthName());
        makeList(selectedDay);
    }

    private void makeList(PersianDate date) {
        long startDay = getStartOfDay(date);
        long endDay = getEndOfDay(date);
        AppDatabase database = AppDatabase.getInMemoryDatabase(getContext());
        ArrayList<PillUsage> usages = new ArrayList<>(database.pillUsageDao().listPillToday(startDay, endDay));

        if (usages != null) {

            countOfDay.setText(usages.size() + "");
            adapter = new TodayUsageAdapter(getContext(), usages,this);
            list.addItemDecoration(new DividerDecorator(getContext()));

            list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            list.setAdapter(adapter);
            if (usages.size() > 0) {

                countOfUsed.setText(adapter.getUsedUsageCount() + "");
            } else {
                countOfUsed.setText("0");

            }
            if(adapter.getItemCount()>0) {
                int position = 0;
                for(PillUsage usage:usages){
                    position++;
                    if(usage.getSetedTime()>=System.currentTimeMillis()){
                        break;
                    }
                }
                if(position>1)
                list.scrollToPosition(position-2);
            }
        } else {
            countOfDay.setText("0");
            countOfDay.setText("0");
            countOfUsed.setText("0");
        }

        PillUsage pillUsage = database.pillUsageDao().getNearestUsage(System.currentTimeMillis());
        if (pillUsage != null) {
            long dif = pillUsage.getUsageTime() - System.currentTimeMillis();
            dif=dif+60000;
            startCounter(dif);
            difrenceLayout.setVisibility(View.VISIBLE);

        } else {
            difrenceLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        makePage();
        super.onResume();
    }

    public void startCounter(long dif) {
       if(countDownTimer==null) {
           countDownTimer = new CountDownTimer(dif, 1000) { // adjust the milli seconds here

               public void onTick(long millisUntilFinished) {

                   txtNextRemind.setText("" + String.format(FORMAT,
                           TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                           TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                   TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                           TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                   TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
               }

               public void onFinish() {
                   AppDatabase database1 = AppDatabase.getInMemoryDatabase(getContext());
                   PillUsage pillUsage = database1.pillUsageDao().getNearestUsage(System.currentTimeMillis());
                   if (pillUsage != null) {
                       long dif = pillUsage.getUsageTime() - System.currentTimeMillis();
                       dif=dif+60000;
                       startCounter(dif);
                       difrenceLayout.setVisibility(View.VISIBLE);

                   } else {
                       difrenceLayout.setVisibility(View.GONE);
                   }
               }
           }.start();
       }else {
           countDownTimer.cancel();
           countDownTimer = new CountDownTimer(dif, 1000) { // adjust the milli seconds here

               public void onTick(long millisUntilFinished) {

                   txtNextRemind.setText("" + String.format(FORMAT,
                           TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                           TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                   TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                           TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                   TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
               }

               public void onFinish() {
                   AppDatabase database1 = AppDatabase.getInMemoryDatabase(getContext());
                   PillUsage pillUsage = database1.pillUsageDao().getNearestUsage(System.currentTimeMillis());
                   if (pillUsage != null) {
                       long dif = pillUsage.getUsageTime() - System.currentTimeMillis();
                       dif=dif+60000;
                       startCounter(dif);
                       difrenceLayout.setVisibility(View.VISIBLE);

                   } else {
                       difrenceLayout.setVisibility(View.GONE);
                   }
               }
           }.start();
       }

    }
    @OnClick(R.id.fabBtn)
    public void onClick() {

        Intent intent = new Intent(getContext(), AddMedicianActivity.class);
        startActivity(intent);
    }

    private long getStartOfDay(PersianDate date) {
           date.setHour(0);
           date.setMinute(0);
           date.setSecond(0);
           return date.getTime();
    }

    private long getEndOfDay(PersianDate date) {
        date.setHour(23);
        date.setMinute(59);
        date.setSecond(59);
        return date.getTime();

    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
       if(jumperItem==null) {
           PersianDate calendar = new PersianDate(millseconds);
           Toast.makeText(getContext(), calendar.getShYear() + "", Toast.LENGTH_LONG).show();
       }else {
           PersianDate selDate = new PersianDate(millseconds);

           PersianDate nowDate = new PersianDate(System.currentTimeMillis());
           if(nowDate.getHour()>selDate.getHour()){
               Toast.makeText(getContext(), "زمان انتخابی نباید قبل از زمان فعلی باشد.", Toast.LENGTH_LONG).show();
           }else if(nowDate.getHour()==selDate.getHour() && nowDate.getMinute()>selDate.getMinute()){
               Toast.makeText(getContext(), "زمان انتخابی نباید قبل از زمان فعلی باشد.", Toast.LENGTH_LONG).show();
           }else {
               nowDate.setHour(selDate.getHour());
               nowDate.setMinute(selDate.getMinute());
               long time = nowDate.getTime();
               time/=10000;
               time*=10000;
               jumperItem.setUsageTime(time);
               AppDatabase database = AppDatabase.getInMemoryDatabase(getContext());
               jumperItem.setTime(PersianCalculater.getHourseAndMin(selDate.getTime()));
               database.pillUsageDao().update(jumperItem);
               Utility.reCalculateManager(getContext());


               updateList();
           }

       }
    }

    @Override
    public void usageAct(final PillUsage item) {
        final DialogUsagePicker dialog = new DialogUsagePicker(getContext(),getActivity(),getFragmentManager(),item.getUsageTime(),item);
        dialog.setListener(new TimeUsageInterface() {
            @Override
            public void onSuccess(long timeUsage) {
                AppDatabase database = AppDatabase.getInMemoryDatabase(getContext());
                item.setUsedTime(timeUsage);
                item.setState(1);
                database.pillUsageDao().update(item);
                dialog.dismiss();
                Utility.reCalculateManager(getContext());
                updateList();

            }

            @Override
            public void onRejected() {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void cancelUseAct(final PillUsage item) {
        AppDatabase database = AppDatabase.getInMemoryDatabase(getContext());
        PillObject pill = database.pillObjectDao().specialPil(item.getPillName(),item.getCatNme());
        if(pill.getUseType()==1){
            final YesNoDialog dialog = new YesNoDialog(getContext());
            dialog.setListener(new YesNoListener() {
                @Override
                public void onReject() {
                    dialog.dismiss();
                }

                @Override
                public void onSuccess() {
                    DatabaseManager.cancelUsage(getContext(), item);
                    updateList();
                    Utility.reCalculateManager(getContext());
                    dialog.dismiss();
                }
            });
            dialog.show();
        }else {
            final CancelDialog dialog = new CancelDialog(getContext(),pill);
            dialog.setListener(new CancelListener() {
                @Override
                public void onReject() {
                    dialog.dismiss();
                }

                @Override
                public void onSuccess(int type) {
                    if (type == 1) {
                        DatabaseManager.cancelUsage(getContext(), item);
                        updateList();
                        Utility.reCalculateManager(getContext());

                    } else {
                        DatabaseManager.cancelUsage(getContext(),item);
                        DatabaseManager.addToEnd(getContext(), item);
                        updateList();
                        Utility.reCalculateManager(getContext());

                    }
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    @Override
    public void cancelAct(final PillUsage item) {

           final FinishDialog dialog = new FinishDialog(getContext(), "از لغو تغییرات اطمینان دارید؟", "در صورت لغو تغییرات، دارو به حالت قبل از مصرف و یا عدم مصرف بر می گردد و در زمان مشخص به شما هشدار می دهد.");
           dialog.setListener(new FinishListener() {
               @Override
               public void onReject() {

                   dialog.dismiss();
               }

               @Override
               public void onSuccess() {
                   DatabaseManager.resetFactoryUsage(item, getContext());
                   updateList();
                   dialog.dismiss();
               }
           });
           dialog.show();

    }

    @Override
    public void jumpAct(PillUsage item) {
        fromDatePicker = false;
        jumperItem = item;
        timePickerDialog = Utility.getTimeDialog(this,getResources().getColor(R.color.colorPrimary),item.getUsageTime());
        timePickerDialog.show(getFragmentManager(),"یادآوری کن در ساعت");
   }

    @Override
    public void EditAct(final PillUsage item) {
        if (item.getState() !=0 ) {
            AppDatabase database = AppDatabase.getInMemoryDatabase(getContext());
            PillObject pillObject = database.pillObjectDao().specialPil(item.getPillName(),item.getCatNme());
            final CancelEditDialog dialog = new CancelEditDialog(getContext(),pillObject);
            dialog.setListener(new CancelListener() {
                @Override
                public void onReject() {
                    dialog.dismiss();
                }

                @Override
                public void onSuccess(int type) {
                    if(type==1){
                        // delete
                       /* if(item.getState()!=1) {*/
                            DatabaseManager.cancelUsage(getContext(), item);
                            updateList();
                            Utility.reCalculateManager(getContext());
                        /*}*/

                    }else if(type==2){
                        /*if(item.getState()!=2 || item.getState()!=1) {*/
                        DatabaseManager.cancelUsage(getContext(), item);

                        DatabaseManager.addToEnd(getContext(), item);
                            updateList();
                            Utility.reCalculateManager(getContext());
                       /* }*/

                    }else if (type==3){
                        // used
                       /* if(item.getState()!=1) {*/
                            final DialogUsagePicker dialog = new DialogUsagePicker(getContext(),getActivity(),getFragmentManager(),item.getUsageTime(),item);
                            dialog.setListener(new TimeUsageInterface() {
                                @Override
                                public void onSuccess(long timeUsage) {
                                    AppDatabase database = AppDatabase.getInMemoryDatabase(getContext());
                                    item.setUsedTime(timeUsage);
                                    item.setState(1);
                                    database.pillUsageDao().update(item);
                                    dialog.dismiss();
                                    Utility.reCalculateManager(getContext());
                                    updateList();

                                }

                                @Override
                                public void onRejected() {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                            /*AppDatabase database = AppDatabase.getInMemoryDatabase(getContext());
                            item.setState(1);
                            item.setUsedTime(item.getUsageTime());
                            database.pillUsageDao().update(item);
                            updateList();*/
                      /*  }*/
                    }
                    dialog.dismiss();
                }
            });
            dialog.show();
        }else {
            Toast.makeText(getContext(), "پیش از مصرف و یا لغو ویرایش امکانپذیر نیست.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void CanNotCancel() {
        Toast.makeText(getContext(), "شما پیش از این، از دارو را لغو کرده اید.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void CanNotSkip() {
       Toast.makeText(getContext(),"امکان پرش برای این دارو وجود ندارد.",Toast.LENGTH_LONG).show();

    }

    @Override
    public void CanNotAccept() {
        Toast.makeText(getContext(), "شما پیش از این ، دارو را مصرف کرده اید.", Toast.LENGTH_LONG).show();
    }

    public void updateList(){
       long startDay = getStartOfDay(selectedDay);
       long endDay = getEndOfDay(selectedDay);
       AppDatabase database = AppDatabase.getInMemoryDatabase(getContext());
       ArrayList<PillUsage> usages = new ArrayList<>(database.pillUsageDao().listPillToday(startDay, endDay));
       adapter.updateList(startDay,endDay);
       PillUsage pillUsage = database.pillUsageDao().getNearestUsage(System.currentTimeMillis());
       if (pillUsage != null) {
           long dif = pillUsage.getUsageTime() - System.currentTimeMillis();
           dif=dif+60000;
           startCounter(dif);
           difrenceLayout.setVisibility(View.VISIBLE);

       } else {
           difrenceLayout.setVisibility(View.GONE);
       }
   }


    @Override
    public void onDateSet(int id, @Nullable Calendar calendar, int day, int month, int year) {

        selectedDay.setShYear(year);
        selectedDay.setShMonth(month);
        selectedDay.setShDay(day);
        PersianDate today = new PersianDate(System.currentTimeMillis());
        String title =selectedDay.dayName()+" "+selectedDay.getShDay()+" "+selectedDay.monthName();
        if(today.getShMonth()==selectedDay.getShMonth() && today.getShDay()==selectedDay.getShDay()){
           title="امروز "+title;
        }
        txtToday.setText(title);

        makeList(selectedDay);

    }
}
