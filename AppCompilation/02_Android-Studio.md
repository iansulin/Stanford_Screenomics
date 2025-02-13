## Using an Open Source Android App Project from GitHub

This documentation provides instructions on how to download and use the open-source Stanford Screenomics platform project from GitHub in Android Studio to set up your own study app.


### Table of Contents

- [Step 1: Install Android Studio](#step-1-install-android-studio)
- [Step 2: Set Up Git (Optional)](#step-2-set-up-git-optional)
- [Step 3: Clone the GitHub Repository](#step-3-clone-the-github-repository)
- [Step 4: Open the Project in Android Studio](#step-4-open-the-project-in-android-studio)
- [Step 5: Sync and Build the Project](#step-5-sync-and-build-the-project)
- [Step 6: Explore and Modify the Project](#step-6-explore-and-modify-the-project)

### Step 1: Install Android Studio

1. **Download Android Studio**:
   - Go to the [Android Studio download page](https://developer.android.com/studio).
   - Download the version suitable for your operating system (Windows, macOS, or Linux).

2. **Install Android Studio**:
   - Run the downloaded installer and follow the on-screen instructions.
   - Ensure that you install the Android SDK and other recommended components during the installation.

3. **Launch Android Studio**:
   - After installation, open Android Studio. You may be prompted to import settings from a previous installation; you can start fresh.

### Step 2: Set Up Git (Optional)

1. **Download Git**:
   - Go to the [Git website](https://git-scm.com/) and download the installer for your OS.

2. **Install Git**:
   - Run the installer and follow the instructions.

3. **Verify Installation**:
   - Open a terminal or command prompt and type `git --version` to confirm that Git is installed.

### Step 3: Clone the GitHub Repository

1. **Find the Repository**:
   - Go to the GitHub page of the open-source Android project you want to use.

2. **Copy the Repository URL**:
   - Click on the green **Code** button and copy the URL (choose HTTPS or SSH).

3. **Clone the Repository**:
   - Open a terminal or command prompt.
   - Navigate to the directory where you want to save the project using the `cd` command.
   - Run the command:
     ```bash
     git clone <repository-url>
     ```
   - Replace `<repository-url>` with the URL you copied.

### Step 4: Open the Project in Android Studio

1. **Launch Android Studio**:
   - If you haven't opened it yet, start Android Studio.

2. **Open Existing Project**:
   - On the welcome screen, click on **Open an existing Android Studio project**.

3. **Select the Cloned Folder**:
   - Navigate to the folder where you cloned the repository and select it. Click **OK**.

### Step 5: Sync and Build the Project

1. **Gradle Sync**:
   - Android Studio will automatically sync the project with Gradle files. If prompted, allow it to download any necessary dependencies.

2. **Resolve Issues**:
   - If there are any errors or unresolved dependencies, Android Studio will show them in the **Build** window. Follow the instructions to resolve these issues.

3. **Run the Project**:
   - Connect an Android device via USB or start an Android Emulator.
   - Click on the green **Run** button (the play icon) in the toolbar.
   - Select your device or emulator and click **OK**. The app should build and run.

### Step 6: Explore and Modify the Project

1. **Explore the Code**:
   - Familiarize yourself with the project structure, including the `src`, `res`, and `manifest` folders.

2. **Make Changes**:
   - You can modify the code, UI, and resources as needed.

3. **Test Your Changes**:
   - After making changes, run the app again to ensure everything works as expected.

### Summary

By following these steps, you'll be able to download an open-source Android app project from GitHub and run it in Android Studio. Make sure to resolve any dependencies or issues that come up during the Gradle sync, and enjoy exploring and modifying the project!













---
3. **Add `google-services.json` to Your Project**:
   - Open Android Studio.
   - Copy the `google-services.json` file into the `app/` directory of your Android project.

What Does google-services.json Do in Android Studio?
Configuration: The google-services.json file configures your app to communicate with Firebase services. It contains information for authentication, database access, and other service integrations.
Automatic Configuration: When you include the Firebase SDKs in your project and apply the google-services plugin in your build.gradle file, the file ensures that the SDKs know how to connect to your Firebase project and which services are enabled.
