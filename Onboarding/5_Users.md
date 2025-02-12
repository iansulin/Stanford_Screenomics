
## Users

Every user of the app, when signing up, registers with:
* **Code**: A sequence of letters and/or numbers identifying which study group the user is in
* **Number**: A number identifying this specific user, to help record who this actually is
* **StudyID**: A study name
* **Password**: A password users must know in order to register successfully (so that only
onboarded participants can participate). It needs to be configured during the app compilation process.

In the database, special characters (such as the @ sign if there is any in the StudyID or group Code). So,
all usernames are condensed into **[code][number][studyid]**, with spaces and special characters
omitted, and converted to lowercase. An example is “**int101studyone**” if **Code = INT, Number = 101, and StudyID = StudyOne**.

---

### Crash Logging

The Stanford Screenomics utilize Crashlytics to efficiently notify developers of crashes. This mechanism enhances our ability to debug issues and provides a more streamlined approach to crash reporting.

When the app experiences a crash, Crashlytics automatically captures the event, eliminating the need for manual log creation. Crash reports are sent in real-time and include comprehensive information such as:
* **Timestamp**: The exact time when the crash occurred.
* **User Identifier**: A unique identifier for the user who experienced the crash (ensuring privacy and compliance with data regulations).
* **Logcat Dump**: A detailed log of the app's activities leading up to the crash, which is invaluable for debugging.
* **Stack Trace**: A complete stack trace of the crash, highlighting the code path that led to the failure.

In addition to crash reports, Crashlytics provides insights into non-fatal issues, allowing developers to track minor errors that may not terminate the app but still affect user experience.

The app maintains a "crashes" node in the database, which records any significant issues encountered by users. For each crash or significant error, an entry is created with relevant details, including:
* **App Crash**: When the app crashes and is terminated.
> * **Exception Class**: The type of Java exception that occurred.
> * **Exception Message**: A description of the exception.
* **Background Service Termination**: Android will occasionally kill off background services that have been running for a long time, and Screenomics is not exempt from this. If the Android system kills the Screenomics app, Crashlytics captures this event as well.
> * **Message**: A predefined message indicating that the service has been restarted (e.g., "MediaProjection was null on startup…").
> * **Tag**: Identifies the service affected (e.g., "CaptureUploadService").
These entries are generally benign and can be safely ignored, as the app automatically restarts itself, which is a normal part of its operation.







