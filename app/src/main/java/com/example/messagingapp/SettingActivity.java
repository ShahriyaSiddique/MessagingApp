package com.example.messagingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SettingActivity extends AppCompatActivity {

    Button updateAccBtn;
    EditText updateUserName,updateStatus;
    private String currentUserId;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        updateAccBtn = findViewById(R.id.update_setting_btn);
        updateUserName = findViewById(R.id.set_user_name);
        updateStatus= findViewById(R.id.set_profile_status);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUserId = firebaseAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        updateUserName.setVisibility(View.INVISIBLE);

        updateAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                updateSetting();
                
            }
        });

        retrieveUserInfo();


    }

    private void retrieveUserInfo()
    {
        databaseReference.child("Users").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists() && dataSnapshot.hasChild("name") && dataSnapshot.hasChild("image"))
                {
                    String retrieveUsername = dataSnapshot.child("name").getValue().toString();
                    String retrieveStatus= dataSnapshot.child("status").getValue().toString();
                    String retrieveImage = dataSnapshot.child("image").getValue().toString();
                    
                    updateUserName.setText(retrieveUsername);
                    updateStatus.setText(retrieveStatus);

                }
                else if (dataSnapshot.exists() && dataSnapshot.hasChild("name") )
                {
                    String retrieveUsername = dataSnapshot.child("name").getValue().toString();
                    String retrieveStatus= dataSnapshot.child("status").getValue().toString();

                    updateUserName.setText(retrieveUsername);
                    updateStatus.setText(retrieveStatus);
                }
                else 
                {
                    updateUserName.setVisibility(View.VISIBLE);
                    Toast.makeText(SettingActivity.this, "Please update your profile", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateSetting()
    {
        String setUserName = updateUserName.getText().toString().trim();
        String setUserStatus= updateStatus.getText().toString().trim();

        if (TextUtils.isEmpty(setUserName))
        {
            Toast.makeText(this, "please write your username", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(setUserStatus))
        {
            Toast.makeText(this, "please write your status", Toast.LENGTH_SHORT).show();
        }
        else
        {
            HashMap<String,String> profileMap = new HashMap<>();
            profileMap.put("uid",currentUserId);
            profileMap.put("name",setUserName);
            profileMap.put("status",setUserStatus);

            databaseReference.child("Users").child(currentUserId).setValue(profileMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) 
                        {
                        if (task.isSuccessful())
                        {
                            settingToMain();
                            Toast.makeText(SettingActivity.this, "profile updated successfully", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(SettingActivity.this, "Error :" +task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                        }
                    });
        }
    }
    private void settingToMain()
    {
        Intent intent = new Intent(SettingActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
