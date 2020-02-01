package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Main3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText textView = findViewById(R.id.textView);
                final EditText textView2 = findViewById(R.id.textView2);
                final EditText textView3 = findViewById(R.id.textView3);
                final EditText textView5 = findViewById(R.id.textView5);
                final ProgressBar progressBar2=findViewById(R.id.progressBar2);
                if (!(textView.getText().toString().isEmpty()) && !(textView2.getText().toString().isEmpty()) && !(textView3.getText().toString().isEmpty()) && !(textView5.getText().toString().isEmpty())) {
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
                                                            firebaseFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).set(
                                                                    new User(textView3.getText().toString(), textView5.getText().toString(), textView.getText().toString())).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                 @Override
                                                                                                 public void onComplete(@NonNull Task<Void> task) {
                                                                                                     progressBar2.setVisibility(View.GONE);
                                                                                                     if (task.isSuccessful()) {
                                                                                                         Toast.makeText(Main3Activity.this, "Successfully Registered", Toast.LENGTH_LONG).show();
                                                                                                         firebaseAuth.signOut();
                                                                                                         onBackPressed();
                                                                                                         finish();
                                                                                                     } else {
                                                                                                         Toast.makeText(Main3Activity.this, "Failed", Toast.LENGTH_LONG).show();
                                                                                                     }
                                                                                                 }
                                                                                             }
                                                            );

                                                        } else {
                                                            progressBar2.setVisibility(View.GONE);
                                                            Toast.makeText(Main3Activity.this, "Failed Signing in", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                    } else {
                                        progressBar2.setVisibility(View.GONE);
                                        Toast.makeText(Main3Activity.this, "Failed Registering", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });


                }else{
                    Toast.makeText(Main3Activity.this, "Enter all Details", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
