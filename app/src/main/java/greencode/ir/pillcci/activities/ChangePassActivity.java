package greencode.ir.pillcci.activities;

import android.content.Intent;
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
import greencode.ir.pillcci.interfaces.ChangePassIterface;
import greencode.ir.pillcci.retrofit.reqobject.ChangePassReq;
import greencode.ir.pillcci.retrofit.respObject.ChangePassRes;
import greencode.ir.pillcci.presenters.ChangePassPresenter;
import greencode.ir.pillcci.utils.BaseActivity;
import greencode.ir.pillcci.utils.Constants;
import greencode.ir.pillcci.utils.Utility;

/**
 * Created by alireza on 5/12/18.
 */

public class ChangePassActivity extends BaseActivity implements ChangePassIterface {
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
    @BindView(R.id.edtPassRetry)
    EditText edtPassRetry;
    @BindView(R.id.btnSetPass)
    Button btnSetPass;
    @BindView(R.id.lyOne)
    LinearLayout lyOne;
    @BindView(R.id.btnRegiser)
    Button btnRegiser;
    @BindView(R.id.btnLogin)
    Button btnLogin;

    String userId;
    KProgressHUD kProgressHUD;
    ChangePassPresenter presenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass_three);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            edtUser.setText(bundle.getString(Constants.PREF_USER_NAME));
            userId = bundle.getString(Constants.PREF_USER_ID);
        }
        presenter = new ChangePassPresenter(this);
    }

    public void showWaiting(){
        kProgressHUD=  KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("لطفا منتظر بمانید")
                .setDetailsLabel("در حال ثبت اطلاعات کاربر")
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
    public void showError(String str){
        error.setVisibility(View.VISIBLE);
        error.setText(str);
    }


    @OnClick({R.id.btnSetPass, R.id.btnRegiser, R.id.btnLogin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSetPass:
                Utility.hideKeyboard();
                presenter.checkValidation(edtPass.getText().toString(),edtPassRetry.getText().toString(),userId);
                break;
            case R.id.btnRegiser:
                Intent registerIntent = new Intent(this, RegisterActivity.class);
                registerIntent.putExtra(Constants.PREF_USER_NAME, edtUser.getText().toString());
                startActivity(registerIntent);
                finish();
                break;
            case R.id.btnLogin:
                Intent loginIntent = new Intent(this, LoginActivity.class);
                loginIntent.putExtra(Constants.PREF_USER_NAME, edtUser.getText().toString());
                startActivity(loginIntent);
                finish();
                break;
        }
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
    public void onValid(ChangePassReq registerRequest) {
        showWaiting();
        presenter.changePass(registerRequest);
    }

    @Override
    public void onErrorRegister(String error) {
        disMissWaiting();
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSuccessRegister(ChangePassRes changePassRes) {
        disMissWaiting();
        Toast.makeText(this, "کلمه عبور با موفقیت تغییر کرد.", Toast.LENGTH_LONG).show();
        Intent loginIntent = new Intent(this, LoginActivity.class);
        loginIntent.putExtra(Constants.PREF_USER_NAME, edtUser.getText().toString());
        startActivity(loginIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent loginIntent = new Intent(this, LoginActivity.class);
        loginIntent.putExtra(Constants.PREF_USER_NAME, edtUser.getText().toString());
        startActivity(loginIntent);
        finish();

    }
}
