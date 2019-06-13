package greencode.ir.pillcci.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
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

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.activities.ActivityPilDetail;
import greencode.ir.pillcci.activities.AddMedicianActivity;
import greencode.ir.pillcci.adapter.PillAdapter;
import greencode.ir.pillcci.controler.AppDatabase;
import greencode.ir.pillcci.database.PillObject;
import greencode.ir.pillcci.database.PillUsage;
import greencode.ir.pillcci.dialog.FinishDialog;
import greencode.ir.pillcci.dialog.FinishListener;
import greencode.ir.pillcci.objects.PillShelf;
import greencode.ir.pillcci.retrofit.SyncController;
import greencode.ir.pillcci.utils.Constants;
import greencode.ir.pillcci.utils.DatabaseManager;
import greencode.ir.pillcci.utils.PreferencesData;
import greencode.ir.pillcci.utils.TransitionHelper;
import greencode.ir.pillcci.utils.Utility;
import saman.zamani.persiandate.PersianDate;

import static greencode.ir.pillcci.utils.CalcTimesAndSaveUsage.makePillUsageInPerAmountMoodReStart;
import static greencode.ir.pillcci.utils.CalcTimesAndSaveUsage.makePillUsageInPerCountMoodReStart;
import static greencode.ir.pillcci.utils.CalcTimesAndSaveUsage.makePillUsageInPerFreeMoodReStart;

/**
 * Created by alireza on 5/27/18.
 */

public class FragmentPills extends Fragment implements PillAdapter.PillListInterface {


    @BindView(R.id.list2)
    RecyclerView list;
    @BindView(R.id.imgClose)
    AppCompatImageView imgClose;
    @BindView(R.id.edtSearch)
    EditText edtSearch;
    @BindView(R.id.txtToday)
    TextView txtToday;
    @BindView(R.id.imgSearch)
    ImageView imgSearch;
    PillAdapter adapter;
    ArrayList<PillShelf> pillShelves;
    public static PillObject reActiveObject;
    KProgressHUD kProgressHUD;
    @BindView(R.id.fabBtn)
    FloatingActionButton fabBtn;

    FirebaseAnalytics firebaseAnalytics;


    static final String EXTRA_TYPE = "type";
    static final int TYPE_XML = 1;
    @BindView(R.id.header)
    RelativeLayout header;
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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pills, container, false);
        ButterKnife.bind(this, view);
        fabBtn.bringToFront();
        firebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PreferencesData.getBoolean(Constants.PREF_Guess, getContext())) {
                    Answers.getInstance().logCustom(new CustomEvent("Search")
                            .putCustomAttribute("page", "pill")
                            .putCustomAttribute("phone", AppDatabase.getInMemoryDatabase(getContext()).profileDao().getMyProfile().getPhone())
                    );
                }
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
        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getContext(), AddMedicianActivity.class);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    intent.putExtra(EXTRA_TYPE, TYPE_XML);
                    transitionTo(intent);
                } else {
                    startActivity(intent);
                }
            }
        });


        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("unchecked")
    void transitionTo(Intent i) {
        final Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants(getActivity(), true);
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), pairs);
        startActivity(i, transitionActivityOptions.toBundle());
    }

    public void filter(String s) {
        if (adapter != null) {
            ArrayList<PillShelf> shelves = adapter.getFullPill();
            ArrayList<PillShelf> filteredList = new ArrayList<>();
            if (shelves.size() > 0) {
                for (PillShelf shelf : shelves) {
                    if (shelf.getName().startsWith(s)) {
                        filteredList.add(shelf);
                    }
                }
                adapter.filter(filteredList);
            }
        }

    }

    public void visibleSearch() {
        Bundle params = new Bundle();
        params.putString("phoneNumber", Utility.getPhoneNumber(getContext()));
        firebaseAnalytics.logEvent("pil_search", params);
        txtToday.setVisibility(View.GONE);
        imgSearch.setVisibility(View.GONE);
        edtSearch.setVisibility(View.VISIBLE);
        imgClose.setVisibility(View.VISIBLE);
        Utility.setKeyboardFocus(edtSearch);
    }

    public void goneSearch() {
        txtToday.setVisibility(View.VISIBLE);
        imgSearch.setVisibility(View.VISIBLE);
        edtSearch.setVisibility(View.GONE);
        imgClose.setVisibility(View.GONE);
        edtSearch.setText("");
        Utility.hideKeyboard();
        adapter.filter(adapter.getFullPill());
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        makePillList();
        super.onResume();
    }

    private void makePillList() {
        pillShelves = new ArrayList<>();
        makePillShelve();
        adapter = new PillAdapter(getActivity(), pillShelves, this);
        list.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        list.setAdapter(adapter);
    }

    private void makePillShelve() {
        AppDatabase database = AppDatabase.getInMemoryDatabase(getContext());
        List<PillObject> pillObjects = database.pillObjectDao().getAllPillBySort();

        if(pillObjects.size()>0){
            rootEmpty.setVisibility(View.GONE);
        }else {
           empty();
        }
        for (PillObject pilName : pillObjects) {
            PillUsage pillNext = database.pillUsageDao().getNextPill(System.currentTimeMillis(), pilName.getMidname(), pilName.getCatName());
            PillUsage pillLast = database.pillUsageDao().getLastPill(System.currentTimeMillis(), pilName.getMidname(), pilName.getCatName());
            rootEmpty.setVisibility(View.GONE);
            String lastUsed = "";
            String nextUsed = "";
            String catname = pilName.getCatName();
            String img = pilName.getB64();
            if (pillLast == null) {
                lastUsed = "تا بحال مصرف نشده است.";
            } else {

                PersianDate pdate = new PersianDate(pillLast.getUsedTime());
                lastUsed = pdate.getShYear() + "/" + pdate.getShMonth() + "/" + pdate.getShDay() + " " + (pdate.getHour() > 9 ? pdate.getHour() : ("0" + pdate.getHour())) + ":" + (pdate.getMinute() > 9 ? pdate.getMinute() : ("0" + pdate.getMinute()));
            }

            if (pillNext == null) {
                nextUsed = "دارویی وجود ندارد.";
            } else {

                PersianDate pdate = new PersianDate(pillNext.getUsageTime());

                nextUsed = pdate.getShYear() + "/" + pdate.getShMonth() + "/" + pdate.getShDay() + "-" + (pdate.getHour() > 9 ? pdate.getHour() : ("0" + pdate.getHour())) + ":" + (pdate.getMinute() > 9 ? pdate.getMinute() : ("0" + pdate.getMinute()));
            }

            pillShelves.add(new PillShelf(pilName.getMidname(), lastUsed, nextUsed, catname, img, pilName.getCatColor(), pilName.getUnitUse(), pilName.getDrName(), pilName.getCountOfUsagePerDay() + "", pilName.getState(), pilName.getTypeOfUsage()));

        }
    }

    public void empty() {
        rootEmpty.setVisibility(View.VISIBLE);
        txtEmptyTitle.setText("سبد دارو خالیه!");
        String htmlStringWithMathSymbols = "داروهایی که ثبت می‌کنی همراه با اطلاعاتشون اینجا ذخیره میشن!";

        txtEmptyDesc.setText(Html.fromHtml(htmlStringWithMathSymbols));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onDelete(final PillShelf item) {
        Bundle params = new Bundle();
        params.putString("phoneNumber", Utility.getPhoneNumber(getContext()));
        firebaseAnalytics.logEvent("pil_delete", params);
        final FinishDialog dialog = new FinishDialog(getContext(), "از حذف این دارو مطمئنی؟", "در صورت حذف دارو تمامی گزارش‌های مصرف این دارو نیز حذف خواهند شد.");
        dialog.setListener(new FinishListener() {
            @Override
            public void onReject() {
                dialog.dismiss();
            }

            @Override
            public void onSuccess() {
                DatabaseManager.deletePill(item, getContext());
                pillShelves = new ArrayList<>();
                makePillShelve();
                adapter.update(pillShelves);
                if(pillShelves.size()>0){
                    rootEmpty.setVisibility(View.GONE);
                }else {
                    empty();
                }
                Utility.reCalculateManager(getContext());
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onDetail(PillShelf item, ImageView imgLogo, TextView txtMedName) {
        Intent intent = new Intent(getContext(), ActivityPilDetail.class);
        intent.putExtra("midName", item.getName());
        intent.putExtra("catName", item.getCatName());
        Bundle params = new Bundle();
        params.putString("phoneNumber", Utility.getPhoneNumber(getContext()));
        firebaseAnalytics.logEvent("pil_click_on_pil_for_detail", params);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Pair<View, String> pair1 = Pair.create((View) imgLogo, imgLogo.getTransitionName());
            Pair<View, String> pair2 = Pair.create((View) txtMedName, txtMedName.getTransitionName());
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(getActivity(), pair1, pair2 /*holder.imgLogo,"simple_activity_transition"*/);
            intent.putExtra("midName", item.getName());
            startActivity(intent, options.toBundle());
        } else {
            intent.putExtra("midName", item.getName());
            startActivity(intent);
        }
    }

    @Override
    public void onStop(final PillShelf item) {
        Bundle params = new Bundle();
        params.putString("phoneNumber", Utility.getPhoneNumber(getContext()));
        firebaseAnalytics.logEvent("pil_stop", params);
        final FinishDialog dialog;
        if (item.getUsageType() == 4) {
            dialog = new FinishDialog(getContext(), "از توقف مصرف دارو اطمینان دارید؟", "");

        } else {
            dialog = new FinishDialog(getContext(), "از توقف مصرف دارو اطمینان دارید؟", "");

        }
        dialog.setListener(new FinishListener() {
            @Override
            public void onReject() {
                dialog.dismiss();
            }

            @Override
            public void onSuccess() {
                DatabaseManager.stopPill(item, getContext());
                pillShelves = new ArrayList<>();
                makePillShelve();
                adapter.update(pillShelves);
                if(pillShelves.size()>0){
                    rootEmpty.setVisibility(View.GONE);
                }else {
                    empty();
                }
                Utility.reCalculateManager(getContext());
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void showWaiting() {
        kProgressHUD = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("")
                .setDetailsLabel("پیلچی در حال تنظیم یادآورهاست...")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.8f)
                .setBackgroundColor(getResources().getColor(R.color.colorPrimary))
                .show();
    }

    public void disMissWaiting() {
        if (kProgressHUD != null) {
            kProgressHUD.dismiss();
        }
    }

    @Override
    public void start(PillShelf data) {
        if (data.getUsageType() == 4) {
             Toast toast =    Toast.makeText(getContext(), "در چرخه ضد بارداری امکان شروع مجدد وجود ندارد.", Toast.LENGTH_LONG);
             Utility.centrizeAndShow(toast);
        } else {
            AppDatabase database = AppDatabase.getInMemoryDatabase(getContext());
            PillObject object = database.pillObjectDao().specialPil(data.getName(), data.getCatName());
            setReActivePill(object);
            new ReActive().execute();
        }
    }

    public static void setReActivePill(PillObject object) {
        reActiveObject = object;
    }

    public static PillObject getReActivePill() {
        return reActiveObject;
    }

    class ReActive extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            showWaiting();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            PillObject pillObject = getReActivePill();
            ArrayList<PillUsage> usageList = new ArrayList<>();
            if (pillObject.getUseType() == 1) {
                ///masrafe modam
                usageList = makePillUsageInPerFreeMoodReStart(pillObject, getContext());

            } else if (pillObject.getUseType() == 2) {
                // masraf bar asase rooz;
                usageList = makePillUsageInPerCountMoodReStart(pillObject, getContext());

            } else {
                usageList = makePillUsageInPerAmountMoodReStart(pillObject, getContext());
                //masraf bar asase mizane daroo;
            }
            AppDatabase database = AppDatabase.getInMemoryDatabase(getContext());
            for(PillUsage usage : usageList){
                if(database.pillUsageDao().isSame(usage.getPillName(),usage.getCatNme(),usage.getSetedTime())>0){
                    database.pillUsageDao().deleteSamePill(usage.getPillName(),usage.getCatNme(),usage.getSetedTime());
                }
            }
            database.pillUsageDao().insertPillList(usageList);
            pillObject.setState(1);
            pillObject.setSync(0);
            database.pillObjectDao().update(pillObject);
            Utility.reCalculateManager(getContext());
            SyncController sync = new SyncController();
            sync.checkDataBaseForUpdate();


            return "";
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            disMissWaiting();
            pillShelves = new ArrayList<>();
            makePillShelve();
            adapter.update(pillShelves);

            if(pillShelves.size()>0){
                rootEmpty.setVisibility(View.GONE);
            }else {
                empty();
            }
        }

    }
}
