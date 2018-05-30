package greencode.ir.pillcci.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import greencode.ir.pillcci.R;

/**
 * Created by alireza on 5/18/18.
 */

public class ChosePhotoTakerDialog extends BottomSheetDialog {

    PhotoChoserInterface myInterface;
    Context context;
    LinearLayout lyGallery,lyCamera;
    public ChosePhotoTakerDialog(Context context){
        super(context);
        this.context = context;
    }
    public void setListener(PhotoChoserInterface listener) {
        this.myInterface = listener;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        View view = View.inflate(context, R.layout.dialog_photo_selector, null);

        setContentView(view);
        setCancelable(false);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = -1;
        getWindow().setAttributes(params);


        lyCamera = findViewById(R.id.lyCamera);
        lyGallery = findViewById(R.id.lyGallery);


        lyCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterface.onSuccess(1);
            }
        });
        lyGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myInterface.onSuccess(2);
            }
        });
    }

    @Override
    public void onBackPressed() {
        myInterface.onRejected();
        super.onBackPressed();
    }
}
