package com.shivam.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.shivam.whatsappclone.Models.Users;
import com.shivam.whatsappclone.databinding.ActivitySignUpBinding;

public class signUp extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    ProgressDialog progressDialog;

    ActivitySignUpBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        auth =FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(signUp.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We're creating your account");


        binding.etclicksignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(signUp.this,SignIn.class));
            }
        });


        binding.btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();

                final String name = binding.etname.getText().toString();
                final String email = binding.etemail.getText().toString();
                final String password = binding.etpassword.getText().toString();

                auth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        progressDialog.dismiss();

                        if(task.isSuccessful())
                        {
                            Users user = new Users(name,email,password);
                            String id = task.getResult().getUser().getUid();
                            database.getReference().child("Users").child(id).setValue(user);
                            startActivity(new Intent(signUp.this,SignIn.class));
                            Toast.makeText(signUp.this, "User Created Successful", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(signUp.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }
}