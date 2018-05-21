package com.example.sohail.rescue;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class EmergencyPeople extends AppCompatActivity implements View.OnClickListener{
    String Latitude,Longitude,Name;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference0,databaseReference,databaseReference1;
    Button JavaShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_people);
        JavaShow = findViewById(R.id.XmlShow);
        JavaShow.setOnClickListener(this);

        databaseReference0 = FirebaseDatabase.getInstance().getReference("EmergencyNames");
        databaseReference0.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Name = dataSnapshot.getValue(String.class);
                EmergencyLocation(Name);
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

//        firebaseDatabase = FirebaseDatabase.getInstance();
//        databaseReference =  firebaseDatabase.getReference("DetailsOfNeedy").child(Name).child("Latitude");
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Latitude = dataSnapshot.getValue().toString();
//                Toast.makeText(EmergencyPeople.this,"Latitude"+dataSnapshot.getValue(),Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//        databaseReference1 =  firebaseDatabase.getReference("DetailsOfNeedy").child(Name).child("Longitude");
//        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Longitude = dataSnapshot.getValue().toString();
//                Toast.makeText(EmergencyPeople.this,"Longitude"+dataSnapshot.getValue(),Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

    }

    public void EmergencyLocation(String Name){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference =  firebaseDatabase.getReference("DetailsOfNeedy").child(Name).child("Latitude");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Latitude = dataSnapshot.getValue().toString();
//                Toast.makeText(EmergencyPeople.this,"Latitude"+dataSnapshot.getValue(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference1 =  firebaseDatabase.getReference("DetailsOfNeedy").child(Name).child("Longitude");
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Longitude = dataSnapshot.getValue().toString();
//                Toast.makeText(EmergencyPeople.this,"Longitude"+dataSnapshot.getValue(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if(JavaShow == v){
            Toast.makeText(EmergencyPeople.this,"Sohail:-"+"Latitude"+Latitude+",Longitude"+Longitude,Toast.LENGTH_SHORT).show();
//            Toast.makeText(EmergencyPeople.this,"Name"+Name,Toast.LENGTH_SHORT).show();
        }
    }
}
