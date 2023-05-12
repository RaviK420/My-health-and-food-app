package com.example.food;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Register extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText mFullName,mEmail,mPassword;
    Spinner mDiet;
    Button mRegisterBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Intent receivingIntent = getIntent();



        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.Password);

        mRegisterBtn = findViewById(R.id.RegisterBtn);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);

//        if(fAuth.getCurrentUser() != null){
//            startActivity(new Intent(getApplicationContext(),MainActivity.class));
//            finish();
//        }

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String name = receivingIntent.getStringExtra("name");
                String diet = receivingIntent.getStringExtra("diet");
                String goal = receivingIntent.getStringExtra("goal");
                ArrayList<String> favourite = new ArrayList<>();
                ArrayList<String> fName = new ArrayList<>();
                ArrayList<String> fImage = new ArrayList<>();
                ArrayList<String> fServing = new ArrayList<>();
                ArrayList<String> fLikes = new ArrayList<>();
                ArrayList<String> time = new ArrayList<>();
                ArrayList<String> calories = new ArrayList<>();
                ArrayList<String> calImage = new ArrayList<>();
                ArrayList<String> calName = new ArrayList<>();
                ArrayList<String> amount = new ArrayList<>();
                ArrayList<String> calID = new ArrayList<>();
                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is required");
                    return;
                }
                if(password.length() < 6) {
                    mPassword.setError("Password must be Six characters long or more");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Register.this,"User Created",Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("Name",name);
                            user.put("Email",email);
                            user.put("Diet",diet);
                            user.put("Goal",goal);
                            user.put("Favourite_ID",favourite);
                            user.put("Name",fName);
                            user.put("Serving",fServing);
                            user.put("Likes",fLikes);
                            user.put("Time",time);
                            user.put("Image",fImage);
                            user.put("calories",calories);
                            user.put("calImages",calImage);
                            user.put("calName",calName);
                            user.put("amount",amount);
                            user.put("calID",calID);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG,"onSuccess: User profile is created for"+ userID);
                                }
                            });
                            progressBar.setVisibility(View.INVISIBLE);
                            startActivity(new Intent(getApplicationContext(),HomeScreen.class));

                        }
                        else{
                            Toast.makeText(Register.this,"Error!"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });


            }
        });


    }
}