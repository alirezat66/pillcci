package greencode.ir.pillcci.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;

import greencode.ir.pillcci.controler.AppDatabase;
import greencode.ir.pillcci.service.SyncService;
import greencode.ir.pillcci.utils.BaseActivity;
import greencode.ir.pillcci.utils.Constants;
import greencode.ir.pillcci.utils.Utility;

public class RecalculateActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppDatabase database = AppDatabase.getInMemoryDatabase(this);
        Utility.appendLog("we are in delay service");
        Utility.reCalculateManager(this);
        Intent serviceIntent = new Intent(this, SyncService.class);
        serviceIntent.setAction(Constants.ACTION_START_FOREGROUND_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startService(serviceIntent);
            Log.d("restart","yes we calculate again and syncservice");

        }else {
            startService(serviceIntent);
            Log.d("restart","yes we calculate again and syncservice");
        }
        finish();
    }
}
