package greencode.ir.pillcci.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import greencode.ir.pillcci.R;

/**
 * Created by alireza on 5/18/18.
 */

public class YesNoDialog extends Dialog {

    YesNoListener myInterface;
    Context context;
    @BindView(R.id.header)
    TextView header;
    @BindView(R.id.logoDelete)
    ImageView logoDelete;
    @BindView(R.id.deleteAllLayout)
    RelativeLayout deleteAllLayout;
    @BindView(R.id.logoadd)
    ImageView logoadd;
    @BindView(R.id.deleteAndAdd)
    RelativeLayout deleteAndAdd;
    @BindView(R.id.backButton)
    ImageView backButton;

    public YesNoDialog(Context context) {
        super(context);
        this.context = context;

    }

    public void setListener(YesNoListener listener) {
        this.myInterface = listener;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        View view = View.inflate(context, R.layout.dialog_cancel_yes_no, null);
        ButterKnife.bind(this, view);
        setContentView(view);
        setCancelable(false);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = -1;
        getWindow().setAttributes(params);

        deleteAllLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterface.onSuccess();
            }
        });
        deleteAndAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterface.onReject();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterface.onReject();
            }
        });

    }


    @Override
    public void onBackPressed() {
        myInterface.onReject();
        super.onBackPressed();
    }
}
