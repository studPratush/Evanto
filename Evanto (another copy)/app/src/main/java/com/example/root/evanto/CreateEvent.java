package com.example.root.evanto;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CreateEvent extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference,userreference,eventreference;

    EditText eventName,discription,organizer,Tstart,Tend,mobile;
    public static EditText venue,city;
    ImageView Dstart,Dend;
    Button buttonCreateEvent;
    Calendar calendar;

    int day,month,year,hours,minutes;
    String storeStartDate,storeEndDate;
    String myLocation,myCity;
    String strUser;
    String storeCity,storeVenue;
    private static final int MY=1;
    public static String customCity,customVenue;
    AlertDialog alertDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createevent_activity_create_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        authStateListener = new FirebaseAuth.AuthStateListener(){


            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {


                if (firebaseUser != null)
                {
                    Toast.makeText(getApplicationContext(),"current user is"+firebaseUser.getEmail(),Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Error"+firebaseUser.getEmail(),Toast.LENGTH_SHORT).show();
                }
            }
        };





        eventName = (EditText)findViewById(R.id.textEventName);
        venue =(EditText)findViewById(R.id.textVenue);
        city=(EditText)findViewById(R.id.textCity);
        Tstart = (EditText)findViewById(R.id.txtStartDate);
        Tend = (EditText)findViewById(R.id.txtEndDate);
        mobile = (EditText)findViewById(R.id.textmobile);
        Dstart = (ImageView)findViewById(R.id.dateStart);
        Dend = (ImageView)findViewById(R.id.dateEnd);
        discription =(EditText)findViewById(R.id.textDescription);
        organizer =(EditText)findViewById(R.id.textOrganizer);
        buttonCreateEvent = (Button)findViewById(R.id.btnCreateEvent);

        venue.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(CreateEvent.this);

                View mView = getLayoutInflater().inflate(R.layout.dialog_map,null);

                final Button myLocation = (Button)mView.findViewById(R.id.btnMyLocation);
                final Button enterLocation = (Button)mView.findViewById(R.id.btnEnterLocation);
                myLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view){
                        useMyLocation();
                        Toast.makeText(getApplicationContext(),"hello",Toast.LENGTH_SHORT).show();

                    }
                });
                enterLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                      useCustomLocation();
//                        MapCreateEvent mapCreateEvent = new MapCreateEvent();
//                        customCity =mapCreateEvent.myCity;
//                        customVenue = mapCreateEvent.myLocation;
//                        venue.setText(customVenue);
//                        city.setText(customCity);

                    }
                });
                mBuilder.setView(mView);
                alertDialog = mBuilder.create();
                alertDialog.show();



            }
        });




        /*city.setText(myCity);
        venue.setText(myLocation);*/

        calendar = Calendar.getInstance();

        Dstart.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {

                pickDate();


            }
        });

        Dend.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                pickDate2();
            }
        });
        strUser = firebaseUser.getUid().toString();
        Toast.makeText(CreateEvent.this,""+strUser,Toast.LENGTH_SHORT).show();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(strUser);


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String str = dataSnapshot.child("Fullname").getValue(String.class);


                //  String name = dataSnapshot.getValue(String.class);
                organizer.setText(str);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        buttonCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* MapValues mapValues = new MapValues();

M
                String mcity =  mapValues.getCity();
                String mlocation = mapValues.getAddr();*/

               /* String mcity = MapCreateEvent.myCity;
                String mlocation = MapCreateEvent.myLocation;
                city.setText(mcity);
                venue.setText(mlocation);*/

              storeDatabase();


            }
        });


    }


private void storeDatabase() {

    String sEventName = eventName.getText().toString();
    String sVenue=venue.getText().toString();
    String sCity=city.getText().toString();
    String  sDstart=storeStartDate;
    String sDend=storeEndDate;
    String sDiscription=discription.getText().toString();
    String  sOrganozer=organizer.getText().toString();
    String sMobile = mobile.getText().toString();

Toast.makeText(getApplicationContext(),""+sVenue,Toast.LENGTH_SHORT).show();
    eventName.setError(null);

    boolean cancle = false;
    View focusView = null;


    if (TextUtils.isEmpty(sEventName)) {
        eventName.setError(getString(R.string.error_field_required));
        focusView = eventName;
        cancle = true;
    }
    else {
        userreference = FirebaseDatabase.getInstance().getReference().child(strUser);
        EachEvent eachUser = new EachEvent(sEventName,sVenue,sCity,sDstart,sDend,sDiscription,sOrganozer,sMobile);
        userreference.child(sEventName).setValue(eachUser);

        eventreference = FirebaseDatabase.getInstance().getReference().child("EventInfo");
        EachEvent eachEvent = new EachEvent(sEventName,sVenue,sCity,sDstart,sDend,sDiscription,sOrganozer,sMobile,strUser);
        eventreference.child(sEventName).setValue(eachEvent);

        Toast.makeText(getApplicationContext(),"Event Created",Toast.LENGTH_SHORT);

        startActivity(new Intent(CreateEvent.this,MainActivity.class));
        finish();
    }
}


    private void logoutUser()
    {
        firebaseAuth.signOut();
        startActivity(new Intent(CreateEvent.this,Start.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.createevent_create_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify Description parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            startActivity(new Intent(CreateEvent.this,MyProfile.class));
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Toast.makeText(this,"You are already on this activity",Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_logout) {
            logoutUser();

               /* firebaseAuth.signOut();
                startActivity(new Intent(CreateEvent.this,Start.class));
                finish();*/


        } else if (id == R.id.nav_share) {

            shareApp();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void shareApp()
    {

            try
            {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT,"Evanto");
                String aux = "\nlet me recommand you the Evanto app\n\n";
                aux= aux+"Sample link";
                intent.putExtra(Intent.EXTRA_TEXT,aux);
                startActivity(Intent.createChooser(intent,"Choose to share"));

            }catch (Exception e)
            {

            }

    }
    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(firebaseAuth!=null)
        {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    private  void  pickDate()
    {

        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        month +=1;
        year = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(CreateEvent.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int YEAR, int MONTH, int DAY) {
                Tstart.setText(YEAR + "/" + MONTH + "/" + DAY);
                pickTime();

            }
        }, year, month, day);

        datePickerDialog.show();

    }
    private void pickTime()
    {

        hours = calendar.get(Calendar.HOUR_OF_DAY);
        minutes = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(CreateEvent.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int HOURS, int MINUTES) {
                String appendDate = " " + HOURS + ":" + MINUTES;
                Tstart.append(appendDate);
                storeStartDate = Tstart.getText().toString();
            }
        }, hours, minutes, false);


        timePickerDialog.show();
    }



    private  void  pickDate2()
    {

        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        month +=1;
        year = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(CreateEvent.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int YEAR, int MONTH, int DAY) {
                Tend.setText(YEAR + "/" + MONTH + "/" + DAY);
                pickTime2();

            }
        }, year, month, day);

        datePickerDialog.show();

    }

    private void pickTime2()
    {

        hours = calendar.get(Calendar.HOUR_OF_DAY);
        minutes = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(CreateEvent.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int HOURS, int MINUTES) {
                String appendDate = " " + HOURS + ":" + MINUTES;
                Tend.append(appendDate);
                storeEndDate = Tend.getText().toString();
            }
        }, hours, minutes, false);


        timePickerDialog.show();
    }




    private void useMyLocation() {
        if (ContextCompat.checkSelfPermission(CreateEvent.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(CreateEvent.this, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
                ActivityCompat.requestPermissions(CreateEvent.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, MY);

            } else {
                ActivityCompat.requestPermissions(CreateEvent.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY);

            }
        } else {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            try {
                List<Address> lst1 = hereLocation(location.getLatitude(), location.getLongitude());
                myLocation =lst1.get(0).getAddressLine(0);
                myCity = lst1.get(0).getLocality();
                city.setText(myCity);
                venue.setText(myLocation);
                storeCity =city.getText().toString();
                storeVenue = venue.getText().toString();
                cancleDialog();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Not found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void  useCustomLocation()
    {
        startActivity(new Intent(CreateEvent.this,MapCreateEvent.class));
        cancleDialog();


    }


    public List<Address> hereLocation(double lat,double lon)
    {
        String curCity="";
        Geocoder geocoder = new Geocoder(CreateEvent.this, Locale.getDefault());
        List<Address>addressList=null;
        try{
            addressList = geocoder.getFromLocation(lat,lon,1);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return addressList;
    }

    private void cancleDialog()
    {
        alertDialog.cancel();
    }





}



    /*
    private void storeUserLevel(){

       String sEventName = eventName.getText().toString();
        String sVenue=venue.getText().toString();
        String sCity;
        String  sDstart=storeStartDate;
        String sDend=storeEndDate;
        String sDiscription=discription.getText().toString();
        String  sOrganozer=organizer.getText().toString();

    }

    private void storeAppLevel(){

        String sEventName = eventName.getText().toString();
        String sVenue=venue.getText().toString();
        String sCity;
        String  sDstart;
        String sDend;
        String sDiscription=discription.getText().toString();
        String  sOrganozer=organizer.getText().toString();

    }*/

