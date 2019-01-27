package com.freshall.freshall.Controller;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.freshall.freshall.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    ProgressBar progressBar;
    EditText editTextEmail,  editTextPassword, editTextConfirmPass, editTextName;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextEmail = (EditText) findViewById(R.id.registerEmailEditText);
        editTextPassword = (EditText) findViewById(R.id.registerPasswordEditText);
        editTextConfirmPass = (EditText) findViewById(R.id.registerConfirmPassEditText);
        editTextName = (EditText) findViewById(R.id.nameEditText);
        progressBar = (ProgressBar) findViewById(R.id.registerProgBar);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.registerAccButton).setOnClickListener(this);
    }

    private void registerUser() {
        final String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String confpassword = editTextConfirmPass.getText().toString();
        final String name = editTextName.getText().toString();

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password cannot be empty");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Passwords must be six characters or longer");
            editTextPassword.requestFocus();
            return;
        }

        if (!password.equals(confpassword)) {
            Toast.makeText(getApplicationContext(), "Passwords must match", Toast.LENGTH_SHORT).show();
            editTextConfirmPass.requestFocus();
            return;
        }

        if (name.isEmpty()) {
            editTextName.setError("Name is required");
            editTextName.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);

                    // set display name
                    UserProfileChangeRequest profileChangeRequest = new  UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build();
                    FirebaseUser user = mAuth.getCurrentUser();
                    user.updateProfile(profileChangeRequest);

                    Intent intent = new Intent(getApplicationContext(), FeedActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "User " + email + " already registered", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.registerAccButton:
                registerUser();
                break;
        }
    }
}