package greencode.ir.pillcci.activities;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.interfaces.LoginInterface;
import greencode.ir.pillcci.objects.LoginReq;
import greencode.ir.pillcci.objects.LoginResponse;
import greencode.ir.pillcci.presenters.LoginPresenter;
import greencode.ir.pillcci.utils.BaseActivity;
import greencode.ir.pillcci.utils.Constants;
import greencode.ir.pillcci.utils.PreferencesData;
import greencode.ir.pillcci.utils.Utility;

/**
 * Created by alireza on 5/11/18.
 */

public class LoginActivity extends BaseActivity implements LoginInterface{
    @BindView(R.id.imgLogi)
    CircleImageView imgLogi;
    @BindView(R.id.txtPilchi)
    TextView txtPilchi;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.txtSubTitle)
    TextView txtSubTitle;
    @BindView(R.id.error)
    TextView error;
    @BindView(R.id.edtUser)
    EditText edtUser;
    @BindView(R.id.edtPass)
    EditText edtPass;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.forgetPass)
    TextView forgetPass;
    @BindView(R.id.lyOne)
    LinearLayout lyOne;
    @BindView(R.id.btnRegiser)
    Button btnRegiser;
    KProgressHUD kProgressHUD;
    LoginPresenter presenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Bundle bundle = getIntent().getExtras();

        ButterKnife.bind(this);
        PreferencesData.saveBool(Constants.PREF_FIRST,true);

        if(bundle!=null){
            edtUser.setText(bundle.getString(Constants.PREF_USER_NAME));
        }
        forgetPass.setPaintFlags(forgetPass.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        presenter  = new LoginPresenter(this);

    }

    @OnClick({R.id.btnLogin, R.id.forgetPass, R.id.btnRegiser})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                Utility.hideKeyboard();
                  LoginReq req = new LoginReq(edtUser.getText().toString(),edtPass.getText().toString());
                presenter.checkValidation(req);
                break;
            case R.id.forgetPass:
                Intent forgetIntent = new Intent(this,ForgetPassOneActivity.class);
                forgetIntent.putExtra(Constants.PREF_USER_NAME,edtUser.getText().toString());
                startActivity(forgetIntent);
                finish();
                break;
            case R.id.btnRegiser:
                Intent intent = new Intent(this,RegisterActivity.class);
                intent.putExtra(Constants.PREF_USER_NAME,edtUser.getText().toString());
                startActivity(intent);
                finish();
                break;
        }
    }
    public void showError(String str){
        error.setVisibility(View.VISIBLE);
        error.setText(str);
    }
    public void hiddenError(){
        error.setVisibility(View.INVISIBLE);
    }
    @Override
    public void onErrorLogin(String error) {
        disMissWaiting();
        showError("نام کاربری یا کلمه عبور وارد شده صحیح نیست");

    }

    @Override
    public void onSuccessLogin(LoginResponse resp) {
        disMissWaiting();
        hiddenError();
        PreferencesData.saveBool(Constants.PREF_LOGIN,true);

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void onValidUserPass(LoginReq req) {
        showWaiting();
        hiddenError();
        presenter.tryToLogin(req);
    }

    @Override
    public void onPassEmpty() {
        showError("لطفا کلمه عبور خود را وارد کنید.");
    }

    @Override
    public void onNumberEmpty() {
        showError("لطفا نام کاربری خود را وارد کنید.");
    }

    @Override
    public void onNumberWrong() {
        showError("نام کاربری ۱۱ کارکتر می باشد.");
    }

    public void showWaiting(){
        kProgressHUD=  KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("لطفا منتظر بمانید")
                .setDetailsLabel("در حال بررسی صلاحیت ورود")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.8f)
                .setBackgroundColor(getResources().getColor(R.color.colorPrimary))
                .show();
    }
    public void disMissWaiting(){
        if(kProgressHUD!=null){
            kProgressHUD.dismiss();
        }
    }
}
