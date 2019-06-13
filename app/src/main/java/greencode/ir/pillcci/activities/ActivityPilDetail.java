package greencode.ir.pillcci.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.controler.AppDatabase;
import greencode.ir.pillcci.database.PillObject;
import greencode.ir.pillcci.database.PillUsage;
import greencode.ir.pillcci.utils.BaseActivity;
import greencode.ir.pillcci.utils.PersianCalculater;
import greencode.ir.pillcci.utils.Utility;
import uk.co.chrisjenx.calligraphy.CalligraphyUtils;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

/**
 * Created by alireza on 6/26/18.
 */

public class ActivityPilDetail extends BaseActivity {


    String midName;
    String catName;
    PillObject object;
    List<PillUsage> usage;
    @BindView(R.id.imgLogo)
    ImageView imgLogo;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.scroll)
    NestedScrollView scroll;
    @BindView(R.id.txtMedName)
    TextView txtMedName;
    @BindView(R.id.txtCatName)
    TextView txtCatName;
    @BindView(R.id.txtDrName)
    TextView txtDrName;
    @BindView(R.id.txtUnitUsage)
    TextView txtUnitUsage;
    @BindView(R.id.txtRepeatCount)
    TextView txtRepeatCount;
    @BindView(R.id.txtused)
    TextView txtused;
    @BindView(R.id.useDays)
    TextInputEditText useDays;
    @BindView(R.id.usePartDays)
    TextInputEditText usePartDays;
    @BindView(R.id.edtStartTime)
    TextInputEditText edtStartTime;
    @BindView(R.id.edtUseEachTime)
    TextInputEditText edtUseEachTime;
    @BindView(R.id.edtDescription)
    TextInputEditText edtDescription;
    @BindView(R.id.cardOne)
    CardView cardOne;
    @BindView(R.id.cardTwo)
    CardView cardTwo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pil_detail);
        ButterKnife.bind(this);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbar.setTitle("جزییات دارو");
        collapsingToolbar.setCollapsedTitleTypeface(Utility.getRegularTypeFace(this));
        collapsingToolbar.setExpandedTitleTypeface(Utility.getRegularTypeFace(this));

      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imgLogo.setTransitionName("simple_activity_transition");
            txtMedName.setTransitionName("simple_activity_transition");
        }*/
        AppDatabase database = AppDatabase.getInMemoryDatabase(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            midName = bundle.getString("midName");
            catName = bundle.getString("catName");
            txtMedName.setText(midName);
            object = database.pillObjectDao().specialPil(midName,catName);
            usage = database.pillUsageDao().listSpecialPillUsage(midName,catName);
        }
        txtCatName.setText(object.getCatName());
        txtCatName.setTextColor(object.getCatColor());
        txtUnitUsage.setText(object.getUnitUse());
        txtDrName.setText(object.getDrName());
        if(usage.size()>0) {
            txtRepeatCount.setText(usage.get(0).getCountPerDay());
        }
        List<PillUsage> usedUsage = database.pillUsageDao().listSpecialUsedPillUsage(midName,catName);
        double amount =0 ;// masrafe ye rooz
        String[]amounts = object.getUnitsCount().split(",");
        for(String mount :amounts){
            amount+=Double.parseDouble(mount);
        }
        double total = 0 ;
        double totalAmount = object.getAllPillCount();
        if(totalAmount>1){
            total = totalAmount;
        }else {
            if(object.getUseType()==2){
                // bar asase tedade rooz haye sabt shode
                total = amount * object.getAllUseDays();
            }else if(object.getUseType()==1){
                // free mode
                total = 0 ;
            }else {
              // amunt ; // bar asase mizane masraf
              total = object.getTotalAmounts();
            }


        }
        String strTotal ="";
        if(total - (int)total == 0 ){
            strTotal = (int)total+"";
        }else {
            strTotal = total+"";
        }
        if(total==0){
            strTotal=getResources().getString(R.string.infinity);
        }

        double usedAmount = 0;
        for(PillUsage usage : usedUsage){
            usedAmount+=Double.parseDouble(usage.getUnitAmount());
        }
        String usedAmountStr;
        if(usedAmount-(int)usedAmount==0){
            usedAmountStr=(int)usedAmount+"";
        }else {
            usedAmountStr=usedAmount+"";
        }

        txtused.setText(" مصرف شده :" +"\n"+ usedAmountStr+" / "+strTotal);
        // changeToolbarFont(toolbar, this);
        useDays.setText(object.getshowDays());

        String[] times  = object.getUnitTimes().split(",");
        String strTimesList = "";
        for(String s : times){
            String hhmm = PersianCalculater.getHourseAndMin(Long.parseLong(s));
            strTimesList = strTimesList + hhmm;
            strTimesList = strTimesList +",";
        }
        if(strTimesList.length()>0){
            strTimesList = strTimesList.substring(0,strTimesList.length()-1);
        }
        usePartDays.setText(strTimesList);
        edtStartTime.setText(PersianCalculater.getHourseAndMin(object.getStardHour(),object.getStartMin()));
        edtDescription.setText(object.getDescription());
        edtUseEachTime.setText(object.getEachTime());


        if (!object.getB64().equals("")) {
            byte[] decodedString = Base64.decode(object.getB64(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imgLogo.setImageBitmap(decodedByte);
        } else {
            imgLogo.setImageResource(R.drawable.empty_pill);
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( ActivityPilDetail.this,ActivityEditPill.class);
                intent.putExtra("pillName", midName);
                intent.putExtra("catName", catName);

                    startActivity(intent);
                    finish();

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static void changeToolbarFont(Toolbar toolbar, Activity context) {
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof TextView) {

                CalligraphyUtils.applyFontToTextView((TextView) view, TypefaceUtils.load(context.getAssets(), "iransansmobilefanum.ttf"));
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
