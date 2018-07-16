package greencode.ir.pillcci.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.activities.RegisterActivity;
import greencode.ir.pillcci.objects.RegisterRequest;
import greencode.ir.pillcci.utils.Utility;

/**
 * Created by alireza on 5/15/18.
 */

public class FragmentSignUpStepSetPass extends Fragment {
    onActionStepTwo onAction;
    @BindView(R.id.imgLogi)
    CircleImageView imgLogi;
    @BindView(R.id.txtPilchi)
    TextView txtPilchi;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.txtSubTitle)
    TextView txtSubTitle;
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
        edtUser.setText(request.getUserName());
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
                request.setUserName(edtUser.getText().toString());
                request.setPass(edtPass.getText().toString());
                request.setRetryPass(edtPassRetry.getText().toString());
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
