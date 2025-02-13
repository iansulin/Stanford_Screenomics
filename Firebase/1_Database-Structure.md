
## 2. Database Structure


### Firestore
This section mirrors the hierarchical structure of the actual Firestore database's organization. The numbers in parentheses indicate the section number in this document that explain the corresponding database entry in more detail.

```
- install
    - [timestamp]
    - [timestamp]
    - [...]
- settings_profiles
    - \_default\_
        * [setting]
        * [...]
    - [XXX]
        * [setting]
        * [...]
- ticker
    - [username]
        * event: [timestamp]
        * event: [...]
- users
    - [username]
        - events
            * [event]
            * [...]
        - settings
            * [setting]
            * [...]
        - specs
            * [spec]
            * [...]
    - [...]
```

### Google Cloud Storage
