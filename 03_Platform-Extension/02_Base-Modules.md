## Base Modules


---

### Base Module 1. Module Manager


#### Reusable Classes/Methods

The Module Manager module does not utilize any methods or classes from the Database Manager module. However, the Database Manager module does make use of classes and methods from the Module Manager module, specifically the `EventTimestamp` and `ModuleCharacteristics` classes. `EventTimestamp` is focused on time management and formatting, while `ModuleCharacteristics` centralizes the definitions and configurations for various modules in the application. Together, they help manage events effectively by providing time-related data and relevant characteristics for each event type.

 * **`EventTimestamp`**
   * **Purpose**: This class is designed to handle timestamps related to events, providing functionality to capture and format timestamps based on system time or server time.
   * **Key Features**:
     * **Timestamps**: It can create timestamps in different formats, including a machine-readable format and a human-friendly format.
     * **Time Zones**: The class accounts for time zones, allowing for timestamps to be generated in UTC or specific local times (e.g., Los Angeles).
     * **Serialization**: It supports serialization, enabling timestamps to be preserved across application restarts.
     * **Reference Time**: It maintains reference values for server time and elapsed real-time to calculate real-world timestamps accurately.
     * **Methods**:
       * `getTimestring()`: Returns the best available timestamp string.
       * `refresh()`: Adjusts the timestamp based on known server time.
       * `getRealTimeMillis()`: Provides the real-world time in milliseconds since the epoch.

**Example from the `EventData` class:**
```
public class EventData {
    private final String time;
    private final String timeLocal;
    private final String type;
    private final String UniqueEventId;
    private final Map<String, String> additionalFields;

    // Private constructor to enforce the use of Builder
    private EventData(Builder builder) {
        this.time = builder.time;
        this.UniqueEventId = builder.UniqueEventId;
        this.timeLocal = builder.timeLocal;
        this.type = builder.type;
        this.additionalFields = builder.additionalFields;
    }

    // Builder Class for creating EventData objects efficiently
    public static class Builder {
        private final String type;
        private final String UniqueEventId;
        private final String time;
        private final String timeLocal;
        private Map<String, String> additionalFields = new HashMap<>();
        EventTimestamp timestamp = new EventTimestamp();

        public Builder(String type, String UniqueEventId) {
            this.time = timestamp.getTimestring(); // Get the timestamp for the event.
            this.UniqueEventId = GenerateEventId(UniqueEventId); // Generate a unique ID.
            this.timeLocal = timestamp.getSystemClockTimestring(); // Get local timestamp.
            this.type = type;
        }

        // Other methods...
    }
}
```

- In the `EventData.Builder` class, an instance of `EventTimestamp` is created. The time is set using `timestamp.getTimestring()`, which provides the best available timestamp. It also gets the local time using `timestamp.getSystemClockTimestring()`. This ensures each event has accurate time information.

* **`ModuleCharacteristics`**
  * **Purpose**: This singleton class defines characteristics for various modules within the application. It contains metadata about different event types and their configurations.
  * **Key Features**:
    * **Singleton Pattern**: Ensures only one instance of the class is created, providing a global point of access.
    * **Event Characteristics**: It provides methods to retrieve characteristics for various events (e.g., location events, battery state, screen on/off, etc.).
    * **Data Representation**: Each event characteristic is represented as a map, encapsulating details such as event name, type, and other relevant parameters.
    * **Methods**:
      * `getLocationEventCharacteristics()`: Returns characteristics related to GPS location events.
      * `getPowerScreenOnOffCharacteristics()`: Retrieves characteristics for screen on/off events.
      * ...
      * Other similar methods for different event types.

**Example from the `EventData` class:**
```
public class EventData {
    // Builder Class for creating EventData objects efficiently
    public static class Builder {
        // Other fields...

        public String GenerateEventId(String className) {
            return className + " " + timestamp + "_" + 
                   ModuleCharacteristics.getInstance().getLocationEventCharacteristics().get("id");
        }
    }
}
```

- The `GenerateEventId` method retrieves the ID from the characteristics of a location event using `ModuleCharacteristics.getInstance().getLocationEventCharacteristics().get("id")`. This allows the `EventData` to be associated with its corresponding module characteristics, providing context for what kind of event it represents.


#### **ModuleController** 

The `ModuleController` class primarily consists of static boolean fields (`true` [module = activated]/`false` [module = deactivated]) that act as flags to activate or deactivate modules in the application. There are no methods defined in this class; it serves mainly as a configuration holder for module activation states.

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

The `EventTimestamp` class of Module Manager module is primarily utilized in the `EventOperationManager` class, allowing it to manage event timing effectively within the Database Manager module.

**A relevant snippet from the `EventOperationManager` class:**
```
public void addEvent(Map<String, String> moduleInfo, HashMap<String, String> eventDetails) {
    synchronized (LOCK) {
        EventData event = new EventData.Builder(moduleInfo.get("type"), moduleInfo.get("className"))
                .addFields(eventDetails)
                .build();

        // Use timestamp to log the current time
        DataStorage.getInstance().addEvent("MostRecentEventTime", timestamp.getTimestringFriendly());
    }
}
```
- `EventTimestamp` provides the current timestamp using `timestamp.getTimestringFriendly()`, which is used to log the most recent event time in the `DataStorage` class. This shows how the `EventOperationManager` relies on the `EventTimestamp` class to ensure that all events have accurate and formatted timestamps.



The `ModuleCharacteristics` class of Module Manager module is utilized to provide metadata about different types of events, **ensuring that each event can be properly identified and contextualized**.

**A relevant snippet from the `ModuleCharacteristics` class:**
```
public Map<String, String> getLocationEventCharacteristics() {
    return new ModuleCharacteristicsData("GPSLocationEvent", "location", "1").toMap();
}

public Map<String, String> getNetworkEventCharacteristics() {
    return new ModuleCharacteristicsData("InternetEvent", "Internet", "1").toMap();
}
```
- `getLocationEventCharacteristics` returns a map containing characteristics specific to location events (e.g., event name, type, and an update ticker). `getNetworkEventCharacteristics` returns a similar map for network-related events, allowing the application to understand what data is associated with network changes. This enables different modules to utilize the same characteristics to maintain consistency in how events are handled. For instance, events logged from both the Location module and the Network module can reference the same metadata structure.

#### Reusable Classes/Methods

* **`EventOperationManager`**



