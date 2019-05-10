package greencode.ir.pillcci.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import greencode.ir.pillcci.utils.Constants;

public class BroadCastRestart extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(BroadCastRestart.class.getSimpleName(), "Service Stops! Oooooooooooooppppssssss!!!!");
        Intent syncIntent = new Intent(context, SyncService.class);
        syncIntent.setAction(Constants.ACTION_START_FOREGROUND_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startService(syncIntent);
            Log.d("restart","yes we calculate again and syncservice");

        }else {
            context.startService(syncIntent);
            Log.d("restart","yes we calculate again and syncservice");
        }
    }
}
