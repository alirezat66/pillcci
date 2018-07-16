package greencode.ir.pillcci.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nirigo.mobile.view.passcode.PasscodeIndicator;
import com.nirigo.mobile.view.passcode.PasscodeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.adapter.KeyAdapter;
import greencode.ir.pillcci.utils.BaseActivity;
import greencode.ir.pillcci.utils.Constants;

/**
 * Created by alireza on 5/11/18.
 */

public class ActivityValidateCode extends BaseActivity {


    @BindView(R.id.passcode_view)
    PasscodeView passcodeView;
    KeyAdapter iosPasscodeAdapter;
    @BindView(R.id.txtUser)
    TextView txtUser;

    String pass;
    @BindView(R.id.lyOne)
    LinearLayout lyOne;
    @BindView(R.id.remindTime)
    TextView remindTime;
    @BindView(R.id.btnChangePhone)
    Button btnChangePhone;
    @BindView(R.id.btnSendAgain)
    Button btnSendAgain;
    @BindView(R.id.lyTimer)
    LinearLayout lyTimer;
    private StringBuilder yourCurrentPasscode = new StringBuilder();
    @BindView(R.id.passcode_indicator)
    PasscodeIndicator passcodeIndicator;
    String code="12345";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_code);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            txtUser.setText(bundle.getString(Constants.PREF_USER_NAME));
            code = bundle.getString(Constants.PREF_CODE);
        }
        iosPasscodeAdapter = new KeyAdapter(this);

        passcodeView.setAdapter(iosPasscodeAdapter);
        startCountDown();

        passcodeView.setOnItemClickListener(new PasscodeView.OnItemClickListener() {
            public void onItemClick(PasscodeView view, int position, View item, final Object o) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("checking","click");
                        if (!passcodeIndicator.isAnimationInProgress()) {
                            Log.d("checking",o.toString()+" presed");

                            if (o.toString().equals("<")) {
                                Log.d("checking","deleted");

                                if (yourCurrentPasscode.length() != 0) {
                                    yourCurrentPasscode.deleteCharAt(yourCurrentPasscode.length() - 1);
                                    passcodeIndicator.setIndicatorLevel(yourCurrentPasscode.length());

                                }
                            } else {
                                Log.d("checking","new char");
                                yourCurrentPasscode.append(o.toString());
                                passcodeIndicator.setIndicatorLevel(yourCurrentPasscode.length());

                                if (yourCurrentPasscode.length() == passcodeIndicator.getIndicatorLength()) {
                                    if(yourCurrentPasscode.toString().equals(code)){
                                        Toast.makeText(ActivityValidateCode.this, "true", Toast.LENGTH_LONG).show();
                                    }else {
                                        yourCurrentPasscode = new StringBuilder();
                                        passcodeIndicator.wrongPasscode();
                                    }
                                }
                            }


                        }
                    }
                });



            }
        });

        

    }

    private void startCountDown() {
        Log.d("checking","instartcountdown");
        btnSendAgain.setVisibility(View.GONE);
        lyTimer.setVisibility(View.VISIBLE);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new CountDownTimer(60000, 1000) {

                    public void onTick(long millisUntilFinished) {

                        remindTime.setText("" + millisUntilFinished / 1000);
                        //here you can have your logic to set text to edittext
                    }

                    public void onFinish() {
                        lyTimer.setVisibility(View.GONE);
                        btnSendAgain.setVisibility(View.VISIBLE);
                    }

                }.start();
            }
        });

    }

    @OnClick({R.id.btnSendAgain, R.id.btnChangePhone})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSendAgain:
                startCountDown();
                break;
            case R.id.btnChangePhone:
                Intent intent = new Intent(this,RegisterActivity.class);
                intent.putExtra(Constants.PREF_USER_NAME,txtUser.getText().toString());
                startActivity(intent);
                finish();
                break;
        }
    }
}
