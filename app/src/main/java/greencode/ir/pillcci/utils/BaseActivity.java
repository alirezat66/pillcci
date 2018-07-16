package greencode.ir.pillcci.utils;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import greencode.ir.pillcci.controler.AppController;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


/**
 * Created by alireza on 2/1/18.
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppController.setActivityContext(this,this);

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        AppController.setActivityContext(this,this);
        super.onResume();
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}


