##  Modular Architecture of The Stanford Screenomics

The Stanford Screenomics Data Collection App features a modular architecture that streamlines data collection across various device aspects, enabling developers to easily enable or disable functionalities for greater flexibility and scalability. It consists of two core base modules—`ModuleManager` and `DatabaseManager`—which provide the essential framework for the app’s operations and facilitate the integration of additional data collection modules. These base modules handle critical tasks like data structure management and database setup, creating a robust foundation that simplifies the development and integration of new modules. Developers can leverage existing functionalities from the base modules, ensuring their custom modules align seamlessly with the app’s architecture. Currently, the app includes nine built-in data collection modules, each tailored to specific needs and utilizing features from the base modules. This design encourages collaborative enhancements, allowing multiple developers to contribute to the evolution of the app by collectively updating the base modules, improving data collection methodologies, and reinforcing security measures. As a result, this modular setup not only enhances usability and adaptability but also positions the app to evolve efficiently with changing data collection requirements while maintaining a secure and robust environment.

The `ModuleManager` acts as the central control system for the app, overseeing the operational status of various data collection modules. It provides the necessary infrastructure to activate or deactivate features based on user preferences or system conditions, ensuring the app adapts to different contexts without compromising performance. This dynamic configuration allows for real-time adjustments to functionality, enabling developers to tailor the user experience to specific needs or constraints. Additionally, the `ModuleManager` ensures centralized event management by consistently timestamping all data collected across different modules, which is vital for accurate data analysis. It also centralizes the generation of characteristics for various events, such as location updates and battery state changes, ensuring uniformity in how events are defined and logged throughout the app. Furthermore, the `ModuleManager` maintains static flags that indicate whether specific data collection modules are enabled or disabled, promoting cohesive module coordination and preventing conflicts within the system.

The `Database Manager` is essential for data storage and retrieval, acting as the primary repository for all user-related data and preferences. This module provides a secure storage solution for sensitive information, such as user authentication data and communication preferences, ensuring that this information is well-protected yet accessible when needed. It offers robust data management options for both text-based data, like user location coordinates, and non-text-based data, such as screenshot images. Initially, all data is stored in a hidden local storage on the user's device. When connected to a network, text-based data is transferred to Firestore, while non-text-based data is sent to Google Cloud Storage. Developers can set network preferences for data transfer separately for text and non-text data; for instance, specifying Wi-Fi only for non-text data ensures uploads occur exclusively when connected to Wi-Fi. Additionally, the module regularly syncs data collected from various modules and manages the upload of events to maintain an up-to-date database of user interactions. It also tracks communication-related preferences, enabling modules to respond effectively to user needs and system conditions. For example, if a developer has set preferences based on device power states, the app can adjust its notification settings to ensure users receive timely alerts about inactivity. 

The data collection modules consist of eight distinct components, each responsible for gathering specific types of data based on user interactions or system events. The `screenshots` module captures and uploads screenshots automatically at defined intervals, providing a visual record of screen contents on a user device. The `apps` module monitors and logs the currently active applications, offering valuable insights into user engagement patterns. The `interactions` module records user actions, including clicks and scrolls, allowing for a comprehensive analysis of user behavior. The `locations` module manages continuous location updates, tracking the device's geographic position using latitude and longitude. The `activities` module logs step counts through the device's sensors, contributing to a better understanding of physical engagement. The `battery` module monitors battery status and logs related events, facilitating effective power usage management. The `power` module tracks screen state changes (on/off) to manage user presence and interaction, while the Network module monitors connectivity to Wi-Fi and data plans, logging changes to optimize data usage management. The 'specs' module collects basic device specifications upon account creation. 

---

### Base Module 1. Module Manager

**Class 1. EventTimestamp** The `EventTimestamp` class manages timestamps in an Android app, providing both system and server time references. It initializes a timestamp using the system clock and can anchor it to a reference server time for accuracy. The class includes methods to retrieve formatted time strings: standard strings for precise, machine-readable formats (e.g., `yyyyMMddHHmmssSSS`) and friendly strings that are more human-readable (e.g., `yyyy-MM-dd HH:mm:ss`). Additionally, it can return the real-world time, typically based on a server reference or UTC, as well as local device time in milliseconds since the epoch. The class also tracks elapsed time since the system boot, providing insight into how long the app has been running. It implements Serializable, allowing timestamps to be saved across application sessions.

| Methods | Description |
|---|---|
| `EventTimestamp()` | Initializes the timestamp, sets the elapsed time, and generates the system clock timestamp. It also calls `refresh()` to derive real-world time if the server time is set. |
| `refresh()` | Updates the real-world time based on the server time if it is known. This method should be called before serialization to ensure the timestamp remains accurate. |
| `getTimestring()` | Returns a string representation of the timestamp using the best available method, either the server time or the system clock time. |
| `getTimestringFriendly()` | Returns a formatted string representation of the current date and time in the Los Angeles time zone. |
| `getSystemClockTimestring()` | Returns a string representing the local timestamp based on the device's system clock. |
| `getRealTimestring()` | Returns a string representation of the timestamp in real-world time, adjusted to GMT. |
| `getRealTimestringFriendly()` | Returns a friendly formatted string of the real-world timestamp in the Los Angeles time zone. |
| `getRealTimeMillis() | Returns the real-world time of the timestamp in milliseconds since epoch UTC, calculating it based on the server time if needed. |
| `isServerTimeSet()` | Checks if the server time has been set. |
| `toString()` | Overrides the default `toString()` method to return the string representation of the timestamp using `getTimestring()`. |

**Class 2. ModuleCharacteristics** The `ModuleCharacteristics` class provides static methods to generate characteristics for various event types in an application, such as location updates, screen on/off events, and battery state changes. Each method returns a `HashMap` containing attributes like class name, event type, a unique ID, and timestamps. This structure allows for centralized management of event details, enabling easy retrieval and consistent handling of different modules within the application. Additionally, it includes flags to control whether certain events should update a ticker display.

| Methods | Description |
|---|---|
| `public static HashMap<String, String> getLocationEventCharacteristics()` | Retrieves characteristics for location events, useful for GPS-related functionalities. |
| `public static HashMap<String, String> getPowerScreenOnOffCharacteristics()` | Provides details for screen on/off events, which can be used to track when the screen state changes. |
| `public static HashMap<String, String> getInteractionEventCharacteristics()` | Gathers characteristics for accessibility interaction events, allowing the app to respond to user interactions. |
| `public static HashMap<String, String> getNetworkEventCharacteristics()` | Fetches details related to network events, which can help monitor internet connectivity and related actions. |
| `public static HashMap<String, String> getStepCountEventCharacteristics()` | Retrieves characteristics for step count events, useful for fitness tracking and activity monitoring. |
| `public static HashMap<String, String> getBatteryStateEventCharacteristics()` | Provides characteristics related to battery state changes, helping manage power usage and notifications. |
| `public static HashMap<String, String> getBatteryChargingEventCharacteristics()` | Fetches details about battery charging events, enabling the app to respond to changes in charging status. |
| `public static HashMap<String, String> getScreenshotFailureCharacteristics()` | Retrieves characteristics for screenshot failure events to help in debugging or error handling. |
| `public static HashMap<String, String> getScreenshotEventCharacteristics()` | Provides characteristics for successful screenshot events, allowing the app to track or manage screenshots. |
| `public static HashMap<String, String> getScreenshotUploadEventCharacteristics()` | Retrieves characteristics for screenshot upload events, useful for tracking uploads and managing data. |
| `public static HashMap<String, String> getForegroundAppModuleCharacteristics()` | Gathers characteristics of the currently active foreground application, useful for monitoring app usage. |
| `public static HashMap<String, String> getCaptureStartupCharacteristics()` | Provides details for capture startup events, which can be used to initialize capturing functionality. |
| `public static HashMap<String, String> getLowMemoryEventCharacteristics()` | Retrieves characteristics for low memory events to help the app respond to memory constraints. |
| `public static HashMap<String, String> getSystemPowerEventCharacteristics()` | Provides characteristics of system power events, which can be useful for managing energy consumption. |
| `public static HashMap<String, String> getAlarmManagerCharacteristics()` | Retrieves details for alarm manager notification events, useful for scheduling and managing alarms. |

**Class 3. ModuleController** The `ModuleController` class primarily consists of static boolean fields (`true` [module = activated]/`false` [module = deactivated]) that act as flags to activate or deactivate modules in the application. There are no methods defined in this class; it serves mainly as a configuration holder for module activation states.

```
// Fields
public static boolean ENABLE_ACTIVITIES = true;
public static boolean ENABLE_SCREENSHOTS = true;
public static boolean ENABLE_APPS = true;
public static boolean ENABLE_INTERACTIONS = true;
public static boolean ENABLE_LOCATIONS = true;
public static boolean ENABLE_NETWORK = true;
public static boolean ENABLE_POWER = true;
public static boolean ENABLE_SPECS = true;
public static boolean ENABLE_BATTERY = true;
```

---

### Base Module 2. Database Manager

**Class 1. DatabaseHelper - InterCommunicationPreference** This class manages communication-related preferences, such as tracking notification states, device power status, and which activity started a service. It enables different components of the application to share and access state information efficiently.

**Class 2. Database Helper - LogInPreference** This class handles user login information, allowing the app to store and retrieve credentials like group code, user ID, study ID, and password. It provides methods for setting, getting, and clearing user-related data, streamlining the login process and maintaining user sessions.
















**Class 10. EventMapBuilder** The `EventMapBuilder` class creates a complete map of event data by combining default fields with additional parameters. The `buildCompleteMap` method generates timestamps and initializes a HashMap with the event type and these timestamps, merging any extra fields provided, such as user ID, event source, or error messages. This utility simplifies the construction of event data for logging or reporting in applications.

**Class 11. EventTimestamp** This class manages timestamps in an Android app, providing both system and server time references. It initializes a timestamp using the system clock and can anchor it to a reference server time for accuracy. The class includes methods to retrieve formatted time strings: standard strings for precise, machine-readable formats (e.g., `yyyyMMddHHmmssSSS`) and friendly strings that are more human-readable (e.g., `yyyy-MM-dd HH:mm:ss`). Additionally, it can return the real-world time, typically based on a server reference or UTC, as well as local device time in milliseconds since the epoch. The class also tracks elapsed time since the system boot, providing insight into how long the app has been running. It implements Serializable, allowing timestamps to be saved across application sessions.

**Class 12. UploadEventsToFireStore** The `UploadEventsToFireStore` class manages the uploading of events to Firebase Firestore in an Android application. It contains methods to upload offline events from a local database, directly upload single events with unique identifiers, and log ticker events for tracking purposes. The class also updates the most recent event time in Firestore and generates simple class names for events by appending a timestamp and a random UUID. This functionality ensures efficient synchronization of event data while maintaining user-specific organization in the Firestore database.









