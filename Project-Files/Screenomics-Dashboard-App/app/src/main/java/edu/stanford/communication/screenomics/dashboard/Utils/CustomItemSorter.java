/**
 * CustomItemSorter.java
 * Created by Ian Kim; Updated on 2025-06-20.
 * © The Stanford Screenomics Lab
 */

package edu.stanford.communication.screenomics.dashboard.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.stanford.communication.screenomics.dashboard.PojoClass.EventDetails;
import edu.stanford.communication.screenomics.dashboard.PojoClass.UserTickerInfo;

public class CustomItemSorter {

    private static final String MOST_RECENT_EVENT_TIME_KEY = "MostRecentEventTime";

    /**
     * Sorts a list of UserTickerInfo such that items with
     * IsUserActiveInLast24Hours == false come before true.
     */
    public static void sortCustomItemList(List<UserTickerInfo> list) {
        list.sort(new Comparator<UserTickerInfo>() {
            @Override
            public int compare(UserTickerInfo o1, UserTickerInfo o2) {
                if (o1.getIsUserActiveInLast24Hours() == o2.getIsUserActiveInLast24Hours()) {
                    return 0;
                }
                // false before true
                return o1.getIsUserActiveInLast24Hours() ? 1 : -1;
            }
        });
    }

    /**
     * Sorts the list of EventDetails such that the event
     * with name "MostRecentEventTime" is always first,
     * and others are sorted alphabetically by event name.
     */
    public static void sortEventDetailsList(ArrayList<EventDetails> eventDetailsList) {
        Collections.sort(eventDetailsList, (event1, event2) -> {
            if (event1.getEventName().equals(MOST_RECENT_EVENT_TIME_KEY)) {
                return -1;
            } else if (event2.getEventName().equals(MOST_RECENT_EVENT_TIME_KEY)) {
                return 1;
            } else {
                return event1.getEventName().compareTo(event2.getEventName());
            }
        });
    }
}
