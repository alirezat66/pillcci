package greencode.ir.pillcci.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.imgAdd)
    AppCompatImageView imgAdd;


    FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_boox);
        ButterKnife.bind(this);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
        LinearLayoutManager staggeredGridLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);

        list.setLayoutManager(staggeredGridLayoutManager);
        list.setAdapter(new PhoneAdapter(this, phoneBooks, this));
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
        if(phoneBook.isInitial()){
            Bundle params = new Bundle();
            params.putString("phoneNumber", Utility.getPhoneNumber(this));
            firebaseAnalytics.logEvent("phonebook_call", params);
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneBook.getPhone(), null));
            startActivity(intent);
        }else {
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
}
