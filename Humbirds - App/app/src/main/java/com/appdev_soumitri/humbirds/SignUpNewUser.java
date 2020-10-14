package com.appdev_soumitri.humbirds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.auth.User;

import org.w3c.dom.Text;

import java.util.Objects;

public class SignUpNewUser extends AppCompatActivity {

    private EditText etEmail,etPass,etAge,etName;
    private TextView loginText,gndr;
    private Button btnSignUp;
    private String gender;
    private ProgressBar signupBar;
    private FirebaseAuth mAuth;

    private FirebaseDatabase root;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_new_user);

        ActionBar actionBar=getSupportActionBar();
        assert actionBar!=null;
        actionBar.setTitle("Sign Up");

        loginText=findViewById(R.id.loginText);

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Action: ","user chooses to login");
                startActivity(new Intent(SignUpNewUser.this,LoginActivity.class));
                finish();
            }
        });

        etName=findViewById(R.id.etNameNew);
        etAge=findViewById(R.id.etAgeNew);
        etEmail=findViewById(R.id.etEmailNew);
        etPass=findViewById(R.id.etPassNew);
        gndr=findViewById(R.id.tv2);
        btnSignUp=findViewById(R.id.btnSignUpNew);
        signupBar=findViewById(R.id.signupBar);

        mAuth=FirebaseAuth.getInstance();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email=etEmail.getText().toString().trim(),pass=etPass.getText().toString().trim();
                String name=etName.getText().toString().trim();
                int age=Integer.parseInt(etAge.getText().toString().trim());

                signUpUser(email,pass,name,age,gender);

            }
        });

    }

    private void signUpUser(final String email, String pass,final String name, final int age, final String gender) {

        // validation
        if(email.length()==0 || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please enter valid email");
            etEmail.requestFocus();
            return;
        }
        if(pass.length()<6) {
            etPass.setError("password length must atleast 6");
            etPass.requestFocus();
            return;
        }
        if(name.length()==0) {
            etName.setError("Please enter name");
            etName.requestFocus();
            return;
        }
        if(age<0 || age>100) {
            etAge.setError("enter valid age in years");
            etAge.requestFocus();
            return;
        }
        if(gender==null) {
            gndr.setError("Please specify gender");
            gndr.requestFocus();
            return;
        }

        // firebase authenticating new user

        signupBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(SignUpNewUser.this, "Sign Up successful", Toast.LENGTH_SHORT).show();
                            Log.d("Result: ","signed up  with email: "+email);

                            signupBar.setVisibility(View.GONE);

                            // adding to the database
                            FirebaseUser mUser=mAuth.getCurrentUser();
                            assert mUser != null;

                            String uID = mUser.getUid();

                            UserAPI user=new UserAPI(name,email,age,gender,uID);

                            Log.d("UserID: ",uID);

                            root=FirebaseDatabase.getInstance();
                            reference=root.getReference("Users");

                            reference.child(uID)
                                    .setValue(user);

                            // opening MainActivity.java
                            Intent intent=new Intent(SignUpNewUser.this,MainActivity.class);
                            intent.putExtra("name",user.getName());
                            intent.putExtra("email",user.getEmail());
                            intent.putExtra("age",user.getAge());
                            intent.putExtra("gender",user.getGender());

                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();

                        } else {
                            // if email already present
                            if(task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(SignUpNewUser.this, "Account already exists, please Login instead", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(SignUpNewUser.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });


    }

    public void onGenderSelected(View view)
    {
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.male:
                if (checked)
                    // male
                    gender="M";
                    break;
            case R.id.female:
                if (checked)
                    // female
                    gender="F";
                    break;
            case R.id.other:
                if(checked)
                    // other
                    gender="O";
                    break;
        }
    }
}