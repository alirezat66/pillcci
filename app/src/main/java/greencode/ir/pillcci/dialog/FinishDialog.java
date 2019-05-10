package greencode.ir.pillcci.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import greencode.ir.pillcci.R;

/**
 * Created by alireza on 5/18/18.
 */

public class FinishDialog extends Dialog {

    FinishListener myInterface;
    Context context;
    @BindView(R.id.yesBtn)
    TextView yesBtn;
    @BindView(R.id.noBtn)
    TextView noBtn;
    String title;
    String desc;
    @BindView(R.id.header)
    TextView header;
    @BindView(R.id.txtDesc)
    TextView txtDesc;

    public FinishDialog(Context context, String title, String desc) {
        super(context);
        this.context = context;
        this.title = title;
        this.desc = desc;
    }

    public void setListener(FinishListener listener) {
        this.myInterface = listener;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        View view = View.inflate(context, R.layout.dialog_yes_no_for_finish, null);
        ButterKnife.bind(this, view);
        setContentView(view);
        setCancelable(false);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = -1;
        getWindow().setAttributes(params);

        if(this.desc.equals("")){
            txtDesc.setVisibility(View.GONE);
        }else {
            txtDesc.setText(this.desc);
            txtDesc.setVisibility(View.VISIBLE);
        }

        header.setText(this.title);

    }


    @Override
    public void onBackPressed() {
        myInterface.onReject();
        super.onBackPressed();
    }

    @OnClick({R.id.yesBtn, R.id.noBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.yesBtn:
                myInterface.onSuccess();
                break;
            case R.id.noBtn:
                myInterface.onReject();
                break;
        }
    }
}
