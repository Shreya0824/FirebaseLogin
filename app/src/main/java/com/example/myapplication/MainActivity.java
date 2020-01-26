package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final FirebaseAuth firebaseAuth =FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null){
            Intent intent = new Intent(MainActivity.this, Main2Activity.class);
            startActivity(intent);
            finish();
        }

        Button button =findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressBar progressBar = findViewById(R.id.progressBar);
                EditText email=findViewById(R.id.editText);
                EditText password=findViewById(R.id.editText2);
                String emailText= email.getText().toString();
                String passwordText= password.getText().toString();
                if(!(emailText.isEmpty()) && !(passwordText.isEmpty())){
                    progressBar.setVisibility(View.VISIBLE);
                    firebaseAuth.signInWithEmailAndPassword(emailText, passwordText).
                            addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if(task.isSuccessful()) {
                                        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        Toast.makeText(MainActivity.this, "Wrong Credentials", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(MainActivity.this, "Enter Email and Password", Toast.LENGTH_LONG).show();
                }
            }
        });
        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Main3Activity.class);
                startActivity(intent);
            }
        });
    }
}
