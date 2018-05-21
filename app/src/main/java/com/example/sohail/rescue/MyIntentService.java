package com.example.sohail.rescue;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MyIntentService extends IntentService {
    Context context;
    public MyIntentService() {
        super("MyIntentService");
    }

    public MyIntentService(Context context) {
        super("MyIntentService");
        this.context = context;
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
                ListenToRealTimeDatabase();
            }
        }


    private void ListenToRealTimeDatabase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Emergency");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Toast.makeText(MyIntentService.this,"Child Added",Toast.LENGTH_SHORT).show();
                String name = dataSnapshot.getKey();
                Emergency emergency = dataSnapshot.getValue(Emergency.class);
                String need = emergency.need;
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                String namE =firebaseUser.getDisplayName();
                if(!namE.equals(name)){
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(MyIntentService.this,"M_CH_ID");
                    builder.setSmallIcon(R.drawable.ic_launcher);
                    builder.setContentTitle(name);
                    builder.setContentText("Need : "+need);
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    builder.setSound(notification);
                    Intent intent = new Intent(MyIntentService.this,MapsActivity.class);
                    TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(MyIntentService.this);
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
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Toast.makeText(MyIntentService.this,"Child Changed",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Toast.makeText(MyIntentService.this,"Child Removed",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Toast.makeText(MyIntentService.this,"Child Moved",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MyIntentService.this,"Child Cancelled",Toast.LENGTH_SHORT).show();

            }
        });

    }
}
