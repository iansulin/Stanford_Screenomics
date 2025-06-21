/**
 * ShowUserList.java
 * Created by Ian Kim; Updated on 2025-06-19.
 * © The Stanford Screenomics Lab
 */

package edu.stanford.communication.screenomics.dashboard.AppActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;

import edu.stanford.communication.screenomics.dashboard.Adapters.EmailAdapter;
import edu.stanford.communication.screenomics.dashboard.Adapters.EventAdapter;
import edu.stanford.communication.screenomics.dashboard.DB.LogInPref;
import edu.stanford.communication.screenomics.dashboard.LoginUi;
import edu.stanford.communication.screenomics.dashboard.PojoClass.EventDetails;
import edu.stanford.communication.screenomics.dashboard.PojoClass.UserTickerInfo;
import edu.stanford.communication.screenomics.dashboard.R;
import edu.stanford.communication.screenomics.dashboard.Utils.AndroidUtils;
import edu.stanford.communication.screenomics.dashboard.Utils.CustomItemSorter;
import edu.stanford.communication.screenomics.dashboard.Utils.DateTimeChecker;
import edu.stanford.communication.screenomics.dashboard.databinding.ActivityShowUserListBinding;

public class ShowUserList extends AppCompatActivity {

    ActivityShowUserListBinding binding;
    private FirebaseFirestore db;
    private ArrayList<UserTickerInfo> eventList;
    private ArrayList<EventDetails> UserEventList;

    private EmailAdapter Adapter;
    private EventAdapter eventAdapter;

    private SwipeRefreshLayout swipeRefreshLayout;

    private static final String[] ALLOWED_EVENTS = {
            "MostRecentEventTime",
            "Alarm-Manager-Notification-Event",
            "BatteryChargingEvent",
            "BatteryStateEvent",
            "CaptureStartupEvent",
            "GPSLocationEvent",
            "InteractionEvent",
            "InternetEvent",
            "LogInOutEvent",
            "NewForegroundAppEvent",
            "ScreenOnOffEvent",
            "ScreenshotEvent",
            "ScreenshotPauseEvent",
            "ScreenshotUploadEvent",
            "StepCountEvent",
            "SystemPowerEvent"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowUserListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        db = FirebaseFirestore.getInstance();
        FirebaseApp.initializeApp(this);

        swipeRefreshLayout = binding.swipeRefreshLayout;

        binding.ShowProgress.setVisibility(View.VISIBLE);

        Map map = (Map) getIntent().getSerializableExtra("Map");

        if (map != null) {
            ExtractMaps(map);
            binding.SendUserEmail.setVisibility(View.VISIBLE);

            binding.SendUserEmail.setOnClickListener(v -> {
                String emailExtra = getIntent().getStringExtra("email");
                if (emailExtra != null) {
                    ProgressDialog pDialog = new ProgressDialog(ShowUserList.this);
                    pDialog.setMessage("Loading...!");
                    pDialog.setCancelable(false);
                    pDialog.show();

                    FirebaseFirestore.getInstance()
                            .collection("user_directory")
                            .document(emailExtra)
                            .get()
                            .addOnFailureListener(e -> Toast.makeText(ShowUserList.this, "Something went wrong", Toast.LENGTH_SHORT).show())
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Map<String, Object> data = document.getData();
                                        Log.d("ShowUserList", data.toString());
                                        String email = data.get("email").toString();
                                        AndroidUtils.SendUserEmail(ShowUserList.this, email);
                                    } else {
                                        Toast.makeText(ShowUserList.this, "No User Email Found", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                pDialog.dismiss();
                            });
                } else {
                    Toast.makeText(ShowUserList.this, "No User Email Found", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            eventList = new ArrayList<>();
            filterUsersWithScreenshotPauseEvent();
        }

        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (getIntent().getSerializableExtra("Map") == null) {
                filterUsersWithScreenshotPauseEvent();
            } else {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void ExtractMaps(Map maps) {
        setTitle(getIntent().getStringExtra("email"));

        UserEventList = new ArrayList<>();
        Map<String, Object> map = maps;

        Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            String key = entry.getKey();

            if (isEventAllowed(key)) {
                Object value = entry.getValue();
                UserEventList.add(new EventDetails(key, String.valueOf(value)));
            }
        }

        CustomItemSorter.sortEventDetailsList(UserEventList);

        eventAdapter = new EventAdapter(UserEventList, ShowUserList.this);
        binding.DataList.setAdapter(eventAdapter);
        binding.ShowProgress.setVisibility(View.INVISIBLE);
        swipeRefreshLayout.setRefreshing(false);
    }

    private void filterUsersWithScreenshotPauseEvent() {
        binding.ShowProgress.setVisibility(View.VISIBLE);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
        Date currentDate = calendar.getTime();

        calendar.add(Calendar.HOUR_OF_DAY, -24);
        Date thresholdDate = calendar.getTime();

        db.collection("ticker")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        eventList.clear();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("ShowUserList", "User ID: " + document.getId() + ", MostRecentEventTime: " + document.getString("MostRecentEventTime"));

                            if (DateTimeChecker.isMostRecentEventWithinLast24Hours(document)) {
                                eventList.add(new UserTickerInfo(document.getId(), true, filterAllowedEvents(document.getData())));
                            } else {
                                eventList.add(new UserTickerInfo(document.getId(), false, filterAllowedEvents(document.getData())));
                            }
                        }

                        Adapter = new EmailAdapter(eventList, ShowUserList.this);
                        binding.DataList.setAdapter(Adapter);
                        binding.ShowProgress.setVisibility(View.INVISIBLE);
                        swipeRefreshLayout.setRefreshing(false);

                    } else {
                        Log.d("Firestore", "Error getting documents: ", task.getException());
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    private boolean isEventAllowed(String eventName) {
        for (String allowed : ALLOWED_EVENTS) {
            if (allowed.equals(eventName)) return true;
        }
        return false;
    }

    // Fix here: Also include "MostRecentEventTime" so adapter can read it for display
    private Map<String, Object> filterAllowedEvents(Map<String, Object> originalData) {
        Map<String, Object> filtered = new HashMap<>();
        for (String key : originalData.keySet()) {
            if (isEventAllowed(key) || key.equals("MostRecentEventTime")) {
                filtered.put(key, originalData.get(key));
            }
        }
        return filtered;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.log_out) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirmation");
            builder.setMessage("Are you sure you want to proceed?");

            builder.setPositiveButton("Yes", (dialog, which) -> {
                FirebaseAuth.getInstance().signOut();
                LogInPref pref = new LogInPref(ShowUserList.this);
                pref.clearAllPreferences();

                startActivity(new Intent(ShowUserList.this, LoginUi.class));
                finish();
                Toast.makeText(ShowUserList.this, "Successfully logged out.", Toast.LENGTH_SHORT).show();
            });

            builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

            AlertDialog dialog = builder.create();
            dialog.show();
        }

        return super.onOptionsItemSelected(item);
    }
}
