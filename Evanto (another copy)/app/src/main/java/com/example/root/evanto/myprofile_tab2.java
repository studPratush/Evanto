package com.example.root.evanto;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class myprofile_tab2 extends Fragment {

    ListView lst;
    ArrayList<String> arrayList = new ArrayList<>();
    String value;
    //ArrayList<String> arrayList2 = new ArrayList<>();
    DatabaseReference databaseReference,eventReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    public static String val;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView2 = inflater.inflate(R.layout.myprofile_tab2, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser  = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(firebaseUser.getUid());
        lst = (ListView)rootView2.findViewById(R.id.listEvent);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(rootView2.getContext(),android.R.layout.simple_expandable_list_item_1,arrayList);

        lst.setAdapter(arrayAdapter);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String data = dataSnapshot.getKey();
                if(!data.equals("Fullname") && !data.equals("DOB") & !data.equals("Email") & !data.equals("Gender") & !data.equals("Joined") & !data.equals("Mobile") & !data.equals("Password") & !data.equals("Participated")){
                    arrayList.add(data);
                    arrayAdapter.notifyDataSetChanged();
                }



                /*
*/

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

        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                value = adapterView.getItemAtPosition(i).toString();
                /*Toast.makeText(getActivity(),
                        "Click ListItem Number " + value, Toast.LENGTH_LONG)
                        .show();*/
                Intent intent = new Intent(getActivity(),ReaderActivity.class);
                intent.putExtra(val,value);
                startActivity(intent);
               // getEventDetails();
            }
        });



        return rootView2;
    }
    private void getEventDetails()
    {
        eventReference = FirebaseDatabase.getInstance().getReference().child(firebaseUser.getUid()+"/"+value);
        eventReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String eventname = dataSnapshot.getValue(String.class);
                Toast.makeText(getActivity(),""+eventname,Toast.LENGTH_SHORT).show();

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
