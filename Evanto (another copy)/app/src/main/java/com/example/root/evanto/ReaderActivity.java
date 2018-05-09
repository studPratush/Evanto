package com.example.root.evanto;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

public class ReaderActivity extends AppCompatActivity {
    private Button scan_btn;
    TextView eventName;
    ListView lst;
    DatabaseReference databaseReference,childReference,eventReference;
    FirebaseUser user;
    String evname;
    Intent ii;
    List<String> arrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        scan_btn = (Button) findViewById(R.id.scan_btn);
        lst = (ListView) findViewById(R.id.listview);
        eventName = (TextView)findViewById(R.id.evname);
        ii = getIntent();
        evname = ii.getStringExtra(myprofile_tab2.val);
        eventName.setText(evname);
eventReference = FirebaseDatabase.getInstance().getReference("EventInfo").child(evname).child("Participant");
        arrayList.clear();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,arrayList);

        lst.setAdapter(arrayAdapter);
eventReference.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()) {
            if (dataSnapshot1.getValue().equals("true")) {
                arrayList.add(dataSnapshot1.getKey().toString());
                arrayAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
});

        final Activity activity = this;
        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents()==null){
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            }
            else {


                getContent(result);

            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private  void getContent(IntentResult intentResult)
    {
        IntentResult i = intentResult;


        String checkUser = i.getContents();
        final String updatesCheckUser = checkUser.substring(29);
       // Toast.makeText(this, i.getContents(),Toast.LENGTH_LONG).show();

        databaseReference = FirebaseDatabase.getInstance().getReference("EventInfo").child(evname).child("Participant");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
String iterator = dataSnapshot.getKey();
                if(iterator.equals(updatesCheckUser)){
storeDatabase(iterator);
                }
                else {
                    Toast.makeText(getApplicationContext(), "no user found",Toast.LENGTH_LONG).show();
                }
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

    private void storeDatabase(String iterator)
    {
        childReference = FirebaseDatabase.getInstance().getReference("EventInfo").child(evname).child("Participant");
        childReference.child(iterator).setValue("true");
        Toast.makeText(this,"Data saved",Toast.LENGTH_SHORT).show();
    }
}