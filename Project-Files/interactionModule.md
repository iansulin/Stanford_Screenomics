## Reusable Components in `moduleManager` and `databaseManager`

This document provides an overview of the most important reusable classes and methods in `moduleManager` and `databaseManager`. These components ensure consistent event processing, timestamping, storage, and uploading across different modules.

---

## moduleManager

### Purpose
- Generates timestamps for all events.
- Defines event metadata to ensure consistency across modules.

---

### Reusable Classes

#### EventTimestamp
**Purpose:**
- Generates timestamps in UTC format and local time (PT).
- Ensures all events use consistent timestamps across modules.

**Where?**
- `moduleManager/EventTimestamp.java`

**How to Use It?**
```java
EventTimestamp timestamp = new EventTimestamp();
String eventTime = timestamp.getTimestring();
String localTime = timestamp.getSystemClockTimestring();
```
**Method Definitions**
```java
public String getTimestring() {
    return isServerTimeSet() ? getRealTimestring() : getSystemClockTimestring();
}
public String getSystemClockTimestring() {
    return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(Calendar.getInstance().getTime());
}
```
**Dependencies in Data Flow**  
- Used in `databaseManager.EventData.Builder` (assigns timestamps when an event is created).  
- Ensures all event logs and Firestore data are timestamped properly.  

---

#### ModuleCharacteristics
**Purpose:**
- Defines event metadata (event type, ID, ticker updates).
- Ensures each event has a structured format before storage.

**Where?**
- `moduleManager/ModuleCharacteristics.java`

**How to Use It?**
```java
Map<String, String> metadata = ModuleCharacteristics.getInstance().getNetworkEventCharacteristics();
```
**Method Definitions**
```java
public Map<String, String> getNetworkEventCharacteristics() {
    return new ModuleCharacteristicsData("InternetEvent", "Internet", "1").toMap();
}
```
**Dependencies in Data Flow**  
- Used in `EventOperationManager.addEvent()` to define event characteristics.  
- Ensures all events follow a standardized format.  

---

### Reusable Methods

#### EventTimestamp.getTimestring()
**Purpose:**
- Returns a formatted timestamp in UTC.
- Ensures all events have synchronized timestamps.

**How to Use It?**
```java
String timestamp = new EventTimestamp().getTimestring();
```
- Used in `EventData.Builder` to timestamp events.

---

#### ModuleCharacteristics.getEventCharacteristics()
**Purpose:**
- Fetches metadata for event types.

**How to Use It?**
```java
Map<String, String> metadata = ModuleCharacteristics.getInstance().getBatteryStateEventCharacteristics();
```
- Used in `EventOperationManager.addEvent()` to classify event types.

---

## databaseManager

### Purpose
- Manages event storage, timestamps, and batch uploading to Firestore.
- Ensures data consistency across different modules.

---

### Reusable Classes

#### DataStorage
**Purpose:**
- Stores event timestamps before batch upload to Firestore.

**Where?**
- `databaseManager/DataStorage.java`

**How to Use It?**
```java
DataStorage.getInstance().addEvent("BatteryEvent", "20250215123045678");
```
**Method Definitions**
```java
public void addEvent(String eventName, String timestamp) {
    eventTimestampMap.put(eventName, timestamp);
}
public ConcurrentHashMap<String, String> getEvents() {
    return new ConcurrentHashMap<>(eventTimestampMap);
}
```
**Dependencies in Data Flow**  
- Used in `EventUploader` to fetch stored events before uploading.

---

#### EventData
**Purpose:**
- Formats event data before saving it in SQLite and Firestore.

**Where?**
- `databaseManager/EventData.java`

**How to Use It?**
```java
EventData event = new EventData.Builder("BatteryEvent", "BatteryStateChange")
    .addField("status", "Charging")
    .build();
```
**Dependencies in Data Flow**  
- Used in `EventOperationManager` when an event is created.  
- Ensures each event is properly formatted before storage.  

---

### Reusable Methods

#### EventOperationManager.addEvent()
**Purpose:**
- Centralized event processing (timestamps, buffering, storage).

**Where?**
- `databaseManager/EventOperationManager.java`

**How to Use It?**
```java
EventOperationManager.getInstance(context).addEvent(
    ModuleCharacteristics.getInstance().getInteractionEventCharacteristics(),
    userInput
);
```
- Ensures events are buffered and saved in SQLite.

---

#### EventData.Builder.addField()
**Purpose:**
- Dynamically add extra fields to events.

**How to Use It?**
```java
eventData.addField("batteryLevel", "85%");
```
- Ensures flexible event formatting.

---

#### EventUploader.uploadImmediately()
**Purpose:**
- Forces immediate event upload to Firestore (bypasses batch processing).

**How to Use It?**
```java
EventUploader.getInstance(context).uploadImmediately(eventData);
```
- Used for time-sensitive events like logouts.

---

## Final Summary

| Category | Class / Method | Purpose | Where? |
|-------------|----------------|-----------|----------|
| moduleManager | `EventTimestamp` | Generates timestamps | `moduleManager` |
|  | `ModuleCharacteristics` | Defines event metadata | `moduleManager` |
|  | `getTimestring()` | Provides UTC timestamp | `moduleManager` |
| databaseManager | `DataStorage` | Stores temporary events | `databaseManager` |
|  | `EventData` | Formats event data | `databaseManager` |
|  | `addEvent()` | Logs events to buffer | `databaseManager` |
|  | `uploadImmediately()` | Uploads critical events | `databaseManager` |

- Developers should always use these reusable components to ensure consistency.

