package com.example.root.evanto;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by root on 30/9/17.
 */

public class myprofile_tab1 extends Fragment {

    TextView fname,email,mobile,gender,dob,joined;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.myprofile_tab1, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(firebaseUser.getUid());
        fname = (TextView)rootView.findViewById(R.id.setfname);
        email = (TextView)rootView.findViewById(R.id.setemail);
        mobile =(TextView)rootView.findViewById(R.id.setmobile);
        gender = (TextView)rootView.findViewById(R.id.setgender);
        dob = (TextView)rootView.findViewById(R.id.setdob);
        joined = (TextView)rootView.findViewById(R.id.setjoined);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String sname= dataSnapshot.child("Fullname").getValue(String.class);
                String semail= dataSnapshot.child("Email").getValue(String.class);
                String smobile= dataSnapshot.child("Mobile").getValue(String.class);
                String sgender= dataSnapshot.child("Gender").getValue(String.class);
                String sdob= dataSnapshot.child("DOB").getValue(String.class);
                String sjoined = dataSnapshot.child("Joined").getValue(String.class);
                fname.setText(sname);
                email.setText(semail);
                mobile.setText(smobile);
                gender.setText(sgender);
                dob.setText(sdob);
                joined.setText(sjoined);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return rootView;
    }

}
