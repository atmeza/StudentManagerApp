

package com.example.des.samplehomescreen;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.des.studentmanagerredux.HomeScreen;
import com.example.des.studentmanagerredux.R;
import com.example.des.studentmanagerredux.db.EventDbHelper;
import com.example.des.studentmanagerredux.db.GPADbHelper;
import com.example.des.studentmanagerredux.db.ToDoDbHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.GregorianCalendar;
import java.util.Objects;


public class Login extends AppCompatActivity implements View.OnClickListener {


    Button bLogin;
    EditText etUsername, etPassword;
    TextView tvRegisterLink;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "Login";
    private static DatabaseReference mDataRef;
    private Object firebaseTime = new Object();
    private GPADbHelper GPAHelper = new GPADbHelper(this);
    private ToDoDbHelper ToDoHelper = new ToDoDbHelper(this);
    private EventDbHelper eventHelper = new EventDbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bLogin = (Button) findViewById(R.id.bLogin);
        tvRegisterLink = (TextView) findViewById(R.id.tvRegisterLink);


        bLogin.setOnClickListener(this);
        tvRegisterLink.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //case R.id.bLogin:




               // break;
            case R.id.tvRegisterLink:
                Intent intenta= new Intent(this, Register.class);
                startActivity(intenta);
                // startActivity(new Intent(this, Register.class));
                break;
            case R.id.bLogin:
                signIn(v);
                //Intent intentb= new Intent(this, HomeScreen.class);
                //startActivity(intentb);
                // startActivity(new Intent(this, Register.class));
                break;
        }





    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

        // notify databases that user is logged out since they entered the login page again
        EventDbHelper.logout();
        GPADbHelper.logout();
        ToDoDbHelper.logout();
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }}

    public void signIn(View v){

        final View view = v;
        final String email = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail", task.getException());
                            System.out.println("fail");
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        else {

                            // notify databases that user is logged in
                            GPADbHelper.login(email.replace('.', '_'));
                            EventDbHelper.login(email.replace('.', '_'));
                            ToDoDbHelper.login(email.replace('.', '_'));

                            // if the user is logged in, then update the firebase/local databases
                            if (GPADbHelper.loggedIn()) {


                                mDataRef = Register.getDbRef();
                                FirebaseDatabase.getInstance().goOffline();
                                FirebaseDatabase.getInstance().goOnline();

                                String username = GPADbHelper.getUsername();
                                System.out.println("USERNAME: " + username);

                                // get timestamp of most recent firebase sync
                                mDataRef = FirebaseDatabase.getInstance().getReference("users");
                                mDataRef = mDataRef.getRoot().child("users").child(username).child("LastAccess");

                                System.out.println("PATH: " + mDataRef.toString());
                                System.out.println("GETTING TIME");

                                mDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        System.out.println("ABOUT TO GET TIME");
                                        System.out.println("TIME IS: " + dataSnapshot.getValue());
                                        System.out.println("THIS IS THE TIME BEING GOTTEN");
                                        //try {
                                        //    Thread.sleep(300000);                 //1000 milliseconds is one second.
                                        //} catch(InterruptedException ex) {
                                        //    Thread.currentThread().interrupt();
                                        //}
                                        completeLogin(view, email.replace('.', '_'), dataSnapshot, dataSnapshot.getValue().toString());
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        System.out.println("firebase read cancelled");
                                    }
                                });

                            }


                            Intent intent = new Intent(view.getContext() , HomeScreen.class);
                                startActivity(intent);

                        }

                        // ...
                    }
                });
    }

    private void completeLogin(View v, String username, DataSnapshot dataSnap, String time) {

        // firebaseTime = dataSnap.getValue();

        System.out.println("TIME GOTTEN");

        // System.out.println(firebaseTime.toString());
        // System.out.println("CLASS OF OBJECT: " + firebaseTime.getClass());
        GregorianCalendar FBCal = new GregorianCalendar();
        FBCal.setTimeInMillis(Long.parseLong(time));
        System.out.println(FBCal.getTimeInMillis());

        SharedPreferences sharedPreferences = getSharedPreferences("timestamp", MODE_PRIVATE);
        long localTime = sharedPreferences.getLong("time", 0);

        GregorianCalendar localCal = new GregorianCalendar();
        localCal.setTimeInMillis(localTime);

        // compare the local and firebase times
        System.out.println("LOCAL TIME: " + localCal.getTimeInMillis());
        System.out.println("ONLINE TIME: " + FBCal.getTimeInMillis());

        System.out.println(localCal.compareTo(FBCal));

        // if the local time is more recent than the firebase time, then overwrite firebase
         /*if (localCal.compareTo(FBCal) > 0) {
            GPAHelper.firebaseOverwrite();
            ToDoHelper.firebaseOverwrite();
        } */

        // if firebase is more recent than the local time, then overwrite local database
        //else if (localCal.compareTo(FBCal) < 0) {
            GPAHelper.removeAllClasses();
            GPAHelper.localOverwrite();
            eventHelper.removeAllEvents();
            eventHelper.localOverwrite();

            /* Cursor cursor = eventHelper.getAllEvents();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            System.out.println(cursor.getString(1));
            cursor.moveToNext();
        } */
            ToDoHelper.removeAllEvents();
            ToDoHelper.localOverwrite();
        

        // update firebase and local times to be the same
        System.out.println("setting local time");
        long currTime = System.currentTimeMillis();
        SharedPreferences.Editor editor = getSharedPreferences("timestamp", MODE_PRIVATE).edit();
        editor.putLong("time", currTime);
        editor.commit();

        System.out.println("setting online time");
        mDataRef = mDataRef.getRoot().child("users").child(username).child("LastAccess");
        mDataRef.setValue("" + System.currentTimeMillis());
        System.out.println("online time set");

    }
}




