package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Main3Activity extends AppCompatActivity {

    Uri filePath=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Button button2 = findViewById(R.id.button2);
        Button button4 = findViewById(R.id.button4);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText textView = findViewById(R.id.textView);
                final EditText textView2 = findViewById(R.id.textView2);
                final EditText textView3 = findViewById(R.id.textView3);
                final EditText textView5 = findViewById(R.id.textView5);
                final ProgressBar progressBar2=findViewById(R.id.progressBar2);
                if (!(textView.getText().toString().isEmpty()) && !(textView2.getText().toString().isEmpty()) && !(textView3.getText().toString().isEmpty()) && !(textView5.getText().toString().isEmpty())){
                    if(!(filePath==null)){
                    progressBar2.setVisibility(View.VISIBLE);
                    final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                    firebaseAuth.createUserWithEmailAndPassword(
                            textView.getText().toString(),
                            textView2.getText().toString()).
                            addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Main3Activity.this, "Registered", Toast.LENGTH_LONG).show();
                                        firebaseAuth.signInWithEmailAndPassword(textView.getText().toString(), textView2.getText().toString()).
                                                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(Main3Activity.this, "Loggedin", Toast.LENGTH_LONG).show();
                                                            FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();
                                                                    firebaseStorage.getReference().child(firebaseAuth.getCurrentUser().getUid()).putFile(filePath).
                                                                            addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                                                    firebaseFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).set(
                                                                                            new User(textView3.getText().toString(), textView5.getText().toString(), textView.getText().toString())).
                                                                                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                @Override public void onComplete(@NonNull Task<Void> task) { progressBar2.setVisibility(View.GONE);
                                                                                                if (task.isSuccessful()) {
                                                                                                    Toast.makeText(Main3Activity.this, "Successfully Registered", Toast.LENGTH_LONG).show();
                                                                                                    Intent intent = new Intent(Main3Activity.this, Main2Activity.class);
                                                                                                    startActivity(intent);
                                                                                                } else {
                                                                                                    Toast.makeText(Main3Activity.this, "Failed", Toast.LENGTH_LONG).show();
                                                                                                firebaseAuth.signOut();
                                                                                                }}
                                                                                            });
                                                                                }
                                                                            });

                                                        } else {
                                                            progressBar2.setVisibility(View.GONE);
                                                            Toast.makeText(Main3Activity.this, "Failed Signing in", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                    } else {
                                        progressBar2.setVisibility(View.GONE);
                                        Toast.makeText(Main3Activity.this, "Failed Registering" + task.getException(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                    }else{
                        Toast.makeText(Main3Activity.this, "Choose an image first", Toast.LENGTH_LONG).show();
                    }


                }else{
                    Toast.makeText(Main3Activity.this, "Enter all Details", Toast.LENGTH_LONG).show();
                }
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
        }


    }
}
