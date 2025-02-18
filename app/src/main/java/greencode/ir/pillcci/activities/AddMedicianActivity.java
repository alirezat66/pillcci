package greencode.ir.pillcci.activities;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.AppCompatImageView;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.controler.AppDatabase;
import greencode.ir.pillcci.database.Category;
import greencode.ir.pillcci.database.PillObject;
import greencode.ir.pillcci.database.PillUsage;
import greencode.ir.pillcci.dialog.FinishDialog;
import greencode.ir.pillcci.dialog.FinishListener;
import greencode.ir.pillcci.fragments.FragmentComplitationMedic;
import greencode.ir.pillcci.fragments.FragmentGeneralMedic;
import greencode.ir.pillcci.fragments.FragmentUsageMedic;
import greencode.ir.pillcci.objects.EndUseFields;
import greencode.ir.pillcci.objects.GeneralFields;
import greencode.ir.pillcci.objects.UsageFields;
import greencode.ir.pillcci.retrofit.SyncController;
import greencode.ir.pillcci.utils.BaseActivity;
import greencode.ir.pillcci.utils.Constants;
import greencode.ir.pillcci.utils.PreferencesData;
import greencode.ir.pillcci.utils.Utility;

import static greencode.ir.pillcci.utils.CalcTimesAndSaveUsage.makePillObject;
import static greencode.ir.pillcci.utils.CalcTimesAndSaveUsage.makePillUsageInPerAmountMood;
import static greencode.ir.pillcci.utils.CalcTimesAndSaveUsage.makePillUsageInPerCountMood;
import static greencode.ir.pillcci.utils.CalcTimesAndSaveUsage.makePillUsageInPerFreeMood;

/**
 * Created by alireza on 5/14/18.
 */

public class AddMedicianActivity extends BaseActivity implements FragmentGeneralMedic.onActionStepOne, FragmentUsageMedic.onActionStepTwo, FragmentComplitationMedic.onActionStepThree {


    @BindView(R.id.img_back)
    AppCompatImageView imgBack;
    @BindView(R.id.title)
    TextView txtTitle;

    @BindView(R.id.checkedOne)
    AppCompatImageView checkedOne;
    @BindView(R.id.lyNumberOne)
    FrameLayout lyNumberOne;
    @BindView(R.id.txtStepOne)
    TextView txtStepOne;

    @BindView(R.id.checkedTwo)
    AppCompatImageView checkedTwo;
    @BindView(R.id.lyNumberTwo)
    FrameLayout lyNumberTwo;
    @BindView(R.id.txtStepTwo)
    TextView txtStepTwo;
    @BindView(R.id.checkedThree)
    AppCompatImageView checkedThree;
    @BindView(R.id.lyNumberThree)
    FrameLayout lyNumberThree;
    @BindView(R.id.txtStepThree)
    TextView txtStepThree;
    @BindView(R.id.container)
    LinearLayout container;
    @BindView(R.id.lyNumberTwoText)
    TextView lyNumberTwoText;
    @BindView(R.id.lyNumberThreeText)
    TextView lyNumberThreeText;
    @BindView(R.id.lineOne)
    RelativeLayout lineOne;
    @BindView(R.id.linetwo)
    RelativeLayout linetwo;


    private int mSelectedColor;
    public static GeneralFields generalFields;
    public static UsageFields usageFields;
    public static EndUseFields endUseFields;
    ArrayList<Fragment> fragments = new ArrayList<>();
    KProgressHUD kProgressHUD;


    private static final int DELAY = 100;
    private Scene scene0;
    private Scene scene1;
    private Scene scene2;
    private Scene scene3;
    private Scene scene4;
    private final List<View> viewsToAnimate = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medician);
        ButterKnife.bind(this);
        txtTitle.setText("افزودن دارو");
        addFragmentList();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragments.get(0)).commit();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFinishDialog();
            }
        });
         int isVibrate = (PreferencesData.getBoolean(Constants.PREF_VIBRATE, false) ? 1 : 0);
        int  isLight = (PreferencesData.getBoolean(Constants.PREF_LOGHT, false) ? 1 : 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setupWindowAnimations();
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations() {
        Transition transition;
        transition = TransitionInflater.from(this).inflateTransition(R.transition.slide_from_bottom);
        getWindow().setEnterTransition(transition);

    }






    public void showWaiting() {
        kProgressHUD = KProgressHUD.create(this)
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public static UsageFields getUsageFields() {
        return usageFields;
    }

    public static GeneralFields getGeneralFields() {
        return generalFields;
    }

    private void addFragmentList() {
        fragments.add(new FragmentGeneralMedic());
        fragments.add(new FragmentUsageMedic());
        fragments.add(new FragmentComplitationMedic());
    }

    @Override
    public void onSaveButton(GeneralFields object) {

        lyNumberOne.setVisibility(View.INVISIBLE);
        checkedOne.setVisibility(View.VISIBLE);
        lineOne.setBackgroundColor(getResources().getColor(R.color.tealLight));
        txtStepTwo.setTextColor(getResources().getColor(R.color.black));
        lyNumberTwoText.setBackground(getResources().getDrawable(R.drawable.oval_green));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter, R.anim.exit);
        transaction.replace(R.id.container,  fragments.get(1) ).commit();
  //      getSupportFragmentManager().beginTransaction().replace(R.id.container, fragments.get(1)).commit();
        generalFields = object;
        txtTitle.setText("افزودن دارو" + " (" + object.getMidName() + ")");
        Utility.hideKeyboard();

    }
    public static void hideKeyBoardFromActivity(){
        Utility.hideKeyboard();
    }

    @Override
    public void onCancele() {
        showFinishDialog();
     /*   Utility.hideKeyboard();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        }else {
            finish();
        }*/
    }

    @Override
    public void onSaveButtonTwo(UsageFields object) {
        lyNumberTwo.setVisibility(View.INVISIBLE);
        checkedTwo.setVisibility(View.VISIBLE);
        linetwo.setBackgroundColor(getResources().getColor(R.color.tealLight));
        txtStepThree.setTextColor(getResources().getColor(R.color.black));
        lyNumberThreeText.setBackground(getResources().getDrawable(R.drawable.oval_green));

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter, R.anim.exit);
        transaction.replace(R.id.container,  fragments.get(2) ).commit();
   //     getSupportFragmentManager().beginTransaction().replace(R.id.container, fragments.get(2)).commit();
        usageFields = object;
        Utility.hideKeyboard();
    }

    @Override
    public void onCanceleTwo() {
        lyNumberOne.setVisibility(View.VISIBLE);
        checkedOne.setVisibility(View.INVISIBLE);
        lineOne.setBackgroundColor(getResources().getColor(R.color.gray));
        txtStepTwo.setTextColor(getResources().getColor(R.color.grayLiteText));
        lyNumberTwoText.setBackground(getResources().getDrawable(R.drawable.oval_gray));

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter, R.anim.exit);
        transaction.replace(R.id.container,  fragments.get(0) ).commit();
    //    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragments.get(0)).commit();
        Utility.hideKeyboard();

    }

    @Override
    public void onSaveButtonThree(EndUseFields endUseFields) {


        this.endUseFields = endUseFields;
        // keyboard off
        new CalcAlarm().execute();


    }

    public void saveStartProcess() {
        Utility.hideKeyboard();
        PillObject pillObject = savePillAndCat();

        // send for server two


        savePillUsage(pillObject);


        Utility.reCalculateManager(AddMedicianActivity.this);

    }

    private void savePillUsage(PillObject pillObject) {


        ArrayList<PillUsage> usageList = new ArrayList<>();
        if (pillObject.getUseType() == 1) {
            ///masrafe modam
            usageList = makePillUsageInPerFreeMood(pillObject, this);
        } else if (pillObject.getUseType() == 2) {
            // masraf bar asase rooz;
            usageList = makePillUsageInPerCountMood(pillObject, this);
        } else if (pillObject.getUseType() == 3) {
            usageList = makePillUsageInPerAmountMood(pillObject, this);
            //masraf bar asase mizane daroo;
        }
        AppDatabase database = AppDatabase.getInMemoryDatabase(this);
        database.pillUsageDao().insertPillList(usageList);
        SyncController sync = new SyncController();
        sync.checkDataBaseForUpdate();

    }


    private PillObject savePillAndCat() {
        AppDatabase database = AppDatabase.getInMemoryDatabase(this);
        String catName;
        if (generalFields.getCatName().length() == 0) {
            catName = "";
        } else {
            catName = generalFields.getCatName();
        }
        database.categoryDao().insertCat(new Category(catName, generalFields.getCatColour(), generalFields.getAlarmUrl()));

        int id = database.pillObjectDao().getLastId() + 1;
        PillObject pillObject = makePillObject(generalFields, usageFields, endUseFields, id);
        database.pillObjectDao().insertPill(pillObject);
        return pillObject;
    }


    @Override
    public void onCanceleThree() {

        lyNumberTwo.setVisibility(View.VISIBLE);
        checkedTwo.setVisibility(View.INVISIBLE);
        linetwo.setBackgroundColor(getResources().getColor(R.color.gray));

        txtStepThree.setTextColor(getResources().getColor(R.color.grayLiteText));
        lyNumberThreeText.setBackground(getResources().getDrawable(R.drawable.oval_gray));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter, R.anim.exit);
        transaction.replace(R.id.container,  fragments.get(1) ).commit();
      //  getSupportFragmentManager().beginTransaction().replace(R.id.container, fragments.get(1)).commit();
        Utility.hideKeyboard();

    }

    @Override
    public void onBackPressed() {


        showFinishDialog();
    }

    private void showFinishDialog() {
        final FinishDialog mydialog = new FinishDialog(this, "از ثبت دارو خارج می شوید؟", "");
        mydialog.setListener(new FinishListener() {
            @Override
            public void onReject() {
                mydialog.dismiss();
            }

            @Override
            public void onSuccess() {
                mydialog.dismiss();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                }else {
                    finish();
                }
            }


        });
        mydialog.show();
    }

    public static String getUnit() {
        if(usageFields!=null) {
            return usageFields.getUnitUsage();
        }else {
            return "";
        }
    }

    class CalcAlarm extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            showWaiting();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            saveStartProcess();
            return "";
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            disMissWaiting();
            usageFields = null;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAfterTransition();
            }else {
                finish();
            }

        }

    }

}
