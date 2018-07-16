package greencode.ir.pillcci.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.interfaces.ChangePassTwoInterface;
import greencode.ir.pillcci.objects.ChangePassStepTwoReq;
import greencode.ir.pillcci.objects.ChangePassStepTwoRes;
import greencode.ir.pillcci.presenters.ChangePassTwoPresenter;
import greencode.ir.pillcci.utils.BaseActivity;
import greencode.ir.pillcci.utils.Constants;
import greencode.ir.pillcci.utils.Utility;

/**
 * Created by alireza on 5/11/18.
 */

public class ForgetPassTwoActivity extends BaseActivity implements ChangePassTwoInterface{
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
    @BindView(R.id.btnGetCode)
    Button btnGetCode;
    @BindView(R.id.lyOne)
    LinearLayout lyOne;
    @BindView(R.id.btnRegiser)
    Button btnRegiser;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.edtCode)
    EditText edtCode;
    KProgressHUD kProgressHUD;
    ChangePassTwoPresenter presenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass_two);
        ButterKnife.bind(this);
        txtTitle.setText("بازیابی کلمه عبور");
        txtSubTitle.setText("کد بازیابی به شماره تلفن همراهتان ارسال می شود");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            edtUser.setText(bundle.getString(Constants.PREF_USER_NAME));
            if (edtUser.getText().length() == 11) {
                btnGetCode.setEnabled(true);
                btnGetCode.setBackground(getResources().getDrawable(R.drawable.ripple_blue));
            }
        }
        edtCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edtCode.getText().length() == 6) {
                    btnGetCode.setEnabled(true);
                    btnGetCode.setBackground(getResources().getDrawable(R.drawable.ripple_blue));

                } else {
                    btnGetCode.setEnabled(false);
                    btnGetCode.setBackground(getResources().getDrawable(R.drawable.ripple_gray));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        presenter =  new ChangePassTwoPresenter(this);
    }
    public void showWaiting(){
        kProgressHUD=  KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("لطفا منتظر بمانید")
                .setDetailsLabel("در حال بررسی کد ورودی")
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
    @OnClick({R.id.btnGetCode, R.id.btnRegiser, R.id.btnLogin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnGetCode:
                Utility.hideKeyboard();
                showWaiting();
                presenter.chechPass(new ChangePassStepTwoReq(1,"123456",edtUser.getText().toString()));
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
    public void onSuccess(ChangePassStepTwoRes response) {
        disMissWaiting();
        Intent intent = new Intent(this,ChangePassActivity.class);
        intent.putExtra(Constants.PREF_USER_NAME,edtUser.getText().toString());
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent forgetIntent = new Intent(this,ForgetPassOneActivity.class);
        forgetIntent.putExtra(Constants.PREF_USER_NAME,edtUser.getText().toString());
        startActivity(forgetIntent);
        finish();
    }

    @Override
    public void onError(String error) {
        disMissWaiting();
    }
}
