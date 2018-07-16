package greencode.ir.pillcci.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.adapter.PhoneAdapter;
import greencode.ir.pillcci.controler.AppDatabase;
import greencode.ir.pillcci.database.PhoneBook;
import greencode.ir.pillcci.utils.BaseActivity;

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

    }

    @Override
    protected void onStart() {
        super.onStart();
        AppDatabase database = AppDatabase.getInMemoryDatabase(this);
        ArrayList<PhoneBook> phoneBooks = new ArrayList<>(database.phoneBookDao().listOfPhone());
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        list.setLayoutManager(staggeredGridLayoutManager);
        list.setAdapter(new PhoneAdapter(this, phoneBooks, this));
    }

    @OnClick(R.id.imgAdd)
    public void onClick() {
        Intent intent = new Intent(this, ActivityAddPhone.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(PhoneBook phoneBook) {

            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneBook.getPhone(), null));
            startActivity(intent);

    }

    @Override
    public void onItemEdit(PhoneBook phoneBook) {
        Intent intent = new Intent(this, ActivityEditPhone.class);
        intent.putExtra("phone", phoneBook.getPhone());
        startActivity(intent);
    }
}
