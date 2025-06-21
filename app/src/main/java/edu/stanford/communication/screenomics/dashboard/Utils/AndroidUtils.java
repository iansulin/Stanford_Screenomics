/**
 * AndroidUtils.java
 * Created by Ian Kim; Updated on 2025-06-20.
 * © The Stanford Screenomics Lab
 */

package edu.stanford.communication.screenomics.dashboard.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class AndroidUtils {

    /**
     * Hides the soft keyboard for the given view.
     *
     * @param UserView The currently focused view
     * @param context  Context to access system services
     */
    public static void HideKeyboard(View UserView, Context context) {
        View view = UserView;
        if (UserView != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(UserView.getWindowToken(), 0);
        }
    }

    /**
     * Launches an email client to send an email to the specified user email address.
     *
     * @param context  Context used to start the email intent
     * @param UserMail Recipient email address
     */
    public static void SendUserEmail(Context context, String UserMail) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", UserMail, null));
        context.startActivity(Intent.createChooser(intent, "Choose an Email client :"));
    }
}
