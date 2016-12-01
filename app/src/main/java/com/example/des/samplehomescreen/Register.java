package com.example.des.samplehomescreen;


import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.des.studentmanagerredux.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

// Firebase authentication based on the tutorial at
// firebase.google.com/docs/auth/android/custom-auth

public class Register extends AppCompatActivity implements View.OnClickListener {

    //declare's these variable for the whole class
    Button bRegister;
    EditText etName, etAge, etUsername, etPassword;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "Login";
    private static DatabaseReference mDatabaseReference;



    // sets up the firebase authentication object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        etName = (EditText) findViewById(R.id.etName);
        etAge = (EditText) findViewById(R.id.etAge);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bRegister = (Button) findViewById(R.id.bRegister);


        bRegister.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        createAccount(v);
        /*switch (v.getId()){
            //case R.id.bRegister:




                //break;
            case R.id.bRegister:
                Intent intent= new Intent(this, Login.class);
                startActivity(intent);
                // startActivity(new Intent(this, Register.class));
                break;
        }*/
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public static DatabaseReference getDbRef() {
        return mDatabaseReference;
    }

    public void createAccount(View v) {

         //checks to make sure all the fields are not null and are filled
          //displays an error message

        final View view = v;
        if (etAge.getText().toString().equals("") || etName.getText().toString().equals("")
                || etPassword.getText().toString().equals("") || etUsername.getText().toString().equals("")) {
            Toast.makeText(Register.this, "please fill all fields",
                    Toast.LENGTH_SHORT).show();
        } else {
            final String email = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(Register.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                            // successful register, make firebase data branch for user, and go back
                            // to the login page
                            else {

                                // update the user's online timestamp
                                mDatabaseReference = FirebaseDatabase.getInstance().getReference("users");
                                mDatabaseReference = mDatabaseReference.child(email.replace('.', '_'));
                                mDatabaseReference = mDatabaseReference.child("LastAccess");
                                mDatabaseReference.setValue("" + System.currentTimeMillis());

                                // store current time locally as well
                                SharedPreferences.Editor editor = getSharedPreferences("timestamp", MODE_PRIVATE).edit();
                                editor.putLong("time", System.currentTimeMillis());
                                editor.commit();

                                // return to the login page after the successful register
                                Intent intent = new Intent(view.getContext(), Login.class);
                                startActivity(intent);
                            }

                            // ...
                        }
                    });
        }
    }
}






















