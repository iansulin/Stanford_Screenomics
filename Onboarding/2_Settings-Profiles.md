## Settings Profiles

Settings profiles enable the creation of groups of settings values to apply to large numbers of users. Settings profiles are recorded in the `settings_profiles` collection in Firestore (one of the top level collections). 

Profiles are named according to study groups (the three-letter codes that users sign up with). When a user signs up with a specific study group, the app copies all of the settings profile values from the profile with that study group name. For example, if there is a `settings_profile` named *CON* users who sign up with this code will receive the values of the settings in `/settings_profiles/CON/` in Firestore. 

The *_default_* settings profile contain default values for all of the settings. If a user signs up with a group code for which a settings profile does not exist, they will receive exactly the settings listed in the *_default_* profile. The *_default_* profile also means it's NOT necessary to specify values for ALL settings in named settings profiles. This is because, for a new user, settings not explicitly listed in their settings profile will be drawn from the defaults instead.

Below are some example scenario to explain what settings new users receive. Suppose the `settings_profiles` section of the database contains the following:

```
_default_
  gps-enabled = 0
  data-nontext-upload-wifi-only = 1
  // other settings excluded for simplicity

CON
  gps-enabled = 1
```

Suppose a new user signs up with the group code CON. This user will have GPS enabled, and will upload non-text-based data over Wi-Fi only. This is because they took the `gps-enabled` value from the CON settings profile, but received the default value for `data-nontext-upload-wifi-only` since this setting isn't mentioned in the CON profile. 

Alternatively, say a new user signs up with a group code *INT*. This code doesn't have an associated settings profile, so the user receives entirely the default settings. This means they will have GPS disabled, but will upload non-text-based data over Wi-Fi only. 

---

### Configuring settings on a per-user basis

The settings profile system makes it easy to configure settings for large groups of users, but it's also possible to dynamically control settings for individual users. This can be done in Firestore by going to the collection `/users/[username]/settings/user-settings` and changing settings there manually.

For any changes to take effect for this user, it is also necessary to set the value of the `settings-group-override` setting to 0 for this user. If this is not done, the changes made will be overwritten the settings from the settings profile the user loads from (i.e., the settings for their study group), and nothing will change. This is explained in the next section. 

The parameter values listed in a user's own settings directory reflect exactly what settings that user is using. All settings that exist are listed here, so to check the current settings values of a specific user, you can look in their settings directory in Firestore. 

---

### settings-group-override

The `settings-group-override` parameter, if set to 1 (enabled), will cause a user to draw their settings from their settings profile at all times. This is the default for app users. 

The utility of `settings-group-override` being **enabled** is that it allows for quick changes to the settings values for entire study groups, or for all Stanford Screenomics users. If a setting is changed for a specific study group profile, all members of that study group that have `settings-group-override` enabled will receive that change. This new value will be reflected in those users' individual settings collections [[See Section **Configuring settings on a per-user basis**](#Configuring-settings-on-a-per-user-basis)]. If a setting is changed in the `_default_` profile, then it will be changed for all users in the system that have `settings-group-override` enabled, provided that said settings isn't superseded by a different value in a study-group named profile. 

The use of `settings-group-override` being disabled for a specific user is that it enables manual configuration of settings for that user. For example, if there is one user who is okay with uploading non-text-based data over their data plan (e.g. 5G, LTE), you can change `data-nontext-upload-wifi-only` to 0 and `settings-group-override` to 0 in that user’s own settings collection. Changing `data-nontext-upload-wifi-only` will allow their Screenomics app to upload over data plan; changing `settings-group-override` will prevent the Wi-Fi change from being overwritten by the value in the `_default_` profile. If `settings-group-override` is re-enabled for this user later on, then all manual changes for this user will be overwritten with
the profile values (in this case, `data-nontext-upload-wifi-only` will be set back to 1).

A possible point of confusion is that `settings-group-override` itself is present both in the
`_default_` directory of the settings_profiles section, as well as in individual users’ settings. It is worth noting that the value in the individual users’ settings is the one that has an effect. So, if you disable `settings-group-override` for a specific user, this user will NOT be loading their settings from their study group’s profile. The value in the `_default_` section is simply the value being given to new users.

Funnily enough, changing `settings-group-override` to 0 in the `_default_` profile will result in this value propagating to all users! Then, all users will no longer be syncing their settings with the settings profiles. So, **never change `settings-group-override` to 0 in the `_default_` profile**, because then re-enabling it for all users will require manually going through every user in the database and changing the values back to 1.


[Back to Top](#Settings-Profiles)
