package greencode.ir.pillcci.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import greencode.ir.pillcci.utils.Constants;
import greencode.ir.pillcci.utils.Utility;

/**
 * Created by alireza on 5/29/18.
 */

public class StartMyActivityAtBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("restart","we are in broadcast");
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.d("restart","yes we are in if");


            Log.d("restart","yes we update all expected in if");

            Utility.reCalculateManager(context);
            Log.d("restart","yes we calculate again");
            Intent syncIntent = new Intent(context, SyncService.class);
            syncIntent.setAction(Constants.ACTION_START_FOREGROUND_SERVICE);
            SyncService.enqueueWork(context, new Intent());

          /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
              //  context.startService(syncIntent);
                Log.d("restart","yes we calculate again and syncservice");

            }else {
                context.startService(syncIntent);
                Log.d("restart","yes we calculate again and syncservice");
            }*/

        }
    }
}
