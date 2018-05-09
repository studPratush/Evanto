package com.example.root.evanto;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Description extends AppCompatActivity implements View.OnClickListener{
    public TextView eventName,city,by,map,phone,address,dets;
    public Button apply;
    int convert;
    public String username;
    DatabaseReference databaseReference, childReference,ref1,ref2,userRef;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    List<String> list;
    AlertDialog alertDialog;
    public static String senduid,useEvent;
    public static String sendLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        userRef = FirebaseDatabase.getInstance().getReference().child(firebaseUser.getUid());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                username = dataSnapshot.child("Fullname").getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

       // t = (TextView) findViewById(R.id.viewonmap);
        Intent ii = getIntent();
        String a = ii.getStringExtra(RecyclerAdapter.name);
        convert = Integer.parseInt(a);
        //t.setText(a);

        eventName = (TextView)findViewById(R.id.eventNameTextView);
        city = (TextView)findViewById(R.id.city);
        by = (TextView)findViewById(R.id.organizer);
        map = (TextView)findViewById(R.id.mapview);
        phone = (TextView)findViewById(R.id.phoneTextView);
        address = (TextView)findViewById(R.id.addressTextView);
        apply = (Button)findViewById(R.id.applyButton);
        dets = (TextView)findViewById(R.id.dateSt);
        apply.setOnClickListener(this);
        map.setOnClickListener(this);

        databaseReference = FirebaseDatabase.getInstance().getReference("EventInfo");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list = new ArrayList<String>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    list.add(dataSnapshot1.getKey());

                }
                connection(list.get(convert));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    private void connection(String position) {
        childReference = FirebaseDatabase.getInstance().getReference("EventInfo").child(position);
        childReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String seventname=dataSnapshot.child("EventName").getValue(String.class);
                String scity=dataSnapshot.child("City").getValue(String.class);
                String sorg=dataSnapshot.child("Organizer").getValue(String.class);
                String sphone=dataSnapshot.child("Mobile").getValue(String.class);
                String saddress=dataSnapshot.child("Venue").getValue(String.class);
                String sdate=dataSnapshot.child("StartDate").getValue(String.class);
                eventName.setText("Event Name - "+seventname);
                city.setText("City - "+scity);
                by.setText("Event By - "+sorg);
                phone.setText("Contact -"+sphone);
                address.setText("Venue - "+saddress);
                dets.setText("Date - "+sdate.substring(0,9));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.mapview){
            String getLocation = address.getText().toString();

            Intent i = new Intent(Description.this,ViewMap.class);
            i.putExtra(sendLocation,getLocation.substring(8));
            startActivity(i);
//            Toast.makeText(Description.this," "+getLocation.substring(9),Toast.LENGTH_SHORT).show();

        }
        if(view.getId() == R.id.applyButton)
        {
            ref1 = FirebaseDatabase.getInstance().getReference("EventInfo").child(list.get(convert)).child("Participant");

            ref1.child(username).setValue("false");
            Toast.makeText(getApplicationContext(),"Hello",Toast.LENGTH_SHORT).show();

            ref2 = FirebaseDatabase.getInstance().getReference(firebaseUser.getUid()).child("Participated");
            ref2.child(eventName.getText().toString().substring(13)).setValue("");

            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(Description.this);

            View mView = getLayoutInflater().inflate(R.layout.activity_ask_generate,null);

           final Button QrAsk = (Button)mView.findViewById(R.id.askgenerate);

QrAsk.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        startQrActivity();

    }
});
            mBuilder.setView(mView);
            alertDialog = mBuilder.create();
            alertDialog.show();
            }
        }

        private void startQrActivity()
        {
            String userid = firebaseUser.getUid();
            useEvent = eventName.getText().toString();
            Intent i = new Intent(Description.this,GeneratorActivity.class);
            i.putExtra(senduid,userid+" "+username);
            startActivity(i);
            cancleDialog();
        }
    private void cancleDialog()
    {
        alertDialog.cancel();
    }
    }

