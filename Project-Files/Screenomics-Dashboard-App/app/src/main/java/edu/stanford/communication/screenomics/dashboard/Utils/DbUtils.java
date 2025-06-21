/**
 * DbUtils.java
 * Created by Ian Kim; Updated on 2025-06-19.
 * © The Stanford Screenomics Lab
 */

package edu.stanford.communication.screenomics.dashboard.Utils;

import android.content.Context;
import edu.stanford.communication.screenomics.dashboard.DB.LogInPref;

public class DbUtils {

    /**
     * Retrieves the user subject ID from shared preferences.
     */
    public static String getSubjectId(Context context) {
        LogInPref sharedPref = new LogInPref(context);
        return sharedPref.GetUserSubjId();
    }

    /**
     * Retrieves the user group code from shared preferences.
     */
    public static String getGroupCode(Context context) {
        LogInPref sharedPref = new LogInPref(context);
        return sharedPref.GetUserCode();
    }

    /**
     * Retrieves the sanitized data subject ID from the current context.
     */
    public static String getDataSubjectId(Context context) {
        String subjectId = getSubjectId(context);
        return getDataSubjectId(subjectId);
    }

    /**
     * Sanitizes the given subject ID by removing all non-alphanumeric characters.
     */
    public static String getDataSubjectId(String subjectId) {
        return subjectId.replaceAll("[^a-zA-Z0-9]", "");
    }
}
