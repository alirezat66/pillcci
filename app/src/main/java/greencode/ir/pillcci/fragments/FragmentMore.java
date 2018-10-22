package greencode.ir.pillcci.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.kaopiz.kprogresshud.KProgressHUD;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.activities.ActivityBotBook;
import greencode.ir.pillcci.activities.ActivityPhoneBook;
import greencode.ir.pillcci.activities.ActivityProfile;
import greencode.ir.pillcci.activities.ActivitySetting;
import greencode.ir.pillcci.activities.LoginActivity;
import greencode.ir.pillcci.controler.AppDatabase;
import greencode.ir.pillcci.database.Profile;
import greencode.ir.pillcci.dialog.FinishDialog;
import greencode.ir.pillcci.dialog.FinishListener;
import greencode.ir.pillcci.interfaces.MoreInterface;
import greencode.ir.pillcci.presenters.MorePresenter;
import greencode.ir.pillcci.utils.Constants;
import greencode.ir.pillcci.utils.PreferencesData;
import greencode.ir.pillcci.utils.Utility;

/**
 * Created by alireza on 5/27/18.
 */

public class FragmentMore extends Fragment implements MoreInterface {


    @BindView(R.id.cardCall)
    RelativeLayout cardCall;
    @BindView(R.id.cardProfile)
    RelativeLayout cardProfile;
    @BindView(R.id.cardSetting)
    RelativeLayout cardSetting;
    @BindView(R.id.logoImg)
    ImageView logoImg;
    @BindView(R.id.profileImg)
    CircleImageView profileImg;
    @BindView(R.id.shareCard)
    RelativeLayout shareCard;
    @BindView(R.id.lawCard)
    RelativeLayout lawCard;
    @BindView(R.id.cardDeleteAccount)
    RelativeLayout cardDeleteAccount;
    @BindView(R.id.cardExit)
    RelativeLayout cardExit;

    MorePresenter presenter;
    Profile profile;
    KProgressHUD progressHUD;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FirebaseAnalytics firebaseAnalytics;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        ButterKnife.bind(this, view);
        Bundle params = new Bundle();
        firebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        params.putString("phoneNumber", Utility.getPhoneNumber(getContext()));
        firebaseAnalytics.logEvent("more_open", params);
        presenter = new MorePresenter(this);
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

         profile = database.profileDao().getMyProfile();
        if (profile.getImg() != null) {
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
        } else {
            profileImg.setVisibility(View.GONE);
            logoImg.setVisibility(View.VISIBLE);
        }
        super.onStart();
    }

    @OnClick({R.id.cardCall, R.id.cardProfile, R.id.cardSetting,R.id.shareCard, R.id.lawCard, R.id.cardDeleteAccount, R.id.cardExit,R.id.cardBot})
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
            case R.id.shareCard:
                Bundle params = new Bundle();
                params.putString("phoneNumber", Utility.getPhoneNumber(getContext()));
                firebaseAnalytics.logEvent("more_share_app", params);
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "http://pillcci.ir");
                startActivity(Intent.createChooser(shareIntent, "معرفی به دوستان"));
                break;
            case R.id.cardBot:
                Intent intent3 = new Intent(getContext(),ActivityBotBook.class);
                startActivity(intent3);
            case R.id.lawCard:
                 params = new Bundle();
                params.putString("phoneNumber", Utility.getPhoneNumber(getContext()));
                firebaseAnalytics.logEvent("more_circumstance", params);
                break;
            case R.id.cardDeleteAccount:
                final FinishDialog dialog = new FinishDialog(getContext(),"حذف حساب کاربری","در صورت تایید، حساب شما به صورت کامل حذف شده و اطلاعات شما قابل بازیابی نمی باشد.");
                dialog.setListener(new FinishListener() {
                    @Override
                    public void onReject() {
                        dialog.dismiss();
                    }

                    @Override
                    public void onSuccess() {

                        dialog.dismiss();
                        progressHUD = KProgressHUD.create(getContext())
                                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                                .setLabel("لطفا منتظر بمانید")
                                .setDetailsLabel("در حال حذف حساب کاربری")
                                .setCancellable(false)
                                .setAnimationSpeed(2)
                                .setDimAmount(0.8f)
                                .setBackgroundColor(getResources().getColor(R.color.colorPrimary))
                                .show();
                        presenter.deleteAccount(profile.getMyId());
                    }
                });
                dialog.show();
                break;
            case R.id.cardExit:

                final FinishDialog mydialog = new FinishDialog(getContext(),"خروج از حساب کاربری","آیا مطمئن هستید ؟");
                mydialog.setListener(new FinishListener() {
                    @Override
                    public void onReject() {
                        mydialog.dismiss();
                    }

                    @Override
                    public void onSuccess() {
                        mydialog.dismiss();
                        PreferencesData.saveBool(Constants.PREF_LOGIN,false);
                        AppDatabase database = AppDatabase.getInMemoryDatabase(getContext());
                        database.phoneBookDao().nukeTable();
                        database.pillUsageDao().nukeTable();
                        database.pillObjectDao().nukeTable();
                        database.categoryDao().nukeTable();
                        database.profileDao().nukeTable();
                        Intent intent = new Intent(getContext(),LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
                mydialog.show();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }


    @Override
    public void onError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onSuccess() {
        progressHUD.dismiss();
        PreferencesData.saveBool(Constants.PREF_LOGIN,false);
        AppDatabase database = AppDatabase.getInMemoryDatabase(getContext());
        database.phoneBookDao().nukeTable();
        database.pillUsageDao().nukeTable();
        database.pillObjectDao().nukeTable();
        database.categoryDao().nukeTable();
        database.profileDao().nukeTable();
       Intent intent = new Intent(getContext(),LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
