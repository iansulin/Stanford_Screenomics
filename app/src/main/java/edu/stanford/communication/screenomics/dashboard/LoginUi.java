/**
 * LoginUi.java
 * Created by Ian Kim; Updated on 2025-06-20.
 * © The Stanford Screenomics Lab
 */

package edu.stanford.communication.screenomics.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.stanford.communication.screenomics.dashboard.AppActivity.ShowUserList;
import edu.stanford.communication.screenomics.dashboard.DB.LogInPref;
import edu.stanford.communication.screenomics.dashboard.Utils.AndroidUtils;
import edu.stanford.communication.screenomics.dashboard.databinding.ActivityLoginUiBinding;

public class LoginUi extends AppCompatActivity {

    private ActivityLoginUiBinding binding;
    private Task<AuthResult> mLoginTask;
    private FirebaseAuth mAuth;
    private final String StandardPassword = "Stanford5!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginUiBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Configure loginCode input to capitalize sentences
        binding.loginCode.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        // Ensure loginCode text is always uppercase after editing
        binding.loginCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No action needed
            }

            @Override
            public void afterTextChanged(Editable et) {
                String s = et.toString();
                if (!s.equals(s.toUpperCase())) {
                    s = s.toUpperCase();
                    binding.loginCode.setText(s);
                    binding.loginCode.setSelection(binding.loginCode.length());
                }
            }
        });

        // If user email exists in shared preferences, skip login and go to ShowUserList
        LogInPref sharedpref = new LogInPref(LoginUi.this);
        if (!sharedpref.GetUserEmail().isEmpty()) {
            startActivity(new Intent(this, ShowUserList.class));
            finish();
        }

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Handle login button click
        binding.loginSignIn.setOnClickListener(v -> {
            AndroidUtils.HideKeyboard(v, LoginUi.this);

            String code = binding.loginCode.getText().toString().toLowerCase();
            String number = binding.loginNumber.getText().toString();
            String email = binding.Email.getText().toString();
            String password = binding.password.getText().toString();

            if (!code.isEmpty() && !number.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                if (password.equals(StandardPassword)) {
                    signIn(calcSubjectId(code, number, email), password);
                    EnableUserUi(getString(R.string.action_sign_in), false);
                } else {
                    EnableUserUi(getString(R.string.action_sign_in), true);
                    Toast.makeText(LoginUi.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                }
            } else {
                EnableUserUi(getString(R.string.action_sign_in), true);
                Toast.makeText(LoginUi.this, "Fill Every Detail To Log In", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signIn(String subjId, String password) {
        mLoginTask = mAuth.signInWithEmailAndPassword(subjId, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                onUserAcquired(user, true);
            } else {
                EnableUserUi(getString(R.string.action_sign_in), true);
                Toast.makeText(LoginUi.this, "Could not log in with the provided information.", Toast.LENGTH_SHORT).show();
            }
            mLoginTask = null;
        });
    }

    private void onUserAcquired(FirebaseUser user, boolean enable) {
        if (user == null || user.getEmail() == null) {
            EnableUserUi(getString(R.string.action_sign_in), true);
            return;
        }
        loginComplete(user);
        EnableUserUi(getString(R.string.please_wait), enable);
    }

    private void EnableUserUi(String text, boolean enable) {
        binding.Email.setAlpha(1f);
        binding.loginNumber.setAlpha(1f);
        binding.loginCode.setAlpha(1f);
        binding.password.setAlpha(1f);
        binding.loginSignIn.setAlpha(1f);

        binding.password.setEnabled(enable);
        binding.Email.setEnabled(enable);
        binding.loginNumber.setEnabled(enable);
        binding.loginCode.setEnabled(enable);
        binding.loginSignIn.setEnabled(enable);

        binding.loginSignIn.setText(text);
    }

    private void loginComplete(FirebaseUser user) {
        if (user != null && !binding.Email.getText().toString().isEmpty()) {
            LogInPref sharedPref = new LogInPref(getApplicationContext());

            String pref_id = user.getEmail();
            sharedPref.AddUserSubjId(pref_id);
            sharedPref.AddUserPassword(binding.password.getText().toString());

            final String code_id = binding.loginCode.getText().toString().toUpperCase();
            sharedPref.AddUserCode(code_id);

            String email = binding.Email.getText().toString();
            sharedPref.AddUserEmail(email);

            String number = binding.loginNumber.getText().toString();
            sharedPref.AddUserNumber(number);

            Intent intent = new Intent(this, ShowUserList.class);
            startActivity(intent);
            finish();
        }
    }

    public static String calcSubjectId(String code, String number, String email) {
        return code.toUpperCase() + "_" + number + "_" + email;
    }
}
