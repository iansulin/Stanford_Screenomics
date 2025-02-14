## 02. Android Studio Setup

This documentation provides instructions on how to download and use the open-source Stanford Screenomics platform project from GitHub in Android Studio to build your own study app.

Before you begin:
* Ensure you have your `google-services.json` file ready. This file can be obtained from the the Firebase console for your project ([Refer Section 01.2.b](../AppCompilation/01_Firebase-Setup.md)).
* 

---

### 02.1. Install Android Studio

a. **Download Android Studio**
   - Go to the [Android Studio download page].
   - Download the version suitable for your operating system (Windows, macOS, or Linux).

b. **Install Android Studio**
   - Run the downloaded installer and follow the on-screen instructions.
   - Ensure that you install the Android SDK and other recommended components during the installation.

c. **Launch Android Studio**
   - After installation, open Android Studio. You may be prompted to import settings from a previous installation; you can start fresh.

---

### 02.2. Download the Project from GitHub

a. **Find the Repository**
   - Go to the GitHub page of the project file: ABCDESAFDASDFASFVGASD

b. **Download the Project**
   - Click on the green **Code** button.
   - Select **Download ZIP** from the dropdown menu.
   - Save the ZIP file to your computer.

c. **Extract the ZIP File**
   - Locate the downloaded ZIP file and extract it to a folder of your choice.

---

### 02.3. Add the google-services.json File

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
   - Copy the `google-services.json` file (Command + C or Ctrl + C).
   - In Android Studio, **Select the App Module** (the folder icon with a green square).
   - **Paste the JSON file into the App Module**.
   - If/When a "Copy" or "Replace" window appears, do **NOT** change any settings--simply click **OK**.

f. **Locate "databaseManager" Module**
   - Find the **databaseManager module**, which has **a folder icon with a bar chart**. This module is one of two core base modules of the Screenomics platform, providing reusable database-related methods for other data collection modules.

g. **Add the JSON File to the "databaseManager" Module**
   - Open the folder on your computer where the `google-services.json` file is located.
   - Copy the `google-services.json` file (Command + C or Ctrl + C).
   - In Android Studio, **Select the databaseManager Module** (the folder icon with a bar chart).
   - **Paste the JSON file into the App Module**.
   - If/When a "Copy" or "Replace" window appears, do **NOT** change any settings--simply click **OK**.

> What Does `google-services.json` Do in Android Studio?
> * **Configuration**: The google-services.json file configures your app to communicate with Firebase services. It contains information for authentication, database access, and other service integrations.
> * **Automatic Configuration**: When you include the Firebase SDKs in your project and apply the google-services plugin in your build.gradle file, the file ensures that the SDKs know how to connect to your Firebase project and which services are enabled.

> Why Add `google-services.json` to Two Modules?
> In a multi-module Android project, you need to add `google-services.json` to each module that directly interacts with Firebase services. The reason you must do this twice (once for the app module and once for the databaseManager module) is due to how Firebase and Gradle handle dependencies in a multi-module setup.
> In Stanford Screenomics platform, the `databaseManager` module is not just a dependency but a standalone module that provides reusable database-related methods. Since it interacts directly with Firebase services (i.e., Firestore Database, Google Cloud Storage), it requires its own `google-services.json` file for proper authentication and access.
> Other data collection modules do not need their own `google-services.json` file because they rely on the reusable methods provided by `databaseManager`, which already has Firebase access.
> The main `app` module does depend on `databaseManager` for database operations, but it still requires its own `google-services.json` file because it interacts with Firebase directly in ways that databaseManager does not fully cover (i.e., Firebase Authentication, Crashlytics).



---




---

### 02.5. Refactor Package Name


Cloud Storage link replace


---

### 02.6. Sync the Project

a. **Gradle Sync**:
   
   -  (the elephant icon) in the toolbar.

b. **Resolve Issues**:
   - If there are any errors or unresolved dependencies, Android Studio will show them in the **Build** window. Follow the instructions to resolve these issues.

---












---
3. **Add `google-services.json` to Your Project**:
   - Open Android Studio.
   - Copy the `google-services.json` file into the `app/` directory of your Android project.


