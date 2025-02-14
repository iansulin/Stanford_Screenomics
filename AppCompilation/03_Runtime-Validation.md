
## 03. Runtime Validation

The **Runtime Validation Process** ensures that the app functions correctly when executed in a simulated environment. This process involves **running the app on an emulator** and **verifying data flow** to ensure proper interaction with the database. Once the app is launched in the emulator, various user actions are simulated to test core functionalities. During this process, the database is actively monitored to confirm that data is being stored, retrieved, and updated correctly. By performing runtime validation, developers can catch potential issues related to app behavior and data flow before deploying the app to real devices.

---

### 03.1. Simulating the App in an Emulator

a. **Set Up an Emulator**
  - Click on the **Device Manager** icon in the toolbar (📱 it looks like a phone).
    - If the icon is not visible, go to **Tools > Device Manager**.
  - Click **Create Virtual Device**.
  - Choose a device from the list and click **Next**.
  - Select a system image (download it if prompted) and click **Next**.
  - Configure additional settings if needed, then click **Finish**.

b. **Run the App on the Emulator**
  - Start the **Emulator**
    - In Device Manager, find your created virtual device and click the green "Run ▶" button (green triangle) or press `Shift + F10` to launch it.
  - Wait for the Emulator to Boot (it may take a few minutes to boot up for the first time).
  - Once the emulator is running, you can interact with your app as you would on a physical device.
    - Use your mouse to click, drag, and type.
