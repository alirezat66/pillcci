package greencode.ir.pillcci.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.activities.FilterActivity;
import greencode.ir.pillcci.adapter.HistoryAdapter;
import greencode.ir.pillcci.controler.AppDatabase;
import greencode.ir.pillcci.database.PillObject;
import greencode.ir.pillcci.database.PillUsage;
import greencode.ir.pillcci.dialog.CancelEditDialog;
import greencode.ir.pillcci.dialog.CancelListener;
import greencode.ir.pillcci.dialog.DialogUsagePicker;
import greencode.ir.pillcci.dialog.TimeUsageInterface;
import greencode.ir.pillcci.utils.DatabaseManager;
import greencode.ir.pillcci.utils.Utility;
import saman.zamani.persiandate.PersianDate;

import static android.app.Activity.RESULT_OK;

/**
 * Created by alireza on 5/27/18.
 */

public class FragmentHistory extends Fragment implements HistoryAdapter.UsageInterface {

    boolean isFilter = false;
    @BindView(R.id.list)
    RecyclerView list;
    List<PillUsage> pillUsages;
    @BindView(R.id.imgClose)
    AppCompatImageView imgClose;
    @BindView(R.id.edtSearch)
    EditText edtSearch;
    @BindView(R.id.txtToday)
    TextView txtToday;
    @BindView(R.id.imgSearch)
    ImageView imgSearch;
    HistoryAdapter adapter;
    FirebaseAnalytics firebaseAnalytics;
    @BindView(R.id.txt_empty_title)
    TextView txtEmptyTitle;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.txt_empty_desc)
    TextView txtEmptyDesc;
    @BindView(R.id.root_empty)
    RelativeLayout rootEmpty;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void filter(String s) {
        if (adapter != null) {
            List<PillUsage> usages = adapter.getFullPill();
            List<PillUsage> filteredList = new ArrayList<>();
            if (usages != null) {
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 5817 && resultCode == RESULT_OK) {
                int sDay = data.getIntExtra("sDay", 0);
                int sMonth = data.getIntExtra("sMonth", 0);
                int sYear = data.getIntExtra("sYear", 0);
                int fDay = data.getIntExtra("fDay", 0);
                int fMonth = data.getIntExtra("fMonth", 0);
                int fYear = data.getIntExtra("fYear", 0);
                String pillName = data.getStringExtra("pilName");
                String catName = data.getStringExtra("catName");


                PersianDate startDate = new PersianDate();


                PersianDate endDate = new PersianDate();

                boolean hasStart = false;
                boolean hasEnd = false;
                boolean hasName = false;
                boolean hasCat = false;
                if (sYear != 0) {
                    startDate.setShYear(sYear);
                    startDate.setShMonth(sMonth);
                    startDate.setShDay(sDay);
                    hasStart = true;
                }
                if (fYear != 0) {
                    endDate.setShYear(fYear);
                    endDate.setShMonth(fMonth);
                    endDate.setShDay(fDay);
                    hasEnd = true;
                }
                if (!pillName.equals("")) {
                    hasName = true;
                }
                if (!catName.equals("")) {
                    hasCat = true;
                }
                if (catName.equals("خودم")) {
                    catName = "";
                }
                ArrayList<PillUsage> temp = new ArrayList<>();

                for (PillUsage usage : adapter.getFullPill()) {
                    boolean isIn = true;
                    if (hasName) {
                        if (!usage.getPillName().equals(pillName)) {
                            isIn = false;
                        }
                    }
                    if (hasCat) {
                        if (!usage.getCatNme().equals(catName)) {
                            isIn = false;
                        }
                    }
                    if (hasStart) {
                        if (usage.getSetedTime() < startDate.getTime()) {
                            isIn = false;
                        }
                    }
                    if (hasEnd) {
                        if (usage.getSetedTime() > endDate.getTime()) {
                            isIn = false;
                        }
                    }
                    if (isIn) {
                        temp.add(usage);
                    }
                }


                pillUsages = temp;
                adapter.update(temp);


            }
        } catch (Exception ex) {
           Toast toast = Toast.makeText(getContext(), ex.toString(),
                    Toast.LENGTH_SHORT);
           Utility.centrizeAndShow(toast);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this, view);
        firebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFilter = true;
                Intent intent = new Intent(getContext(), FilterActivity.class);
                startActivityForResult(intent, 5817);

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
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isFilter) {
            makeList();
        }
        isFilter = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        edtSearch.setText("");
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    public void makeList() {
        AppDatabase database = AppDatabase.getInMemoryDatabase(getContext());
        long now = System.currentTimeMillis();
        PersianDate nowDate = new PersianDate(now);
        for (int i = 0; i < 30; i++) {
            nowDate.addDay(1);
        }
        PersianDate persianDate = new PersianDate(System.currentTimeMillis());
        pillUsages = database.pillUsageDao().getHistory(nowDate.getTime());
        list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new HistoryAdapter(getContext(), pillUsages, this);
        list.setAdapter(adapter);

        int position = calculateFirstPosition();
        if (pillUsages.size() > 0) {
            list.scrollToPosition(position);
            rootEmpty.setVisibility(View.GONE);
        }else {
            empty();
        }
    }
    public void empty() {
        rootEmpty.setVisibility(View.VISIBLE);
        txtEmptyTitle.setText("گزارشی وجود نداره!");
        String htmlStringWithMathSymbols = "گزارش مصرف داروها اینجا ذخیره میشه!";

        txtEmptyDesc.setText(Html.fromHtml(htmlStringWithMathSymbols));
    }

    private int calculateFirstPosition() {
        if (pillUsages.size() > 0) {
            long now = System.currentTimeMillis();
            PersianDate nowDate = new PersianDate(now);
            int position = 0;
            for (PillUsage pillUsage : pillUsages) {
                PersianDate thisPillDate = new PersianDate(pillUsage.getUsageTime());
                if (thisPillDate.getShYear() == nowDate.getShYear() &&
                        thisPillDate.getShMonth() == nowDate.getShMonth() &&
                        thisPillDate.getShDay() == nowDate.getShDay()) {
                    break;
                }
                position++;
            }
            return position;

        } else {
            return -1;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void EditAct(final PillUsage item) {
        if (item.getState() != 0) {
            AppDatabase database = AppDatabase.getInMemoryDatabase(getContext());
            PillObject pillObject = database.pillObjectDao().specialPil(item.getPillName(), item.getCatNme());
            if (pillObject != null) {
                Bundle params = new Bundle();
                params.putString("phoneNumber", Utility.getPhoneNumber(getContext()));
                firebaseAnalytics.logEvent("history_edit_with_button", params);
                final CancelEditDialog dialog = new CancelEditDialog(getContext(), pillObject);
                dialog.setListener(new CancelListener() {
                    @Override
                    public void onReject() {
                        dialog.dismiss();
                    }

                    @Override
                    public void onSuccess(int type) {
                        if (type == 1) {
                            // delete
                            /* if(item.getState()!=1) {*/
                            DatabaseManager.cancelUsage(getContext(), item);
                            updateList();
                            Utility.reCalculateManager(getContext());
                            /*}*/

                        } else if (type == 2) {
                            /*if(item.getState()!=2 || item.getState()!=1) {*/
                            DatabaseManager.cancelUsage(getContext(), item);
                            DatabaseManager.addToEnd(getContext(), item);
                            updateList();
                            Utility.reCalculateManager(getContext());
                            /* }*/

                        } else if (type == 3) {
                            // used
                            /* if(item.getState()!=1) {*/
                            final DialogUsagePicker dialog = new DialogUsagePicker(getContext(), getActivity(), getFragmentManager(), item.getUsageTime(), item);
                            dialog.setListener(new TimeUsageInterface() {
                                @Override
                                public void onSuccess(long timeUsage) {
                                    AppDatabase database = AppDatabase.getInMemoryDatabase(getContext());
                                    item.setUsedTime(timeUsage);
                                    item.setState(1);
                                    item.setIsSync(0);
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
            }
        } else {
            Toast toast = Toast.makeText(getContext(), "پیش از مصرف یا انصراف مصرف دارو، ویرایش وضعیت مصرف ممکن نیست.", Toast.LENGTH_LONG);
            Utility.centrizeAndShow(toast);
        }
    }

    private void updateList() {
        AppDatabase database = AppDatabase.getInMemoryDatabase(getContext());
        PersianDate persianDate = new PersianDate(System.currentTimeMillis());
        pillUsages = database.pillUsageDao().getHistory(persianDate.getTime());
        list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new HistoryAdapter(getContext(), pillUsages, this);
        list.setAdapter(adapter);
    }
}
