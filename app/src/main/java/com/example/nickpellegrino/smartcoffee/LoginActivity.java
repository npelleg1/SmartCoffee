package com.example.nickpellegrino.smartcoffee;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private boolean isVendor;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        database = FirebaseDatabase.getInstance();
        final EditText emailText = (EditText) findViewById(R.id.emailTextField);
        final EditText passwordText = (EditText) findViewById(R.id.passwordTextField);
        isVendor = false;
        myRef = database.getReference().child("Vendors");
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    new VerifyVendorTask().execute(mAuth.getCurrentUser().getEmail());
                } else {
                    // User is signed out
                }
            }
        };

        Button signUpButton = (Button) findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AuthenticateTask().execute(emailText.getText().toString(), passwordText.getText().toString());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private class AuthenticateTask extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... params) {
            mAuth.createUserWithEmailAndPassword(params[0], params[1])
                    .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Sign Up Failed! Try Again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            return null;
        }
    }

    private class VerifyVendorTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            final String email = params[0];
            myRef = database.getReference().child("Vendors");

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        if (ds.getKey().equals("email") && ds.getValue().equals(email)){
                            isVendor = true;
                            Intent intent = new Intent(getApplicationContext(), VendorHomeActivity.class);
                            intent.putExtra("UserID", mAuth.getCurrentUser().getUid());
                            startActivity(intent);
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println(databaseError.getMessage());
                }
            });
            return isVendor;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (!result) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.putExtra("UserID", mAuth.getCurrentUser().getUid());
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(getApplicationContext(), VendorHomeActivity.class);
                intent.putExtra("UserID", mAuth.getCurrentUser().getUid());
                startActivity(intent);
            }
        }
    }
}


