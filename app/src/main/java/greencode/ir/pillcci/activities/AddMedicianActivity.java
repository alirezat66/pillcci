package greencode.ir.pillcci.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.controler.AppDatabase;
import greencode.ir.pillcci.database.Category;
import greencode.ir.pillcci.database.PillObject;
import greencode.ir.pillcci.database.PillUsage;
import greencode.ir.pillcci.fragments.FragmentComplitationMedic;
import greencode.ir.pillcci.fragments.FragmentGeneralMedic;
import greencode.ir.pillcci.fragments.FragmentUsageMedic;
import greencode.ir.pillcci.objects.EndUseFields;
import greencode.ir.pillcci.objects.GeneralFields;
import greencode.ir.pillcci.objects.UsageFields;
import greencode.ir.pillcci.service.EventReciver;
import greencode.ir.pillcci.utils.BaseActivity;
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
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.txtStepOne)
    TextView txtStepOne;
    @BindView(R.id.lyStepOne)
    LinearLayout lyStepOne;
    @BindView(R.id.txtStepTwo)
    TextView txtStepTwo;
    @BindView(R.id.lyStepTwo)
    LinearLayout lyStepTwo;
    @BindView(R.id.txtStepThree)
    TextView txtStepThree;
    @BindView(R.id.lyStepThree)
    LinearLayout lyStepThree;
    @BindView(R.id.container)
    LinearLayout container;
    KProgressHUD kProgressHUD;

    private int mSelectedColor;
    public static GeneralFields generalFields;
    public static UsageFields usageFields;
    public static EndUseFields endUseFields;
    ArrayList<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medician);
        ButterKnife.bind(this);
        txtTitle.setText("افزودن داروی جدید");
        addFragmentList();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragments.get(0)).commit();



    }

    public static UsageFields getUsageFields() {
        return usageFields;
    }

    private void addFragmentList() {
        fragments.add(new FragmentGeneralMedic());
        fragments.add(new FragmentUsageMedic());
        fragments.add(new FragmentComplitationMedic());
    }

    @Override
    public void onSaveButton(GeneralFields object) {
        txtStepOne.setTextColor(getResources().getColor(R.color.grayText));
        txtStepTwo.setTextColor(getResources().getColor(R.color.VioletBlue));
        txtStepThree.setTextColor(getResources().getColor(R.color.grayText));
        lyStepOne.setBackgroundColor(getResources().getColor(R.color.transparent));
        lyStepTwo.setBackgroundColor(getResources().getColor(R.color.VioletBlue));
        lyStepThree.setBackgroundColor(getResources().getColor(R.color.transparent));
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragments.get(1)).commit();
        generalFields = object;
        txtTitle.setText(txtTitle.getText() + "(" + object.getMidName() + ")");
        Utility.hideKeyboard();

    }

    @Override
    public void onCancele() {
        Utility.hideKeyboard();

        finish();
    }

    @Override
    public void onSaveButtonTwo(UsageFields object) {
        txtStepOne.setTextColor(getResources().getColor(R.color.grayText));
        txtStepTwo.setTextColor(getResources().getColor(R.color.grayText));
        txtStepThree.setTextColor(getResources().getColor(R.color.VioletBlue));
        lyStepOne.setBackgroundColor(getResources().getColor(R.color.transparent));
        lyStepTwo.setBackgroundColor(getResources().getColor(R.color.transparent));
        lyStepThree.setBackgroundColor(getResources().getColor(R.color.VioletBlue));
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragments.get(2)).commit();
        usageFields = object;
        Utility.hideKeyboard();
    }

    @Override
    public void onCanceleTwo() {
        txtStepOne.setTextColor(getResources().getColor(R.color.VioletBlue));
        txtStepTwo.setTextColor(getResources().getColor(R.color.grayText));
        txtStepThree.setTextColor(getResources().getColor(R.color.grayText));
        lyStepOne.setBackgroundColor(getResources().getColor(R.color.VioletBlue));
        lyStepTwo.setBackgroundColor(getResources().getColor(R.color.transparent));
        lyStepThree.setBackgroundColor(getResources().getColor(R.color.transparent));
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragments.get(0)).commit();
        Utility.hideKeyboard();

    }

    @Override
    public void onSaveButtonThree(EndUseFields endUseFields) {

        this.endUseFields = endUseFields;

        kProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("لطفا منتظر بمانید")
                .setDetailsLabel("در حال انجام محاسبات و ذخیره دارو")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.8f)
                .setBackgroundColor(getResources().getColor(R.color.colorPrimary))
                .show();
        Utility.hideKeyboard();


        AppDatabase database = AppDatabase.getInMemoryDatabase(this);
        String catName;
        if (generalFields.getCatName().length() == 0) {
            catName = "عمومی";
        } else {
            catName = generalFields.getCatName();
        }
        database.categoryDao().insertCat(new Category(catName, generalFields.getCatColour(), generalFields.getAlarmUrl()));

        PillObject pillObject = makePillObject(generalFields, usageFields, endUseFields);
        database.pillObjectDao().insertPill(pillObject);
        ArrayList<PillUsage> usageList = new ArrayList<>();
        if (pillObject.getUseType() == 1) {
            usageList = makePillUsageInPerFreeMood(pillObject);
            //usage continusely
        } else if (pillObject.getUseType() == 2) {
            // usage per count;
            usageList = makePillUsageInPerCountMood(pillObject);

        } else {
            usageList = makePillUsageInPerAmountMood(pillObject);
            //usage per amount;
        }
        database.pillUsageDao().insertPillList(usageList);

        PillUsage pillUsage = database.pillUsageDao().getNearestUsage(System.currentTimeMillis());
        startAlarmPillReminder(pillUsage);
        kProgressHUD.dismiss();
        finish();
    }

    private void startAlarmPillReminder(PillUsage pillUsage) {
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(AddMedicianActivity.this, EventReciver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (pillUsage.getUsageTime() - System.currentTimeMillis()), pendingIntent);
    }

    @Override
    public void onCanceleThree() {
        txtStepOne.setTextColor(getResources().getColor(R.color.grayText));
        txtStepTwo.setTextColor(getResources().getColor(R.color.VioletBlue));
        txtStepThree.setTextColor(getResources().getColor(R.color.grayText));
        lyStepOne.setBackgroundColor(getResources().getColor(R.color.transparent));
        lyStepTwo.setBackgroundColor(getResources().getColor(R.color.VioletBlue));
        lyStepThree.setBackgroundColor(getResources().getColor(R.color.transparent));
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragments.get(1)).commit();
        Utility.hideKeyboard();

    }


    public static String getUnit() {
        return usageFields.getUnitUsage();
    }
}
