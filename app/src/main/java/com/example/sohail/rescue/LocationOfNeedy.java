package com.example.sohail.rescue;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LocationOfNeedy extends AppCompatActivity {
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    String Uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_of_needy);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null)
        {
            Uid = firebaseUser.getUid();
        }
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Location").child(Uid).child("Name");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String Latitude = dataSnapshot.getValue(String.class).toString();
                String Longitude = dataSnapshot.getValue(String.class).toString();
                String Name = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
