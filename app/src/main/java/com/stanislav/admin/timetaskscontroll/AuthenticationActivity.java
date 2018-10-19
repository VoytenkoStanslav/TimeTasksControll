package com.stanislav.admin.timetaskscontroll;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * Created by Admin on 05.09.2018.
 */

public class AuthenticationActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    FirebaseUser currentFirebaseUser;

    private EditText mEditEmail;
    private EditText mEditPassword;

    private static final int REQUEST_REGISTRATION_TRUE = 1;
    private static final int REQUEST_FORGOT_TRUE = 2;

    private String email;
    private String password;

    private Button btnRegistration;
    private Button btnAutorithetion;
    private Button btnGoogle;

    private TextView tvForgotPassword;

    private LinearLayout lnAuth;

    private String idToken;

    private GoogleApiClient mGoogleApiClient;

    private static final int RC_SIGN_IN = 9001;

    private static final String EXTRA_REGISTRATION_TRUE =
            "com.stanislav.admin.timetaskscontroll.registration_true";

    private static final String EXTRA_REGISTRATION_EMAIL =
            "com.stanislav.admin.timetaskscontroll.registration_email";

    private static final String EXTRA_REGISTRATION_EMAIL_TOAST =
            "com.stanislav.admin.timetaskscontroll.registration_email_toast";

    private static final String EXTRA_REGISTRATION_PASSWORD =
            "com.stanislav.admin.timetaskscontroll.registration_password";

    private boolean mRegistrtionIsTrue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authentication);

        setTitle(getString(R.string.label_auth));

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();

        mAuth = FirebaseAuth.getInstance();

        lnAuth = (LinearLayout) findViewById(R.id.lnAuth);

        mAuth = com.google.firebase.auth.FirebaseAuth.getInstance();


        mRegistrtionIsTrue = getIntent().getBooleanExtra(EXTRA_REGISTRATION_TRUE, false);

        mEditEmail = (EditText) findViewById(R.id.editEmail);

        mEditPassword = (EditText) findViewById(R.id.editPassword);

        tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AuthenticationActivity.this, ForgotActivity.class);
                //startActivityForResult(intent, REQUEST_FORGOT_TRUE);
                startActivity(intent);
            }
        });

        btnGoogle = (Button) findViewById(R.id.btnGoogle);
        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnAuth.setVisibility(View.VISIBLE);
                signIn();
            }
        });

        btnRegistration = (Button) findViewById(R.id.btnAuthRegistration);
        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = RegistrationActivity.newIntent(AuthenticationActivity.this);
                startActivityForResult(intent, REQUEST_REGISTRATION_TRUE);
            }
        });
        btnAutorithetion = (Button) findViewById(R.id.btnAutorithetion);
        btnAutorithetion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mEditEmail.getText().toString().isEmpty()) {
                    if(!mEditPassword.getText().toString().isEmpty()) {
                        lnAuth.setVisibility(View.VISIBLE);
                        autorizationInApp(mEditEmail.getText().toString(), mEditPassword.getText().toString());
                    } else {
                        Snackbar.make(btnAutorithetion, getString(R.string.password_empty), Snackbar.LENGTH_LONG)
                                .setAction("ActionDZ", null).show();
                    }
                } else {
                    Snackbar.make(btnAutorithetion, getString(R.string.email_empty), Snackbar.LENGTH_LONG)
                            .setAction("ActionDZ", null).show();
                }
            }
        });


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(AuthenticationActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                }
            }
        };

    }


    public void autorizationInApp(String email, String password){

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    lnAuth.setVisibility(View.GONE);
                    Intent intent = new Intent(AuthenticationActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    lnAuth.setVisibility(View.GONE);
                    if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                        Snackbar.make(btnAutorithetion, getString(R.string.auth_exeption), Snackbar.LENGTH_LONG)
                                .setAction("ActionDZ", null).show();
                    } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        Snackbar.make(btnAutorithetion, getString(R.string.auth_exeption), Snackbar.LENGTH_LONG)
                                .setAction("ActionDZ", null).show();
                    }
                }
            }
        });

    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_REGISTRATION_TRUE) {
            if (data == null) {
                return;
            }
            email = data.getStringExtra(EXTRA_REGISTRATION_EMAIL);
            mEditEmail.setText(email);
            password = data.getStringExtra(EXTRA_REGISTRATION_PASSWORD);
            mEditPassword.setText(password);
        }

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {

            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            lnAuth.setVisibility(View.GONE);
                            Intent intent = new Intent(AuthenticationActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            lnAuth.setVisibility(View.GONE);
                            Snackbar.make(btnGoogle, "Authentication Failed.", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }



    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    // [END on_start_add_listener]

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.out_title));
        builder.setMessage(getString(R.string.out_app_from_auth));
        builder.setPositiveButton(getString(R.string.out), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finishAffinity();
                System.exit(0);

            }
        });

        builder.setNeutralButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}