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

public class FragmentSignUpStepOne extends Fragment {
    onActionStepOne onAction;

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
    @BindView(R.id.edtCode)
    EditText edtCode;
    @BindView(R.id.lyOne)
    LinearLayout lyOne;
    @BindView(R.id.btnRegiser)
    Button btnRegiser;


    RegisterRequest request;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_one, container, false);
        ButterKnife.bind(this, view);
        txtTitle.setText("ایجاد حساب کاربری");
        txtSubTitle.setText("ایحاد حساب کاربری رایگان است.");
        request = RegisterActivity.getRequest();
        edtUser.requestFocus();
        edtUser.setText(request.getUserName()==null?"":request.getUserName());
        edtCode.setText(request.getMoarefCode()==null?"":request.getMoarefCode());
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAction = (FragmentSignUpStepOne.onActionStepOne) context;

    }


    @OnClick(R.id.btnRegiser)
    public void onClick() {
        Utility.hideKeyboard();
        request.setUserName(edtUser.getText().toString());
        request.setMoarefCode(edtCode.getText().toString());
        onAction.onRegisterButton(request);
    }

    public interface onActionStepOne {
        public void onRegisterButton(RegisterRequest request);
    }
}
