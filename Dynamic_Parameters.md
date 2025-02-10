Dynamic App Configuration for Research Studies

The Stanford Screenomics offers researchers the flexibility to customize features and adapt to changing study needs without involving participants. Using the Firestore [settings_profiles] collection, researchers can define and update key parameters such as sampling intervals, data collection preferences, and network usage directly via the Firebase console. The app periodically retrieves these updates at intervals specified by the "settings-refresh-interval" setting, enabling efficient adjustments while balancing device considerations like battery life, storage, and privacy.

Below is a list of all dynamic parameters that are configurable per-group or per-user through the settings framework.

| Parameter Name | Unit | Range [Default] | Description |
|---|---|---|---|
| `settings-refresh-interval` | Millisecond (ms) | 1 - infinite [300000] | The interval at which dynamic parametert values in the `settings_profile` will be reloaded from Firestore and used by the app. If the parameter values are changed for a participant or a study group, this is the longest amount of time you can expect it to take for the new settings to take effect. |
| `settings-group-override` | True(Yes)/False(No) | 1/0 [0] | Whether or not a participant's study group profile takes priority over individual participant profile settings. |
| `data-text-upload-wifi-only` | True(Yes)/False(No) | 1/0 [0] | Whether or not text-based data uploads should only be done over Wi-Fi. |
| `data-nontext-upload-wifi-only` | True(Yes)/False(No) | 1/0 [0] | Whether or not non-text-based data uploads should only be done over Wi-Fi. |
| `data-text-upload-interval` | Millisecond (ms) | 1 - infinite [5000] | The interval at which the app starts uploading a new batch of text-based data, assuming the previous upload round finished. |
| `data-nontext-upload-interval` | Millisecond (ms) | 1 - infinite [5000] | The interval at which the app starts uploading a new batch of non-text-based data, assuming the previous upload round finished. |

- It’s important to note that decreasing the values of `data-text-upload-interval` and `data-nontext-upload-interval` won’t really make the batch upload “faster,” because whether or not screenshots accumulate in storage is strictly dependent on connection speed. For instance, if the number of bytes/sec of screenshot images being captured is greater than the bytes/sec of the netwofk connection speed, we can expect screenshots to accumulate on the user’s phone regardless of the value of this setting.

| Parameter Name | Unit | Range [Default] | Description |
|---|---|---|---|
| `screenshot-interval` | Millisecond (ms) | 1 - infinite [100000] | The interval at which screenshots are taken. |
| `screenshot-absolute-timing` | True(Yes)/False(No) | 1/0 [0] | Whether or not the app should use an absolute interval for the timing of screenshots. |

- If this setting is enabled, the app uses absolute timing, and screenshots will always be taken on the interval they were on when the app first started. For example, suppose screenshots are taken every 5 seconds, and when the app started screenshots were being taken at times xx:xx:04 and xx:xx:09. Then, the app will continue screenshotting at timestamps with seconds values ending in 4 and 9, throughout the lifetime of the app.
- If this setting is disabled, screen-onset relative timing is used. This means a new interval is started every time the user turns on the screen. The first screenshot in the interval will be taken at the moment of screen onset. Assuming a 5-second capture interval, the next screenshot will be 5 seconds after onset, the following one 10 seconds after onset, etc. Regardless of the value of this setting, a screenshot is always taken immediately when the screen is turned on. In the case that this setting is enabled, said screenshot will occur in addition to the regularly scheduled interval. This setting is enabled by default.
- In reality, these aren't entirely true, as there is some imprecision in Android’s timing system, but that’s the idea.

| Parameter Name | Unit | Range [Default] | Description |
|---|---|---|---|
| `screenshot-check-interval` | Millisecond (ms) | 1 - infinite [60000] | The interval at which the app checks itself to make sure screenshotting is still occurring regularly. This should never really need to be changed. For developers, this is the interval at which `CaptureUploadService.checkScreengrabbing()` is run.
| `force-image-quality` | % | (10 to 100) [50] | The image quality level (compression efficiency) to use for all screenshots. If this is set to a value of 10-100, then all screenshots will be saved with an image quality of 10-100%, respecitvely. Higher quality levels will take more storage space, but will be better images. In essence, the image size in pixels stays the same, but the image file size changes based on the quality level. |

- It is important to note that the value (10-100) is not a direct percentage of the file size but rather a measure of compression quality. While a lower value may result in a smaller file, the reduction is not always linear or predictable because Android's compression algorithms analyze image content, and the compression efficiency depends on the image's format, color patterns and details. Generally, JPEG images and smoother visuals tend to compress better than PNG images or complex, detailed graphics.

| Parameter Name | Unit | Range [Default] | Description |
|---|---|---|---|
| `gps-enabled` | True(Yes)/False(No) | 1/0 [1] | Whether or not GPS data is gathered at `gps-location-interval` for this participant. |
| `gps-location-interval` | Millisecond (ms) | 1 - infinite [300000] | The interval at which new GPS positions are recorded. This is the interval at which we can expect to see GPSLocationEvents being recorded. |

- Regardless of the value of `gps-enabled`, participants will be asked for location permissions during the initial app setup if the location module was activated during app compilation.
- During permission granting, participants can choose between: 1. Precise location (participant's exact location; ideal for studies requiring detailed positioning) or 2. Approximate location (a general, coarse location within an area, with accuracy between 1 to 3 kilometers; suitable for studies that only need a broad understanding of participant locations or have high privacy concerns).
- Regardless of the value of `gps-location-interval`, Android will skip reporting events if it determines that the participant hasn’t moved since the last event.
- A smaller value of `gps-location-interval` will provide richier (more frequent) data, but at the cost of increased battery usage.

| Parameter Name | Unit | Range [Default] | Description |
|---|---|---|---|
| `pa-enabled` | True(Yes)/False(No) | 1/0 [1] | Whether or not Physical Activity (PA) data is gathered at `gps-location-interval` for this participant. |
| `pa-stepcounts-interval` | Millisecond (ms) | 1 - infinite [300000] | The interval at which step counts via the PA module are recorded. This defines how frequently the total steps taken during each interval are recorded. |
- A smaller value of `pa-stepcounts-interval` will provide richier (more frequent) data, but at the cost of increased battery usage.

| Parameter Name | Unit | Range [Default] | Description |
|---|---|---|---|
| `foreground-app-check-interval` | Millisecond (ms) | 1 - infinite [1000] | The interval at which Screenomics checks to see if the foreground app has changed. This effectively determines the temporal resolution of NewForegroundAppEvents. |
| `kill-switch` | True(On)/False(No) | 1/0 [0] | This value is used to immediately force close the app on participants' phones. If the kill-switch is off, the app behaves normally. If the kill-switch is turned on, the app will terminate itself as soon as it synchronizes profile settings with the database. The affected participant(s) will be unable to start up the app manually until the kill-switch is turned off again. |
- The `kill-switch` could be useful if a participant wants to stop data collection but cannot figure out how to uninstall the app, or if a malicious user is found for whatever reason.
