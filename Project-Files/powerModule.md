package edu.stanford.mystudyapp.screenomics.power;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;


import java.util.HashMap;


import edu.stanford.mystudyapp.screenomics.TextBasedEventData.EventOperationManager;
import edu.stanford.mystudyapp.screenomics.TextBasedEventData.HashMapPool;
import edu.stanford.mystudyapp.screenomics.modulemanager.ModuleCharacteristics;

public class ScreenOnOffStatusCapture extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if (Intent.ACTION_SCREEN_ON.equals(action)) {
            handleScreenOnEvent(context);
        } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
            logEvent(false, false, context);
        } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
            logEvent(true, false, context);
        }
    }

    private void handleScreenOnEvent(Context context) {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

        // Check if the device is interactive, if not it's due to a notification
        if (!powerManager.isInteractive()) {
            logEvent(true, true, context);
        } else {
            logEvent(true, false, context);
        }
    }

    private void logEvent(boolean screenOn, boolean Notification, Context context) {

        HashMap<String, String> powerMap = HashMapPool.getMap();

        powerMap.put("screen", screenOn ? "on" : "off");
        powerMap.put("notification", Notification ? "yes" : "no");

        ModuleCharacteristics moduleCharacteristics = ModuleCharacteristics.getInstance();


        EventOperationManager.getInstance(context).addEvent(moduleCharacteristics.getPowerScreenOnOffCharacteristics(), powerMap);

        HashMapPool.releaseMap(powerMap);


    }
}


