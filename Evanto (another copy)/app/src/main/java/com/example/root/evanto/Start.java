package com.example.root.evanto;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Start extends AppCompatActivity implements View.OnClickListener {

    private Button login,register;
    private Intent intent;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authStateListener!=null)
        {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null)
                {
                    Toast.makeText(Start.this,"Login as :"+user.getEmail(),Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Start.this,MainActivity.class));
                    finish();
                }
                else
                {
                    Toast.makeText(Start.this,"StartActivity",Toast.LENGTH_SHORT).show();
                }
            }
        };


        login= (Button)findViewById(R.id.login_user);
        register = (Button)findViewById(R.id.register);

        login.setOnClickListener(this);
        register.setOnClickListener(this);




    }


    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.login_user) {

            intent = new Intent(Start.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        if(view.getId()==R.id.register)
        {
            intent = new Intent(Start.this,RegisterActivity.class);
            startActivity(intent);
            finish();

        }
    }
}
