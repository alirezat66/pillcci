package greencode.ir.pillcci.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import greencode.ir.pillcci.R;
import greencode.ir.pillcci.database.PillObject;

/**
 * Created by alireza on 5/18/18.
 */

public class CancelEditDialog extends Dialog {

    CancelListener myInterface;
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
    @BindView(R.id.logoUse)
    AppCompatImageView logoUse;
    @BindView(R.id.useLay)
    RelativeLayout useLay;

    PillObject pillObject;

    public CancelEditDialog(Context context, PillObject pillObject) {
        super(context);
        this.context = context;
        this.pillObject=pillObject;

    }

    public void setListener(CancelListener listener) {
        this.myInterface = listener;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        View view = View.inflate(context, R.layout.dialog_cancel_edit_pill, null);
        ButterKnife.bind(this, view);
        setContentView(view);
        setCancelable(false);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = -1;
        getWindow().setAttributes(params);

        deleteAllLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterface.onSuccess(1);
            }
        });
        deleteAndAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterface.onSuccess(2);
            }
        });
        useLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterface.onSuccess(3);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterface.onReject();
            }
        });
        if(pillObject.getUseType()==1){
            deleteAndAdd.setVisibility(View.GONE);
        }else {
            deleteAndAdd.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onBackPressed() {
        myInterface.onReject();
        super.onBackPressed();
    }
}
