## 02. Android Studio Setup

This documentation provides instructions on how to download and use the open-source Stanford Screenomics platform project from GitHub in Android Studio to build your own study app.


### 02.1. Install Android Studio

a. **Download Android Studio**:
   - Go to the [Android Studio download page].
   - Download the version suitable for your operating system (Windows, macOS, or Linux).

b. **Install Android Studio**:
   - Run the downloaded installer and follow the on-screen instructions.
   - Ensure that you install the Android SDK and other recommended components during the installation.

c. **Launch Android Studio**:
   - After installation, open Android Studio. You may be prompted to import settings from a previous installation; you can start fresh.

### 02.2. Download the Project from GitHub

a. **Find the Repository**:
   - Go to the GitHub page of the open-source Android project you want to use.

b. **Download the Project**:
   - Click on the green **Code** button.
   - Select **Download ZIP** from the dropdown menu.
   - Save the ZIP file to your computer.

c. **Extract the ZIP File**:
   - Locate the downloaded ZIP file and extract it to a folder of your choice.

### Step 3: Open the Project in Android Studio

1. **Launch Android Studio**:
   - If you haven't opened it yet, start Android Studio.

2. **Drag and Drop the Project Folder**:
   - Open the folder where you extracted the project.
   - Drag the entire project folder into the Android Studio window.

3. **Open the Project**:
   - Release the folder in Android Studio. This will prompt you to open the project.

### Step 4: Sync and Build the Project

1. **Gradle Sync**:
   - Android Studio will automatically sync the project with Gradle files. If prompted, allow it to download any necessary dependencies.

2. **Resolve Issues**:
   - If there are any errors or unresolved dependencies, Android Studio will show them in the **Build** window. Follow the instructions to resolve these issues.

3. **Run the Project**:
   - Connect an Android device via USB or start an Android Emulator.
   - Click on the green **Run** button (the play icon) in the toolbar.
   - Select your device or emulator and click **OK**. The app should build and run.

## Step 5: Explore and Modify the Project

1. **Explore the Code**:
   - Familiarize yourself with the project structure, including the `src`, `res`, and `manifest` folders.

2. **Make Changes**:
   - You can modify the code, UI, and resources as needed.

3. **Test Your Changes**:
   - After making changes, run the app again to ensure everything works as expected.













---
3. **Add `google-services.json` to Your Project**:
   - Open Android Studio.
   - Copy the `google-services.json` file into the `app/` directory of your Android project.

What Does google-services.json Do in Android Studio?
Configuration: The google-services.json file configures your app to communicate with Firebase services. It contains information for authentication, database access, and other service integrations.
Automatic Configuration: When you include the Firebase SDKs in your project and apply the google-services plugin in your build.gradle file, the file ensures that the SDKs know how to connect to your Firebase project and which services are enabled.
