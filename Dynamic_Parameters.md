

Dynamic Parameters

The Firestore [settings_profiles] collection serves as a centralized hub for configuration settings tailored to various user groups, allowing researchers to dynamically assign or modify parameters throughout the study via the Firebase console. Currently, there are sixteen dynamic parameters, such as settings refresh intervals and GPS data collection frequencies. If a parameter like PA-enabled is changed, the app will adjust data collection accordingly. 

Dynamic Controller Parameter Setup. Prior to onboarding participants, researchers can customize the data collection app's features to align with specific study requirements, including adjustments to sampling intervals and data transfer network preferences. The app periodically retrieves the latest parameter values at intervals specified in the settings profiles (through a dynamic parameter called ‘settings-refresh-interval’), allowing researchers to update data collection methods as needed, without needing to recompile the app or contact participants to have them change anything. Researchers can set the ‘settings-refresh-interval’ parameter to manage how frequently these updates occur. Overall, this dynamic control allows researchers to balance the frequency of updates with considerations for device battery life, storage, privacy, and study needs. 

Currently, thirteen features can be dynamically controlled to tailor the app to specific research needs, and this file provides with a full list of features and their parameter names, units, and descriptions available here.
