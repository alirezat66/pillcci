package greencode.ir.pillcci.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.activities.ActivityPhoneBook;

/**
 * Created by alireza on 5/27/18.
 */

public class FragmentMore extends Fragment {


    @BindView(R.id.cardCall)
    CardView cardCall;
    @BindView(R.id.cardProfile)
    CardView cardProfile;
    @BindView(R.id.cardSetting)
    CardView cardSetting;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        ButterKnife.bind(this, view);


        return view;
    }



    @OnClick({R.id.cardCall, R.id.cardProfile, R.id.cardSetting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cardCall:
                Intent intent = new Intent(getContext(), ActivityPhoneBook.class);
                startActivity(intent);
                break;
            case R.id.cardProfile:
                break;
            case R.id.cardSetting:
                break;
        }
    }
}
