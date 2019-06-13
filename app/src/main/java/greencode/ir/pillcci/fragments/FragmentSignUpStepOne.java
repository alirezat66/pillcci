package greencode.ir.pillcci.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hbb20.CountryCodePicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.activities.LoginActivity;
import greencode.ir.pillcci.activities.RegisterActivity;
import greencode.ir.pillcci.objects.RegisterRequest;
import greencode.ir.pillcci.utils.Constants;
import greencode.ir.pillcci.utils.NumericKeyBoardTransformationMethod;
import greencode.ir.pillcci.utils.Utility;

/**
 * Created by alireza on 5/15/18.
 */

public class FragmentSignUpStepOne extends Fragment {
    onActionStepOne onAction;


    @BindView(R.id.edtCode)
    EditText edtCode;

    @BindView(R.id.btnRegiser)
    Button btnRegiser;


    RegisterRequest request;
    @BindView(R.id.cpCodePicker)
    CountryCodePicker cpCodePicker;
    @BindView(R.id.edtPhone)
    EditText edtPhone;
    @BindView(R.id.btn_back)
    Button btnBack;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_one, container, false);
        ButterKnife.bind(this, view);

        request = RegisterActivity.getRequest();
        edtPhone.setTransformationMethod(new NumericKeyBoardTransformationMethod());

        cpCodePicker.registerCarrierNumberEditText(edtPhone);
        cpCodePicker.setPhoneNumberValidityChangeListener(new CountryCodePicker.PhoneNumberValidityChangeListener() {
            @Override
            public void onValidityChanged(boolean isValidNumber) {
                if (isValidNumber) {
                    edtPhone.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_green, 0);

                } else {
                    edtPhone.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                }
                // your code
            }
        });
        // cpCodePicker.setcountry(request.getCodePhone());
        cpCodePicker.setFullNumber(request.getCodePhone() + request.getPhone());
        edtPhone.setText(request.getPhone());
        edtCode.setText(request.getMoarefCode() == null ? "" : request.getMoarefCode());
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),LoginActivity.class);

                intent.putExtra(Constants.PREF_CODE, cpCodePicker.getSelectedCountryCodeAsInt());
                intent.putExtra(Constants.PREF_USER_Phone, edtPhone.getText().toString());
                startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAction = (onActionStepOne) context;

    }


    @OnClick(R.id.btnRegiser)
    public void onClick() {
        Utility.hideKeyboard();
        String phone = cpCodePicker.getFullNumberWithPlus();
        phone = phone.replace("+", "00");
        request.setUserName(phone);
        request.setMoarefCode(edtCode.getText().toString());
        if (cpCodePicker.isValidFullNumber()) {
            onAction.onRegisterButton(request);
        } else {

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public interface onActionStepOne {
        public void onRegisterButton(RegisterRequest request);

        public void onInvide();

        public void onEmpty();
    }
}
