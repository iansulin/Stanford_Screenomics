## 01. Firebase Setup

### 01.1.  Create a Firebase Project
a. **Go to Firebase Console**:
   - Open [Firebase Console](https://console.firebase.google.com/).

b. **Add a New Project**:
   - Click **Add project**.
   - Enter a project name (e.g., "YourStudyName").
   - Optionally enable Google Analytics.
   - Click **Create Project** and then **Continue**.

> When setting up your Firebase project, think of a clear and descriptive name that reflects the purpose of your app or the project itself. In Firebase, the project name is a human-readable identifier you choose when creating a new Firebase project. Here are some key points about the project name:
> * **Purpose**: It helps you identify your project in the Firebase Console, especially if you have multiple projects. For example, you might name it after your app or its functionality (e.g., "YourStudyName").
> * **Flexibility**: You can choose any name that meets your needs, and it can include letters, numbers, and spaces.
> * **Not Unique**: The project name does not have to be unique across all Firebase projects. However, the project ID, which is generated automatically, must be unique within Firebase.
> * **Visibility**: The project name is visible to you and anyone else who has access to your Firebase Console, making it easier to manage and organize your projects.

---

### 01.2. Add Android App to Firebase
a. **Register Your App**:
   - In the Firebase project dashboard, click the Android icon.
   - Enter your app's package name (e.g., `edu.stanford.**mystudyapp**.screenomics`).
   - Optionally, add an app nickname and SHA-1 fingerprint.
   - Click **Register app**.

**App Package Name**
* The original package name of the Screenomics app is `edu.stanford.communication.screenomics`. Typically, the package name follows a 3-level hierarchical format, such as `edu.stanford.yourstudyname`, where each segment represents a different level in Android project: `edu` indicates the top-level domain, `stanford` can represent the second highest, and `mystudyapp` is the lowest. In the case of the Screenomics app, the package is organized into four levels: `edu`, `stanford`, `communication`, and `screenomics`.
* Changing multiple segments or reshaping the levels of the package hierarchy can be quite complex and is not advisable for those with limited experience, as it may lead to various configuration issues throughout the project. **The suggested action is to replace the `communication` part with your chosen study name in lowercase without spacing (e.g., `yourstudyname`), resulting in your new package name format of `edu.stanford.yourstudyname.screenomics`.** This keeps the overall structure intact while minimizing complexity and allowing you to personalize the app for your needs.
> * **Uniqueness**: The app package name is a unique identifier for your Android application, so it is essential to choose a distinctive study name. Therefore, the package name must be unique across all applications on the Google Play Store. It helps distinguish your app from others. 
> * **Not the App Name**: The app package name is not the same as the app's display name (the name users see on their device). The display name is set in your app’s resources and can be different from the package name.
 
b. **Download `google-services.json`**:
   - After registering your app, you will be prompted to download the `google-services.json` file.
   - Download the `google-services.json` file and save it to your computer.

> **What is `google-services.json`?** This file contains essential information about your Firebase project, such as API keys, project ID, and service configurations. It allows your Android app to connect to and use various Firebase services.
>    * **Firestore Database**: The google-services.json file allows your app to connect to Firestore, enabling data storage and retrieval.
>    * **Google Cloud Storage**: It facilitates access to Google Cloud Storage for uploading and downloading files.
>    * **Crashlytics**: The file also configures Crashlytics for error reporting, ensuring that crash data is sent to your Firebase project.

---
 
### 01.3. Enable Firestore Database
a. **Go to Firestore Database**:
   - In your Firebase Console, click on **Build > Firestore Database** in the left sidebar.

b. **Create Database**:
   - Click **Create Database**.
   - Choose **Location** (nam5, if you are in United States).
   - Choose **Start in Production Mode** (this ensures that all third party reads and writes are denied).
   - Click **Next**, then select a location for your database (choose the closest region).
   - Click **Done**.

---

### 01.4. Add Settings Profiles
a. Click on the "Start collection" button.
   - Enter **Collection ID**: **`settings_profiles`**
   - Click **Next**.

b. Click on "Add document" button inside the `settings_profiles` collection just added.
   - Enter **Document ID**: **`\_default_\`**
      - Each document ID represents a group profile.
   - In the field input area, add all configurable dynamic parameters one by one, listed in [Section Firebase - 05.1. Dynamic Parameters](stanford_screenomics/Firebase/05_Settings.md).

This process has to be manually done before the app compilation in android studio. The default profile (a must) and all study group profiles (optional) need to be configured before the first time app use because the screenomics app draws the dynamic parameter values from the profile whose code was specified during the account registration in the app. 


### 01.5. 



---

### 01.4. Enable Crashlytics
a. **Enable Crashlytics**:
   - In your Firebase Console, click on **Run > Crashlytics** in the left sidebar.
   - Click **Get Started** to enable Crashlytics for your project.
   - Follow any prompts to complete the setup.

> If you don't see the "Get Started" option in the Crashlytics dashboard of the Firebase Console, possible Reasons are:
> * **Already Enabled**: If you have previously set up Crashlytics for your project, it might already be enabled. You should be able to see crash reports and other data without needing to go through the setup process again.
> * **Project Settings**: Check the project settings to confirm that Crashlytics is enabled. You can do this by navigating to the Project Settings in the Firebase Console and looking under the Integrations tab.
> * **No Data Yet (Most Likely)**: If you recently set up Crashlytics and haven't triggered any crashes or if your app has not been used yet, you might not see any data or prompts.


