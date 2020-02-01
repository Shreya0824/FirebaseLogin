package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        final FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
        final TextView textView =findViewById(R.id.textView);
        final TextView textView2 =findViewById(R.id.textView2);
        final TextView textView3 =findViewById(R.id.textView3);
        FirebaseFirestore firebaseFirestore =FirebaseFirestore.getInstance();
        FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
        firebaseFirestore.collection("Users").document(firebaseAuth.getUid()).get().
                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                        DocumentSnapshot doc= task.getResult();
                        textView.setText(doc.get("name").toString());
                        textView2.setText(doc.get("hobbies").toString());
                        textView3.setText(doc.get("email").toString());
                        }
                    }
                });
        final ImageView imageView=findViewById(R.id.imageView);
        firebaseStorage.getReference().child(firebaseAuth.getUid().toString()+".jfif").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){
                    Glide.with(Main2Activity.this).load(task.getResult()).into(imageView);
                }
            }
        });
        Button button=findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent intent= new Intent(Main2Activity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
