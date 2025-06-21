/**
 * EventDetails.kt
 * Created by Ian Kim; Updated on 2025-06-19.
 * © The Stanford Screenomics Lab
 */

package edu.stanford.communication.screenomics.dashboard.PojoClass

/**
 * POJO class for representing event details including name and time.
 * Used for Events list and user Email list.
 */
data class EventDetails (
    var EventName : String,
    var EventTime : String
)