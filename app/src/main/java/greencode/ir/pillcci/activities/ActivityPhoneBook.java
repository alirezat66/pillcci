package greencode.ir.pillcci.activities;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.adapter.PhoneAdapter;
import greencode.ir.pillcci.controler.AppDatabase;
import greencode.ir.pillcci.database.PhoneBook;
import greencode.ir.pillcci.utils.BaseActivity;
import greencode.ir.pillcci.utils.Utility;

/**
 * Created by alireza on 5/30/18.
 */

public class ActivityPhoneBook extends BaseActivity implements PhoneAdapter.onPhoneClick {
    @BindView(R.id.img_back)
    AppCompatImageView imgBack;
    @BindView(R.id.title)
    TextView txtTitle;
    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.imgAdd)
    AppCompatImageView imgAdd;
    PhoneAdapter adapter;

    FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_boox);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setupWindowAnimations();
        }
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
        Bundle params = new Bundle();
        params.putString("phoneNumber", Utility.getPhoneNumber(this));
        firebaseAnalytics.logEvent("phonebook_open", params);

    }

    @Override
    protected void onStart() {
        super.onStart();
        AppDatabase database = AppDatabase.getInMemoryDatabase(this);
        ArrayList<PhoneBook> phoneBooks = new ArrayList<>(database.phoneBookDao().listOfPhone());
        LinearLayoutManager staggeredGridLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        list.setLayoutManager(staggeredGridLayoutManager);
        adapter = new PhoneAdapter(this, phoneBooks, this);
        list.setAdapter(adapter);
    }

    @OnClick(R.id.imgAdd)
    public void onClick() {
        Bundle params = new Bundle();
        params.putString("phoneNumber", Utility.getPhoneNumber(this));
        firebaseAnalytics.logEvent("addphone_open", params);
        Intent intent = new Intent(this, ActivityAddPhone.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(PhoneBook phoneBook) {
        if (phoneBook.isInitial()) {
            Bundle params = new Bundle();
            params.putString("phoneNumber", Utility.getPhoneNumber(this));
            firebaseAnalytics.logEvent("phonebook_call", params);
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneBook.getPhone(), null));
            startActivity(intent);
        } else {
            Bundle params = new Bundle();
            params.putString("phoneNumber", Utility.getPhoneNumber(this));
            firebaseAnalytics.logEvent("editphone_open", params);
            Intent intent = new Intent(this, ActivityEditPhone.class);
            intent.putExtra("phone", phoneBook.getPhone());
            startActivity(intent);
        }

    }

    @Override
    public void onItemEdit(PhoneBook phoneBook) {
        Bundle params = new Bundle();
        params.putString("phoneNumber", Utility.getPhoneNumber(this));
        firebaseAnalytics.logEvent("phonebook_call", params);
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneBook.getPhone(), null));
        startActivity(intent);

    }

    @Override
    public void onItemDelete(PhoneBook phoneBook) {
        AppDatabase database = AppDatabase.getInMemoryDatabase(this);
        database.phoneBookDao().deletePhone(phoneBook.getid());
        adapter.remove(phoneBook);
        adapter.notifyDataSetChanged();

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

    @OnClick({R.id.lyInfo, R.id.lyOrgance})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lyInfo:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "1490", null));
                startActivity(intent);
                break;
            case R.id.lyOrgance:
                 intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "115", null));
                startActivity(intent);
                break;
        }
    }
}
