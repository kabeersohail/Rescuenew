package com.example.sohail.rescue;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
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

import java.time.Instant;
import java.util.Arrays;

//import static com.example.dell.rescue.MapsActivity.namE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener{
    public static boolean ThroughGoogle=false;
    ProgressDialog progressDialog;
    private static final int RC_SIGN_IN = 1;
    static FirebaseAuth firebaseAuth;
    static FirebaseUser firebaseUser;
    static FirebaseAuth.AuthStateListener stateListener;


    Button JavaSignIn,JavaSignUp;
    EditText JavaEmail,JavaPassword;
    Button GoogleSignIn;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        JavaSignIn = findViewById(R.id.XmlSignIn);
        JavaSignUp = findViewById(R.id.XmlSignUp);
        JavaEmail = findViewById(R.id.XmlEmail);
        JavaPassword = findViewById(R.id.XmlPassword);

        JavaSignIn.setOnClickListener(this);
        JavaSignUp.setOnClickListener(this);
        GoogleSignIn = findViewById(R.id.XmlGoogleSignIn);
        GoogleSignIn.setOnClickListener(this);
        stateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    startActivity(new Intent(MainActivity.this,MapsActivity.class));
                    //Signed in
                }
                else {
                    //Signed out
//                    startActivity(new Intent(MainActivity.this,MainActivity.class));
                }
            }
        };
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
//        UpdateUI(currentUser);
//    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(stateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        firebaseAuth.removeAuthStateListener(stateListener);
    }

    private void UpdateUI(FirebaseUser currentUser) {
        if(currentUser != null){
            startActivity(new Intent(this,MapsActivity.class));
        }
        else {

        }
    }


    @Override
    public void onClick(View v) {
        if(v == JavaSignIn){
            String InputEmail,InputPassword;

            InputEmail = JavaEmail.getText().toString().trim();
            InputPassword = JavaPassword.getText().toString().trim();

            if(TextUtils.isEmpty(InputEmail) && TextUtils.isEmpty(InputPassword)){
                Toast.makeText(this,"Please enter data",Toast.LENGTH_SHORT).show();
                return;
            }

            if(TextUtils.isEmpty(InputEmail)){
                Toast.makeText(this,"Please enter email",Toast.LENGTH_SHORT).show();
                return;
            }

            if(TextUtils.isEmpty(InputPassword)) {
                Toast.makeText(this,"Please enter password",Toast.LENGTH_SHORT).show();
                return;
            }

            progressDialog.setMessage("Sigining in");
            progressDialog.show();

            firebaseAuth.signInWithEmailAndPassword(InputEmail,InputPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Log.d("TAG","SignInWithEmailAndPassword:Successful");

                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this,"Sign in successful",Toast.LENGTH_SHORT).show();
                        UpdateUI(firebaseUser);
                        startActivity(new Intent(MainActivity.this,MapsActivity.class));

                    }
                    else {
                        Log.w("Tag","SignInWithEmailAndPassword:Failure",task.getException());
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this,"Authentication failed",Toast.LENGTH_SHORT).show();
                        UpdateUI(null);
                    }
                }
            });
        }

        if(v == JavaSignUp) {
            startActivity(new Intent(MainActivity.this,Registration.class));
        }

        if(v == GoogleSignIn){
//            MapsActivity.namE="SohaiL";
//            ThroughGoogle=true;
//            startActivity(new Intent(MainActivity.this,MapsActivity.class));
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(Arrays.asList(
//                                    new AuthUI.IdpConfig.EmailBuilder().build(),
//                                    new AuthUI.IdpConfig.PhoneBuilder().build(),
                                    new AuthUI.IdpConfig.GoogleBuilder().build()
//                                    new AuthUI.IdpConfig.FacebookBuilder().build(),
//                                    new AuthUI.IdpConfig.TwitterBuilder().build()
                            ))
                            .build(),
                    RC_SIGN_IN);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

