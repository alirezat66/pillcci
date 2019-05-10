package greencode.ir.pillcci.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.JsonObject;
import com.instabug.bug.BugReporting;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.activities.ActivityBotBook;
import greencode.ir.pillcci.activities.ActivityPhoneBook;
import greencode.ir.pillcci.activities.ActivityProfile;
import greencode.ir.pillcci.activities.ActivitySetting;
import greencode.ir.pillcci.activities.ActivityShowTerms;
import greencode.ir.pillcci.activities.LoginActivity;
import greencode.ir.pillcci.controler.AppDatabase;
import greencode.ir.pillcci.database.PillObject;
import greencode.ir.pillcci.database.PillUsage;
import greencode.ir.pillcci.database.Profile;
import greencode.ir.pillcci.dialog.FinishDialog;
import greencode.ir.pillcci.dialog.FinishListener;
import greencode.ir.pillcci.interfaces.MoreInterface;
import greencode.ir.pillcci.presenters.MorePresenter;
import greencode.ir.pillcci.retrofit.CallerService;
import greencode.ir.pillcci.retrofit.ServerListener;
import greencode.ir.pillcci.retrofit.SyncController;
import greencode.ir.pillcci.utils.Constants;
import greencode.ir.pillcci.utils.PersianCalculater;
import greencode.ir.pillcci.utils.PreferencesData;
import greencode.ir.pillcci.utils.TransitionHelper;
import greencode.ir.pillcci.utils.Utility;

/**
 * Created by alireza on 5/27/18.
 */

public class FragmentMore extends Fragment implements MoreInterface, ServerListener {


    @BindView(R.id.cardCall)
    RelativeLayout cardCall;
    @BindView(R.id.cardProfile)
    RelativeLayout cardProfile;
    @BindView(R.id.cardSetting)
    RelativeLayout cardSetting;
    @BindView(R.id.cardEnter)
    RelativeLayout cardEnter;
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
    boolean isGuess = false;
    MorePresenter presenter;
    Profile profile;
    KProgressHUD progressHUD;
    @BindView(R.id.cardBot)
    RelativeLayout cardBot;
    @BindView(R.id.txt_version)
    TextView txtVersion;

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
        isGuess = PreferencesData.getBoolean(Constants.PREF_Guess, getContext());

        firebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        if (PreferencesData.getBoolean(Constants.PREF_Guess, getContext())) {
            params.putString("phoneNumber", "guest");
            firebaseAnalytics.logEvent("more_open", params);
            cardEnter.setVisibility(View.VISIBLE);
        } else {
            params.putString("phoneNumber", Utility.getPhoneNumber(getContext()));
            firebaseAnalytics.logEvent("more_open", params);
            cardEnter.setVisibility(View.GONE);

        }

        presenter = new MorePresenter(this);

        txtVersion.setMovementMethod(LinkMovementMethod.getInstance());

        String text = "<font color=#999999>نسخه  "+Utility.getVersionName()+"- ©"+ " </font>   <a color = #0000FF href=\"http://www.sarshar.institute\">موسسه مطالعات سلامت سرشار </a>   ";

        /*xtLink.setMovementMethod(LinkMovementMethod.getInstance());

        txtLink.setText(//getResources().getString(R.string.onboarding_page_four) + " " +
                Html.fromHtml(getResources().getString(R.string.string_with_links))); //+ " " +
               // getResources().getString(R.string.onboarding_page_four_tow));*/
        txtVersion.setText(Html.fromHtml(text));
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

        if (PreferencesData.getBoolean(Constants.PREF_Guess, getContext())) {
            cardDeleteAccount.setVisibility(View.GONE);
            cardExit.setVisibility(View.GONE);

        } else {
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
        }
        super.onStart();
    }

    @OnClick({R.id.cardCall, R.id.cardProfile, R.id.cardSetting, R.id.shareCard, R.id.lawCard, R.id.cardDeleteAccount, R.id.cardExit, R.id.cardBot, R.id.cardEnter, R.id.feedBackCard})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cardCall:

                Intent intent = new Intent(getContext(), ActivityPhoneBook.class);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    transitionTo(intent);
                } else {
                    startActivity(intent);
                }

                break;
            case R.id.feedBackCard:
                BugReporting.invoke();
                break;
            case R.id.cardProfile:

                Intent intent1 = new Intent(getContext(), ActivityProfile.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    transitionTo(intent1);
                } else {
                    startActivity(intent1);
                }
                break;
            case R.id.cardSetting:

                Intent intent2 = new Intent(getContext(), ActivitySetting.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    transitionTo(intent2);
                } else {
                    startActivity(intent2);
                }

                // startActivity(intent2);
                break;
            case R.id.shareCard:
                Bundle params = new Bundle();
                if (isGuess) {
                    params.putString("phoneNumber", "Guest");

                } else {
                    params.putString("phoneNumber", Utility.getPhoneNumber(getContext()));

                }
                firebaseAnalytics.logEvent("more_share_app", params);
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "سلام! پیلچی رو دوست داشتم و به شما هم پیشنهادش می\u200Cکنم!\n" +
                        "پیلچی، یادآور هوشمند!  pillcci.ir\n" +
                        "https://t.me/pillcci\n");
                startActivity(Intent.createChooser(shareIntent, "معرفی به دوستان"));


                break;
            case R.id.cardBot:

                if (isGuess) {
                    Toast toast = Toast.makeText(getContext(), "کاربر مهمان نمی‌تواند از این قسمت استفاده کند.", Toast.LENGTH_SHORT);
                    Utility.centrizeAndShow(toast);
                } else {
                    Intent intent3 = new Intent(getContext(), ActivityBotBook.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        transitionTo(intent3);
                    } else {
                        startActivity(intent3);
                    }
                }
                break;
            // startActivity(intent3);
            case R.id.lawCard:

                params = new Bundle();
                params.putString("phoneNumber", Utility.getPhoneNumber(getContext()));
                firebaseAnalytics.logEvent("more_circumstance", params);


                Intent i = new Intent(getActivity(), ActivityShowTerms.class);
                getActivity().startActivity(i);

                break;
            case R.id.cardDeleteAccount:
                final FinishDialog dialog = new FinishDialog(getContext(), "حذف حساب کاربری", "در صورت تأیید، حساب شما حذف شده و اطلاعات شما قابل بازیابی نخواهند بود.");
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

                AppDatabase database = AppDatabase.getInMemoryDatabase(getContext());
                List<PillObject> pills = database.pillObjectDao().getAllUnSyncPill();
                List<PillUsage> usages = database.pillUsageDao().allNotSyncPill();

                final FinishDialog mydialog;
                if (pills != null && usages != null) {

                    if (pills.size() > 0 || usages.size() > 0) {
                        long lastUpdate = PreferencesData.getLong(Constants.Pref_Last_Update);
                        String datetime = PersianCalculater.getYearMonthAndDay(lastUpdate);
                        String timeHour = PersianCalculater.getHourseAndMin(lastUpdate);
                        mydialog = new FinishDialog(getContext(), "خروج از حساب کاربری", "تغییرات از تاریخ " + datetime + " ساعت " + timeHour + " همگام‌سازی نشده و در صورت خروج ذخیره نخواهند شد. آیا از خروج مطمئنی؟");
                    } else {
                        mydialog = new FinishDialog(getContext(), "خروج از حساب کاربری", "آیا مطمئنی؟");

                    }
                } else {
                    mydialog = new FinishDialog(getContext(), "خروج از حساب کاربری", "آیا مطمئنی؟");

                }

                mydialog.setListener(new FinishListener() {
                    @Override
                    public void onReject() {
                        mydialog.dismiss();
                        SyncController syncController = new SyncController();
                        syncController.checkDataBaseForUpdate();
                    }

                    @Override
                    public void onSuccess() {
                        mydialog.dismiss();
                        final AppDatabase database = AppDatabase.getInMemoryDatabase(getContext());


                        FirebaseInstanceId.getInstance().getInstanceId()
                                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                        if (!task.isSuccessful()) {
                                            Log.w("hilevel", "getInstanceId failed", task.getException());
                                            // Log and toast
                                            progressHUD.dismiss();
                                            PreferencesData.saveBool(Constants.PREF_Guess, false);
                                            PreferencesData.saveBool(Constants.PREF_LOGIN, false);
                                            AppDatabase database = AppDatabase.getInMemoryDatabase(getContext());
                                            database.phoneBookDao().nukeTable();
                                            database.pillUsageDao().nukeTable();
                                            database.pillObjectDao().nukeTable();
                                            database.categoryDao().nukeTable();
                                            database.profileDao().nukeTable();
                                            Intent intent = new Intent(getContext(), LoginActivity.class);
                                            startActivity(intent);
                                            getActivity().finish();
                                            PreferencesData.saveString(Constants.Pref_Token, "");
                                            //  database.profileDao().nukeTable();
                                        } else {
                                            String token = task.getResult().getToken();
                                            // Log and toast
                                            String msg = getString(R.string.msg_token_fmt, token);
                                            PreferencesData.saveBool(Constants.PREF_Guess, false);
                                            String userId = database.profileDao().getMyProfile().getMyId();
                                            CallerService.removeToken(FragmentMore.this, userId, token);
                                            PreferencesData.saveBool(Constants.PREF_LOGIN, false);
                                            PreferencesData.saveString(Constants.Pref_Token, "");
                                            database.phoneBookDao().nukeTable();
                                            database.pillUsageDao().nukeTable();
                                            database.pillObjectDao().nukeTable();
                                            database.categoryDao().nukeTable();
                                            database.profileDao().nukeTable();


                                            Log.d("hilevel", msg);
                                        }


                                        Utility.reCalculateManager(getContext());
                                        // Get new Instance ID token


                                    }
                                });


                        showWaitingLoad();

                    }
                });
                mydialog.show();

                break;
            case R.id.cardEnter:
                PreferencesData.saveBool(Constants.PREF_Guess, false, getContext());
                intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
        }
    }

    public void showWaitingLoad() {
        progressHUD = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("لطفا منتظر بمانید")
                .setDetailsLabel("درحال خروج از حساب کاربری ...")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.8f)
                .setBackgroundColor(getResources().getColor(R.color.colorPrimary))
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();


    }


    @Override
    public void onError(String error) {
        if (progressHUD.isShowing()) {
            progressHUD.dismiss();
        }
        Toast toast = Toast.makeText(getContext(), error, Toast.LENGTH_SHORT);
        Utility.centrizeAndShow(toast);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("unchecked")
    void transitionTo(Intent i) {
        final Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants(getActivity(), true);
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), pairs);
        startActivity(i, transitionActivityOptions.toBundle());
    }

    @Override
    public void onSuccess() {
        progressHUD.dismiss();
        PreferencesData.saveBool(Constants.PREF_LOGIN, false);
        AppDatabase database = AppDatabase.getInMemoryDatabase(getContext());
        database.phoneBookDao().nukeTable();
        database.pillUsageDao().nukeTable();
        database.pillObjectDao().nukeTable();
        database.categoryDao().nukeTable();
        database.profileDao().nukeTable();
        Utility.reCalculateManager(getContext());
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onFailure(int i, String str) {
        progressHUD.dismiss();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onSuccess(int i, JsonObject jsonObject) throws JSONException {
        progressHUD.dismiss();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
