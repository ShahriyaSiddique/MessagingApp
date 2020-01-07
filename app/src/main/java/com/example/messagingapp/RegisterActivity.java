package com.example.messagingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private Button registerBtn;
    private EditText emailET,passwordET;
    private TextView alreadyHaveAccount;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference dbReference;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerBtn = findViewById(R.id.register_btn);
        emailET = findViewById(R.id.register_email);
        passwordET = findViewById(R.id.register_password);
        alreadyHaveAccount = findViewById(R.id.already_have_account);
        loadingBar=new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        dbReference = FirebaseDatabase.getInstance().getReference();


        alreadyHaveAccount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                registerToLogin();
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                validate();


            }
        });
    }

    private void registerToLogin()
    {
        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
    }

    private void validate()
    {
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();
        
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Creating New Account");
            loadingBar.setMessage("Please wait.. Until account you created successfully");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            firebaseAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                           if (task.isSuccessful())
                           {
                               String currentUserID = firebaseAuth.getCurrentUser().getUid();
                               dbReference.child("Users").child(currentUserID).setValue("");

                               registerToMain();
                               loadingBar.dismiss();
                               Toast.makeText(RegisterActivity.this, "Your account is created", Toast.LENGTH_SHORT).show();


                           }
                           else
                           {
                               loadingBar.dismiss();
                               Toast.makeText(RegisterActivity.this, "Error :" +task.getException(), Toast.LENGTH_SHORT).show();
                           }
                        }

                        private void registerToMain()
                        {
                            Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    });
        }

    }
}
