## 2.03. Android Studio Setup

**Before you begin:**
* Ensure you have your `google-services.json` file ready. This file can be obtained from the the Firebase console for your project [[See Chapter 2 - Section 02.2.b](../Ch2_App-Compilation/02_Firebase-Setup.md)].
* Have your Cloud Storage `gs://` URI ready [[See Chapter 2 - Section 02.6.d](../Ch2_App-Compilation/02_Firebase-Setup.md)]. 

---

### 2.03.1. Install Android Studio

a. **Download Android Studio**
   - Go to the [Android Studio download page].
   - Download the version suitable for your operating system (Windows, macOS, or Linux).

b. **Install Android Studio**
   - Run the downloaded installer and follow the on-screen instructions.
   - Ensure that you install the Android SDK and other recommended components during the installation.

c. **Launch Android Studio**
   - After installation, open Android Studio. You may be prompted to import settings from a previous installation; you can start fresh.

---

### 2.03.2. Download the Project from GitHub

a. **Find the Repository**
   - [Go to the GitHub page of the project file](../Project-Files)

b. **Download the Project**
   - Click on the green **Code** button.
   - Select **Download ZIP** from the dropdown menu.
   - Save the ZIP file to your computer.

c. **Extract the ZIP File**
   - Locate the downloaded ZIP file and extract it to a folder of your choice.

---

### 2.03.3. Add the google-services.json File

a. **Launch Android Studio**
   - If you haven't opened it yet, start Android Studio.

c. **Open the Project**
   - Click on **Open an existing Android Studio project**.
   - Navigate to the extracted project folder, select it and click **OK**.
     - If prompted, do **NOT** click on the **Sync Project with Gradle Files** yet.

d. **Locate "App" Module**
   - In the **Project View** (usually on the left), click the dropdown at the top and select **Android** to display the project structure.
   - Find the **app module** in the project view, which has **a folder icon with a green square**. This module is the main app component that gets built into an APK or AAB (Android App Bundle).
      - Do not confuse it with the apps module, which has a folder icon with a bar chart.

e.  **Add the JSON File to the "App" Module**
   - Open the folder on your computer where the `google-services.json` file is located.
   - Copy the `google-services.json` file (`Command + C` or `Ctrl + C`).
   - In Android Studio, **Select the App Module** (the folder icon with a green square).
   - **Paste the JSON file into the App Module**.
   - If/When a "Copy" or "Replace" window appears, do **NOT** change any settings--simply click **OK**.

f. **Locate "databaseManager" Module**
   - Find the **databaseManager module**, which has **a folder icon with a bar chart**. This module is one of two core base modules of the Screenomics platform, providing reusable database-related methods for other data collection modules.

g. **Add the JSON File to the "databaseManager" Module**
   - Open the folder on your computer where the `google-services.json` file is located.
   - Copy the `google-services.json` file (`Command + C` or `Ctrl + C`).
   - In Android Studio, **Select the databaseManager Module** (the folder icon with a bar chart).
   - **Paste the JSON file into the App Module**.
   - If/When a "Copy" or "Replace" window appears, do **NOT** change any settings--simply click **OK**.

> What Does `google-services.json` Do in Android Studio?
> * **Configuration**: The `google-services.json` file configures your app to communicate with Firebase services. It contains information for authentication, database access, and other service integrations.
> * **Automatic Configuration**: When you include the Firebase SDKs in your project and apply the `google-services` plugin in your `build.gradle` file, the file ensures that the SDKs know how to connect to your Firebase project and which services are enabled.

> Why Add `google-services.json` to Two Modules?
> * In a **multi-module** Android project, you need to add `google-services.json` to each module that directly interacts with Firebase services. The reason you must do this twice (once for the app module and once for the databaseManager module) is due to how Firebase and Gradle handle dependencies in a multi-module setup.
> * In Stanford Screenomics platform, the `databaseManager` module is not just a dependency but a standalone module that provides reusable database-related methods. Since it interacts directly with Firebase services (i.e., **Firestore Database**, **Google Cloud Storage**), it requires its own `google-services.json` file for proper authentication and access.
> * Other data collection modules do not need their own `google-services.json` file because they rely on the reusable methods provided by `databaseManager`, which already has Firebase access.
> * The main `app` module does depend on `databaseManager` for database operations, but it still requires its own `google-services.json` file because it **interacts with Firebase directly in ways that databaseManager does not fully cover** (i.e., **Firebase Authentication**, **Crashlytics**).

---

### 2.03.4. Refactor App Package Name

a. **Disable Compact Middle Packages**
   - In the Project panel, click on the three-dot menu (⋮) in the top-right corner.
   - **Appearance** > **Uncheck** "**Compact Middle Packages**" from the dropdown menu.

b. **Expand the Package Structure**
   - Once "Compact Middle Packages" is disabled, the app package name `edu.stanford.communication.screenomics` will split into individual folders `edu > stanford > communication > screenomics`.
      - You can now **rename or refactor** specific package segments separately.
      - To verify this split hierarchical structure, navigate to: `app module > java > edu > stanford > communications > screenomics` and expand the folders.

c. **Refactor/Rename `communication` segment**
   - In the hierarchical structure, navigate to: `app > src > main > java > edu > stanford > communication > screenomics` and **Select the `communication` folder** inside the app module.
   - **Right-click** on the communication folder and **Select Refactor > Rename**.
   - When prompted with: "Rename Package Directories: Package 'edu.stanford.communication' is present in multiple directories: ...", **Select "All Directories"** to ensure all occurrences are updated.
   - In the "Rename" window, **change `communication` to `yourstudyname`**.
      - Ensure the option "Rename package 'edu.stanford.communication' and its usages to:" reflects the new name.
   - Click on the "**Refactor**" to apply the changes.
      - The `communication` folder should now be renamed to `yourstudyname` in the package hierarchy.
      - Open other module folders (e.g., databaseManager) and ensure that all occurrences of `communication` have been successfully renamed to `yourstudyname`.

d. **Manually Replace Any Remaining References**
   - Press `Command + Shift + R` or `Ctrl + Shift + R` to open the **Find & Replace in Path** tool.
   - In the **Find** field, enter: `stanford.communication`
   - In the **Replace with** field, enter: `stanford.yourstudyname`
   - Click "**Replace All**" to ensure all occurrences are updated in the entire project.

---

### 2.03.5. Cloud Storage Link Replace

a. **Search** for the old URI in your project
   - Press `Command + Shift + F` or `Ctrl + Shift + F` to open **Find in Path**.
   - **Enter the following in the search bar: `gs://old-bucket-name`**.
      - This will help locate all instances where the Cloud Storage URI is used in your project.
   - **Locate the Storage Reference Code**
```java
mStorageRef = FirebaseStorage.getInstance("gs://old-bucket-name").getReference();
```
   - **Replace the old URI with your new URI**
```java
mStorageRef = FirebaseStorage.getInstance("gs://yourstudyname-f6198.firebasestorage.app").getReference();
```
b. **Save** changes (`Command + S` or `Ctrl + S`)

---

### 2.03.6. Update Password

a. **Search** for the old password in your project
   - Press `Command + Shift + F` or `Ctrl + Shift + F` to open **Find in Path**.
   - **Enter the following in the search bar: `0ldPassword!`**.
   - **Locate the Password Reference Code**
```java
public static final String PASSWORD_RESTRICTION = "0ldPassword!";
```
   - **Replace the old password with your new URI**
```java
public static final String PASSWORD_RESTRICTION = "NewPassw0rd?";
```
b. **Save** changes (`Command + S` or `Ctrl + S`)

---

### 2.03.7. Module Activation/Deactivation

a. **Locate "moduleManager" Module** 
   -  Find the **moduleManager module**, which has **a folder icon with a bar chart**. This module is another core base module of the Screenomics platform, that serves as the central control system, overseeing the operational status of various data collection modules.

b. **Open `ModuleController`**
   - Navigate to: `moduleManager > java > edu.stanford.yourstudyname.screenomics.modulemanager > ModuleController`.
   - Double-click to open it.

c. Activate or Deactivate Modules

``` java   
public static boolean ENABLE_MODULE-NAME = true;
```
   - Locate the above lines in `ModuleController`:
      - There are nine lines like this, each indicating the activation status of a data collection module.
      - The boolean value `true` indicates the module is active, while `false` indicates it is inactive.
   - Set each module's boolean value according to your specific study requirements:
      - By default, all data collection modules are activated (`= true;`)
      - Deactivate modules by setting them to false; (`= false;`)

> The activation or deactivation of a module does not impact the overall performance of the app, as each module operates independently.

> Deactivated modules will NOT collect any data, so **check in advance what event data types are collected by which module** [[See Firebase - Events](../Ch1_Firebase/04_Events.md)].

* **Important Note:** If this is your first time compiling the Screenomics app, it is **NOT advised to deactivate** any modules. During runtime validation, it's essential to ensure that all types of event data are correctly sent to the database. **Testing all modules' functionality is recommended.** You can revisit this step to deactivate any modules after the initial runtime validation.
   
Exemple 1. Eight modules activated, one module deactivated. 
   - The compiled app will collect all types of event data except `StepCountEvent`. 
```java
package edu.stanford.yourstudyname.screenomics.modulemanager;

public class ModuleController {
    public static boolean ENABLE_SCREENSHOTS = true;
    public static boolean ENABLE_APPS = true;
    public static boolean ENABLE_INTERACTIONS = true;
    public static boolean ENABLE_ACTIVITIES = false;
    public static boolean ENABLE_LOCATIONS = true;
    public static boolean ENABLE_BATTERY = true;
    public static boolean ENABLE_POWER = true;
    public static boolean ENABLE_NETWORK = true;
    public static boolean ENABLE_SPECS = true;
}
```

Example 2. Three modules activated, six modules deactivated.
   - The compiled app will only collect 4 types of event data: `NewForegroundAppEvent` through "Apps" module, `BatteryStateEvent` and `BatteryChargingEvent` from "Battery" module, and `SystemPowerEvent` from "Power" module.  
```java
package edu.stanford.yourstudyname.screenomics.modulemanager;

public class ModuleController {
    public static boolean ENABLE_SCREENSHOTS = false;
    public static boolean ENABLE_APPS = true;
    public static boolean ENABLE_INTERACTIONS = false;
    public static boolean ENABLE_ACTIVITIES = false;
    public static boolean ENABLE_LOCATIONS = false;
    public static boolean ENABLE_BATTERY = true;
    public static boolean ENABLE_POWER = false;
    public static boolean ENABLE_NETWORK = true;
    public static boolean ENABLE_SPECS = false;
}
```

---

### 2.03.8. Change App Name

a. **Locate `strings.xml`**
   - In the hierarchical structure, navigate to: `app > res > values`
      - Ensure you open `res` folder, **NOT** `res (generated)` folder.
      - Remember that the `res` folder is for managing your app's resources, while the `res (generated)` folder is for internal use by the build system and should not be modified directly.
   - Double-click `strings.xml` file to open it.

b. **Locate the App Name Reference Code** (line 2)
```java
<string name="app_name">Screenomics</string>
```

c. **Change `Screenomics` to your desired app name.**
```java
<string name="app_name">Your App Name</string>
```

---

### 2.03.9. Clean and Build the Project

a. **Clean the Project**
   - Go to the menu bar and select **Build > Clean Project**
      - This will remove any previously compiled files and clean the project's output directories.

b. **Build the Project**
   - After cleaning, go to **Build > Rebuild Project**
      - This will compile all the files and create a new build of the project.
      
c. **Monitor the Build Output**
   - **Open the Build Output Window**
      - The **Build Output** window is usually located at the bottom of Android Studio.
      - If it’s not visible, go to **View > Tool Windows > Build **to open it.
   - Check **Build Messages**
      - In the Build Output window, you will see messages indicating the progress of the build process. Look for messages that say:
      - "**BUILD SUCCESSFUL**": This means your project has been built without any errors > Proceed to [Section 4](../Ch2_App-Compilation/04_Runtime-Validation.md).
      - "**BUILD FAILED**": This indicates that there were errors during the build process. Check if any previous steps were missed or misconfigured.



[Back to Top](#top)
