package greencode.ir.pillcci.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
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
import greencode.ir.pillcci.objects.RegisterRequest;
import greencode.ir.pillcci.presenters.SetPassPresenter;
import greencode.ir.pillcci.utils.BaseActivity;
import greencode.ir.pillcci.utils.Constants;
import greencode.ir.pillcci.utils.NetworkStateReceiver;
import greencode.ir.pillcci.utils.Utility;

/**
 * Created by alireza on 5/11/18.
 */

public class SetPassActivity extends BaseActivity implements NetworkStateReceiver.NetworkStateReceiverListener {


    @BindView(R.id.netError)
    TextView netError;
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
    @BindView(R.id.btnRegiser)
    Button btnRegiser;
    @BindView(R.id.lyOne)
    LinearLayout lyOne;
    @BindView(R.id.btnChangePhone)
    Button btnChangePhone;
    @BindView(R.id.root)
    ConstraintLayout root;
    SetPassPresenter presenter;
    String moarefCode = "";
    boolean hasNet = true;
    KProgressHUD kProgressHUD;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pass);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            edtUser.setText(bundle.getString(Constants.PREF_USER_NAME));
            moarefCode = bundle.getString(Constants.PREF_MOAREF_CODE);
        }
    }


    @Override
    public void networkAvailable() {
        hasNet = true;
        netError.setVisibility(View.VISIBLE);
    }

    @Override
    public void networkUnavailable() {
        hasNet = false;
        netError.setVisibility(View.GONE);
    }

    @OnClick({R.id.btnRegiser, R.id.btnChangePhone})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRegiser:
                Utility.hideKeyboard();
                if (hasNet) {

                    presenter.checkValidation(new RegisterRequest(edtUser.getText().toString(), edtPass.getText().toString(), edtPassRetry.getText().toString(), moarefCode));
                } else {
                    Toast.makeText(this, "لطفا دسترسی خود به اینترنت را چک کنید.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btnChangePhone:
                Intent intent = new Intent(this, RegisterActivity.class);
                intent.putExtra(Constants.PREF_USER_NAME, edtUser.getText().toString());
                intent.putExtra(Constants.PREF_MOAREF_CODE, moarefCode);
                startActivity(intent);
                finish();
                break;
        }
    }



}
