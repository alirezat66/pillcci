package greencode.ir.pillcci.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.activities.RegisterActivity;
import greencode.ir.pillcci.objects.RegisterRequest;
import greencode.ir.pillcci.utils.Utility;

/**
 * Created by alireza on 5/15/18.
 */

public class FragmentSignUpStepSetPass extends Fragment {
    onActionStepTwo onAction;


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

    public RegisterRequest request;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_two, container, false);
        ButterKnife.bind(this, view);
        request = RegisterActivity.getRequest();

        String phone = request.getUserName();
        if(phone.startsWith("00")){
           phone =  phone.replaceFirst("00","+");
        }

        edtUser.setText(phone);
        edtPass.setTypeface(Utility.getRegularTypeFaceWithOutNumber(getContext()));
        edtPassRetry.setTypeface(Utility.getRegularTypeFaceWithOutNumber(getContext()));
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAction = (onActionStepTwo) context;

    }






    @OnClick({R.id.btnRegiser, R.id.btnChangePhone})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRegiser:
                Utility.hideKeyboard();
                String user = edtUser.getText().toString().replace("+","00");
                request.setUserName(user);
                request.setPass(edtPass.getText().toString());
                request.setRetryPass(edtPassRetry.getText().toString());


                request.setPhone(user);
                onAction.onSetPass(request);

                break;
            case R.id.btnChangePhone:
                onAction.onCancel();
                break;
        }
    }


    public interface onActionStepTwo {
        public void onSetPass(RegisterRequest request);

        public void onCancel();
    }
}
