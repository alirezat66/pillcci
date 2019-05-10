package greencode.ir.pillcci.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.kaopiz.kprogresshud.KProgressHUD;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.interfaces.ChangePassTwoInterface;
import greencode.ir.pillcci.presenters.ChangePassTwoPresenter;
import greencode.ir.pillcci.retrofit.reqobject.ChangePassStepTwoReq;
import greencode.ir.pillcci.retrofit.respObject.ChangePassStepTwoRes;
import greencode.ir.pillcci.utils.BaseActivity;
import greencode.ir.pillcci.utils.Constants;
import greencode.ir.pillcci.utils.Utility;

/**
 * Created by alireza on 5/11/18.
 */

public class ForgetPassTwoActivity extends BaseActivity implements ChangePassTwoInterface{



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
    String code;
    KProgressHUD kProgressHUD;
    ChangePassTwoPresenter presenter;
    String pureNumber;
    int pureCode;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass_two);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

           String phone = bundle.getString(Constants.PREF_USER_NAME);
            pureCode = bundle.getInt(Constants.PREF_USER_CODE);
            pureNumber = bundle.getString(Constants.PREF_USER_Phone);
            /*if(!phone.startsWith("+")) {
                phone = bundle.getString(Constants.PREF_USER_NAME).replaceFirst("00", "+");
            }*/


            if(phone.startsWith("00")){
                phone = phone.replaceFirst("00","+");
            }
            edtUser.setText(phone);
            code = bundle.getString(Constants.PREF_CODE);
            if (edtUser.getText().length() == 11) {
                btnGetCode.setEnabled(true);
                btnGetCode.setBackground(getResources().getDrawable(R.drawable.ripple_pink_round));
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
                    btnGetCode.setBackground(getResources().getDrawable(R.drawable.ripple_pink_round));

                } else {
                    btnGetCode.setEnabled(false);
                    btnGetCode.setBackground(getResources().getDrawable(R.drawable.ripple_pink_round));
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
    public String getNumber(){
        String user = edtUser.getText().toString().replace("+","00");
        return user;
    }
    @OnClick({R.id.btnGetCode, R.id.btnRegiser, R.id.btnLogin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnGetCode:
                Utility.hideKeyboard();
                showWaiting();
                if(edtCode.getText().toString().equals(code)) {
                    presenter.chechPass(new ChangePassStepTwoReq(code,getNumber()));
                }else {
                    disMissWaiting();
                    Toast toast = Toast.makeText(this, "کد وارد شده صحیح نیست.", Toast.LENGTH_SHORT);
                    Utility.centrizeAndShow(toast);
                    edtCode.setText("");
                }
                break;
            case R.id.btnRegiser:
                Intent registerIntent = new Intent(this, RegisterActivity.class);
                registerIntent.putExtra(Constants.PREF_CODE, pureCode);
                registerIntent.putExtra(Constants.PREF_USER_Phone, pureNumber);
                startActivity(registerIntent);
                finish();
                break;
            case R.id.btnLogin:
                Intent loginIntent = new Intent(this, LoginActivity.class);
                loginIntent.putExtra(Constants.PREF_CODE, pureCode);
                loginIntent.putExtra(Constants.PREF_USER_Phone, pureNumber);
                startActivity(loginIntent);
                finish();
                break;
        }
    }

    @Override
    public void onSuccess(ChangePassStepTwoRes response) {
        disMissWaiting();
        Intent intent = new Intent(this,ChangePassActivity.class);
        intent.putExtra(Constants.PREF_USER_NAME,getNumber());
        intent.putExtra(Constants.PREF_USER_ID,response.getUser_id());
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent forgetIntent = new Intent(this,ForgetPassOneActivity.class);
        forgetIntent.putExtra(Constants.PREF_USER_NAME,getNumber());
        startActivity(forgetIntent);
        finish();
    }

    @Override
    public void onError(String error) {
        disMissWaiting();
        Toast toast = Toast.makeText(this, error, Toast.LENGTH_LONG);
        Utility.centrizeAndShow(toast);
    }
}
