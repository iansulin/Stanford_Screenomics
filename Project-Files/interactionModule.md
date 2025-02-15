package edu.stanford.mystudyapp.screenomics.interactions;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;

import java.util.HashMap;


import edu.stanford.mystudyapp.screenomics.DatabaseHelper.LogInPreference;
import edu.stanford.mystudyapp.screenomics.TextBasedEventData.EventOperationManager;
import edu.stanford.mystudyapp.screenomics.TextBasedEventData.HashMapPool;
import edu.stanford.mystudyapp.screenomics.modulemanager.ModuleCharacteristics;
import edu.stanford.mystudyapp.screenomics.modulemanager.ModuleController;

public class UserInteractionCapture extends AccessibilityService {


    int ScrolledX = 0;

    int ScrolledY = 0;

//    LogEventsInFirebase logEventsInFirebase = new LogEventsInFirebase();


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {



        if (CheckIsUserLogin()) {

            if (ModuleController.ENABLE_INTERACTIONS){

                HashMap<String, String> userInput = HashMapPool.getMap();


                if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_SCROLLED) {

                    if (event.getScrollX() > ScrolledX) {

                        userInput.put("activity","scroll-right");

                        EventOperationManager.getInstance(this).addEvent(ModuleCharacteristics.getInstance().getInteractionEventCharacteristics(), userInput);


                        HashMapPool.releaseMap(userInput);

//                        logEventsInFirebase.LogEvent(LogEventsInFirebase.GetSimpleClassName("AccessibilityEvent"), LogEventsInFirebase.GetDefaultMapWithAdditionalValue("accessibility", "activity", ), getApplicationContext());
                        ScrolledX = event.getScrollX();
                    } else if (event.getScrollX() < ScrolledX) {
//                        logEventsInFirebase.LogEvent(LogEventsInFirebase.GetSimpleClassName("AccessibilityEvent"), LogEventsInFirebase.GetDefaultMapWithAdditionalValue("accessibility", "activity", "scroll-left"), getApplicationContext());


                        userInput.put("activity","scroll-left");


                        EventOperationManager.getInstance(this).addEvent(ModuleCharacteristics.getInstance().getInteractionEventCharacteristics(), userInput);

                        HashMapPool.releaseMap(userInput);


                        ScrolledX = event.getScrollX();

                    }

                    if (event.getScrollY() > ScrolledY) {
//                        logEventsInFirebase.LogEvent(LogEventsInFirebase.GetSimpleClassName("AccessibilityEvent"), LogEventsInFirebase.GetDefaultMapWithAdditionalValue("accessibility", "activity", "scroll-up"), getApplicationContext());

                        userInput.put("activity","scroll-up");


                        EventOperationManager.getInstance(this).addEvent(ModuleCharacteristics.getInstance().getInteractionEventCharacteristics(), userInput);

                        HashMapPool.releaseMap(userInput);

                        ScrolledY = event.getScrollY();

                    } else if (event.getScrollY() < ScrolledY) {
//                        logEventsInFirebase.LogEvent(LogEventsInFirebase.GetSimpleClassName("AccessibilityEvent"), LogEventsInFirebase.GetDefaultMapWithAdditionalValue("accessibility", "activity", "scroll-down"), getApplicationContext());

                        userInput.put("activity","scroll-down");


                        EventOperationManager.getInstance(this).addEvent(ModuleCharacteristics.getInstance().getInteractionEventCharacteristics(), userInput);

                        HashMapPool.releaseMap(userInput);

                        ScrolledY = event.getScrollY();

                    }


                } else if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED) {

//                logEventsInFirebase.LogEvent(LogEventsInFirebase.GetSimpleClassName("AccessibilityEvent"), LogEventsInFirebase.GetDefaultMapWithAdditionalValue("accessibility", "activity", "clicked"), getApplicationContext());

                    userInput.put("activity","clicked");


                    EventOperationManager.getInstance(this).addEvent(ModuleCharacteristics.getInstance().getInteractionEventCharacteristics(), userInput);

                    HashMapPool.releaseMap(userInput);

                }else if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_LONG_CLICKED) {

//                logEventsInFirebase.LogEvent(LogEventsInFirebase.GetSimpleClassName("AccessibilityEvent"), LogEventsInFirebase.GetDefaultMapWithAdditionalValue("accessibility", "activity", "long-clicked"), getApplicationContext());
                    userInput.put("activity","long-clicked");


                    EventOperationManager.getInstance(this).addEvent(ModuleCharacteristics.getInstance().getInteractionEventCharacteristics(), userInput);
                    HashMapPool.releaseMap(userInput);


                }
                else if (event.getEventType() == AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_START) {
//                logEventsInFirebase.LogEvent(LogEventsInFirebase.GetSimpleClassName("AccessibilityEvent"), LogEventsInFirebase.GetDefaultMapWithAdditionalValue("accessibility", "activity", "touch-exploration-start"), getApplicationContext());
                    userInput.put("activity","touch-exploration-start");


                    EventOperationManager.getInstance(this).addEvent(ModuleCharacteristics.getInstance().getInteractionEventCharacteristics(), userInput);

                    HashMapPool.releaseMap(userInput);

                }
                else if (event.getEventType() == AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_END) {
//                logEventsInFirebase.LogEvent(LogEventsInFirebase.GetSimpleClassName("AccessibilityEvent"), LogEventsInFirebase.GetDefaultMapWithAdditionalValue("accessibility", "activity", "touch-exploration-end"), getApplicationContext());
                    userInput.put("activity","touch-exploration-end");


                    EventOperationManager.getInstance(this).addEvent(ModuleCharacteristics.getInstance().getInteractionEventCharacteristics(), userInput);
                    HashMapPool.releaseMap(userInput);

                }
            }


        }

    }


    @Override
    public void onInterrupt() {

    }



    @Override
    protected void onServiceConnected() {

        super.onServiceConnected();

        AccessibilityServiceInfo info = new AccessibilityServiceInfo();

        info.eventTypes = AccessibilityEvent.TYPE_VIEW_SCROLLED | AccessibilityEvent.TYPE_VIEW_CLICKED | AccessibilityEvent.TYPE_VIEW_LONG_CLICKED | AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_START | AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_END;

        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK;

        info.notificationTimeout = 100;

        this.setServiceInfo(info);


    }


    boolean CheckIsUserLogin(){
        LogInPreference sharedPref = new LogInPreference(getApplicationContext());
//        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String subject_id = sharedPref.GetUserSubjId().replace(".","").replace("@","").replace("_","");

        if (!subject_id.equals("")) {
            return true;
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                disableSelf();
            }else {
                stopSelf();
            }
            return false;
        }

    }

}
