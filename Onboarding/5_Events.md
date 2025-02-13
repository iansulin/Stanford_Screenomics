## Events

All text-based data streams about user behavior/activity defined in data collection modules are recorded as events. An event represents a discrete action that happened at a particular point in time. Sometimes, data streams translate very well to this interpretation. For example, “a screenshot was taken” or “an error
occurred” are both things that happen at a particular instant. Sometimes the translation is less
intuitive, but events are still almost always the best way to record these given data streams. For
example, GPS location is not really an event per-se, but since we’re sampling location at discrete
intervals, one could consider the event to be “a GPS location was recorded.”

Events are recorded as entries in the Firestore database under `users` - `events` collection. All events have at least a `type` and a `timestamp`. Many events then contain additional metadata that is specific to the type of event. 

---

### Event Syncing

The Screenomics app syncs events with the Firebase in near real-time. Syncing will occur over
either Wi-Fi or data plan (5G or LTE), depending on how user's network preference is defined in the **Settings-Profiles**. Syncing over data plan should use negligible amounts of mobile data allowances, since event data is entirely a text-based stream. 

If the user does not have an internet connection when the app attempts to sync, events will be temporarily stored in a hidden, local storage on the user’s phone, until an internet connection is available. This way, all the events they’ve recorded are saved until they can be synced successfully. The data recorded for a user should remain in Firestore indefinitely, and thus data processing can occur at any time.

---

### Data Processing

In Firestore, cocuments are grouped into collections, which can be thought of as nested arrays of JSON objects, which is not terribly useful for analysis. A tool for converting Firestore events into downloadable CSV files (more apt for analysis) is available on request: iank@stanford.edu 

---

### General Metadata Tags

All events record the following fields in the database:
* **time**: The time at which this event occurred, in GMT time `YYYYMMDDHHMMSSsss`. This timestamp synchronizes with server-based time, and thus will be accurate even if the user’s device clock is not, such as when the user is traveling across different time zones.
* **time-local**: The time at which this event occurred, in the user device’s system time `YYYYMMDDHHMMSSsss` (so usually in the user’s timezone).
* **type**: A name for this type of event. This specific naming convention here (e.g. screenshot-upload) is not often used. Rather, the camel-case convention (e.g. ScreenshotUploadEvent) is more common.

---

### Event Types

Below are listed all of the types of events recorded by Screenomics, grouped by the purpose they serve. All events have the metadata in the above section; any additional metadata will be listed here for each event type. 

#### Module 1. Screenshots

* **`ScreenshotEvent`**: Recorded when a screenshot is successfully captured by the app and
saved to local storage. This is one of the most common event types, as we can expect to
see it once every 5 seconds.
  * `filename`: The name of the file saved to the device for this screenshot.
  * `screenshot-ordered-time`: The time screenshot capture order was made by the app, in GMT time `YYYYMMDDHHMMSSsss`.
  * `screenshot-ordered-time-local`: The time screenshot capture order was made by the app, in user device's system time `YYYYMMDDHHMMSSsss`.

> Unlike other event captures, screenshot capture often takes longer from the moment the app orders the screenshot to the actual moment it is captured. The time taken can be significantly longer compared to other types of data captures, like screen on/off events, which are typically instant and rely on a simple binary state change. The screenshot capture process involves several additional steps, including rendering the current screen state and executing the capture through the operating system. Due to this time difference, we introduced extra entries `screenshot-ordered-time` and `screenshot-ordered-time-local` in the Screenshot module. These entries represent the time when the screenshot capture order was made by the app, while the `time` and `time-local` entries represent the actual time when the screenshot was taken. 

* **`ScreenshotFailureEvent`** : Recorded when a screenshot fails to be taken, or a screenshot-related error occurs in the app. Some of these are entirely benign, and some may require attention.
  * `filename`: The filename this screenshot would’ve had if it had succeeded.
  * `error` : The type of error that occurred. Possible errors:
    * _StorageLimitReached_: The user has <50 MB of storage space remaining on their phone, so the app has refused to take a screenshot in order to prevent space from filling up completely.
    * _ExtraneousScreenshotInterval_: The app tried to take a screenshot too quickly. These result from the finnicky-ness of Android’s timing system, and can be safely ignored.
    * _RestartingCaptureInterval_: We stopped taking screenshots for whatever reason; the app noticed this, and started them back up again. These result from the finnicky-ness of Android’s timing system, and can be safely ignored.
    * _ImageReaderNull_: This may occur if the user changes their screen orientation from portrait to landscape exactly as a screenshot is being taken. It explains the absence of a screenshot in this instant; it can be safely ignored.
    * _WriteFailure_: The screenshot was taken but could not be written to storage. This would happen if space is full, or some external force is disallowing the app from saving files. A one-time occurrence can be
ignored; repeated occurrences should involve contacting the user.
    * _FileNotFoundException_: The screenshot was taken but could not be created in storage. This likely will not happen. A one-time occurrence can be ignored; repeated occurrences should involve contacting the user.
    * _ScreenNotOn_: The app tried to take a screenshot while the screen was off. This is unlikely to happen, and can be safely ignored if it does.

* **`ScreenshotUploadEvent`**: Records events relevant to the uploading of screenshots. This includes when uploading starts (or doesn’t start because e.g. the user isn’t on Wi-Fi), uploading finishes, and when errors are encountered in the upload process. A new upload process is attempted at a fixed interval defined by the `settings_profiles` - `data-nontext-upload-interval` parameter.
  * `phase`: The phase of the upload process in which this event was generated. A value of "start" indicates the event relates to the upload starting up. A value of "complete" means this event signifies an upload completed successfully. A value of image means this event is reporting on an error that occurred when uploading
an individual image.
  * `error`: A "yes"/"no" value of whether this event is reporting an error. The word _error_ is used very loosely here. Any ScreenshotUploadEvent that suggests an upload didn’t fully happen will have "yes" as the error value. For example, this field will be “yes” in the case of an upload not starting because the user wasn’t connected to Wi-Fi (even though this isn’t a critical app error).
  * `remaining-imgs`: In all `ScreenshotUploadEvents`, this field contains the number of screenshots stored locally on the user’s device that haven’t been uploaded yet. In theory, when phase is "start," this will be the number of images about to be uploaded; and when phase is "complete" this will be 0.
  * `remaining-bytes`: In all `ScreenshotUploadEvents`, this field indicates the amount of remaining storage space on the user’s device, in bytes. While not specifically related to uploading, this can be useful for debugging, or monitoring whether users will soon run out of space.
  * `message`: A detailed message explaining the circumstances this event is reporting on. Common values:
    * _starting upload now_: The upload process is now starting.
    * _user not connected to internet..._: The upload is not starting because the user has no internet connection (i.e. neither Wi-Fi nor data plan)
    * _user online but not on wifi..._: The user has a cell-data connection but isn’t on Wi-Fi, and this user is set to only upload images over Wi-Fi. Therefore, the upload is not being started.
    * _Upload already in progress..._: The previous upload process is still running. A new process will not be started until the previous one is complete. We should expect to see these if the user is on a somewhat slow internet connection. The app attempts to start a new upload process at a `data-nontext-upload-interval`, but sometimes one process will take more than the defined interval to complete.
    * Values not specifically mentioned above are system errors, and may be a cause for concern. Contact the user or the app developer about any such errors.

* **`ScreenshotPauseEvent`**: Records whether users have paused or resumed the app's screenshot captures using a toggle button in the main app interface.
  * `type`: A binary value "Paused"/"Resumed" representing the user's action.

#### Module 2. Apps 

* **`NewForegroundAppEvent`**: Recordeds anytime the user switches to a new app. For example, a `NewForegroundAppEvent` will be recorded when the user opens Facebook from the home screen or app drawer, or when they use the recent app switcher to switch from Facebook to Google Maps. `NewForegroundAppEvents` have a temporal resolution defined in the `settings_profiles` - `foreground-app-check-interval`. A recommended interval is 1000 milliseconds (1 second). The Screenomics app checks the current foreground at this defined resolution, and reports an event when the foreground app has changed. That means the timestamp reported in the event may be off by up to 1 second.
  * `package`: The Android package name of the app that the user just switched to. For example, Facebook’s package name is com.facebook.katana (I don’t know exactly how Japanese swords are relevant to social networks). For third-party apps, package names should usually be consistent across all devices. For native apps, such as the SMS messaging app, it can vary from device to device. The home screen is also generally considered an app, and thus the user switching to the home screen will typically generate a `NewForegroundAppEvent`. The package
name for the home screen also varies between devices, but will often contain the word "launcher."

#### Module 3. Interactions 

* **`InteractionEvent`**: Records user-smartphone interaction through accessibility services as they occur.
  * `activity`: The nature of finger gesture-based user-smartphone interactions. Types of activity include:
    * _scroll-right_: When the user moves their finger or a pointing device horizontally to the right across the screen (E.g., When viewing an Instagram photo grid, you can scroll right by swiping your finger across the screen to see more photos in the same row).
    * _scroll-left_: When the user moves their finger or a pointing device horizontally to the left across the screen (E.g., In the satellite view of Google Maps, you can scroll left by dragging your finger to the left side of the screen to view areas that were previously off-screen).
    * _scroll-up_: When the user moves their finger or pointing device vertically upward on the screen (E.g., When reading X feeds, you can scroll up by swiping your finger upward to view older tweets that are above the currently displayed tweet.).
    * _scroll-down_: When the user moves their finger or pointing device vertically downward (E.g, While browsing your Facebook news feed, you can scroll down by dragging your finger downward to see newer posts that appear below the current view).
    * _clicked_: A "click" typically refers to a quick tap on the screen, where the user touches the screen and lifts their finger almost immediately. This action is often used to select an item, open a link, or trigger a command. The duration between the touch down and touch up events is usually short, often less than 300 milliseconds.
    * _long-clicked_: A "long-click" (or long press) involves pressing and holding the touch point on the screen for a longer duration, generally around 300 milliseconds or more. This action is often used to bring up additional options or context menus related to the item being pressed, such as editing or deleting a message. It signifies that the user intends to perform a different action than a simple selection.
    * _touch-exploration-start_: This event occurs when a user initiates a touch interaction on the screen, typically by placing their finger on the display. It signifies the beginning of exploration, where the user may be trying to interact with or examine elements on the screen. This action can involve moving their finger around to gather information about the interface, such as feeling out the layout or identifying specific items.
    * _touch-exploration-end:_ This event marks the conclusion of the touch exploration when the user lifts their finger off the screen. 

* Three important things to note:
  1. The same user action may be categorized differently depending on the app’s context. This is because, while Android’s accessibility services enable the capture of user interactions, the categorization and implementation of these interactions depend on the specific application. For example, a tap on the screen in a photo editing app might be classified as "clicked" when selecting a filter, while in a messaging app, the same action could be categorized as "long-clicked" if the user presses and holds a message to reveal additional options. While a general guideline exists, there is no universal "clear-cut" rule, as categorization depends on each app’s implementation.
  2. If an app does not implement tracking for certain interactions, data related to those actions may not be collected at all.
  3. Even when a user performs a single scroll action, the app typically records multiple timestamps corresponding to the continuous touch movements. In theory, capturing both the speed and distance of the scroll can help distinguish different scrolling gestures, such as quick swipes or slow drags. Additionally, this approach improves error handling by providing more precise interaction data.

#### Module 4. Locations

* **`GPSLocationEvent`**: Records the current GPS coordinates of the user. This will only be recorded for users who have `gps-enabled` set to "1" (true) in their settings. The frequency with which a new location is recorded is also a user-specific setting, located in `gps-location-interval`.
  * `lat`: The user’s latitude.
  * `lng`: The user’s longitude.

#### Module 5. Activities

* **`StepCountEvent`**: Records the step data collected from the user's device and returns the total sum of step counts for the interval specified in `pa-stepcounts-interval`. This will only be recorded for users who have `pa-enabled` set to "1" (true) in their settings. 
  * `count`: The total sum of step counts.

#### Module 6. Battery

* **`BatteryStateEvent`**: Recorded when the Android system reports a critical change in the battery charge level.
  * `action`: This will be "low" if Android is reporting that the charge level has become critically low (usually around 15% charge). This will be "okay" if the battery has now charged back up above that level.
  * `percentage`: The exact battery percentage at the time of this event.

* **`BatteryChargingEvent`**: Recorded when the user starts or stops charging their phone.
  * `charging`: "yes" if the user has just begun charging their phone; “no” if the user has just removed their phone from the charger.
  * `percentage`: The exact battery percentage at the time of this event.

#### Module 7. Power 

* **`SystemPowerEvent`**: Generated when the user’s phone powers on or off.
  * `power`: This will be "on" if the user’s phone just turned on, or "off" if the phone is about to shut off.

#### Module 8. Network

* **`InternetEvent`**: Records network connectivity changes, updating the status whenever the connection type (Wi-Fi or data plan [5G or LTE]) changes.
  * `activity`: When the device connects, it identifies the connection type—either Wi-Fi or data plan—and updates the activity status to "Connected-to-Wifi" or "Connected-to-DataPlan," logging any changes. If the device disconnects, it updates the status to "Disconnected-from-Wifi" or "Disconnected-from-DataPlan." 

> The event logic focuses on the active connection type. That is, if one connection is established without disconnecting another, the app will only record the connection activity. For instance, if the user's device connects to Wi-Fi while still connected to the mobile data plan, the app will record "Connected-to-Wifi" and will not log "Disconnected-from-DataPlan." 

#### Module 9. Specs

When a user first creates their account, Screenomics records some basic specs about their phone.
These are as follows:
* brand: The company that manufactures the phone
● display-id: A random internal ID
● fingerprint: A different random internal ID
● manufacturer: Also the company that manufactures the phone
● model: The precise model number of the phone
● product: The name of the type of phone



 
#### Module 3. Other Lifecycle

CaptureStartupEvent: Recorded when the Screenomics app boots up on the user’s
phone.
○ app-version-code : The internal numeric code of the version of the app the user is
running. The best way to tell which version of Screenomics the user is running, is
to find their latest CaptureStartupEvent and look at this field (or
app-version-name). This goes up by 1 for each new release of the app. At the time
of this writing, the latest app code is 15.
○ app-version-name: The version of the app the user is running (similar to
app-version-code, but this is the more human-readable one). At the time of this
writing, the latest app version is 3.01.

ScreenOnOffEvent: Recorded the instant the user switches the screen on or off.
○ screen: This field will be “on” if the user switched on their screen, or “off” if the
user turned off the screen.



Notifications


















