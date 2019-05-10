package greencode.ir.pillcci.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hbb20.CountryCodePicker;
import com.kaopiz.kprogresshud.KProgressHUD;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.interfaces.ChangePassOneInterface;
import greencode.ir.pillcci.presenters.ChangePassOnePresenter;
import greencode.ir.pillcci.retrofit.reqobject.ChangePassStepOneReq;
import greencode.ir.pillcci.retrofit.respObject.ChangePassStepOneRes;
import greencode.ir.pillcci.utils.BaseActivity;
import greencode.ir.pillcci.utils.Constants;
import greencode.ir.pillcci.utils.NumericKeyBoardTransformationMethod;
import greencode.ir.pillcci.utils.Utility;

/**
 * Created by alireza on 5/11/18.
 */

public class ForgetPassOneActivity extends BaseActivity implements ChangePassOneInterface {


    @BindView(R.id.error)
    TextView error;

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
    @BindView(R.id.cpCodePicker)
    CountryCodePicker cpCodePicker;
    @BindView(R.id.edtPhone)
    EditText edtPhone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass_one);
        ButterKnife.bind(this);
        presenter = new ChangePassOnePresenter(this);

        Bundle bundle = getIntent().getExtras();


        edtPhone.setTransformationMethod(new NumericKeyBoardTransformationMethod());

        cpCodePicker.registerCarrierNumberEditText(edtPhone);
        cpCodePicker.setPhoneNumberValidityChangeListener(new CountryCodePicker.PhoneNumberValidityChangeListener() {
            @Override
            public void onValidityChanged(boolean isValidNumber) {
                if(isValidNumber){
                    edtPhone.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_green, 0);
                    btnGetCode.setEnabled(true);
                    btnGetCode.setBackground(getResources().getDrawable(R.drawable.ripple_pink_round));
                }else {
                    edtPhone.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    btnGetCode.setEnabled(false);
                    btnGetCode.setBackground(getResources().getDrawable(R.drawable.ripple_pink_round));
                }
                // your code
            }
        });
        if (bundle != null) {

            edtPhone.setText(bundle.getString(Constants.PREF_USER_Phone));
            cpCodePicker.setCountryForPhoneCode(bundle.getInt(Constants.PREF_CODE));
            if (cpCodePicker.isValidFullNumber()) {
                btnGetCode.setEnabled(true);
                btnGetCode.setBackground(getResources().getDrawable(R.drawable.ripple_pink_round));
            }
        }


    }


    public void showWaiting() {
        kProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("لطفا منتظر بمانید")
                .setDetailsLabel("در حال بازیابی کلمه عبور")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.8f)
                .setBackgroundColor(getResources().getColor(R.color.colorPrimary))
                .show();
    }

    public void disMissWaiting() {
        if (kProgressHUD != null) {
            kProgressHUD.dismiss();
        }
    }

    @OnClick({R.id.btnGetCode, R.id.btnRegiser, R.id.btnLogin})
    public void onClick(View view) {
        String phone = cpCodePicker.getFullNumberWithPlus();

        switch (view.getId()) {

            case R.id.btnGetCode:
                Utility.hideKeyboard();
                showWaiting();
                presenter.sendPassChangeReq(new ChangePassStepOneReq(phone));
                break;
            case R.id.btnRegiser:

                Intent registerIntent = new Intent(this, RegisterActivity.class);
                registerIntent.putExtra(Constants.PREF_CODE, cpCodePicker.getSelectedCountryCodeAsInt());
                registerIntent.putExtra(Constants.PREF_USER_Phone, edtPhone.getText().toString());
              //  registerIntent.putExtra(Constants.PREF_USER_NAME, phone);
                startActivity(registerIntent);
                finish();
                break;
            case R.id.btnLogin:
                Intent loginIntent = new Intent(this, LoginActivity.class);
                loginIntent.putExtra(Constants.PREF_CODE, cpCodePicker.getSelectedCountryCodeAsInt());
                loginIntent.putExtra(Constants.PREF_USER_Phone, edtPhone.getText().toString());
               // loginIntent.putExtra(Constants.PREF_USER_NAME,phone);
                startActivity(loginIntent);
                finish();
                break;

        }
    }

    @Override
    public void onSuccess(ChangePassStepOneRes response) {
        disMissWaiting();
        Intent forgetPassTwoIntent = new Intent(this, ForgetPassTwoActivity.class);
        String phone = cpCodePicker.getFullNumberWithPlus();
        forgetPassTwoIntent.putExtra(Constants.PREF_USER_CODE, cpCodePicker.getSelectedCountryCodeAsInt());
        forgetPassTwoIntent.putExtra(Constants.PREF_USER_Phone,edtPhone.getText().toString());
        forgetPassTwoIntent.putExtra(Constants.PREF_USER_NAME, phone);
        forgetPassTwoIntent.putExtra(Constants.PREF_CODE, response.getCode());
        startActivity(forgetPassTwoIntent);
        finish();
    }

    @Override
    public void onError(String error) {
        disMissWaiting();
        Intent forgetPassTwoIntent = new Intent(this, ForgetPassTwoActivity.class);
        String phone = cpCodePicker.getFullNumberWithPlus();
        forgetPassTwoIntent.putExtra(Constants.PREF_USER_CODE, cpCodePicker.getSelectedCountryCodeAsInt());
        forgetPassTwoIntent.putExtra(Constants.PREF_USER_Phone,edtPhone.getText().toString());
        forgetPassTwoIntent.putExtra(Constants.PREF_USER_NAME, phone);
        forgetPassTwoIntent.putExtra(Constants.PREF_CODE, "");
        startActivity(forgetPassTwoIntent);
        finish();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent loginIntent = new Intent(this, LoginActivity.class);
        String phone = cpCodePicker.getFullNumberWithPlus();

        loginIntent.putExtra(Constants.PREF_CODE, cpCodePicker.getSelectedCountryCodeAsInt());
        loginIntent.putExtra(Constants.PREF_USER_Phone,edtPhone.getText());
        startActivity(loginIntent);
        finish();
    }
}
