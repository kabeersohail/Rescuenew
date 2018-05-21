package com.example.sohail.rescue;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//import static com.example.dell.rescue.MainActivity.firebaseAuth;
//import static com.example.dell.rescue.MainActivity.firebaseUser;


public class Registration extends AppCompatActivity implements View.OnClickListener{
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    static String Uid;

    public static String InputName;

    ProgressDialog progressDialog;

    EditText JavaEmail,JavaPassword,JavaRePassword,JavaName;
    Button JavaSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        firebaseDatabase = FirebaseDatabase.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(Registration.this);

        JavaEmail = findViewById(R.id.XmlEmail);
        JavaName = findViewById(R.id.XmlName);
        JavaPassword = findViewById(R.id.XmlPassword);
        JavaRePassword = findViewById(R.id.XmlRePassword);
        JavaSignUp = findViewById(R.id.XmlSignUp);

        JavaSignUp.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        String InputEmail,InputPassword,InputRePassword;

        InputName = JavaName.getText().toString().trim();
        InputEmail = JavaEmail.getText().toString().trim();
        InputPassword = JavaPassword.getText().toString().trim();
        InputRePassword = JavaRePassword.getText().toString().trim();

        if(v == JavaSignUp){
            if(TextUtils.isEmpty(InputEmail) && TextUtils.isEmpty(InputName) && TextUtils.isEmpty(InputPassword) && TextUtils.isEmpty(InputRePassword)){
                Toast.makeText(this,"Enter data",Toast.LENGTH_SHORT).show();
                return;
            }

            if(TextUtils.isEmpty(InputName)){
                Toast.makeText(this,"Enter name",Toast.LENGTH_SHORT).show();
                return;
            }

            if(TextUtils.isEmpty(InputEmail)){
                Toast.makeText(this,"Enter email",Toast.LENGTH_SHORT).show();
                return;
            }

            if(TextUtils.isEmpty(InputPassword)){
                Toast.makeText(this,"Enter password",Toast.LENGTH_SHORT).show();
                return;
            }

            if(TextUtils.isEmpty(InputRePassword)){
                Toast.makeText(this,"Confirm password",Toast.LENGTH_SHORT).show();
                return;
            }

            if(!InputPassword.equals(InputRePassword)){
                Toast.makeText(this,"Password does'nt match",Toast.LENGTH_SHORT).show();
                return;
            }

            progressDialog.setMessage("Sigining up, please wait");
            progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(InputEmail,InputRePassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        firebaseUser = firebaseAuth.getCurrentUser();
                        progressDialog.dismiss();
                        Toast.makeText(Registration.this,"Sign up Successful",Toast.LENGTH_SHORT).show();
                        if(firebaseUser != null){
                            Uid = firebaseUser.getUid();
                        }
                        if(Uid!=null)
                        {
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    if(user!=null){
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(InputName).build();
                                        user.updateProfile(profileUpdates);

                                    }
                                }

                        finish();
                        startActivity(new Intent(Registration.this,WelcomeGuide.class));
                    }
                    else{
                        Toast.makeText(Registration.this,"Authentication failed",Toast.LENGTH_SHORT).show();
                        UpdateUI(null);
                    }
                }
            });

        }
    }

    private void UpdateUI(FirebaseUser firebaseUser) {

        if(firebaseUser != null){
            startActivity(new Intent(this,WelcomeGuide.class));
        }
        else{
            startActivity(new Intent(this,MainActivity.class));
        }
    }
}
