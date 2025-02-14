
## 03. Build Validation

The **Build Validation Process** ensures that the Screenomics app is correctly built, runs smoothly, and properly interacts with new databases. This process includes four key steps: **cleaning the project**, **building the app**, **running it on an emulator**, and **verifying data flow**. First, the project is cleaned and rebuilt in Android Studio to remove outdated files and compile fresh code. Next, the app is deployed on an emulator to simulate real device behavior. During this simulation, various features are tested to confirm proper functionality. Finally, the databases are monitored to ensure that all expected data is being recorded and retrieved correctly. This structured validation process helps detect issues early, ensuring a stable and reliable app before deployment.

---

### 03.1. Clean and Build the Project

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
      - "**BUILD SUCCESSFUL**": This means your project has been built without any errors > Proceed to 02.7.
      - "**BUILD FAILED**": This indicates that there were errors during the build process. Check if any previous steps were missed or misconfigured.

