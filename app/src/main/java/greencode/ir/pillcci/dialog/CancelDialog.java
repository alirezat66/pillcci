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
import greencode.ir.pillcci.database.PillObject;

/**
 * Created by alireza on 5/18/18.
 */

public class CancelDialog extends Dialog {

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
    PillObject pillObject;
    public CancelDialog(Context context, PillObject object) {
        super(context);
        this.context = context;
        this.pillObject=object;

    }

    public void setListener(CancelListener listener) {
        this.myInterface = listener;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        View view = View.inflate(context, R.layout.dialog_cancel_pill, null);
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
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterface.onReject();
            }
        });

        if(pillObject.getUseType()==1){
            deleteAndAdd.setVisibility(View.GONE);
        }

    }


    @Override
    public void onBackPressed() {
        myInterface.onReject();
        super.onBackPressed();
    }
}
