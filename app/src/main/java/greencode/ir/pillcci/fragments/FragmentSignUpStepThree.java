package greencode.ir.pillcci.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nirigo.mobile.view.passcode.PasscodeIndicator;
import com.nirigo.mobile.view.passcode.PasscodeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.activities.RegisterActivity;
import greencode.ir.pillcci.adapter.KeyAdapter;
import greencode.ir.pillcci.interfaces.ValidationInterface;
import greencode.ir.pillcci.objects.RegisterRequest;
import greencode.ir.pillcci.presenters.ValidationPresenter;
import greencode.ir.pillcci.utils.Utility;

/**
 * Created by alireza on 5/15/18.
 */

public class FragmentSignUpStepThree extends Fragment implements ValidationInterface {
    onActionStepThree onAction;
    public RegisterRequest request;
    @BindView(R.id.txtUser)
    TextView txtUser;
    @BindView(R.id.passcode_indicator)
    PasscodeIndicator passcodeIndicator;
    @BindView(R.id.lyOne)
    LinearLayout lyOne;
    @BindView(R.id.passcode_view)
    PasscodeView passcodeView;
    @BindView(R.id.btnSendAgain)
    Button btnSendAgain;
    @BindView(R.id.remindTime)
    TextView remindTime;
    @BindView(R.id.lyTimer)
    LinearLayout lyTimer;
    @BindView(R.id.btnChangePhone)
    Button btnChangePhone;
    KeyAdapter iosPasscodeAdapter;
    private StringBuilder yourCurrentPasscode = new StringBuilder();
    ValidationPresenter presenter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_three, container, false);
        ButterKnife.bind(this, view);

        presenter = new ValidationPresenter(this);

        request = RegisterActivity.getRequest();
        String phone = request.getUserName();
        if(phone.startsWith("00")){
          phone =  phone.replaceFirst("00","+");
        }
        txtUser.setText(phone);
        iosPasscodeAdapter = new KeyAdapter(getContext());
        passcodeView.setAdapter(iosPasscodeAdapter);
        startCountDown();

        passcodeView.setOnItemClickListener(new PasscodeView.OnItemClickListener() {
            public void onItemClick(PasscodeView view, int position, View item, final Object o) {
                getActivity().runOnUiThread(new Runnable() {
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
                                    if(yourCurrentPasscode.toString().equals(request.getCode())){
                                        onAction.onTruePass(request);
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
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAction = (onActionStepThree) context;

    }





    @OnClick({R.id.btnSendAgain, R.id.btnChangePhone})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSendAgain:
                presenter.resend(request.getUserName(),request.getMoarefCode());
                break;
            case R.id.btnChangePhone:
                onAction.onChangePhone();
                break;
        }
    }

    private void startCountDown() {
        Log.d("checking","instartcountdown");
        btnSendAgain.setVisibility(View.GONE);
        lyTimer.setVisibility(View.VISIBLE);
        getActivity().runOnUiThread(new Runnable() {
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

    @Override
    public void onCodeReady(String validCode) {
        request.setCode(validCode);
        startCountDown();
    }

    @Override
    public void onError(String message) {
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        Utility.centrizeAndShow(toast);
    }

    public interface onActionStepThree {
        public void onTruePass(RegisterRequest request);

        public void onChangePhone();
    }
}
