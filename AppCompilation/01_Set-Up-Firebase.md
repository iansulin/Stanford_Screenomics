## 01. Set Up Firebase

### 01.1.  Create a Firebase Project
a. **Go to Firebase Console**:
   - Open [Firebase Console](https://console.firebase.google.com/).

b. **Add a New Project**:
   - Click **Add project**.
   - Enter a project name (e.g., "MyAndroidApp").
   - Optionally enable Google Analytics.
   - Click **Create Project** and then **Continue**.

> When setting up your Firebase project, think of a clear and descriptive name that reflects the purpose of your app or the project itself. In Firebase, the project name is a human-readable identifier you choose when creating a new Firebase project. Here are some key points about the project name:
> * **Purpose**: It helps you identify your project in the Firebase Console, especially if you have multiple projects. For example, you might name it after your app or its functionality (e.g., "MyStudyApp").
> * **Flexibility**: You can choose any name that meets your needs, and it can include letters, numbers, and spaces.
> * **Not Unique**: The project name does not have to be unique across all Firebase projects. However, the project ID, which is generated automatically, must be unique within Firebase.
> * **Visibility**: The project name is visible to you and anyone else who has access to your Firebase Console, making it easier to manage and organize your projects.

---

### 01.2. Add Android App to Firebase
a. **Register Your App**:
   - In the Firebase project dashboard, click the Android icon.
   - Enter your app's package name (e.g., `com.example.myapp`).
   - Optionally, add an app nickname and SHA-1 fingerprint.
   - Click **Register app**.
b. **Download `google-services.json`**:
   - Download the `google-services.json` file.
   - Save it to your computer.

---
 
### 01.3 Enable Firestore Database
a. **Go to Firestore Database**:
   - In your Firebase Console, click on **Firestore Database** in the left sidebar.
b. **Create Database**:
   - Click **Create Database**.
   - Choose **Start in Test Mode** (this allows read/write without security rules, suitable for development).
   - Click **Next**, then select a location for your database (choose the closest region).
   - Click **Done**.

---

### 3.4 Enable Crashlytics
a. **Enable Crashlytics**:
   - In your Firebase Console, click on **Crashlytics** in the left sidebar.
   - Click **Get Started** to enable Crashlytics for your project.
   - Follow any prompts to complete the setup.




3. **Add `google-services.json` to Your Project**:
   - Open Android Studio.
   - Copy the `google-services.json` file into the `app/` directory of your Android project.
