package edu.stanford.mystudyapp.screenomics.network;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import java.util.concurrent.atomic.AtomicReference;
import java.util.HashMap;

import edu.stanford.mystudyapp.screenomics.TextBasedEventData.EventOperationManager;
import edu.stanford.mystudyapp.screenomics.TextBasedEventData.HashMapPool;
import edu.stanford.mystudyapp.screenomics.modulemanager.ModuleCharacteristics;
import edu.stanford.mystudyapp.screenomics.modulemanager.ModuleController;

public class NetworkStatusCapture extends BroadcastReceiver {

    private static final AtomicReference<String> lastNetworkStatus = new AtomicReference<>("");
    private static final AtomicReference<Boolean> lastIsWifi = new AtomicReference<>(null);

    private ModuleCharacteristics moduleCharacteristics;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!ModuleController.ENABLE_NETWORK) {
            return; // If network monitoring is disabled, exit early
        }

        moduleCharacteristics = ModuleCharacteristics.getInstance();
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr == null) {
            return;
        }

        HashMap<String, String> networkMap = HashMapPool.getMap(); // Reuse HashMapPool for efficiency
        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            boolean isWifi = activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI;
            String newNetworkStatus = isWifi ? "Connected-to-Wifi" : "Connected-to-DataPlan";

            // Only send an update if the network type actually changed
            if (!newNetworkStatus.equals(lastNetworkStatus.get())) {
                networkMap.put("activity", newNetworkStatus);
                EventOperationManager.getInstance(context).addEvent(moduleCharacteristics.getNetworkEventCharacteristics(), networkMap);

                lastNetworkStatus.set(newNetworkStatus);
                lastIsWifi.set(isWifi);

                Log.d("NetworkStatus", "Updated network status: " + newNetworkStatus);
            }
        } else {
            // Handle disconnection
            if (lastIsWifi.get() != null) {
                String disconnectStatus = lastIsWifi.get() ? "Disconnected-from-Wifi" : "Disconnected-from-DataPlan";

                // Only send if the last status was connected
                if (!disconnectStatus.equals(lastNetworkStatus.get())) {
                    networkMap.put("activity", disconnectStatus);
                    EventOperationManager.getInstance(context).addEvent(moduleCharacteristics.getNetworkEventCharacteristics(), networkMap);

                    lastNetworkStatus.set(disconnectStatus);
                    lastIsWifi.set(null); // Reset to indicate no connection

                    Log.d("NetworkStatus", "Updated network status: " + disconnectStatus);
                }
            }
        }
    }
}

