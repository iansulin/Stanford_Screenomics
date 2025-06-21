/**
 * LogInPref.java
 * Created by Ian Kim; Updated on 2025-06-19.
 * © The Stanford Screenomics Lab
 */

package edu.stanford.communication.screenomics.dashboard.DB;

import static android.content.Context.MODE_PRIVATE;
import android.content.Context;
import android.content.SharedPreferences;
import java.util.Map;

/**
 * Shared Preferences utility for storing and retrieving user login-related data.
 *
 * Naming Convention:
 * - "Add" prefix: saves data into SharedPreferences
 * - "Get" prefix: retrieves data from SharedPreferences
 */
public class LogInPref {

    private final Context context;
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;

    // SharedPreferences file name and keys
    private static final String SHARED_PREF_NAME = "user_login_data";
    private static final String KEY_USER_SUBJ_ID = "user_subj_id";
    private static final String KEY_USER_PASSWORD = "user_password";
    private static final String KEY_USER_CODE = "user_code";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_NUMBER = "user_number";
    private static final String KEY_INSTALL_CODE = "install_code";

    public LogInPref(Context context) {
        this.context = context;
    }

    // =======================
    // Clear All Preferences
    // =======================

    public void clearAllPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    // =======================
    // Add / Save Data
    // =======================

    public void AddSHARED_PREF_PREFIXSetting(String key, int value) {
        editor = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public void AddUserSubjId(String value) {
        editor = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE).edit();
        editor.putString(KEY_USER_SUBJ_ID, value);
        editor.apply();
    }

    public void AddUserPassword(String value) {
        editor = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE).edit();
        editor.putString(KEY_USER_PASSWORD, value);
        editor.apply();
    }

    public void AddUserCode(String value) {
        editor = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE).edit();
        editor.putString(KEY_USER_CODE, value);
        editor.apply();
    }

    public void AddUserEmail(String value) {
        editor = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE).edit();
        editor.putString(KEY_USER_EMAIL, value);
        editor.apply();
    }

    public void AddUserNumber(String value) {
        editor = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE).edit();
        editor.putString(KEY_USER_NUMBER, value);
        editor.apply();
    }

    public void AddInstallCode(String value) {
        editor = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE).edit();
        editor.putString(KEY_INSTALL_CODE, value);
        editor.apply();
    }

    // =======================
    // Get / Retrieve Data
    // =======================

    public String GetUserSubjId() {
        prefs = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        return prefs.getString(KEY_USER_SUBJ_ID, "");
    }

    public String GetUserPassword() {
        prefs = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        return prefs.getString(KEY_USER_PASSWORD, "");
    }

    public String GetUserCode() {
        prefs = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        return prefs.getString(KEY_USER_CODE, "");
    }

    public String GetUserEmail() {
        prefs = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        return prefs.getString(KEY_USER_EMAIL, "");
    }

    public String GetUserNumber() {
        prefs = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        return prefs.getString(KEY_USER_NUMBER, "");
    }

    public String GetInstallCode() {
        prefs = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        return prefs.getString(KEY_INSTALL_CODE, "");
    }

    // =======================
    // Get All Data (Raw Map)
    // =======================

    public Map<String, ?> SharedPrefGetAll() {
        prefs = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        return prefs.getAll();
    }
}
