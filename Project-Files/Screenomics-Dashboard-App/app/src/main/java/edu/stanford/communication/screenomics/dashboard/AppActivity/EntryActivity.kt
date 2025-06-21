/**
 * EntryActivity.kt
 * Created by Ian Kim; Updated on 2025-06-20.
 * © The Stanford Screenomics Lab
 */

package edu.stanford.communication.screenomics.dashboard.AppActivity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import edu.stanford.communication.screenomics.dashboard.DB.LogInPref
import edu.stanford.communication.screenomics.dashboard.LoginUi

class EntryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val auth = FirebaseAuth.getInstance()
        val sharedpref = LogInPref(this@EntryActivity)

        if (auth.currentUser != null) {
            val sharedpref = LogInPref(this@EntryActivity)

            if (sharedpref.GetUserEmail().isNotEmpty()) {
                startActivity(Intent(this@EntryActivity, ShowUserList::class.java))
            } else {
                startActivity(Intent(this@EntryActivity, LoginUi::class.java))
            }
        } else {
            sharedpref.clearAllPreferences()
            startActivity(Intent(this@EntryActivity, LoginUi::class.java))
        }
    }
}
