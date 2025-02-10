Dynamic App Configuration for Research Studies

The Stanford Screenomics offers researchers the flexibility to customize features and adapt to changing study needs without involving participants. Using the Firestore [settings_profiles] collection, researchers can define and update key parameters such as sampling intervals, data collection preferences, and network usage directly via the Firebase console. The app periodically retrieves these updates at intervals specified by the "settings-refresh-interval" setting, enabling efficient adjustments while balancing device considerations like battery life, storage, and privacy.

Below is a list of all dynamic parameters that are configurable per-group or per-user through the settings framework.

| Parameter Name | Unit | Range [Default] | Description |
|---|---|---|---|
| `settings-refresh-interval` | Millisecond (ms) | 1 - infinite [300000] | The interval at which dynamic parametert values in the settings_profile will be reloaded from Firestore and used by the app. If the parameter values are changed for a participant or a study group, this is the longest amount of time you can expect it to take for the new settings to take effect. |
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
| `screenshot-check-interval` | Millisecond (ms) | 1 - infinite [60000] | The interval at which the app checks itself to make sure screenshotting is still occurring regularly. This should never really need to be changed. For developers, this is the interval at which CaptureUploadService.checkScreengrabbing() is run.
| `force-image-quality` | % | (10 to 100) | Quality of image capture 
