package edu.stanford.mystudyapp.screenomics.TextBasedEventData;

import java.util.concurrent.ConcurrentHashMap;

public class DataStorage {
    private static DataStorage instance;
    private final ConcurrentHashMap<String, String> eventTimestampMap;

    private DataStorage() {
        eventTimestampMap = new ConcurrentHashMap<>();
    }

    public static synchronized DataStorage getInstance() {
        if (instance == null) {
            instance = new DataStorage();
        }
        return instance;
    }

    // Store or update latest timestamp
    public void addEvent(String eventName, String timestamp) {
        eventTimestampMap.put(eventName, timestamp);
    }

    // Get all events for batch upload
    public ConcurrentHashMap<String, String> getEvents() {
        return new ConcurrentHashMap<>(eventTimestampMap); // Return a copy
    }

    // Clear events after upload
    public void clearEvents() {
        eventTimestampMap.clear();
    }
}


package edu.stanford.mystudyapp.screenomics.TextBasedEventData;

import java.util.HashMap;
import java.util.Map;

import edu.stanford.mystudyapp.screenomics.modulemanager.EventTimestamp;
import edu.stanford.mystudyapp.screenomics.modulemanager.ModuleCharacteristics;

public class EventData {
    private final String time;
    private final String timeLocal;
    private final String type;
    private final String UniqueEventId;
    private final Map<String, String> additionalFields;

    // Private constructor to enforce the use of Builder
    private EventData(Builder builder) {
        this.time = builder.time;
        this.UniqueEventId = builder.UniqueEventId;
        this.timeLocal = builder.timeLocal;
        this.type = builder.type;
        this.additionalFields = builder.additionalFields;
    }

    // Getter Methods
    public String getTime() {
        return time;
    }

    public String getTimeLocal() {
        return timeLocal;
    }

    public String getUniqueEventId() {
        return UniqueEventId;
    }

    public String getType() {
        return type;
    }

    public Map<String, String> getAdditionalFields() {
        return additionalFields;
    }

    // Convert to a single map (for database insertion)
    public Map<String, String> toMap() {
        Map<String, String> eventMap = new HashMap<>();
        eventMap.put("time", time);
        eventMap.put("time-local", timeLocal);
        eventMap.put("type", type);
        if (additionalFields != null) {
            eventMap.putAll(additionalFields);
        }
        return eventMap;
    }



    // Builder Class for creating EventData objects efficiently
    public static class Builder {

        private final String type;
        private final String UniqueEventId;
        private final String time;
        private final String timeLocal;
        private Map<String, String> additionalFields = new HashMap<>();
        EventTimestamp timestamp = new EventTimestamp();

        public Builder(String type,String UniqueEventId) {
            this.time = timestamp.getTimestring();
            this.UniqueEventId = GenerateEventId(UniqueEventId);
            this.timeLocal = timestamp.getSystemClockTimestring();
            this.type = type;
        }

        public String GenerateEventId(String className) {
            return  className + " "+ timestamp +"_"+ ModuleCharacteristics.getInstance().getLocationEventCharacteristics().get("id");
        }

        public Builder addField(String key, String value) {
            this.additionalFields.put(key, value);
            return this;
        }

        public Builder addFields(Map<String, String> fields) {
            this.additionalFields.putAll(fields);
            return this;
        }

        public EventData build() {
            return new EventData(this);
        }
    }
}


public class EventMapBuilder {

    // Single instance of EventTimestamp to avoid creating a new one every time
    private static final EventTimestamp timestamp = new EventTimestamp();

    static HashMap<String, String> completeMap = new HashMap<>();

    public static HashMap<String, String> buildCompleteMap(Map<String, String> additionalFields,String type) {


        // Create a new HashMap to hold the default fields

        completeMap.clear();

        // Adding default fields
        completeMap.put("time", timestamp.getTimestring());
        completeMap.put("time-local", timestamp.getSystemClockTimestring());
        completeMap.put("type", type);


        // Add all additional fields passed as parameter
        if (additionalFields != null) {
            completeMap.putAll(additionalFields);  // Merge additional fields with default ones
        }

        // Return the complete map
        return completeMap;
    }


}

package edu.stanford.mystudyapp.screenomics.TextBasedEventData;


import android.content.Context;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import edu.stanford.mystudyapp.screenomics.modulemanager.EventTimestamp;


/*
 * This class is used to perform CURD operations on local database
 */



import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventOperationManager {
    private static final String TAG = "EventOperationManager";
    private static EventOperationManager instance;  // Singleton instance
    private final EventDatabaseHelper databaseHelper;
    private final ExecutorService executorService;
    private final List<EventData> eventBuffer = new ArrayList<>(); // Buffer to store events
    private static final int BATCH_SIZE = 10;  // Adjust batch size for optimization
    private static final Object LOCK = new Object();
    EventTimestamp timestamp = new EventTimestamp();// Synchronization lock

    // Private constructor for Singleton pattern
    private EventOperationManager(Context context) {
        this.databaseHelper = EventDatabaseHelper.getInstance(context);
        this.executorService = Executors.newSingleThreadExecutor(); // Background thread
    }

    // Singleton instance method (Thread-Safe)
    public static synchronized EventOperationManager getInstance(Context context) {
        if (instance == null) {
            instance = new EventOperationManager(context);
        }
        return instance;
    }

    // Add event to buffer (Thread-Safe)
    public void addEvent(Map<String, String> moduleInfo, HashMap<String, String> eventDetails) {
        synchronized (LOCK) {
            EventData event = new EventData.Builder(moduleInfo.get("type"),moduleInfo.get("className"))
                    .addFields(eventDetails)
                    .build();

            Log.d(TAG, "Event : " + event.toMap().toString());


            eventBuffer.add(event);  // Add event to buffer

            if (Objects.equals(moduleInfo.get("updateTicker"), "1")) {

                Log.d(TAG, " Update ticker" + moduleInfo.get("updateTicker") + " Event : " + event.toMap().toString());
                    DataStorage.getInstance().addEvent("MostRecentEventTime", timestamp.getTimestringFriendly());
                    DataStorage.getInstance().addEvent(moduleInfo.get("className"), timestamp.getTimestringFriendly() + UpdateTickerField(moduleInfo.get("className"), eventDetails));

            }

            // Check if we need to batch write
            if (eventBuffer.size() >= BATCH_SIZE) {
                flushEventsToDB(); // Batch insert into SQLite
            }
        }
    }

    // Batch insert events into SQLite
    private void flushEventsToDB() {

        if (!executorService.isShutdown()) {
            executorService.execute(() -> {
                synchronized (LOCK) {
                    if (!eventBuffer.isEmpty()) {
                        try {
                            List<EventData> batch = new ArrayList<>(eventBuffer); // Copy buffer
                            eventBuffer.clear(); // Clear buffer before inserting

                            databaseHelper.openDatabase(); // Ensure DB is open
                            for (EventData event : batch) {
                                databaseHelper.insertEvent(event.getUniqueEventId(), MapUtils.serializeMap(event.toMap()));
                            }
                            Log.d(TAG, "Batch inserted " + batch.size() + " events into DB.");
                        } catch (Exception e) {
                            Log.e(TAG, "Error inserting batch events: " + e.getMessage());
                        }
                    }
                }
            });

        }
    }

        public String UpdateTickerField(String ModuleInfo, HashMap<String, String> EventData) {
        if (Objects.equals(ModuleInfo, "InternetEvent")){
            return " ["+EventData.get("activity")+"]";
        } else if (Objects.equals(ModuleInfo, "LogInOutEvent")) {
            return " ["+EventData.get("type")+"]";
        } else if (Objects.equals(ModuleInfo, "ScreenOnOffEvent")) {
            return " ["+EventData.get("screen")+"]";
        }else if (Objects.equals(ModuleInfo, "ScreenshotPauseEvent")) {
            return " ["+EventData.get("type")+"]";
        }else {
            return "";
        }

    }

    // Flush remaining events on app shutdown or when needed
    public void flushAndShutdown() {
        flushEventsToDB();  // Final flush
        executorService.shutdown(); // Shut down executor service
    }


}


package edu.stanford.mystudyapp.screenomics.TextBasedEventData;

public class EventsBatchUploadPojo {

    String EventsName;
    String EventJson;
    Integer id;

    public EventsBatchUploadPojo(String eventJson, String eventsName, Integer id) {
        EventJson = eventJson;
        EventsName = eventsName;
        this.id = id;
    }

    public String getEventJson() {
        return EventJson;
    }

    public void setEventJson(String eventJson) {
        EventJson = eventJson;
    }

    public String getEventsName() {
        return EventsName;
    }

    public void setEventsName(String eventsName) {
        EventsName = eventsName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}


package edu.stanford.mystudyapp.screenomics.TextBasedEventData;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.google.firebase.firestore.FirebaseFirestore;

import edu.stanford.mystudyapp.screenomics.DatabaseHelper.LogInPreference;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.SetOptions;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class EventUploader {

    private static volatile EventUploader instance;
    private final String TAG = "EventUploader";
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final DataStorage dataStorage = DataStorage.getInstance();
    private boolean isUploading = false;
    private final Context context;
    private final Lock uploadLock = new ReentrantLock();
    // Ensures only one thread uploads at a time


    // Private constructor to prevent direct instantiation
    private EventUploader(Context context) {
        this.context = context.getApplicationContext(); // Prevent memory leaks
    }

    // Singleton getInstance() method (Thread-safe)
    public static EventUploader getInstance(Context context) {
        if (instance == null) {
            synchronized (EventUploader.class) {
                if (instance == null) {
                    instance = new EventUploader(context);
                }
            }
        }
        return instance;
    }

    // Runnable to upload events every 10 seconds
    private final Runnable uploadRunnable = new Runnable() {
        @Override
        public void run() {
            uploadEvents(); // Upload in a synchronized manner
            handler.postDelayed(this, 5000);
        }
    };

    public void startUploading() {
        handler.postDelayed(uploadRunnable, 5000);
    }

    public void stopUploading() {
        handler.removeCallbacks(uploadRunnable);
    }



    private void uploadEvents() {
        if (uploadLock.tryLock()) { // Acquire lock if available (non-blocking)
            try {
                if (!isUploading && NetworkUtils.isInternetAvailable(context)) {
                    isUploading = true;

                    LogInPreference sharedPref = new LogInPreference(context);
                    String subject_id = sharedPref.GetUserSubjId();

                    if (!subject_id.isEmpty()) {
                        Map<String, String> eventData = dataStorage.getEvents();
                        if (eventData.isEmpty()) {
                            isUploading = false;
                            return;
                        }

                        db.collection("ticker").document(subject_id)
                                .set(eventData, SetOptions.merge())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Log.d(TAG, "Events " + eventData);
                                        dataStorage.clearEvents();
                                        isUploading = false;
                                        Log.d(TAG, "Pushed");
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Log.d(TAG, e.toString());
                                    isUploading = false;
                                });
                    }
                }
            } finally {
                uploadLock.unlock(); // Ensure lock is released after execution
            }
        } else {
            Log.d(TAG, "Upload already in progress, skipping this request.");
        }
    }


    public void uploadImmediately(Map<String, String> eventData) {
        if (eventData == null || eventData.isEmpty()) {
            Log.d(TAG, "No data to upload.");
            return;
        }

        Log.d(TAG, "Inside upload immediately " + eventData);


        // Acquire lock to ensure only one thread uploads at a time
//        uploadLock.lock();

            if (NetworkUtils.isInternetAvailable(context)) {
                LogInPreference sharedPref = new LogInPreference(context);
                String subject_id = sharedPref.GetUserSubjId();

                if (!subject_id.isEmpty()) {
                    Log.d(TAG, "Uploading event data immediately: " + eventData);

                    db.collection("ticker").document(subject_id)
                            .set(eventData, SetOptions.merge())
                            .addOnCompleteListener(task -> {
                                Log.d(TAG, "Immediate upload successful: " + eventData);
                                dataStorage.clearEvents(); // Clear stored events after upload
                            })
                            .addOnFailureListener(e -> Log.e(TAG, "Immediate upload failed", e));
                }
            } else {
                Log.d(TAG, "No internet, skipping immediate upload.");
            }

//        finally {
//            uploadLock.unlock(); // Always release the lock
//        }
    }


}

package edu.stanford.mystudyapp.screenomics.TextBasedEventData;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.stanford.mystudyapp.screenomics.DatabaseHelper.LogInPreference;
import edu.stanford.mystudyapp.screenomics.EventTimestamp;
import edu.stanford.mystudyapp.screenomics.modulemanager.ModuleCharacteristics;

/**
 * This class is used to upload events to firebase
 *
 */

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicBoolean;

public class EventUploaderToFireStore {

    private static final String TAG = "EventUploaderToFireStore";
    private static EventUploaderToFireStore instance;
    private final EventTimestamp timestamp;
    private final FirebaseFirestore db;
    Context context;
    LogInPreference sharedPref;

    private EventUploaderToFireStore(Context context) {
        this.timestamp = new EventTimestamp();
        this.db = FirebaseFirestore.getInstance();
        this.context = context;
        sharedPref = new LogInPreference(context);
    }

    public static synchronized EventUploaderToFireStore getInstance(Context context) {
        if (instance == null) {
            instance = new EventUploaderToFireStore(context);
        }
        return instance;
    }

    private AtomicBoolean isUploading = new AtomicBoolean(false);

    public void startUploadOfflineToOnlineEvents() {
        if (!NetworkUtils.isInternetAvailable(context)) {
            return; // No internet, skip upload
        }

        if (isUploading.get()) {
            Log.d(TAG, "Upload in progress, waiting for next round...");
            return; // Wait for current upload to finish
        }

        isUploading.set(true); // Mark upload as in progress

        WeakReference<Context> contextRef = new WeakReference<>(context);
        EventDatabaseHelper dbHelper = EventDatabaseHelper.getInstance(contextRef.get());

        try (Cursor cursor = dbHelper.getLimitedEvents(10)) { // Fetch only 10 entries at a time
            if (cursor == null || cursor.getCount() == 0) {
                Log.d(TAG, "No offline events to upload.");
                isUploading.set(false);
                return;
            }

            String subjectId = sharedPref.GetUserSubjId().replace(".", "").replace("@", "").replace("_", "");

            if (subjectId.isEmpty()) {
                Log.d(TAG, "Subject ID is empty. Cannot upload.");
                isUploading.set(false);
                return;
            }

            CollectionReference eventCollection = db.collection("users").document(subjectId).collection("events");

            List<Integer> eventIds = new ArrayList<>(); // Store IDs for deletion
            List<Map<String, Object>> eventsList = new ArrayList<>();

            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String eventName = cursor.getString(cursor.getColumnIndexOrThrow("event_name"));
                String eventData = cursor.getString(cursor.getColumnIndexOrThrow("event_data"));

                eventIds.add(id);

                Map<String, Object> eventMap = new HashMap<>();
                eventMap.put("event_name", eventName);
                eventMap.put("event_data", eventData);

                eventsList.add(eventMap);
            }

            uploadBatch(eventCollection, eventsList, eventIds, dbHelper);
        } catch (Exception e) {
            Log.e(TAG, "Error processing events from SQLite: ", e);
            isUploading.set(false);
        }
    }



    private void uploadBatch(CollectionReference eventCollection, List<Map<String, Object>> eventsData, List<Integer> eventIds, EventDatabaseHelper dbHelper) {
        WriteBatch batch = db.batch();

        if (eventsData.isEmpty()) {
            isUploading.set(false);
            return;
        }


        Log.d(TAG, "Uploading batch of " + eventsData.size() + " events");

        for (Map<String, Object> event : eventsData) {
            String eventName = (String) event.get("event_name");
            String eventData = (String) event.get("event_data");

            if (eventName != null && !eventName.isEmpty()) {
                DocumentReference ref = eventCollection.document(eventName);
                batch.set(ref, new Gson().fromJson(eventData, Map.class));
            }
        }

        batch.commit().addOnSuccessListener(aVoid -> {
            Log.d(TAG, "Batch upload successful. Deleting uploaded events...");

            for (int id : eventIds) {
                dbHelper.deleteEvent(id);
                Log.d(TAG, "Deleted event ID: " + id);
            }

            isUploading.set(false);
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Batch upload failed: " + e.getMessage());
            isUploading.set(false);
        });
    }



    public void uploadSingleEvent(String eventName, HashMap<String, String> eventData, Context context) {
        if (!NetworkUtils.isInternetAvailable(context)) return;

        String subjectId = sharedPref.GetUserSubjId().replace(".", "").replace("@", "").replace("_", "");

        if (!subjectId.isEmpty()) {
            HashMap<String, String> map = HashMapPool.getMap();
            HashMap<String, String> map1 = HashMapPool.getMap();
            map.put(eventName, timestamp.getTimestringFriendly() + EventOperationManager.getInstance(context).UpdateTickerField(eventName, eventData));
            map1.put("MostRecentEventTime", timestamp.getTimestringFriendly());

            FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(subjectId)
                    .collection("events")
                    .document(GenerateEventId(eventName))
                    .set(eventData)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Event Added"))
                    .addOnFailureListener(e -> Log.d(TAG, e.getMessage()));

            EventUploader.getInstance(context).uploadImmediately(map);
            EventUploader.getInstance(context).uploadImmediately(map1);

            HashMapPool.releaseMap(map);
            HashMapPool.releaseMap(map1);
        }
    }


    public String GenerateEventId(String className) {
        return  className + " "+ timestamp +"_"+ ModuleCharacteristics.getInstance().getLocationEventCharacteristics().get("id");
    }
}


package edu.stanford.mystudyapp.screenomics.TextBasedEventData;

import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class HashMapPool {
    private static final int POOL_SIZE = 100; // Adjust based on event load
    private static final Queue<HashMap<String, String>> pool = new ConcurrentLinkedQueue<>();

    public static HashMap<String, String> getMap() {
        HashMap<String, String> map = pool.poll(); // Get from pool
        return (map != null) ? map : new HashMap<>(); // Create new if pool is empty
    }

    public static void releaseMap(HashMap<String, String> map) {
        map.clear(); // Ensure old data is removed
        if (pool.size() < POOL_SIZE) {
            pool.offer(map); // Return to pool if not full
        }
    }
}



package edu.stanford.mystudyapp.screenomics.TextBasedEventData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class MapUtils {

    // Use a single static Gson instance to avoid creating new objects repeatedly
    private static final Gson GSON = new Gson();
    private static final Type MAP_TYPE = new TypeToken<Map<String, String>>() {}.getType();

    public static String serializeMap(Map<String, String> map) {
        return GSON.toJson(map, MAP_TYPE);
    }

    public static Map<String, String> deserializeMap(String jsonString) {
        return GSON.fromJson(jsonString, MAP_TYPE);
    }
}


package edu.stanford.mystudyapp.screenomics.TextBasedEventData;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;

import edu.stanford.mystudyapp.screenomics.FirebaseSettings.SettingsManager;

public class NetworkUtils {
    

    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            Network activeNetwork = connectivityManager.getActiveNetwork();
            if (activeNetwork == null) return false; // No active network

            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
            return capabilities != null &&
                    (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
        }
        return false; // No connectivity manager available
    }

        public static boolean IsOnlyUploadTextOnWifi(Context context){

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null){

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

            if (activeNetwork != null)
            {
                // If we're only allowed to upload over wifi, check that.
                if (SettingsManager.val("data-text-upload-wifi-only") == 1 && activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {

                    return true;

                }
                else if (SettingsManager.val("data-text-upload-wifi-only") == 0 && activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE){

                    return false;

                }else {
                    return false;
                }
            }
        }

        return false;


    }

}


package edu.stanford.mystudyapp.screenomics.TextBasedEventData;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class UtilsForObject {

    // Serialize an object to a byte array
    public static byte[] serializeObject(Object obj) throws IOException {
        ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
        ObjectOutputStream outStream = new ObjectOutputStream(byteOutStream);
        outStream.writeObject(obj);
        outStream.flush();
        return byteOutStream.toByteArray();
    }

    // Deserialize a byte array to an object
    public static Object deserializeObject(byte[] byteArray) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteInStream = new ByteArrayInputStream(byteArray);
        ObjectInputStream inStream = new ObjectInputStream(byteInStream);
        return inStream.readObject();
    }
}


package edu.stanford.mystudyapp.screenomics;

import android.os.SystemClock;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class EventTimestamp implements Serializable
{
    public static final String TAG = "EventTimestamp";
    private static final String TIMESTAMP_PATTERN = "yyyyMMddHHmmssSSS";
    private static final String TIMESTAMP_PATTERN_FRIENDLY = "yyyy-MM-dd HH:mm:ss";

    // Static values for tracking real-world time. Server time is milliseconds since Epoch UTC.
    private static long referenceServerTime = 0;
    private static long referenceElapsedRealtime = 0;

    // Final instance variables for the time of this timestamp, set upon creation.
    private final long elapsedTime;
    private final String systemClockTimestamp;

    // Derived real-world time. This field allows serialization across boot sessions.
    private long time;

    public EventTimestamp()
    {
        // Set stuff that doesn't depend on having the server time.
        elapsedTime = SystemClock.elapsedRealtime();
        SimpleDateFormat sdf = new SimpleDateFormat(TIMESTAMP_PATTERN, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        systemClockTimestamp = sdf.format(Calendar.getInstance().getTime());

        // If we have the server time, we can derive the real-world time since epoch.
        time = 0;
        refresh();
    }

    /**
     * Assuming a server time is known, anchors this timestamp to real-world time. It's good
     * to call this method before serialization (i.e. if the timestamp is being written to disk),
     * because otherwise the time could change if the device is rebooted.
     */
    public void refresh()
    {
        if (time == 0 && isServerTimeSet()) {
            time = referenceServerTime + (elapsedTime - referenceElapsedRealtime);
        }
    }

    /**
     * Gets a timestamp string for this time with the best available method.
     * @return Server time if set, otherwise system clock time.
     */
    public String getTimestring() {
        return isServerTimeSet() ? getRealTimestring() : getSystemClockTimestring();
    }

//    public String getTimestringFriendly() {
//        return isServerTimeSet() ? getRealTimestringFriendly() : getSystemClockTimestring();
//    }


    public String getTimestringFriendly() {
        // Define the time zone for Los Angeles
        TimeZone losAngelesTimeZone = TimeZone.getTimeZone("America/Los_Angeles");

        // Get the current date and time
        Date now = new Date();

        // Set the date and time format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss '(PT)'");
        dateFormat.setTimeZone(losAngelesTimeZone);

        // Format the current date and time in Los Angeles time zone
        return dateFormat.format(now);
    }


    /**
     * Gets a string representing the date/time of this timestamp based on the system clock.
     * This will be in the device's time zone.
     * @return Local timestamp
     */
    public String getSystemClockTimestring() {
//        return systemClockTimestamp;
        return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(Calendar
                .getInstance().getTime());
    }

    /**
     * Gets a string representing the date/time of this timestamp in real-world time, which is
     * robust to changes in the system clock. This is adjusted to GMT Time.
     * @return Real world timestamp
     */
    public String getRealTimestring() {
        SimpleDateFormat sdf = new SimpleDateFormat(TIMESTAMP_PATTERN, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date(getRealTimeMillis()));
    }

    public String getRealTimestringFriendly() {
        SimpleDateFormat sdf = new SimpleDateFormat(TIMESTAMP_PATTERN_FRIENDLY, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        return sdf.format(new Date(getRealTimeMillis())) + " (PT)";
    }

    /**
     * Gets the real-world time of this timestamp in milliseconds.
     * @return milliseconds since epoch UTC
     */
    public long getRealTimeMillis()
    {
        if (time != 0) return time;

        if (isServerTimeSet())
        {
            time = referenceServerTime + (elapsedTime - referenceElapsedRealtime);
            return time;
        }

        return 0;
    }

    public static boolean isServerTimeSet()
    {
        return referenceServerTime != 0;
    }

    @NonNull
    @Override
    public String toString() {
        return getTimestring();
    }
}



