## 02. Android Studio Setup

This documentation provides instructions on how to download and use the open-source Stanford Screenomics platform project from GitHub in Android Studio to build your own study app.

Before you begin:
* Ensure you have your `google-services.json` file ready. This file can be obtained from the the Firebase console for your project ([Refer Section 01.2.b](../AppCompilation/01_Firebase-Setup.md)).
* 


### 02.1. Install Android Studio

a. **Download Android Studio**:
   - Go to the [Android Studio download page].
   - Download the version suitable for your operating system (Windows, macOS, or Linux).

b. **Install Android Studio**:
   - Run the downloaded installer and follow the on-screen instructions.
   - Ensure that you install the Android SDK and other recommended components during the installation.

c. **Launch Android Studio**:
   - After installation, open Android Studio. You may be prompted to import settings from a previous installation; you can start fresh.

---

### 02.2. Download the Project from GitHub

a. **Find the Repository**:
   - Go to the GitHub page of the project file: ABCDESAFDASDFASFVGASD

b. **Download the Project**:
   - Click on the green **Code** button.
   - Select **Download ZIP** from the dropdown menu.
   - Save the ZIP file to your computer.

c. **Extract the ZIP File**:
   - Locate the downloaded ZIP file and extract it to a folder of your choice.

---

### 02.3. Add the google-services.json File
a. **Locate the First Directory**:
   - Inside the extracted project folder, go to `databaseManager/`.

b. **Add the `google-services.json` File to the First Directory**:
   - Copy the downloaded `google-services.json` file from your computer.
   - Paste it into the `databaseManager/` directory of the project.

c. **Locate the Second Directory**:
   - Inside the extracted project folder, go to `app/`.

d. **Add the `google-services.json` File to the Second Directory**:
   - Copy the downloaded `google-services.json` file from your computer.
   - Paste it into the `app/` directory of the project.
   - Rename the file to `google-services_two.json`.

> What Does `google-services.json` Do in Android Studio?
> * **Configuration**: The google-services.json file configures your app to communicate with Firebase services. It contains information for authentication, database access, and other service integrations.
> * **Automatic Configuration**: When you include the Firebase SDKs in your project and apply the google-services plugin in your build.gradle file, the file ensures that the SDKs know how to connect to your Firebase project and which services are enabled.
---

### 02.4. Open the Project in Android Studio

a. **Launch Android Studio**:
   - If you haven't opened it yet, start Android Studio.

b. **Open the Project**:
   - Click on **Open an existing Android Studio project**.
   - Navigate to the folder where you extracted the project, select it and click **OK**.
   - - After opening the project, Android Studio should automatically recognize the new files, `google-services.json` and `google-services_two.json`.
     - Even if prompted, do not click on the **Sync Project with Gradle Files** button yet at the moment.

---

### 02.5. Refactor Package Name





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


