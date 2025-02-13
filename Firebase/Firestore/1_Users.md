
## 1. Users

Every user of the app, when signing up, registers with:
* **Code**: A sequence of letters and/or numbers identifying which study group the user is in
* **Number**: A number identifying this specific user, to help record who this actually is
* **StudyID**: A study name
* **Password**: A password users must know in order to register successfully (so that only
onboarded participants can participate). It needs to be configured during the app compilation process.

In the database, special characters (such as the @ sign if there is any in the StudyID or group Code). So,
all usernames are condensed into **[code][number][studyid]**, with spaces and special characters
omitted, and converted to lowercase. An example is "**int101studyone**" if **Code = INT, Number = 101, and StudyID = StudyOne**.

---

### User Specs

When a user first creates their account, the Screenomics app's "Specs" module records some basic specs about their smartphone device. 
These are as follows:
* **`brand`**: The company that manufactures the device
* **`display-id`**: A random internal ID
* **`fingerprint`**: A different random internal ID
* **`manufacturer`**: Also the company that manufactures the device
* **`model`**: The precise model number of the device
* **`product`**: The name of the type of device

The specs are recorded as entries in the Firestore database under `users` - `specs` collection. 
