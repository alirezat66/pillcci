package greencode.ir.pillcci.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.fragments.FragmentSignUpStepOne;
import greencode.ir.pillcci.fragments.FragmentSignUpStepSetPass;
import greencode.ir.pillcci.fragments.FragmentSignUpStepThree;
import greencode.ir.pillcci.interfaces.RegisterStepOneInterface;
import greencode.ir.pillcci.interfaces.SetPassInterface;
import greencode.ir.pillcci.objects.RegisterRequest;
import greencode.ir.pillcci.objects.RegisterResponse;
import greencode.ir.pillcci.presenters.PresenterRegisterStepOne;
import greencode.ir.pillcci.presenters.SetPassPresenter;
import greencode.ir.pillcci.utils.BaseActivity;
import greencode.ir.pillcci.utils.Constants;
import greencode.ir.pillcci.utils.NetworkStateReceiver;

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
        if (bundle != null) {
            registerRequest.setUserName(bundle.getString(Constants.PREF_USER_NAME));
        }
        state=1;
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
        showError("نام کاربری صحیح نمی باشد.");
    }

    @Override
    public void onValid() {
        //fragment 1 voroode etelaat shomare tayid shod
        hiddenError();
        presenterSetPass = new SetPassPresenter(this);
        prepareNewPage();
        state=2;
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragments.get(1)).commit();
    }
    private void prepareNewPage(){
        error.setVisibility(View.GONE);
        netError.setVisibility(View.GONE);
    }

    @Override
    public void onEmptyUser() {
        showError("نام کاربری را وارد کنید.");
    }

    public void showError(String str) {
        error.setVisibility(View.VISIBLE);
        error.setText(str);


    }

    public void hiddenError() {
        error.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onRegisterButton(RegisterRequest request) {
        registerRequest = request;
        presenter.checkUser(request.getUserName());
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
        registerRequest=request;
        presenterSetPass.Register(registerRequest);
    }

    @Override
    public void onErrorRegister(String error) {
        disMissWaiting();
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSuccessRegister(RegisterResponse request) {
        disMissWaiting();
        prepareNewPage();
        registerRequest.setCode("12345");
        state=2;
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragments.get(2)).commit();

    }


    public void disMissWaiting() {
        if (kProgressHUD != null) {
            kProgressHUD.dismiss();
        }
    }

    @Override
    public void onSetPass(RegisterRequest request) {
        registerRequest=request;
        if (hasNet) {
            presenterSetPass.checkValidation(new RegisterRequest(request.getUserName(), request.getPass(), request.getRetryPass(), request.getMoarefCode()));
        } else {
            Toast.makeText(this, "لطفا پس از اطمینان از اتصال به اینترنت مجددا اقدام فرمایید.", Toast.LENGTH_LONG).show();
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
        Intent intent = new Intent(this,LoginActivity.class);
        intent.putExtra(Constants.PREF_USER_NAME,request.getUserName());
        startActivity(intent);
        finish();
    }

    @Override
    public void onChangePhone() {
        prepareNewPage();
        state=1;
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragments.get(0)).commit();

    }

}
