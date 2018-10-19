package com.stanislav.admin.timetaskscontroll;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class RegistrationActivity extends AppCompatActivity {


    private static final String EXTRA_REGISTRATION_TRUE =
            "com.stanislav.admin.timetaskscontroll.registration_true";

    private static final String EXTRA_REGISTRATION_EMAIL =
            "com.stanislav.admin.timetaskscontroll.registration_email";

    private static final String EXTRA_REGISTRATION_PASSWORD =
            "com.stanislav.admin.timetaskscontroll.registration_password";

    private boolean mRegistrtionIsTrue;

    private Button btnRegistr;
    private EditText editEmail;
    private EditText editPassword;
    private EditText editRepitPassword;

    private TextView textAboutPassword;

    private LinearLayout lnRegistr;

    private FirebaseAuth mAuth;

    private String email;
    private String password;
    private String repitPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        //        .requestIdToken(getString(R.string.default_web_client_id))
        //        .requestEmail()
        //        .build();

        mRegistrtionIsTrue = getIntent().getBooleanExtra(EXTRA_REGISTRATION_TRUE, false);

        lnRegistr = (LinearLayout) findViewById(R.id.lnRegistr);

        editEmail = (EditText) findViewById(R.id.editRegEmail);

        editPassword = (EditText) findViewById(R.id.editRegPassword);
        editPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!editRepitPassword.getText().toString().isEmpty() && !s.toString().isEmpty()) {

                    textAboutPassword.setVisibility(View.VISIBLE);

                    password = s.toString();
                    repitPassword = editRepitPassword.getText().toString();

                    if (password.equals(repitPassword)) {
                        textAboutPassword.setText(R.string.password_true);

                    } else {
                        textAboutPassword.setText(R.string.password_false);
                    }
                } else {
                    textAboutPassword.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mAuth = FirebaseAuth.getInstance();

        textAboutPassword = (TextView) findViewById(R.id.textAboutPassword);

        editRepitPassword = (EditText) findViewById(R.id.editRegRepitPassword);
        editRepitPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!editPassword.getText().toString().isEmpty() && !s.toString().isEmpty()) {

                    textAboutPassword.setVisibility(View.VISIBLE);

                    password = editPassword.getText().toString();
                    repitPassword = s.toString();

                    if (repitPassword.equals(password)) {
                        textAboutPassword.setText(R.string.password_true);

                    } else {
                        textAboutPassword.setText(R.string.password_false);
                    }
                } else {
                    textAboutPassword.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnRegistr = (Button) findViewById(R.id.btnRegistration);
        btnRegistr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String rp = editRepitPassword.getText().toString();
                String p = editPassword.getText().toString();

                if (!editEmail.getText().toString().isEmpty()) {
                    if (!p.isEmpty()) {
                        if (!rp.isEmpty()) {
                            if (rp.equals(p)) {
                                lnRegistr.setVisibility(View.VISIBLE);
                                registrationInApp(editEmail.getText().toString().trim(), editPassword.getText().toString().trim());
                            } else {
                                Snackbar.make(btnRegistr, getString(R.string.not_format_email_exeption), Snackbar.LENGTH_LONG)
                                        .setAction("ActionDZ", null).show();
                            }
                        } else {
                            Snackbar.make(btnRegistr, getString(R.string.password_repit_empty), Snackbar.LENGTH_LONG)
                                    .setAction("ActionDZ", null).show();
                        }
                    } else {
                        Snackbar.make(btnRegistr, getString(R.string.password_empty), Snackbar.LENGTH_LONG)
                                .setAction("ActionDZ", null).show();
                    }
                } else {
                    Snackbar.make(btnRegistr, getString(R.string.email_empty), Snackbar.LENGTH_LONG)
                            .setAction("ActionDZ", null).show();
                }
            }
        });

    }

    private void registrationInApp(final String email, final String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    lnRegistr.setVisibility(View.GONE);
                    setAnswerShownResult(true, email, password);

                } else {
                    lnRegistr.setVisibility(View.GONE);
                    if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
                        Snackbar.make(btnRegistr, getString(R.string.password_length), Snackbar.LENGTH_LONG)
                                .setAction("ActionDZ", null).show();
                    } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        Snackbar.make(btnRegistr, getString(R.string.not_format_email_exeption), Snackbar.LENGTH_LONG)
                                .setAction("ActionDZ", null).show();
                        }
                }
            }
        });
    }

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, RegistrationActivity.class);
        return intent;
    }

    public void setAnswerShownResult(boolean isRegistrationResult, String email, String password) {
        Intent data = new Intent();
        data.putExtra(EXTRA_REGISTRATION_TRUE, isRegistrationResult);
        data.putExtra(EXTRA_REGISTRATION_EMAIL, email);
        data.putExtra(EXTRA_REGISTRATION_PASSWORD, password);
        setResult(RESULT_OK, data);
        finish();
    }

}
