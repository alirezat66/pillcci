package greencode.ir.pillcci.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.Visibility;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.adapter.BotAdapter;
import greencode.ir.pillcci.controler.AppDatabase;
import greencode.ir.pillcci.dialog.FinishDialog;
import greencode.ir.pillcci.dialog.FinishListener;
import greencode.ir.pillcci.interfaces.BotListInterface;
import greencode.ir.pillcci.objects.BotObject;
import greencode.ir.pillcci.presenters.BotListPresenter;
import greencode.ir.pillcci.utils.BaseActivity;
import greencode.ir.pillcci.utils.Utility;

/**
 * Created by alireza on 5/30/18.
 */

public class ActivityBotBook extends BaseActivity implements BotAdapter.onPhoneClick, BotListInterface {
    @BindView(R.id.img_back)
    AppCompatImageView imgBack;
    @BindView(R.id.title)
    TextView txtTitle;

    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.imgAdd)
    AppCompatImageView imgAdd;

    ArrayList<BotObject> botList = new ArrayList<>();
    BotAdapter adapter;
    KProgressHUD progressHUD;
    BotListPresenter presenter;
    FirebaseAnalytics firebaseAnalytics;
    @BindView(R.id.mainphone)
    LinearLayout mainphone;
    @BindView(R.id.txt_hamyar)
    TextView txtHamyar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_boox);
        ButterKnife.bind(this);
        txtHamyar.setVisibility(View.VISIBLE);
        mainphone.setVisibility(View.GONE);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    finish();
                }
            }
        });
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setupWindowAnimations();
        }
        txtTitle.setText("ربات تلگرام");
        Bundle params = new Bundle();
        params.putString("phoneNumber", Utility.getPhoneNumber(this));
        firebaseAnalytics.logEvent("botbook_open", params);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter = new BotListPresenter(this);
        AppDatabase database = AppDatabase.getInMemoryDatabase(this);
        String userId = database.profileDao().getMyProfile().getMyId();
        presenter.getBotList(userId);
        progressHUD = Utility.makeWaiting("لطفا منتطر بمانید", "در حال دریافت اطلاعات", this);
        progressHUD.show();
    }

    @OnClick(R.id.imgAdd)
    public void onClick() {
        Bundle params = new Bundle();
        params.putString("phoneNumber", Utility.getPhoneNumber(this));
        firebaseAnalytics.logEvent("addbot_open", params);
        Intent intent = new Intent(this, ActivityAddBot.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(BotObject phoneBook) {

    }

    @Override
    public void onItemDelete(final BotObject phoneBook) {
        final FinishDialog dialog = new FinishDialog(this, "از حذف این همیار سلامتی مطمئنی؟", "گزارش\u200Cهای مصرف بعد از این برای این همیار فرستاده نمی‌شود.");
        dialog.setListener(new FinishListener() {
            @Override
            public void onReject() {
                dialog.dismiss();
            }

            @Override
            public void onSuccess() {
                dialog.dismiss();
                progressHUD = Utility.makeWaiting("لطفا منتظر بمانید...", "در حال پاک کردن شماره", ActivityBotBook.this);
                progressHUD.show();
                presenter.deleteBotObject(AppDatabase.getInMemoryDatabase(ActivityBotBook.this).profileDao().getMyProfile().getMyId(), phoneBook);
            }
        });
        dialog.show();

    }

    @Override
    public void onError(String message) {
        progressHUD.dismiss();
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        Utility.centrizeAndShow(toast);
    }

    @Override
    public void onListReady(ArrayList<BotObject> bots) {
        progressHUD.dismiss();
        botList = bots;
        adapter = new BotAdapter(this, botList, this);
        list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        list.setAdapter(adapter);
    }

    @Override
    public void onDeleteSuccess() {
        presenter.getBotList(AppDatabase.getInMemoryDatabase(this).profileDao().getMyProfile().getMyId());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations() {
        Transition transition;
        transition = buildEnterTransition();

        getWindow().setEnterTransition(transition);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private Visibility buildEnterTransition() {
        Slide enterTransition = new Slide();
        enterTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        enterTransition.setSlideEdge(Gravity.RIGHT);
        return enterTransition;
    }
   /* @Override
    public void onItemClick(PhoneBook phoneBook) {
        if(phoneBook.isInitial()){
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneBook.getPhone(), null));
            startActivity(intent);
        }else {
            Intent intent = new Intent(this, ActivityEditPhone.class);
            intent.putExtra("phone", phoneBook.getPhone());
            startActivity(intent);
        }

    }

    @Override
    public void onItemEdit(PhoneBook phoneBook) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneBook.getPhone(), null));
        startActivity(intent);

    }*/
}
