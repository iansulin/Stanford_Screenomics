Dynamic App Configuration for Research Studies

The Stanford Screenomics offers researchers the flexibility to customize features and adapt to changing study needs without involving participants. Using the Firestore [settings_profiles] collection, researchers can define and update key parameters such as sampling intervals, data collection preferences, and network usage directly via the Firebase console. The app periodically retrieves these updates at intervals specified by the "settings-refresh-interval" setting, enabling efficient adjustments while balancing device considerations like battery life, storage, and privacy.

Below is a list of all dynamic parameters that are configurable per-group or per-user through the settings framework.

| Parameter Name       | Unit      | Range     | Description                           |
|--------------------|----------------------|----------|---------------------------------------|
| `settings-refresh-interval` | Millisecond (ms) | 1 - infinite  | The interval at which dynamic parametert values in the settings_profile will be reloaded from Firestore and used by the app. If the settings are changed for a participant or study group     |
| `settings-group-override` | Yes/No  | 1/0  | Whether or not a participant's study group profile takes priority over individual participant profile settings. |

| Image Quality      | `image-quality`      | Integer  | Quality of image capture (1 to 100)  |
