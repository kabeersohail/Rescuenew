package com.example.sohail.rescue;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener,NavigationView.OnNavigationItemSelectedListener,GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks {
    Thread t=null;

    Intent mServiceIntent;
    private HelloService helloService;

    Context ctx;

    public Context getCtx() {
        return ctx;
    }


    static String ProfilePicpath;
    int noti=0;
    private static double latitude,longitude;
    HashMap<String,Marker> hashMapMarker = new HashMap<>();
    HashMap<Marker,String> hashMapme = new HashMap<>();
    HashMap<Marker,Object> hashMapMarker1 = new HashMap<>();
    volatile boolean isRunning = false;
    static String name;
    public boolean SignedIn=false;
    LinearLayout layout;
    private SignInButton Signin;
    public static GoogleApiClient apiClient=null;
    private static final int Req_Code = 9001;
    Integer Request_Camera = 1;
    Integer Select_File = 0;
    LinearLayout GoogleBtn;
    CircleImageView circleImageView;
    TextView JavaNeedShow,JavaPhoneShow,JavaCommissionShow,JavaNameShow;
    static int count=0;
    public static boolean out;
    NavigationView navigationView;



    static String namE=null,phonE=null;
    Marker MyMarker;
    DrawerLayout drawerLayout;
    String Uid="0";
    TextView JavaThread;
    DatabaseReference deleteReference;
    Button JavaHelp,JavaHelpC;
    Button JavaEmerg;
    private static GoogleMap mMap;
    ActionBarDrawerToggle toggle;
    Button JavaLocateMe;
    String mPermission = android.Manifest.permission.ACCESS_FINE_LOCATION;
    int REQUEST_CODE_PERMISSION = 2;
    GPSTracker gps;
    LinearLayout mapLayout;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    LocationManager locationManager;

    private void selectImage(){
        final CharSequence[] items = {"Camera","Gallery","Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
        builder.setTitle("Add Profile Pic");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(items[which].equals("Camera")){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,Request_Camera);
                }
                else if(items[which].equals("Gallery")){
                    Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent,"Select File"),Select_File);
                }
                else if(items[which].equals("Cancel")){
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }






    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.deleteRequest){
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Emergency").child(namE);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Emergency emergency = dataSnapshot.getValue(Emergency.class);
                    if(emergency != null){
                        databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isComplete()){
                                    Toast.makeText(MapsActivity.this,"Deleted",Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(MapsActivity.this,"Network issue",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else {
                        Toast.makeText(MapsActivity.this,"Request not present",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        if(id == R.id.settings){
//            startActivity(new Intent(MapsActivity.this,SettingsActivity.class));
        }

        if(id == R.id.Xmlprofilepic)
        {
            Toast.makeText(MapsActivity.this,"ProfilePic",Toast.LENGTH_SHORT).show();
        }

        if(id == R.id.aboutus)
        {
//            AboutUs aboutUs = new AboutUs(MapsActivity.this);
            startActivity(new Intent(MapsActivity.this,AboutUs.class));
        }

        if(id == R.id.signout)
        {
            finish();

            if(MainActivity.ThroughGoogle){
                AuthUI.getInstance().signOut(this);
                SignOut();
                out=true;
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().remove("Path").apply();

                startActivity(new Intent(this,MainActivity.class));
            }
            else {
//                FirebaseAuth.getInstance()
//                        .signOut(this)
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            public void onComplete(@NonNull Task<Void> task) {
//                                // user is now signed out
//                                startActivity(new Intent(MapsActivity.this,MainActivity.class));
//                                finish();
//                            }
//                        });
                if(t!=null){
//                    t.interrupt();
                    t=null;
                }
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().remove("Path").apply();
                namE=null;
                AuthUI.getInstance().signOut(this);
//                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this,MainActivity.class));
            }
        }
        return false;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(toggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public static class Emerg {


        public Emerg(double lati, double longi) {

        }

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
                    SignedIn=false;
                    apiClient=null;
                    UpdateUi(false);
                }
            });
        }

    }

    private void handleResult(GoogleSignInResult result){
        if(result.isSuccess()){
//            startActivity(new Intent(MapsActivity.this,WelcomeGuide.class));
            GoogleSignInAccount account = result.getSignInAccount();
            namE= account.getDisplayName();

//            String email = account.getEmail();
//            Name.setText(name);
//            Gmail.setText(email);
            UpdateUi(true);
        }
        else {
            UpdateUi(false);
        }
    }

    private void UpdateUi(boolean isSignedin){

        if(SignedIn){
            GoogleBtn.setVisibility(View.GONE);
            mapLayout.setVisibility(View.VISIBLE);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED);
        }

        if(isSignedin){
            SignedIn=true;
            WelcomeGuide.welcomeGuideApiClient=apiClient;
//            startActivity(new Intent(MapsActivity.this,WelcomeGuide.class));
            GoogleBtn.setVisibility(View.GONE);
            mapLayout.setVisibility(View.VISIBLE);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED);
//            layout.setVisibility(View.VISIBLE);
            Signin.setVisibility(View.GONE);
        }

        else{
            finish();
            startActivity(new Intent(this,MainActivity.class));
//            mapLayout.setVisibility(View.GONE);
//            navigationView.setVisibility(View.GONE);
//            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//            layout.setVisibility(View.GONE);
//            Signin.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Req_Code){
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(googleSignInResult);
        }

        if(resultCode == Activity.RESULT_OK){
            if(requestCode == Request_Camera){
                Bundle bundle = data.getExtras();
                final Bitmap bmp = (Bitmap) bundle.get("data");
                circleImageView.setImageBitmap(bmp);
               ProfilePicpath = saveToInternalStorage(bmp);
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("Path",ProfilePicpath).apply();
            }
            else if(requestCode == Select_File){
                    Uri selectedUri = data.getData();
                    circleImageView.setImageURI(selectedUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedUri);
                    ProfilePicpath = saveToInternalStorage(bitmap);
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("Path",ProfilePicpath).apply();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }





    @Override
    public void onStart() {
        super.onStart();

        if(SignedIn){
            GoogleBtn.setVisibility(View.GONE);
            mapLayout.setVisibility(View.VISIBLE);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED);
        }
        // Check if user is signed in (non-null) and update UI accordingly.
        if(apiClient!=null && apiClient.isConnected()){
            SignedIn=true;
            Toast.makeText(MapsActivity.this,"done",Toast.LENGTH_SHORT).show();
            UpdateUi(true);
        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    private void loadImageFromStorage(String path)
    {

        try {
            File f=new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            View headerview =  navigationView.getHeaderView(0);
            circleImageView = headerview.findViewById(R.id.Xmlprofilepic);
            circleImageView.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }


    @Override
    protected void onDestroy() {
        stopService(mServiceIntent);
        Log.i("MAINACT", "onDestroy!");
        super.onDestroy();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_maps);
        if(!MainActivity.ThroughGoogle){
            getName();
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        GoogleBtn = findViewById(R.id.SignInButtn);
        firebaseDatabase = FirebaseDatabase.getInstance();
        navigationView =  findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActivityCompat.requestPermissions(MapsActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        View headerview =  navigationView.getHeaderView(0);
        circleImageView = headerview.findViewById(R.id.Xmlprofilepic);

        FirebaseMessaging.getInstance().subscribeToTopic("Emergency");

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        JavaHelpC = findViewById(R.id.XmlHelpC);
        JavaHelp = findViewById(R.id.XmlHelp);
        JavaThread = findViewById(R.id.XmlThread);
        JavaLocateMe = findViewById(R.id.XmlLocateMe);
        JavaEmerg = findViewById(R.id.Emerg);
        drawerLayout = findViewById(R.id.MAP);

        toggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mapLayout = findViewById(R.id.MapLayout);
        loadImageFromStorage( PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("Path", "defaultStringIfNothingFound"));




//        SignOut = findViewById(R.id.googleSignOut1);
        Signin = findViewById(R.id.GoogleSignIn);
        Signin.setOnClickListener(this);
//        SignOut.setOnClickListener(this);

        if(MainActivity.ThroughGoogle){
//            navigationView.setVisibility(View.GONE);
//            drawerLayout.setVisibility(View.GONE);
            if(apiClient==null){
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
                mapLayout.setVisibility(View.GONE);
                apiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API, options).build();
            }
        }
        else {
            GoogleBtn.setVisibility(View.GONE);
        }
        if(firebaseUser!=null){
            ctx = this;
            helloService = new HelloService(getCtx());
            mServiceIntent = new Intent(getCtx(), helloService.getClass());
            if (!isMyServiceRunning(helloService.getClass())) {
                startService(mServiceIntent);
            }
        }


//        HelloService helloService = new HelloService(MapsActivity.this);
//
//        if(!HelloService.Notif){
//            Intent intent = new Intent(MapsActivity.this, HelloService.class);
//            startService(intent);
//        }

//        Intent intent = new Intent(MapsActivity.this,MyIntentService.class);
//        startService(intent);

//        HelloService helloService = new HelloService();
//        helloService.onStartCommand(intent, Service.START_FLAG_RETRY,9878);
        JavaHelp.setOnClickListener(this);
        JavaLocateMe.setOnClickListener(this);
        JavaEmerg.setOnClickListener(this);
        try{
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,new String[]{mPermission},REQUEST_CODE_PERMISSION);
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        GPSTracker gps = new GPSTracker(MapsActivity.this);
        if(!gps.canGetLocation){
            gps.showSettingsAlert();
        }
        final double MyLat = gps.getLatitude();
        final double MyLong = gps.getLongitude();




        databaseReference = firebaseDatabase.getReference("Emergency");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {



                final String name = dataSnapshot.getKey();
                if(dataSnapshot.child("latitude").getValue()!=null && dataSnapshot.child("longitude").getValue()!=null && dataSnapshot.child("phone")!=null && dataSnapshot.child("need")!=null && dataSnapshot.child("commission") != null){
                    latitude = (double) dataSnapshot.child("latitude").getValue();
                    longitude = (double) dataSnapshot.child("longitude").getValue();
                    final String Phone = (String) dataSnapshot.child("phone").getValue();
                    final String Need = (String) dataSnapshot.child("need").getValue();
                    final String Commission = (String) dataSnapshot.child("commission").getValue();
                    final String Uid = (String) dataSnapshot.child("uid").getValue();
                    if(!namE.equals(name)){
                        double dist = DistenceBwTwoLocations.Distence(MyLat,MyLong,latitude,longitude);
//                        if(dist<2000){

                            if(!namE.equals(dataSnapshot.getKey())){
                                if(noti == 0){
                                    noti++;
//                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(MapsActivity.this,"M_CH_ID");
//                                    builder.setSmallIcon(R.drawable.ic_launcher);
//                                    builder.setContentTitle("Emergency Message");
//                                    builder.setContentText("Someone around you is in emegency");
//                                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
//                r.play();
//                                    builder.setSound(notification);

                                    Intent intent = new Intent(MapsActivity.this,MapsActivity.class);
                                    TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(MapsActivity.this);
                                    taskStackBuilder.addParentStack(MapsActivity.class);
                                    taskStackBuilder.addNextIntent(intent);
//                                    PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
//                                    builder.setContentIntent(pendingIntent);
//                                    NotificationManager notificationManager = (NotificationManager)  getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
//                                    notificationManager.notify(0,builder.build());
                                }
                            }

                        if(mMap!=null){
                            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                                @Override
                                public View getInfoWindow(Marker marker) {
                                    return null;
                                }

                                @Override
                                public View getInfoContents(Marker marker) {

                                    String amI = hashMapme.get(marker);

                                    EmergencyDetails emergencyDetails;
                                    emergencyDetails = (EmergencyDetails) hashMapMarker1.get(marker);

                                    View v = getLayoutInflater().inflate(R.layout.accept_request,null);

                                    if(amI == null) {

                                        if(!namE.equals(name)){
                                            JavaNameShow = v.findViewById(R.id.XmlName);
                                            JavaNeedShow = v.findViewById(R.id.XmlNeedShow);
                                            JavaPhoneShow = v.findViewById(R.id.XmlPhoneShow);
                                            JavaCommissionShow = v.findViewById(R.id.XmlComissionShow);
                                            emergencyDetails = (EmergencyDetails) hashMapMarker1.get(marker);
                                            if(JavaNameShow != null && JavaNeedShow != null && JavaCommissionShow!= null && JavaPhoneShow!=null & emergencyDetails != null){
                                                JavaNameShow.setText(emergencyDetails.nameshow);
                                                JavaNeedShow.setText("Need: "+emergencyDetails.needshow);
                                                JavaCommissionShow.setText("Commission: "+emergencyDetails.commissionshow);
                                                JavaPhoneShow.setText("Phone:" +emergencyDetails.phoneshow);
                                            }
//                                            else{
//                                                Toast.makeText(MapsActivity.this,"Solved",Toast.LENGTH_SHORT).show();
//                                                v = getLayoutInflater().inflate(R.layout.name,null);
//                                                JavaNameShow = v.findViewById(R.id.Xmlnamepro);
//                                                JavaNameShow.setText("You: "+namE);
//                                            }

                                        }
//                                        Toast.makeText(MapsActivity.this,"Others",Toast.LENGTH_SHORT).show();
                                    }
                                    else{
//                                        Toast.makeText(MapsActivity.this,"Solved",Toast.LENGTH_SHORT).show();
                                        v = getLayoutInflater().inflate(R.layout.name,null);
                                        JavaNameShow = v.findViewById(R.id.Xmlnamepro);
                                        JavaNameShow.setText("You: "+namE);
//                                        Toast.makeText(MapsActivity.this,"You",Toast.LENGTH_SHORT).show();
                                    }
                                    return v;
                                }
                            });
                        }
                        int height = 100;
                        int width = 90;
                        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.mylocator);
                        Bitmap b=bitmapdraw.getBitmap();
                        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
//                        MarkerOptions options = new MarkerOptions().title(name).position(new LatLng(latitude,longitude)).icon(BitmapDescriptorFactory.fromBitmap(smallMarker)).snippet(Need+","+Phone+","+Commission);
                        MarkerOptions options = new MarkerOptions().title(name).position(new LatLng(latitude,longitude)).snippet(Need+","+Phone+","+Commission);

                        Bitmap bitmap = createEmergencyBitmap();
                        if(bitmap!=null){
                            options.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
                            options.anchor(0.5f, 0.907f);
                        }

                        Marker marker = mMap.addMarker(options);
                        hashMapMarker.put(Uid,marker);
                        EmergencyDetails emergencyDetails = new EmergencyDetails(Need,Commission,Phone,name);
                        hashMapMarker1.put(marker,emergencyDetails);
//                        }
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {



                if(dataSnapshot.child("latitude").getValue()!=null && dataSnapshot.child("longitude").getValue()!=null && dataSnapshot.child("phone")!=null && dataSnapshot.child("need")!=null && dataSnapshot.child("commission") != null) {
                    String Uid = (String) dataSnapshot.child("uid").getValue();
                    Marker marker = hashMapMarker.get(Uid);
                    if(marker!=null){
                        marker.remove();
                    }
                    final String name = dataSnapshot.getKey();
                    final double latitude = (double) dataSnapshot.child("latitude").getValue();
                    final double longitude = (double) dataSnapshot.child("longitude").getValue();
                    final String Phone = (String) dataSnapshot.child("phone").getValue();
                    final String Need = (String) dataSnapshot.child("need").getValue();
                    final String Commission = (String) dataSnapshot.child("commission").getValue();
                    if(!namE.equals(name)){
                        double dist = DistenceBwTwoLocations.Distence(MyLat,MyLong,latitude,longitude);
//                        if(dist<2000){

//                            if(!namE.equals(dataSnapshot.getKey())){
//                                NotificationCompat.Builder builder = new NotificationCompat.Builder(MapsActivity.this,"M_CH_ID");
//                                builder.setSmallIcon(R.drawable.ic_launcher);
//                                builder.setContentTitle("Child added");
//                                builder.setContentText("Someone around need something");
//                                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//                                builder.setSound(notification);
//                                Intent intent = new Intent(MapsActivity.this,MapsActivity.class);
//                                TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(MapsActivity.this);
//                                taskStackBuilder.addParentStack(MapsActivity.class);
//                                taskStackBuilder.addNextIntent(intent);
//                                PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
//                                builder.setContentIntent(pendingIntent);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                                NotificationManager notificationManager = (NotificationManager)  getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
//                                notificationManager.notify(0,builder.build());
//                            }
//                        int height = 100;
//
//                        int width = 100;
//                        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.locateroblue);
//                        Bitmap b=bitmapdraw.getBitmap();
//                        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
//
//                         smallMarker = createEmergencyBitmap();
//
//                        MarkerOptions options = new MarkerOptions().title(name).position(new LatLng(latitude,longitude)).icon(BitmapDescriptorFactory.fromBitmap(smallMarker)).snippet(Need+","+Phone+","+Commission);
//                        marker = mMap.addMarker(options);ytt
//                        hashMapMarker.put(Uid,marker);
//                        EmergencyDetails emergencyDetails = new EmergencyDetails(Need,Commission,Phone,name);
//                        hashMapMarker1.put(marker,emergencyDetails);

                        int height = 100;
                        int width = 90;
                        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.mylocator);
                        Bitmap b=bitmapdraw.getBitmap();
                        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                        MarkerOptions options = new MarkerOptions().title(name).position(new LatLng(latitude,longitude)).icon(BitmapDescriptorFactory.fromBitmap(smallMarker)).snippet(Need+","+Phone+","+Commission);
//                            MarkerOptions options = new MarkerOptions().title(name).position(new LatLng(latitude,longitude)).snippet(Need+","+Phone+","+Commission);

//                        Bitmap bitmap = createEmergencyBitmap();
//                        if(bitmap!=null){
//                            options.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
//                            options.anchor(0.5f, 0.907f);
//                        }

                        marker = mMap.addMarker(options);
                        hashMapMarker.put(Uid,marker);
                        EmergencyDetails emergencyDetails = new EmergencyDetails(Need,Commission,Phone,name);
                        hashMapMarker1.put(marker,emergencyDetails);
//                        }
                    }
                }

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String Uid = (String) dataSnapshot.child("uid").getValue();
                Marker marker = hashMapMarker.get(Uid);
                if(marker!=null){
                    marker.remove();

                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public static void addMarker(double latitude,double longitude,String name){

        LatLng emerg = new LatLng(latitude,longitude);
        mMap.addMarker(new MarkerOptions().position(emerg).title(name));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(emerg));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 20.0f));
    }

    public void getName()
    {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        namE =firebaseUser.getDisplayName();
    }

    public void getPhoneNumber(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        phonE = firebaseUser.getPhoneNumber();
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.mapstyle));

            if (!success) {
//                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
//            Log.e(TAG, "Can't find style. Error: ", e);
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if(mMap!=null){
            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    View v = getLayoutInflater().inflate(R.layout.accept_request,null);
                    return null;
                }
            });
        }

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
//        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
//        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                Log.w("Message from my map",mMap.getCameraPosition()+"");
                return false;
            }
        });


    }

    private Bitmap createEmergencyBitmap() {
        Bitmap result = null;
        try {
            result = Bitmap.createBitmap(dpo(62), dpo(76), Bitmap.Config.ARGB_8888);
            result.eraseColor(Color.TRANSPARENT);
            Canvas canvas = new Canvas(result);
            Drawable drawable = getResources().getDrawable(R.drawable.locaterored);
            drawable.setBounds(0, 0, dpo(62), dpo(76));
            drawable.draw(canvas);

            Paint roundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            RectF bitmapRect = new RectF();
            canvas.save();

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.maggi);
            //Bitmap bitmap = BitmapFactory.decodeFile(path.toString()); /*generate bitmap here if your image comes from any url*/
            if (bitmap != null) {
                BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                Matrix matrix = new Matrix();
                float scale = dpo(55) / (float) bitmap.getWidth();
                matrix.postTranslate(dpo(10), dpo(10));
                matrix.postScale(scale, scale);
                roundPaint.setShader(shader);
                shader.setLocalMatrix(matrix);
                bitmapRect.set(dpo(5), dpo(5), dpo(52 + 5), dpo(52 + 5));
                canvas.drawRoundRect(bitmapRect, dpo(26), dpo(26), roundPaint);
            }
            canvas.restore();
            try {
                canvas.setBitmap(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return result;
    }

    private Bitmap createUserBitmap() {
        Bitmap result = null;
        try {
            result = Bitmap.createBitmap(dpo(62), dpo(76), Bitmap.Config.ARGB_8888);
            result.eraseColor(Color.TRANSPARENT);
            Canvas canvas = new Canvas(result);
            Drawable drawable = getResources().getDrawable(R.drawable.locaterblack);
            drawable.setBounds(0, 0, dpo(62), dpo(76));
            drawable.draw(canvas);

            Paint roundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            RectF bitmapRect = new RectF();
            canvas.save();

            String pp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("Path", "defaultStringIfNothingFound");
            File f=new File(pp, "profile.jpg");
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(f));

//            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sohail);
            //Bitmap bitmap = BitmapFactory.decodeFile(path.toString()); /*generate bitmap here if your image comes from any url*/
            if (bitmap != null) {
                BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                Matrix matrix = new Matrix();
                float scale = dpo(55) / (float) bitmap.getWidth();
                matrix.postTranslate(dpo(5), dpo(5));
                matrix.postScale(scale, scale);
                roundPaint.setShader(shader);
                shader.setLocalMatrix(matrix);
                bitmapRect.set(dpo(5), dpo(5), dpo(52 + 5), dpo(52 + 5));
                canvas.drawRoundRect(bitmapRect, dpo(26), dpo(26), roundPaint);
            }
            canvas.restore();
            try {
                canvas.setBitmap(null);
            } catch (Exception e) {e.printStackTrace();}
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return result;
    }

    public int dpo(float value) {
        if (value == 0) {
            return 0;
        }
        return (int) Math.ceil(getResources().getDisplayMetrics().density * value);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.GoogleSignIn : SignIn();
                break;

        }

//        if(v == JavaEmerg){
//            GPSTracker gps = new GPSTracker(MapsActivity.this);
//            if(!gps.canGetLocation){
//                gps.showSettingsAlert();
//            }
//            final double MyLat = gps.getLatitude();
//            final double MyLong = gps.getLongitude();
//
//
//            databaseReference = firebaseDatabase.getReference("Emergency");
//            databaseReference.addChildEventListener(new ChildEventListener() {
//                @Override
//                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                    final String name = dataSnapshot.getKey();
//                    if(dataSnapshot.child("latitude").getValue()!=null && dataSnapshot.child("longitude").getValue()!=null && dataSnapshot.child("phone")!=null && dataSnapshot.child("need")!=null && dataSnapshot.child("commission") != null){
//                        latitude = (double) dataSnapshot.child("latitude").getValue();
//                        longitude = (double) dataSnapshot.child("longitude").getValue();
//                        final String Phone = (String) dataSnapshot.child("phone").getValue();
//                        final String Need = (String) dataSnapshot.child("need").getValue();
//                        final String Commission = (String) dataSnapshot.child("commission").getValue();
//                        String Uid = (String) dataSnapshot.child("uid").getValue();
//                        if(!namE.equals(name)){
//                            double dist = DistenceBwTwoLocations.Distence(MyLat,MyLong,latitude,longitude);
//                            if(dist<2000){
//                                MarkerOptions options = new MarkerOptions().title(name).position(new LatLng(latitude,longitude)).snippet(Need+","+Phone+","+Commission);
//                                Marker marker = mMap.addMarker(options);
//                                hashMapMarker.put(Uid,marker);
//                            }
//                        }
//                    }
//                }
//
//                @Override
//                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                    if(dataSnapshot.child("latitude").getValue()!=null && dataSnapshot.child("longitude").getValue()!=null && dataSnapshot.child("phone")!=null && dataSnapshot.child("need")!=null && dataSnapshot.child("commission") != null) {
//                        String Uid = (String) dataSnapshot.child("uid").getValue();
//                        Marker marker = hashMapMarker.get(Uid);
//                        marker.remove();
//                        final String name = dataSnapshot.getKey();
//                        final double latitude = (double) dataSnapshot.child("latitude").getValue();
//                        final double longitude = (double) dataSnapshot.child("longitude").getValue();
//                        final String Phone = (String) dataSnapshot.child("phone").getValue();
//                        final String Need = (String) dataSnapshot.child("need").getValue();
//                        final String Commission = (String) dataSnapshot.child("commission").getValue();
//                        if(!namE.equals(name)){
//                            double dist = DistenceBwTwoLocations.Distence(MyLat,MyLong,latitude,longitude);
//                            if(dist<2000){
//                                MarkerOptions options = new MarkerOptions().title(name).position(new LatLng(latitude,longitude)).snippet(Need+","+Phone+","+Commission);
//                                marker = mMap.addMarker(options);
//                                hashMapMarker.put(Uid,marker);
////                                Toast.makeText(MapsActivity.this,"ok",Toast.LENGTH_SHORT).show();
//
//                            }
//                        }
//                    }
//
//                }
//
//                @Override
//                public void onChildRemoved(DataSnapshot dataSnapshot) {
//                    String Uid = (String) dataSnapshot.child("uid").getValue();
//                    Marker marker = hashMapMarker.get(Uid);
//                    marker.remove();
//                }
//
//                @Override
//                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//
//
//        }





        if(v == JavaHelp) {


            if(firebaseUser != null){
                Uid = firebaseUser.getUid();
            }

            gps = new GPSTracker(MapsActivity.this);

            if(!gps.canGetLocation()){
                gps.showSettingsAlert();
            }

            else {

                latitude=17.6786067;
                longitude=83.1907674;
                if(namE!=null){
                    Location location = new Location(namE,latitude,longitude,count);
                    databaseReference = firebaseDatabase.getReference("Location");
//                    databaseReference.child(Uid).setValue(location);
                }




                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                final View mview = getLayoutInflater().inflate(R.layout.request_help,null);
                final EditText JavaNeed = mview.findViewById(R.id.XmlNeed);
                final EditText JavaPhone = mview.findViewById(R.id.XmlPhone);
                final EditText JavaComission = mview.findViewById(R.id.XmlComission);
                Button JavaHelpDialog = (Button) mview.findViewById(R.id.XmlHelpC);
                JavaHelpDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String InputNeed,InputPhone,InputCommission;
                        InputNeed = JavaNeed.getText().toString().trim();
                        InputPhone = JavaPhone.getText().toString().trim();
                        getPhoneNumber();

                        InputCommission = JavaComission.getText().toString().trim();
                        if(TextUtils.isEmpty(InputNeed) && TextUtils.isEmpty(InputPhone)){
                            Toast.makeText(MapsActivity.this,"Enter Details",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if(TextUtils.isEmpty(InputNeed)){
                            Toast.makeText(MapsActivity.this,"Enter need",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if(TextUtils.isEmpty(InputPhone)){
                            Toast.makeText(MapsActivity.this,"Enter phone number",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if(TextUtils.isEmpty(InputCommission)){
                            Toast.makeText(MapsActivity.this,"0",Toast.LENGTH_SHORT).show();
                        }





                        if(firebaseUser != null){
                            Uid = firebaseUser.getUid();
                        }

                        if(latitude != 0 && longitude !=0){


                            Calendar rightNow = Calendar.getInstance();
                            String date = DateFormat.getDateInstance(DateFormat.FULL).format(rightNow.getTime());
                            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                            String time = dateFormat.format(rightNow.getTime());
                            Emergency emergency = new Emergency(latitude,longitude,InputNeed,InputCommission,InputPhone,Uid,count,date,time);
                            DatabaseReference databaseReferenceE = firebaseDatabase.getReference("EmergencyHistory");
                            databaseReference = firebaseDatabase.getReference("Emergency");
                            deleteReference = databaseReference;
                            if(namE!=null){
                                databaseReference.child(namE).setValue(emergency).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(MapsActivity.this,"Request sent",Toast.LENGTH_SHORT).show();
                                    }
                                });

                                databaseReferenceE.push().child(namE).setValue(emergency).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
//                                        Toast.makeText(MapsActivity.this,"pushed",Toast.LENGTH_SHORT).show();
                                    }
                                });
//                                                databaseReference.child(namE).child("Latitude").setValue(latitude);
//                                                databaseReference.child(namE).child("Longitude").setValue(longitude);
//                                                databaseReference.child(namE).child("Need").setValue(InputNeed);
//                                                databaseReference.child(namE).child("commission").setValue(InputCommission);
//                                                databaseReference.child(namE).child("phone").setValue(InputPhone);
//                                                databaseReference.child(namE).child("Uid").setValue(Uid);
//                                                databaseReference.child(namE).child("Time").setValue(count);

                            }
                        }
                    }
                });
                builder.setView(mview);
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        }



        if(v == JavaLocateMe){

            count=0;
            if(t!=null)
            {
//                t=null;
////                t.interrupt();
                isRunning=true;
            }

            if(MyMarker != null){
                MyMarker.remove();
            }


            if(firebaseUser != null){
                Uid = firebaseUser.getUid();
            }

            gps = new GPSTracker(MapsActivity.this);

            latitude=gps.getLatitude();
            longitude=gps.getLongitude();
            LatLng home = new LatLng(latitude,longitude);
            if(namE!=null){
                int height = 150;
                int width = 140;
//                BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.mylocator);
//                Bitmap b=bitmapdraw.getBitmap();
//                Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
//                MyMarker = mMap.addMarker(new MarkerOptions().position(home).icon(BitmapDescriptorFactory.fromBitmap(smallMarker)).title("You1:"+namE));
//                hashMapme.put(MyMarker,"BLUE");

                LatLng latLng = new LatLng(latitude,longitude);
                MarkerOptions options = new MarkerOptions().position(latLng);
                Bitmap bitmap = createUserBitmap();
                if(bitmap!=null){

                   options.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
                    options.anchor(0.5f, 0.907f);
                    MyMarker = mMap.addMarker(options);
                    hashMapme.put(MyMarker,"BLUE");
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                }

            }
            else {
                Toast.makeText(MapsActivity.this,"name not recieved",Toast.LENGTH_SHORT).show();
            }

            if(!gps.canGetLocation()){
                gps.showSettingsAlert();
            }
            else{
                t = new Thread("T"){
                    @Override
                    public void run(){
                        while(t!=null){
//                            while (!t.isInterrupted()){
                            try{
                                sleep(1000 * 10); // 10 second
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        count++;
                                        latitude=gps.getLatitude();
                                        longitude=gps.getLongitude();

                                        if(MyMarker != null){
                                            MyMarker.remove();
                                        }

                                        LatLng latLng = new LatLng(latitude,longitude);
                                        MarkerOptions options = new MarkerOptions().position(latLng);
                                        Bitmap bitmap = createUserBitmap();
                                        if(bitmap!=null){
                                           options.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
                                            options.anchor(0.5f, 0.907f);
                                            MyMarker = mMap.addMarker(options);
                                            hashMapme.put(MyMarker,"BLUE");
//                                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//                                            mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                                        }

                                        if(namE!=null)
                                        {
//                                            Location location = new Location(namE,latitude,longitude,count);
//                                            FirebaseDatabase fb = FirebaseDatabase.getInstance();
//                                            DatabaseReference db = fb.getReference("Location");
//                                                db.child(Uid).setValue(location);
//                                                db.child(Uid).setValue("Sohail");
//                                                db.child(Uid).child("Latitude").setValue(latitude);
//                                                db.child(Uid).child("Longitude").setValue(longitude);
//                                                db.child(Uid).child("Name").setValue(namE);
//                                                db.child(Uid).child("Time").setValue(count);
//                                            JavaThread.setText(String.valueOf(count));
                                        }
//                                        else
//                                        {
//                                                Toast.makeText(MapsActivity.this,"Name not Recieved",Toast.LENGTH_SHORT).show();
//                                        }
                                    }

                                });
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
//                            }
                        }



                    }
                };

                if(!isRunning){
                    t.start();
                    isRunning=true;
                }



                mMap.moveCamera(CameraUpdateFactory.newLatLng(home));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gps.getLatitude(),gps.getLongitude()), 17.0f));
            }

        }
    }

    @Override
    public void onBackPressed() {
        closeContextMenu();
//        Toast.makeText(MapsActivity.this,"No going back",Toast.LENGTH_SHORT).show();
    }


}


