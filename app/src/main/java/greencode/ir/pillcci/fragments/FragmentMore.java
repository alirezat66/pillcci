package greencode.ir.pillcci.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.activities.ActivityPhoneBook;
import greencode.ir.pillcci.activities.ActivityProfile;
import greencode.ir.pillcci.activities.ActivitySetting;
import greencode.ir.pillcci.controler.AppDatabase;
import greencode.ir.pillcci.database.Profile;

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
    @BindView(R.id.logoImg)
    ImageView logoImg;
    @BindView(R.id.profileImg)
    CircleImageView profileImg;

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

    @Override
    public void onStart() {
        AppDatabase database = AppDatabase.getInMemoryDatabase(getContext());
        if (database.profileDao().listOfCats().size() > 0) {
            cardProfile.setVisibility(View.VISIBLE);
        } else {
            cardProfile.setVisibility(View.GONE);
        }

        Profile profile = database.profileDao().getMyProfile();
        if (!profile.getImg().equals("")) {

            byte[] decodedString = Base64.decode(profile.getImg(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            profileImg.setImageBitmap(decodedByte);
            profileImg.setVisibility(View.VISIBLE);
            logoImg.setVisibility(View.GONE);

        } else {
            profileImg.setVisibility(View.GONE);
            logoImg.setVisibility(View.VISIBLE);
        }
        super.onStart();
    }

    @OnClick({R.id.cardCall, R.id.cardProfile, R.id.cardSetting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cardCall:
                Intent intent = new Intent(getContext(), ActivityPhoneBook.class);
                startActivity(intent);
                break;
            case R.id.cardProfile:
                Intent intent1 = new Intent(getContext(), ActivityProfile.class);
                startActivity(intent1);
                break;
            case R.id.cardSetting:
                Intent intent2 = new Intent(getContext(), ActivitySetting.class);
                startActivity(intent2);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
