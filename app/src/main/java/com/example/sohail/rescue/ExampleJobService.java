package com.example.sohail.rescue;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ExampleJobService extends JobService {
    private static final String TAG = "ExampleJobService";
    private boolean jobCancelled = false;
    private Looper mServiceLooper;
    static boolean Notif = false;
    static boolean visited = false;
    public int counter=0;
    static int ik = 0;
    static Context context;
//    private HelloService.ServiceHandler mServiceHandler;
    public static int k=0;
    static int i =0;

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "Job started");
        doBackgroundWork(params);

        return true;
    }

    private void doBackgroundWork(final JobParameters params) {
        GPSTracker gps = new GPSTracker(ExampleJobService.this);
        if(!gps.canGetLocation){
//            stopSelf(msg.arg1);
            jobFinished(params, false);
            return;
        }
        final double MyLat = gps.getLatitude();
        final double MyLong = gps.getLongitude();

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Emergency");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(k == 0){
                    Toast.makeText(ExampleJobService.this,"Child Added",Toast.LENGTH_SHORT).show();
                    String name = dataSnapshot.getKey();
                    Emergency emergency = dataSnapshot.getValue(Emergency.class);
////                    double latitude = emergency.latitude;
////                    double longitude = emergency.longitude;
                    String need = emergency.need;
//                    double dist = DistenceBwTwoLocations.Distence(MyLat,MyLong,latitude,longitude);
////                    if(dist < 5000)
////                    {
//                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
//                        String namE =firebaseUser.getDisplayName();
//                        if(!namE.equals(name)){
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(ExampleJobService.this,"M_CH_ID");
                    builder.setSmallIcon(R.drawable.ic_launcher);
                    builder.setContentTitle(name);
                    builder.setContentText("Need : "+need);
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Notif = true;
                    builder.setSound(notification);
                    Intent intent = new Intent(ExampleJobService.this,MapsActivity.class);
                    TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(ExampleJobService.this);
                    taskStackBuilder.addParentStack(MapsActivity.class);
                    taskStackBuilder.addNextIntent(intent);
                    PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(pendingIntent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    builder.setAutoCancel(true);
                    NotificationManager notificationManager = (NotificationManager)  getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
                    notificationManager.notify(0,builder.build());
//                        }
//                    }
                }
                else {
                    k = 0;
                }


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Toast.makeText(ExampleJobService.this,"Child changed",Toast.LENGTH_SHORT).show();
                String name = dataSnapshot.getKey();
                Emergency emergency = dataSnapshot.getValue(Emergency.class);
                double latitude = emergency.latitude;
                double longitude = emergency.longitude;
                String need = emergency.need;
                double dist = DistenceBwTwoLocations.Distence(MyLat,MyLong,latitude,longitude);
//                    if(dist < 5000)
//                    {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                String namE =firebaseUser.getDisplayName();
                if(!namE.equals(name)){
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(ExampleJobService.this,"M_CH_ID");
                    builder.setSmallIcon(R.drawable.ic_launcher);
                    builder.setContentTitle(name);
                    builder.setContentText("Need : "+need);
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Notif = true;
                    visited = true;
                    builder.setSound(notification);
                    Intent intent = new Intent(ExampleJobService.this,MapsActivity.class);
                    TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(ExampleJobService.this);
                    taskStackBuilder.addParentStack(MapsActivity.class);
                    taskStackBuilder.addNextIntent(intent);
                    PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(pendingIntent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    builder.setAutoCancel(true);
                    NotificationManager notificationManager = (NotificationManager)  getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
                    notificationManager.notify(0,builder.build());
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Toast.makeText(ExampleJobService.this,"Child deleted",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Toast.makeText(ExampleJobService.this,"Child moved",Toast.LENGTH_SHORT).show();

                String name = dataSnapshot.getKey();
                Emergency emergency = dataSnapshot.getValue(Emergency.class);
                double latitude = emergency.latitude;
                double longitude = emergency.longitude;
                String need = emergency.need;
                double dist = DistenceBwTwoLocations.Distence(MyLat,MyLong,latitude,longitude);
//                    if(dist < 5000)
//                    {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                String namE =firebaseUser.getDisplayName();
                if(!namE.equals(name)){
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(ExampleJobService.this,"M_CH_ID");
                    builder.setSmallIcon(R.drawable.ic_launcher);
                    builder.setContentTitle(name);
                    builder.setContentText("Need : "+need);
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Notif = true;
                    visited = true;
                    builder.setSound(notification);
                    Intent intent = new Intent(ExampleJobService.this,MapsActivity.class);
                    TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(ExampleJobService.this);
                    taskStackBuilder.addParentStack(MapsActivity.class);
                    taskStackBuilder.addNextIntent(intent);
                    PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(pendingIntent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    builder.setAutoCancel(true);
                    NotificationManager notificationManager = (NotificationManager)  getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
                    notificationManager.notify(0,builder.build());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ExampleJobService.this,"Child canceled",Toast.LENGTH_SHORT).show();

            }
        });
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 0; i < 100000; i++) {
//                    Log.d(TAG, "run: " + i);
//                    if (jobCancelled) {
//                        return;
//                    }
//
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                Log.d(TAG, "Job finished");
//                jobFinished(params, false);
//            }
//        }).start();
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "Job cancelled before completion");
        jobCancelled = true;
        return true;
    }
}