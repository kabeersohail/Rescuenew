package com.example.sohail.rescue;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;



public class Welcome extends AppCompatActivity implements View.OnClickListener{
    public static GoogleApiClient Welcomeapi;
    public static String Name;
    Button JavaGetStarted;
    EditText JavaName;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        firebaseAuth = FirebaseAuth.getInstance();
        JavaGetStarted = findViewById(R.id.XmlGetStarted);
        JavaName = findViewById(R.id.XmlName);
        JavaGetStarted.setOnClickListener(this);
    }





    @Override
    public void onClick(View v) {


        if(v == JavaGetStarted){
//            Name = JavaName.getText().toString().trim();
//            if(!TextUtils.isEmpty(Name)){
                finish();
                MapsActivity.apiClient=Welcomeapi;
                startActivity(new Intent(this,MapsActivity.class));
//            }
//            else {
//                Toast.makeText(this,"Please enter name",Toast.LENGTH_SHORT).show();
//            }
        }
    }
}
