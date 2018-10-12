package com.example.harshj.gpstracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class InviteCodeActivity extends AppCompatActivity {

    String name, email, password, date, issharing, code;
    Uri imageUri;
    ProgressDialog progressDialog;
    TextView t1;
    FirebaseAuth auth;
    String userId;
    FirebaseUser user;
    StorageReference storageReference;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_code);
        t1 = (TextView) findViewById(R.id.textView);
        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        Intent myIntent = getIntent();


        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        storageReference = FirebaseStorage.getInstance().getReference().child("user_Image");


        if (myIntent != null) {
            name = myIntent.getStringExtra("name");
            email = myIntent.getStringExtra("email");
            password = myIntent.getStringExtra("password");
            code = myIntent.getStringExtra("code");
            issharing = myIntent.getStringExtra("isSharing");
            imageUri = myIntent.getParcelableExtra("imageUri");
        }

        t1.setText(code);
    }

    public void registerUser(View v) {
        progressDialog.setMessage("Please Wait We're Creating Account");
        progressDialog.show();


        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    user = auth.getCurrentUser();

                    CreateUser createUser = new CreateUser(name, email, password, code, "false", "na", "na", "na",user.getUid());

                    user = auth.getCurrentUser();
                    userId = user.getUid();


                    reference.child(userId).setValue(createUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {


                                /* for Image saving  */
                                StorageReference sr = storageReference.child(user.getUid() + ".jpg");
                                sr.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        if (task.isSuccessful()) {



                                            /*may be .getdownload url*/
                                            String download_img_path = task.getResult().getTask().toString();
                                            reference.child(user.getUid()).child("imageUrl").setValue(download_img_path).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {


                                                        progressDialog.dismiss();

                                                        Toast.makeText(getApplicationContext(), "User Registered Successfully", Toast.LENGTH_SHORT).show();

                                                        finish();
                                                        Intent myIntent = new Intent(InviteCodeActivity.this, UserLocationMainActivity.class);
                                                        startActivity(myIntent);


                                                    } else {
                                                        progressDialog.dismiss();

                                                        Toast.makeText(getApplicationContext(), "Could Not Register", Toast.LENGTH_SHORT).show();

                                                    }
                                                }

                                            });
                                        }
                                    }


                                });


                            } else {
                                progressDialog.dismiss();

                                Toast.makeText(getApplicationContext(), "Could Not Register", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });


                }


            }
        });

    }
}




