package greencode.ir.pillcci.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.interfaces.RegisterStepOneInterface;
import greencode.ir.pillcci.presenters.PresenterRegisterStepOne;
import greencode.ir.pillcci.utils.BaseActivity;
import greencode.ir.pillcci.utils.Constants;
import greencode.ir.pillcci.utils.NetworkStateReceiver;
import greencode.ir.pillcci.utils.Utility;

/**
 * Created by alireza on 5/11/18.
 */

public class RegisterActivity extends BaseActivity implements NetworkStateReceiver.NetworkStateReceiverListener,RegisterStepOneInterface {
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
    @BindView(R.id.edtCode)
    EditText edtCode;
    @BindView(R.id.lyOne)
    LinearLayout lyOne;
    @BindView(R.id.btnRegiser)
    Button btnRegiser;
    @BindView(R.id.root)
    ConstraintLayout root;
    @BindView(R.id.netError)
    TextView netError;

    private NetworkStateReceiver networkStateReceiver;

    PresenterRegisterStepOne presenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        txtTitle.setText("ایجاد حساب کاربری");
        txtSubTitle.setText("ایحاد حساب کاربری رایگان است.");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            edtUser.setText(bundle.getString(Constants.PREF_USER_NAME));
            if(bundle.containsKey(Constants.PREF_MOAREF_CODE)){
                edtCode.setText(bundle.getString(Constants.PREF_MOAREF_CODE));
            }

        }
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        presenter = new PresenterRegisterStepOne(this);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(Constants.PREF_USER_NAME, edtUser.getText().toString());
        startActivity(intent);
        finish();
    }

    @Override
    public void networkAvailable() {
        netError.setVisibility(View.GONE);

    }

    @Override
    public void networkUnavailable() {
        netError.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btnRegiser)
    public void onClick() {
        Utility.hideKeyboard();

        presenter.checkUser(edtUser.getText().toString());
    }

    @Override
    public void onInvalid() {
        showError("نام کاربری صحیح نمی باشد.");
    }

    @Override
    public void onValid() {
        hiddenError();
        Intent intent = new Intent(this,SetPassActivity.class);
        intent.putExtra(Constants.PREF_USER_NAME,edtUser.getText().toString());
        intent.putExtra(Constants.PREF_MOAREF_CODE,edtCode.getText().toString());
        startActivity(intent);
        finish();
    }

    @Override
    public void onEmptyUser() {
        showError("نام کاربری را وارد کنید.");
    }
    public void showError(String str){
        error.setVisibility(View.VISIBLE);
        error.setText(str);
    }
    public void hiddenError(){
        error.setVisibility(View.INVISIBLE);
    }
}
