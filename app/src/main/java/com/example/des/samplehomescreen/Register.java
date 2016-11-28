package com.example.des.samplehomescreen;



import android.content.Intent;
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


public class Register extends AppCompatActivity implements View.OnClickListener {


    Button bRegister;
    EditText etName, etAge, etUsername, etPassword;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "Login";
    private DatabaseReference mDatabaseReference;




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

public void createAccount(View v) {
    final View view = v;
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

                        mDatabaseReference = FirebaseDatabase.getInstance().getReference("users");
                        mDatabaseReference = mDatabaseReference.child(email.replace('.', '_'));
                        /* mDatabaseReference = mDatabaseReference.child("Grades");
                        mDatabaseReference = mDatabaseReference.child("EXAMPLE GRADE");
                        Map<String, String> Map1 = new HashMap<String, String>();
                        Map1.put("class", "cse110");
                        Map1.put("units", "4");
                        Map1.put("letter", "A");
                        mDatabaseReference.setValue(Map1);
                        mDatabaseReference = mDatabaseReference.getParent().getParent().child("ToDo");
                        mDatabaseReference = mDatabaseReference.child("EXAMPLE TASK");
                        Map1 = new HashMap<String, String>();
                        Map1.put("name", "homework");
                        Map1.put("progress", "100");
                        Map1.put("done", "true");
                        mDatabaseReference.setValue(Map1);
                        mDatabaseReference = mDatabaseReference.getParent().getParent().child("Events");
                        Map1 = new HashMap<String, String>();
                        mDatabaseReference = mDatabaseReference.child("EXAMPLE EVENT");
                        Map1.put("name", "cse110");
                        Map1.put("start", "100000");
                        Map1.put("end", "100001");
                        mDatabaseReference.setValue(Map1);
                        mDatabaseReference = mDatabaseReference.getParent().child("EXAMPLE EVENT2");
                        Map1.clear();
                        Map1.put("name", "cse10");
                        Map1.put("start", "100");
                        Map1.put("end", "0001");
                        mDatabaseReference.setValue(Map1);
                        mDatabaseReference = mDatabaseReference.getParent();
                        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                    for (DataSnapshot postPostSnapshot: postSnapshot.getChildren()) {
                                        Object data = postPostSnapshot.getValue();
                                        System.out.println(data.toString());
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        }); */

                        Intent intent = new Intent(view.getContext() , Login.class);
                        startActivity(intent);
                    }

                    // ...
                }
            });
}
}






















