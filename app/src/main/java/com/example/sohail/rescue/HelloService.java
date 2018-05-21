package com.example.sohail.rescue;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import android.os.Process;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;
import java.util.TimerTask;

import static android.app.Service.START_STICKY;

public class HelloService extends Service {
    private Looper mServiceLooper;
    static boolean Notif = false;
    static boolean visited = false;
    public int counter=0;
    static int ik = 0;
    static Context context;
    private ServiceHandler mServiceHandler;
    public static int k=0;
    static int i =0;

    public HelloService(Context context){
        this.context = context;
    }

    public HelloService(){

    }

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {


        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {




            GPSTracker gps = new GPSTracker(HelloService.this);
            if(!gps.canGetLocation){
                stopSelf(msg.arg1);
                return;
            }
            final double MyLat = gps.getLatitude();
            final double MyLong = gps.getLongitude();

            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Emergency");
            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if(k == 0){
                        Toast.makeText(HelloService.this,"Child Added",Toast.LENGTH_SHORT).show();
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
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(HelloService.this,"M_CH_ID");
                        builder.setSmallIcon(R.drawable.ic_launcher);
                        builder.setContentTitle(name);
                        builder.setContentText("Need : "+need);
                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        Notif = true;
                        builder.setSound(notification);
                        Intent intent = new Intent(HelloService.this,MapsActivity.class);
                        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(HelloService.this);
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
                    Toast.makeText(HelloService.this,"Child changed",Toast.LENGTH_SHORT).show();
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
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(HelloService.this,"M_CH_ID");
                        builder.setSmallIcon(R.drawable.ic_launcher);
                        builder.setContentTitle(name);
                        builder.setContentText("Need : "+need);
                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        Notif = true;
                        visited = true;
                        builder.setSound(notification);
                        Intent intent = new Intent(HelloService.this,MapsActivity.class);
                        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(HelloService.this);
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
                    Toast.makeText(HelloService.this,"Child deleted",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    Toast.makeText(HelloService.this,"Child moved",Toast.LENGTH_SHORT).show();

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
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(HelloService.this,"M_CH_ID");
                        builder.setSmallIcon(R.drawable.ic_launcher);
                        builder.setContentTitle(name);
                        builder.setContentText("Need : "+need);
                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        Notif = true;
                        visited = true;
                        builder.setSound(notification);
                        Intent intent = new Intent(HelloService.this,MapsActivity.class);
                        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(HelloService.this);
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
                    Toast.makeText(HelloService.this,"Child canceled",Toast.LENGTH_SHORT).show();

                }
            });

            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                // Restore interrupt status.
//                Thread.currentThread().interrupt();
//            }
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
//            stopSelf(msg.arg1);
        }
    }

    @Override
    public void onCreate() {
        // Start up the thread running the service. Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block. We also make it
        // background priority so CPU-intensive work doesn't disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        Toast.makeText(this, "service starting ", Toast.LENGTH_SHORT).show();
//        // For each start request, send a message to start a job and deliver the
//        // start ID so we know which request we're stopping when we finish the job
//        super.onStartCommand(intent, flags, startId);
//        startTimer();
//        Message msg = mServiceHandler.obtainMessage();
//        msg.arg1 = startId;
//        mServiceHandler.sendMessage(msg);
//
//        // If we get killed, after returning from here, restart
//
//            return START_STICKY;
//    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(HelloService.this,"Service starting",Toast.LENGTH_SHORT).show();
        super.onStartCommand(intent, flags, startId);
        startTimer();
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("EXIT", "ondestroy!");
        Toast.makeText(HelloService.this,"Destroy",Toast.LENGTH_SHORT).show();
        Intent broadcastIntent = new Intent("com.example.dell.rescue.RestartSensor");
        sendBroadcast(broadcastIntent);
        stoptimertask();
    }

    private Timer timer;
    private TimerTask timerTask;
    long oldTime=0;
    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask(context);

        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 1000, 1000); //
    }

    /**
     * it sets the timer to print the counter every x seconds
     */
    public void initializeTimerTask(final Context context) {
        timerTask = new TimerTask() {
            public void run() {
//                if(ik == 0){
//                    Looper.prepare();
//                    ik++;
//                }
//                Toast.makeText(context,"Timer",Toast.LENGTH_SHORT).show();
                Log.i("in timer", "in timer ++++  "+ (counter++));
            }
        };
    }

    /**
     * not needed
     */
    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
