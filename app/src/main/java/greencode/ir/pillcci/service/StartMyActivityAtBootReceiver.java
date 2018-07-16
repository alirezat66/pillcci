package greencode.ir.pillcci.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.List;

import greencode.ir.pillcci.database.PillUsage;
import greencode.ir.pillcci.utils.DatabaseManager;
import greencode.ir.pillcci.utils.Utility;

/**
 * Created by alireza on 5/29/18.
 */

public class StartMyActivityAtBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            List<PillUsage> expendedUsage = DatabaseManager.getAllExpendedPillUsage(context);
            DatabaseManager.updateToExpendedMode(context,expendedUsage);
            Utility.reCalculateManager(context);
        }
    }
}
