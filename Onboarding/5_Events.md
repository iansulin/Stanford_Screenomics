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

* `**ScreenshotEvent**`: Recorded when a screenshot is successfully captured by the app and
saved to local storage. This is one of the most common event types, as we can expect to
see it once every 5 seconds.
  * `filename`: The name of the file saved to the device for this screenshot.
  * `screenshot-ordered-time`: The time screenshot capture order was made by the app, in GMT time `YYYYMMDDHHMMSSsss`.
  * `screenshot-ordered-time-local`: The time screenshot capture order was made by the app, in user device's system time `YYYYMMDDHHMMSSsss`.

> Unlike other event captures, screenshot capture often takes longer from the moment the app orders the screenshot to the actual moment it is captured. The time taken can be significantly longer compared to other types of data captures, like screen on/off events, which are typically instant and rely on a simple binary state change. The screenshot capture process involves several additional steps, including rendering the current screen state and executing the capture through the operating system. Due to this time difference, we introduced extra entries `screenshot-ordered-time` and `screenshot-ordered-time-local` in the Screenshot module. These entries represent the time when the screenshot capture order was made by the app, while the `time` and `time-local` entries represent the actual time when the screenshot was taken. 

* `**ScreenshotFailureEvent**`: Recorded when a screenshot fails to be taken, or a screenshot-related error occurs in the app. Some of these are entirely benign, and some may require attention.
  * `filename`: The filename this screenshot would’ve had if it had succeeded.
  * `error` : The type of error that occurred. Possible errors:
   * `StorageLimitReached`: The user has <50 MB of storage space remaining on their phone, so the app has refused to take a screenshot in order to prevent space from filling up completely.
   * `ExtraneousScreenshotInterval`: Tried to take a screenshot too quickly.
   * `RestartingCaptureInterval`: Stopped taking screenshots.
   * `ImageReaderNull`: Occurs when screen orientation changes.
   * `WriteFailure`: Screenshot taken but not written to storage.
   * `FileNotFoundException`: Screenshot taken but not created.
   * `ScreenNotOn`: Tried to take a screenshot while the screen was off.





| **ScreenshotUploadEvent** | Records events relevant to the uploading of screenshots.                                                          | - Includes when uploading starts (or doesn’t start due to conditions like not being on Wi-Fi), finishes, and when errors are encountered. A new upload process is attempted every 5 minutes by default.                                                                                                                                                                 |































