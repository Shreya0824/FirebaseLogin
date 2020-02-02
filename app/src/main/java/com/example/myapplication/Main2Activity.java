package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.KeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class Main2Activity extends AppCompatActivity {
     Boolean inputType=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        final ProgressBar progressBar=findViewById(R.id.progressBar);
        final FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
        final EditText textView =findViewById(R.id.textView);
        final EditText textView2 =findViewById(R.id.textView2);
        final EditText textView3 =findViewById(R.id.textView3);
        textView.setInputType(InputType.TYPE_NULL);
        textView2.setInputType(InputType.TYPE_NULL);
        textView3.setInputType(InputType.TYPE_NULL);
        final FirebaseFirestore firebaseFirestore =FirebaseFirestore.getInstance();
        FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
        firebaseFirestore.collection("Users").document(firebaseAuth.getUid()).get().
                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            progressBar.setVisibility(View.GONE);
                        DocumentSnapshot doc= task.getResult();
                        textView.setText(doc.get("name").toString());
                        textView2.setText(doc.get("hobbies").toString());
                        textView3.setText(doc.get("email").toString());
                        }
                    }
                });
        final ImageView imageView=findViewById(R.id.imageView);
        firebaseStorage.getReference().child(firebaseAuth.getUid().toString()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){
                    Glide.with(Main2Activity.this).load(task.getResult()).into(imageView);
                }
            }
        });
        final Button button=findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent intent= new Intent(Main2Activity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        final Button button5 =findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!inputType){
                    textView.setInputType(InputType.TYPE_CLASS_TEXT);
                    textView2.setInputType(InputType.TYPE_CLASS_TEXT);
                    textView3.setInputType(InputType.TYPE_CLASS_TEXT);
                    textView.requestFocus();
                    button5.setText("Save");
                    inputType=true;
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    firebaseFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).set(
                            new User(textView.getText().toString(), textView2.getText().toString(), textView3.getText().toString())).
                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        button5.setText("Edit");
                                        inputType=false;
                                        textView.setInputType(InputType.TYPE_NULL);
                                        textView2.setInputType(InputType.TYPE_NULL);
                                        textView3.setInputType(InputType.TYPE_NULL);
                                        Toast.makeText(Main2Activity.this, "Data Uploaded", Toast.LENGTH_LONG).show();
                                    }
                                else{   Toast.makeText(Main2Activity.this, "Upload Failed", Toast.LENGTH_LONG).show();
                                    }
                                }});

                }
            }
        });

    }
}
