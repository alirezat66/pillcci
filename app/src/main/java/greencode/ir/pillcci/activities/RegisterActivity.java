package greencode.ir.pillcci.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.controler.AppDatabase;
import greencode.ir.pillcci.database.Profile;
import greencode.ir.pillcci.fragments.FragmentSignUpStepOne;
import greencode.ir.pillcci.fragments.FragmentSignUpStepSetPass;
import greencode.ir.pillcci.fragments.FragmentSignUpStepThree;
import greencode.ir.pillcci.interfaces.RegisterStepOneInterface;
import greencode.ir.pillcci.interfaces.SetPassInterface;
import greencode.ir.pillcci.objects.LoginResponse;
import greencode.ir.pillcci.objects.RegisterRequest;
import greencode.ir.pillcci.objects.RegisterResponse;
import greencode.ir.pillcci.presenters.PresenterRegisterStepOne;
import greencode.ir.pillcci.presenters.SetPassPresenter;
import greencode.ir.pillcci.retrofit.reqobject.LoginRequest;
import greencode.ir.pillcci.retrofit.reqobject.SignUpRequest;
import greencode.ir.pillcci.utils.BaseActivity;
import greencode.ir.pillcci.utils.Constants;
import greencode.ir.pillcci.utils.NetworkStateReceiver;
import greencode.ir.pillcci.utils.PreferencesData;
import greencode.ir.pillcci.utils.Utility;

/**
 * Created by alireza on 5/11/18.
 */

public class RegisterActivity extends BaseActivity implements NetworkStateReceiver.NetworkStateReceiverListener, RegisterStepOneInterface, FragmentSignUpStepOne.onActionStepOne, SetPassInterface, FragmentSignUpStepSetPass.onActionStepTwo,FragmentSignUpStepThree.onActionStepThree {

    @BindView(R.id.netError)
    TextView netError;
    @BindView(R.id.error)
    TextView error;
    @BindView(R.id.container)
    LinearLayout container;
    @BindView(R.id.root)
    ConstraintLayout root;
    private NetworkStateReceiver networkStateReceiver;
    PresenterRegisterStepOne presenter;
    SetPassPresenter presenterSetPass;
    KProgressHUD kProgressHUD;
    public static RegisterRequest registerRequest;
    ArrayList<Fragment> fragments = new ArrayList<>();
    boolean hasNet = false;

    String phone;
    String pass;

    int state = 1; //1 step one 2/ step two 3/step three
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        fragments.add(new FragmentSignUpStepOne());
        fragments.add(new FragmentSignUpStepSetPass());
        fragments.add(new FragmentSignUpStepThree());
        registerRequest = new RegisterRequest();
        Bundle bundle = getIntent().getExtras();

        state=1;
        if (bundle != null) {

            registerRequest.setCodePhone(bundle.getInt(Constants.PREF_CODE));
            registerRequest.setPhone(bundle.getString(Constants.PREF_USER_Phone));
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragments.get(0)).commit();
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        presenter = new PresenterRegisterStepOne(this);

    }

    public void showWaiting() {
        kProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("لطفا منتظر بمانید")
                .setDetailsLabel("در حال ثبت اطلاعات کاربر")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.8f)
                .setBackgroundColor(getResources().getColor(R.color.colorPrimary))
                .show();
    }

    @Override
    public void onBackPressed() {
        if(state==1) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra(Constants.PREF_USER_NAME, registerRequest.getUserName());
            startActivity(intent);
            finish();
        }else if(state==2){
            state=1;
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragments.get(0)).commit();

        }else {
            state=2;
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragments.get(1)).commit();

        }
    }

    @Override
    public void networkAvailable() {
        netError.setVisibility(View.GONE);
        hasNet = true;
    }

    @Override
    public void networkUnavailable() {
        hasNet = false;
        netError.setVisibility(View.VISIBLE);
    }

    public static RegisterRequest getRequest() {
        return registerRequest;
    }

    @Override
    public void onInvalid() {
        disMissWaiting();
        showError("نام کاربری صحیح نیست.");
    }

    @Override
    public void onInvalid(String message) {
        showError(message);
    }


    @Override
    public void onValidPhone(SignUpRequest request) {
        registerRequest = new RegisterRequest(request.getUsername(),"","",request.getReferrer());
        //fragment 1 voroode etelaat shomare tayid shod
        hiddenError();
        showWaiting();
        presenter.callService(request);


      /*
       */
    }
    private void prepareNewPage(){
        error.setVisibility(View.GONE);
        netError.setVisibility(View.GONE);
    }

    @Override
    public void onEmptyUser() {
        showError("نام کاربری را وارد کنید.");
    }

    @Override
    public void onSuccessPhone(RegisterResponse resp) {
        disMissWaiting();
        presenterSetPass = new SetPassPresenter(this);
        prepareNewPage();
        state=2;
        registerRequest.setCode(resp.getValidCode());
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragments.get(2)).commit();

    }

    @Override
    public void onLoginValid(LoginResponse resp) {
        disMissWaiting();
        hiddenError();
        PreferencesData.saveBool(Constants.PREF_LOGIN, true);
        final Profile profile = resp.getProfile(); //new Profile(edtUser.getText().toString(),"","",0,0,"","","","","","");
        AppDatabase database = AppDatabase.getInMemoryDatabase(this);
        database.profileDao().insertProfile(profile);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("hilevel", "getInstanceId failed", task.getException());
                            if(kProgressHUD.isShowing()){
                                kProgressHUD.dismiss();
                                showError("اشکال در دسترسی به شبکه. لطفا دوباره تلاش کن!");
                            }
                            return;
                        }
                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        PreferencesData.saveBool(Constants.PREF_Guess, false);
                        PreferencesData.saveString(Constants.Pref_Token,token);
                        AppDatabase database = AppDatabase.getInMemoryDatabase(RegisterActivity.this);
                        String userId = database.profileDao().getMyProfile().getMyId();
                        presenter.updateToken(userId,token);

                        Log.d("hilevel", msg);
                    }
                });


    }

    @Override
    public void onUpdateInvalidToken() {
        PreferencesData.saveBool("tokenSeted",false);
        Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onUpdateToken() {
        PreferencesData.saveBool("tokenSeted",true);
        Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void showWaitingLoad() {
        kProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("لطفا منتظر بمانید")
                .setDetailsLabel("درحال همگام سازی سوابق ...")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.8f)
                .setBackgroundColor(getResources().getColor(R.color.colorPrimary))
                .show();
    }


    public void showError(String str) {
        disMissWaiting();
        Toast toast =  Toast.makeText(this, str, Toast.LENGTH_LONG);
        Utility.centrizeAndShow(toast);

    }

    public void hiddenError() {
        error.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onRegisterButton(RegisterRequest request) {
        registerRequest = request;

        presenter.checkUser(request.getUserName(),request.getMoarefCode(),this);
    }

    @Override
    public void onInvide() {
        showError("نام کاربری اشتباه است.");
    }

    @Override
    public void onEmpty() {
        showError("نام کاربری را وارد کنید.");
    }

    @Override
    public void onPassEmpty() {
        showError("کلمه عبور را وارد کنید.");
    }

    @Override
    public void onPassWrong() {
        showError("کلمه عبور باید حداقل ۶ کارکتر از حروف و اعداد باشد.");
    }

    @Override
    public void onPassNotMatch() {
        showError("کلمه عبور و تکرار آن همسان نیستند.");
    }

    @Override
    public void onValid(RegisterRequest request) {
        showWaiting();
        presenterSetPass.callService(request);
    }

    @Override
    public void onErrorRegister(String error) {
        disMissWaiting();
        Toast toast = Toast.makeText(this, error, Toast.LENGTH_LONG);
        Utility.centrizeAndShow(toast);
    }

    @Override
    public void onSuccessRegister(RegisterResponse request) {

        LoginRequest req = new LoginRequest(phone,pass);
        presenter.tryToLogin(req);
       /* Intent intent = new Intent(this,LoginActivity.class);
        intent.putExtra(Constants.PREF_USER_NAME,request.getUserName());
        startActivity(intent);*/
      /*  finish();*/
        /*prepareNewPage();
        registerRequest.setCode(request.getValidCode());
        state=2;
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragments.get(2)).commit();*/

    }

    public void disMissWaiting() {
        if (kProgressHUD != null) {
            kProgressHUD.dismiss();
        }
    }

    @Override
    public void onSetPass(RegisterRequest request) {
        registerRequest=request;
        pass = request.getPass();
        phone = request.getUserName();
        if (hasNet) {
            presenterSetPass.checkValidation(request);
        } else {
            Toast toast = Toast.makeText(this, "لطفا پس از اطمینان از اتصال به اینترنت مجددا اقدام فرمایید.", Toast.LENGTH_LONG);
            Utility.centrizeAndShow(toast);
        }
    }

    @Override
    public void onCancel() {
        prepareNewPage();
        state=1;
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragments.get(0)).commit();
    }

    @Override
    public void onTruePass(RegisterRequest request) {
        prepareNewPage();
        state=2;
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragments.get(1)).commit();

    }

    @Override
    public void onChangePhone() {
        prepareNewPage();
        state=1;
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragments.get(0)).commit();

    }

}
