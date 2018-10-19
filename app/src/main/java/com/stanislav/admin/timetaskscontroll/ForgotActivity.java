package com.stanislav.admin.timetaskscontroll;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotActivity extends AppCompatActivity {

    private static final String EXTRA_REGISTRATION_EMAIL_TOAST =
            "com.stanislav.admin.timetaskscontroll.registration_email_toast";

    private FirebaseAuth mAuth;

    FirebaseUser currentFirebaseUser;

    private EditText mEditEmailForgot;

    private LinearLayout lnForgot;

    private Button btnSendEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        mAuth = FirebaseAuth.getInstance();

        mEditEmailForgot = (EditText) findViewById(R.id.editEmailForgot);

        lnForgot = (LinearLayout) findViewById(R.id.lnForgot);

        btnSendEmail = (Button) findViewById(R.id.btnSendEmail);
        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mEditEmailForgot.getText().toString().isEmpty()) {
                    lnForgot.setVisibility(View.VISIBLE);
                    sendResetPasswordEmail();
                } else {
                    Snackbar.make(btnSendEmail, getString(R.string.email_empty), Snackbar.LENGTH_LONG)
                            .setAction("ActionDZ", null).show();
                }
            }
        });

    }

    public void setAnswerShownResult(String emailToast) {
        Intent data = new Intent();
        data.putExtra(EXTRA_REGISTRATION_EMAIL_TOAST, emailToast);
        setResult(RESULT_OK, data);
        finish();
    }

    private void sendResetPasswordEmail() {
        final String email = ((EditText) findViewById(R.id.editEmailForgot))
                .getText().toString();
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {

                        if (task.isSuccessful()) {
                            lnForgot.setVisibility(View.GONE);
                            Toast.makeText(ForgotActivity.this, getString(R.string.email_send), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(ForgotActivity.this, AuthenticationActivity.class);
                            startActivity(intent);
                        } else {
                            lnForgot.setVisibility(View.GONE);

                            Snackbar.make(btnSendEmail, getString(R.string.email_did_not_send), Snackbar.LENGTH_LONG)
                                    .setAction("ActionDZ", null).show();
                        }

                    }
                });
    }

}
