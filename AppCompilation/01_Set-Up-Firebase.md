## 01. Set Up Firebase

### 01.1.  Create a Firebase Project
a. **Go to Firebase Console**:
   - Open [Firebase Console](https://console.firebase.google.com/).

b. **Add a New Project**:
   - Click **Add project**.
   - Enter a project name (e.g., "MyScreenomicsApp").
   - Optionally enable Google Analytics.
   - Click **Create Project** and then **Continue**.

> When setting up your Firebase project, think of a clear and descriptive name that reflects the purpose of your app or the project itself. In Firebase, the project name is a human-readable identifier you choose when creating a new Firebase project. Here are some key points about the project name:
> * **Purpose**: It helps you identify your project in the Firebase Console, especially if you have multiple projects. For example, you might name it after your app or its functionality (e.g., "MyScreenomicsApp").
> * **Flexibility**: You can choose any name that meets your needs, and it can include letters, numbers, and spaces.
> * **Not Unique**: The project name does not have to be unique across all Firebase projects. However, the project ID, which is generated automatically, must be unique within Firebase.
> * **Visibility**: The project name is visible to you and anyone else who has access to your Firebase Console, making it easier to manage and organize your projects.

---

### 01.2. Add Android App to Firebase
a. **Register Your App**:
   - In the Firebase project dashboard, click the Android icon.
   - Enter your app's package name (e.g., `com.example.myscreenomicsapp`).
   - Optionally, add an app nickname and SHA-1 fingerprint.
   - Click **Register app**.

> The app package name is a unique identifier for your Android application, and it serves several important purposes:
> * **Uniqueness**: The package name must be unique across all applications on the Google Play Store. It helps distinguish your app from others. For example, com.example.myapp is a typical format.
> * **Naming Convention**: Package names are usually structured in a reverse domain format:
>   * **Domain**: If you own a domain (like example.com), you might use that as part of your package name.
>   * **App Name**: Include the application name or a relevant identifier at the end (e.g., `com.example.myscreenomicsapp`).
> * **Not the App Name**: The app package name is not the same as the app's display name (the name users see on their device). The display name is set in your app’s resources and can be different from the package name.
> * **Example**: For an app named "MyScreenomicsApp" with a domain of example.com, the package name might look like this:
>    * **Package Name**: `com.example.myscreenomicsapp`

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
   - In your Firebase Console, click on **Firestore Database** in the left sidebar.
b. **Create Database**:
   - Click **Create Database**.
   - Choose **Start in Test Mode** (this allows read/write without security rules, suitable for development).
   - Click **Next**, then select a location for your database (choose the closest region).
   - Click **Done**.

---

### 01.4. Enable Crashlytics
a. **Enable Crashlytics**:
   - In your Firebase Console, click on **Crashlytics** in the left sidebar.
   - Click **Get Started** to enable Crashlytics for your project.
   - Follow any prompts to complete the setup.




3. **Add `google-services.json` to Your Project**:
   - Open Android Studio.
   - Copy the `google-services.json` file into the `app/` directory of your Android project.

What Does google-services.json Do in Android Studio?
Configuration: The google-services.json file configures your app to communicate with Firebase services. It contains information for authentication, database access, and other service integrations.
Automatic Configuration: When you include the Firebase SDKs in your project and apply the google-services plugin in your build.gradle file, the file ensures that the SDKs know how to connect to your Firebase project and which services are enabled.
