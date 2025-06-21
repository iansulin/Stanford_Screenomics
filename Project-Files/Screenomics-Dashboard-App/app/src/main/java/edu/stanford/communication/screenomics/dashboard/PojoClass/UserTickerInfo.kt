/**
 * UserTickerInfo.kt
 * Created by Ian Kim; Updated on 2025-06-19.
 * © The Stanford Screenomics Lab
 */

package edu.stanford.communication.screenomics.dashboard.PojoClass

import com.google.android.gms.common.internal.Objects

/**
 * POJO for storing user email, activity status, and user event data map.
 */
data class UserTickerInfo(
    var userEmail: String,
    var IsUserActiveInLast24Hours: Boolean,
    var UserEventsData :Map<String,Object>
)