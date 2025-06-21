/**
 * ShowUserEventsRecord.java
 * Created by Ian Kim; Updated on 2025-06-20.
 * © The Stanford Screenomics Lab
 */

package edu.stanford.communication.screenomics.dashboard.Adapters;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import edu.stanford.communication.screenomics.dashboard.PojoClass.EventDetails;
import edu.stanford.communication.screenomics.dashboard.R;
import edu.stanford.communication.screenomics.dashboard.databinding.ActivityShowsUserEventsRecordBinding;

public class ShowsUserEventsRecord extends AppCompatActivity {

    ActivityShowsUserEventsRecordBinding binding;
    private EventAdapter eventAdapter;

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
        EdgeToEdge.enable(this);

        binding = ActivityShowsUserEventsRecordBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.UserDetails.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<EventDetails> eventDetailsList = generateDummyEventDetails();
        eventAdapter = new EventAdapter(eventDetailsList, this);
        binding.UserDetails.setAdapter(eventAdapter);
    }

    private ArrayList<EventDetails> generateDummyEventDetails() {
        ArrayList<EventDetails> list = new ArrayList<>();
        Random random = new Random();

        for (String eventName : ALLOWED_EVENTS) {
            String eventTime = String.format("%02d:%02d:%02d",
                    random.nextInt(24),
                    random.nextInt(60),
                    random.nextInt(60));
            list.add(new EventDetails(eventName, eventTime));
        }
        return list;
    }
}
