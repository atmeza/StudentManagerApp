

package com.example.des.samplehomescreen;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.des.studentmanagerredux.HomeScreen;
import com.example.des.studentmanagerredux.R;


public class Login extends AppCompatActivity implements View.OnClickListener {


    Button bLogin;
    EditText etUsername, etPassword;
    TextView tvRegisterLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                Intent intentb= new Intent(this, HomeScreen.class);
                startActivity(intentb);
                // startActivity(new Intent(this, Register.class));
                break;
        }
    }
}




