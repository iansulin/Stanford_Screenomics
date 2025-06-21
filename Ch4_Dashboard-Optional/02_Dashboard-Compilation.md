## O4.02. Dashboard App Compilation

**Study Dashboard App** provides researchers with real-time, continuous monitoring of study participants by displaying activity summaries and detailed event logs fetched from Ticker, enabling effective tracking of engagement and data quality.

**Before you begin:**
* Compile your data collection app and complete Firebase setup to ensure **Ticker** is fully configured and ready to use [[Complete all sections in Chapter 2](../Ch2_App-Compilation/)].
* Ensure you have your `google-services.json` file ready. This file can be obtained from the the Firebase console for your project [[See Chapter 2 - Section 02.2.b](../Ch2_App-Compilation/02_Firebase-Setup.md)].

---

### 4.02.1. Download the Project from GitHub

a. **Download the Project**
   - https://github.com/iansulin/stanford_screenomics/releases/download/Source-Code/ScreenomicsDashboard_External_062025.zip

b. **Extract the ZIP File**
   - Locate the downloaded ZIP file and extract it to a folder of your choice.

---

### 4.02.2. Add the google-services.json File

a. **Launch Android Studio**
   - If you haven't opened it yet, start Android Studio.

b. **Open the Project**
   - Click on **Open**.
   - Navigate to the extracted project folder, select it and click **OK**.
     - If prompted, do **NOT** click on the **Sync Project with Gradle Files** yet.

c. **Locate "App" Module**
   - In the **Project View** (usually on the left), click the dropdown at the top and select **Android** to display the project structure.
   - Find the **app module** in the project view, which has **a folder icon with a green square**. 

d.  **Add the JSON File to the "App" Module**
   - Open the folder on your computer where the `google-services.json` file is located.
   - Copy the `google-services.json` file (`Command + C` or `Ctrl + C`).
   - In Android Studio, **Select the App Module** (the folder icon with a green square).
   - **Paste the JSON file into the "App" Module**.
   - If/When a "Copy" or "Replace" window appears, do **NOT** change any settings--simply click **OK**.

> * You **do not** need to refactor anything, and **do not** need to change the app name (unless you prefer to personalize it). This app is intended for **internal use only** and to be installed manually via APK distribution (not via Google Playstore). If you **do** want to change the app name, refer to: [Chapter 2.03.8 – Change App Name](../Ch2_App-Compilation/03_Android-Studio-Setup.md)

> * You **do not** need to configure or set up a new password—authentication. You can log in using the same credentials you created when registering for the Screenomics data collection app. The Dashboard App uses the same Firebase Authentication backend as the data collection app. All user authentication is managed centrally through Firebase, so once your account is registered for data collection, it can also be used to access the Dashboard App without additional setup. Make sure the data collection app is compiled and Firebase is fully set up **before** working on the dashboard app.

---

### 4.02.3. Clean and Build the Project

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
      - "**BUILD SUCCESSFUL**": This means your project has been built without any errors.
      - "**BUILD FAILED**": This indicates that there were errors during the build process. Check if any previous steps were missed or misconfigured.



[Back to Top](#top)


