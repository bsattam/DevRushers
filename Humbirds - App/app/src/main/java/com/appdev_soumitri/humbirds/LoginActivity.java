package com.appdev_soumitri.humbirds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private TextView signupText;
    private EditText etEmail, etPass;
    private Button btnLogin;
    private ProgressBar loginBar;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase root;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar=getSupportActionBar();
        assert actionBar!=null;
        actionBar.setTitle("Login");

        signupText=findViewById(R.id.signupText);
        etEmail=findViewById(R.id.etEmailLogin);
        etPass=findViewById(R.id.etPassLogin);
        loginBar=findViewById(R.id.loginBar);

        mAuth=FirebaseAuth.getInstance();

        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Action: ","User chooses to Sign Up");
                startActivity(new Intent(LoginActivity.this,SignUpNewUser.class));
                finish();
            }
        });

        btnLogin=findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email=etEmail.getText().toString().trim();
                String pass=etPass.getText().toString().trim();

                authenticateLogin(email,pass);
            }
        });
    }

    // function to authenticate and login the user
    private void authenticateLogin(final String email, String pass) {
        if(email.length()==0 || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Invalid email");
            etEmail.requestFocus();
            return;
        }
        if(pass.length()==0) {
            etPass.setError("password is required");
            etPass.requestFocus();
            return;
        }

        // firebase authentication
        loginBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                               Log.d("Status: ","Login successful");
                                Toast.makeText(LoginActivity.this, "Logged in successfully !", Toast.LENGTH_SHORT).show();
                                loginBar.setVisibility(View.GONE);

                                // retrieve from DB
                                mUser=mAuth.getCurrentUser();
                                assert mUser != null;

                                final String uID = mUser.getUid();

                                root=FirebaseDatabase.getInstance();
                                reference=root.getReference("Users");

                                Query checkUser = reference.orderByChild("userID").equalTo(uID);

                                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        if(snapshot.exists()) {

                                            String _name=snapshot.child(uID).child("name").getValue(String.class);
                                            int _age=Objects.requireNonNull(snapshot.child(uID).child("age").getValue(Integer.class));
                                            String _gender = snapshot.child(uID).child("gender").getValue(String.class);

                                            Log.d("name:",_name);
                                            Log.d("age:",_age+" ");
                                            Log.d("gender",_gender);

                                            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                            intent.putExtra("name",_name);
                                            intent.putExtra("email",email);
                                            intent.putExtra("age",_age);
                                            intent.putExtra("gender",_gender);

                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();

                                        }

                                        else {
                                            Toast.makeText(LoginActivity.this, "Failed to retrieve data !", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            } else {
                                Log.d("Status","Authentication Unsuccessful");
                                loginBar.setVisibility(View.GONE);
                                Toast.makeText(LoginActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                    }
                });

    }
}