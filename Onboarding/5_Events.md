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
  * `type`: A binary value "Paused" or "Resumed" representing the user's action.

#### Module 2. App Uage 

* **`NewForegroundAppEvent`**: Recorded anytime the user switches to a new app. For example, a `NewForegroundAppEvent` will be recorded when the user opens Facebook from the home screen or app drawer, or when they use the recent app switcher to switch from Facebook to Google Maps. `NewForegroundAppEvents` have a temporal resolution defined in `settings_profiles` - `foreground-app-check-interval` (1000 milliseconds [= 1 second] is recommended). The Screenomics app checks the current foreground at the defined resolution, and reports an event when the foreground app has changed. That means the timestamp reported in the event may be off by up to 1 second.
*
* package: The Android package name of the app that the user just switched to. For
example, Facebook’s package name is com.facebook.katana (I don’t know exactly
how Japanese swords are relevant to social networks). For third-party apps,
package names should usually be consistent across all devices. For native apps,
such as the SMS messaging app, it can vary from device to device. The home
screen is also generally considered an app, and thus the user switching to the
home screen will typically generate a NewForegroundAppEvent. The package
name for the home screen also varies between devices, but will often contain the
word “launcher.”



























