package com.example.sohail.rescue;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.signin.SignIn;
import com.google.android.gms.signin.SignInOptions;

public class GoogleSignIn extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener{
    static String name;
    public static boolean Google=false;
    LinearLayout layout;
    private Button SignOut;
    private SignInButton Signin;
    private TextView Name,Gmail;
    private ImageView imageView;
    private GoogleApiClient apiClient;
    private static final int Req_Code = 9001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_sign_in);

        layout = findViewById(R.id.LayoutId);
        SignOut = findViewById(R.id.googleSignOut);
        Signin = findViewById(R.id.GoogleSignIn);
        imageView = findViewById(R.id.XmlImage);
        Name = findViewById(R.id.XmlGoogleName);
        Gmail = findViewById(R.id.XmlGmail);
        Signin.setOnClickListener(this);
        SignOut.setOnClickListener(this);
        layout.setVisibility(View.GONE);

        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        apiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API, options).build();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.GoogleSignIn : SignIn();
                                     break;

            case R.id.googleSignOut: SignOut();
                                     break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void SignIn(){
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(apiClient);
        startActivityForResult(intent,Req_Code);
//        Google=1;
//        MapsActivity.namE=name;
//        startActivity(new Intent(GoogleSignIn.this,WelcomeGuide.class));
    }

    public void SignOut(){


        if(apiClient!=null){
            Auth.GoogleSignInApi.signOut(apiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    UpdateUi(false);
                }
            });
        }

    }

    private void handleResult(GoogleSignInResult result){
            if(result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                name = account.getDisplayName();
                String email = account.getEmail();
                Name.setText(name);
                Gmail.setText(email);
                UpdateUi(true);
            }
            else {
                UpdateUi(false);
            }
    }

    private void UpdateUi(boolean isSignedin){
        if(isSignedin){
            layout.setVisibility(View.VISIBLE);
            Signin.setVisibility(View.GONE);
        }

        else{
            layout.setVisibility(View.GONE);
            Signin.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Req_Code){
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(googleSignInResult);
        }

    }
}
