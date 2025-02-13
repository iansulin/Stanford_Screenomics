## 01. Set Up Firebase

### 01.1.  Create a Firebase Project
a. **Go to Firebase Console**:
   - Open [Firebase Console](https://console.firebase.google.com/).
b. **Add a New Project**:
   - Click **Add project**.
   - Enter a project name (e.g., "MyAndroidApp").
   - Optionally enable Google Analytics.
   - Click **Create Project** and then **Continue**.

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
