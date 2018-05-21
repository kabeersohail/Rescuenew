package com.example.sohail.rescue;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EmergencyList extends AppCompatActivity {
    String Latitude,Longitude,Name;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference0,databaseReference,databaseReference1;
    String v,key,lat;
    private ListView mUserList;
    private DatabaseReference mDatabase;
    private ArrayList<String> mUserNames = new ArrayList<>();
    private ArrayList<String> mKeys = new ArrayList<>();
//    GetLocationOfNeedy getLocationOfNeedy;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_list);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("EmergencyNames");
//        mUserList = (ListView) findViewById(R.id.XmlEmergency);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(EmergencyList.this,android.R.layout.simple_list_item_1,mUserNames);
        mUserList.setAdapter(arrayAdapter);

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



        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String value = dataSnapshot.getValue(String.class);
                String ku = dataSnapshot.getKey();
                String k = mDatabase.getKey();
                final DatabaseReference databaseReference =  FirebaseDatabase.getInstance().getReference("DetailsOfNeedy").child(Name).child("Latitude");
                final DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("DetailsOfNeedy").child(Name).child("Longitude");
                String ki = databaseReference.getKey();
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot1) {
                        String lat = (String) dataSnapshot1.getValue().toString();
//                        Toast.makeText(EmergencyList.this,"when Lat"+lat,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                databaseReference2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot2) {
                        String longi = (String) dataSnapshot2.getValue().toString();
//                        Toast.makeText(EmergencyList.this,"when Long"+longi,Toast.LENGTH_SHORT).show();
                        Toast.makeText(EmergencyList.this,"Name:"+Name+"Latitude:"+Latitude+",Longitude"+Longitude,Toast.LENGTH_SHORT).show();
                        double lati = Double.parseDouble(Latitude);
                        double longit = Double.parseDouble(Longitude);
                        MapsActivity.addMarker(lati,longit,Name);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
//                v = dataSnapshot.getValue(String.class);
                mUserNames.add(value);
                String key = dataSnapshot.getKey();
                mKeys.add(key);
                arrayAdapter.notifyDataSetChanged();
//                Toast.makeText(EmergencyList.this,"value"+v,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String value = dataSnapshot.getValue(String.class);
                key = dataSnapshot.getValue(String.class);
                int index = mKeys.indexOf(key);
                mUserNames.set(index,value);
                arrayAdapter.notifyDataSetChanged();
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

        mUserList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
}
