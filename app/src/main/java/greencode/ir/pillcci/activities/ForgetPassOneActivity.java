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
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.interfaces.ChangePassOneInterface;
import greencode.ir.pillcci.objects.ChangePassStepOneReq;
import greencode.ir.pillcci.objects.ChangePassStepOneRes;
import greencode.ir.pillcci.presenters.ChangePassOnePresenter;
import greencode.ir.pillcci.utils.BaseActivity;
import greencode.ir.pillcci.utils.Constants;
import greencode.ir.pillcci.utils.Utility;

/**
 * Created by alireza on 5/11/18.
 */

public class ForgetPassOneActivity extends BaseActivity implements ChangePassOneInterface{
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

    ChangePassOnePresenter presenter;
    KProgressHUD kProgressHUD;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass_one);
        ButterKnife.bind(this);
        presenter =new ChangePassOnePresenter(this);
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
        edtUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edtUser.getText().length() == 11) {
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

    @OnClick({R.id.btnGetCode, R.id.btnRegiser, R.id.btnLogin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnGetCode:
                Utility.hideKeyboard();
                showWaiting();
                presenter.sendPassChangeReq(new ChangePassStepOneReq(edtUser.getText().toString()));
                break;
            case R.id.btnRegiser:
                Intent registerIntent = new Intent(this,RegisterActivity.class);
                registerIntent.putExtra(Constants.PREF_USER_NAME,edtUser.getText().toString());
                startActivity(registerIntent);
                finish();
                break;
            case R.id.btnLogin:
                Intent loginIntent = new Intent(this,LoginActivity.class);
                loginIntent.putExtra(Constants.PREF_USER_NAME,edtUser.getText().toString());
                startActivity(loginIntent);
                finish();
                break;
        }
    }

    @Override
    public void onSuccess(ChangePassStepOneRes response) {
        disMissWaiting();
        Intent forgetPassTwoIntent = new Intent(this,ForgetPassTwoActivity.class);
        forgetPassTwoIntent.putExtra(Constants.PREF_USER_NAME,edtUser.getText().toString());
        startActivity(forgetPassTwoIntent);
        finish();
    }

    @Override
    public void onError(String error) {
        disMissWaiting();
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();

    }



}
