package com.example.root.evanto;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {


    EditText mFullname,mReenter,mMobile;
    //RadioButton mMale,mFemale;
    RadioGroup mRadioGroup;
    DatePicker datePicker;
    EditText mEmail,mPassword;
    Button mButton;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference rootReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseAuth = FirebaseAuth.getInstance();


        datePicker = (DatePicker)findViewById(R.id.datepicker);
        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);

        rootReference = FirebaseDatabase.getInstance().getReference();

        mFullname = (EditText)findViewById(R.id.fullname);
        mReenter = (EditText) findViewById(R.id.repassword);
        mMobile = (EditText) findViewById(R.id.mobile);

        mEmail = (EditText)findViewById(R.id.emailText);
        mPassword = (EditText)findViewById(R.id.passwordText);
        mButton = (Button)findViewById(R.id.email_sign_up_button);
        final String email = mEmail.getText().toString();
        //mEmail.setError(null);
        //final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";



        //mEmail.setError(null);
        mButton.setOnClickListener(this);
    }
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 8;
    }
    private boolean isMobileValid(String mobile) {
        //TODO: Replace this with your own logic
        return mobile.length() ==10;
    }


    private void registerUser() {


        mEmail.setError(null);
        mPassword.setError(null);

        String fullname = mFullname.getText().toString();
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        String repassword = mReenter.getText().toString();
        String mobile = mMobile.getText().toString();

        boolean cancle = false;
        View focusView = null;



        if(TextUtils.isEmpty(fullname))
        {
            mFullname.setError(getString(R.string.error_field_required));
            focusView = mFullname;
            cancle = true;
        }

        else if(!isEmailValid(email))
        {
            mEmail.setError("Invalid email");
            focusView = mEmail;
            cancle = true;
        }
        else if (TextUtils.isEmpty(email)) {
            mEmail.setError(getString(R.string.error_field_required));
            focusView = mEmail;
            cancle = true;
        }
        else if (TextUtils.isEmpty(password)) {
            mPassword.setError(getString(R.string.error_field_required));
            focusView = mPassword;
            cancle = true;

        }
        else if(!isPasswordValid(password)){
            mPassword.setError("Password should be atleast 8 characters long");
            focusView = mPassword;
            cancle = true;
        }
        else if(!repassword.equals(password))
        {

            mReenter.setError(getString(R.string.error_field_should_same));
            focusView = mReenter;
            cancle= true;
        }
        else if(!isMobileValid(mobile))
        {
            mMobile.setError("Invalid mobile number");
            focusView = mMobile;
            cancle= true;
        }
        else if(TextUtils.isEmpty(mobile))
        {
            mMobile.setError(getString(R.string.error_field_required));
            focusView = mMobile;
            cancle = true;
        }



        if (cancle) {
            focusView.requestFocus();
        } else {


            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                enterTodatabase();
                                Toast.makeText(getApplicationContext(), "Registerd successfully", Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Registerd unsuccessfully", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });









        }
    }

    private  void  enterTodatabase()
    {
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
        String formattedDate = df.format(c.getTime());


        String fullname = mFullname.getText().toString();
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        String mobile = mMobile.getText().toString();

        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth()+1;
        int year = datePicker.getYear();

        String dateFormat = new String(day+"/"+month+"/"+year);

        String radiovalue=((RadioButton)findViewById(mRadioGroup.getCheckedRadioButtonId())).getText().toString();

        String participated="Sample";

        firebaseUser = firebaseAuth.getCurrentUser();

        String uId = firebaseUser.getUid();
        UserInformation userInformation = new UserInformation(fullname,email,password,mobile,radiovalue,dateFormat,formattedDate,participated);
        rootReference.child(uId).setValue(userInformation);




        addNotification();
    }

    private void addNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_assignment_late_black_24dp)
                        .setContentTitle("Registered Successfully")
                        .setContentText("Welcome to Evanto");

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
    @Override
    public void onClick(View view) {
        registerUser();
    }


}
