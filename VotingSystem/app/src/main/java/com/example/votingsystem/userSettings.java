package com.example.votingsystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class userSettings extends AppCompatActivity
{
    DatabaseReference databaseReference;
    StorageReference storageReference;
    FirebaseUser mUser;
    String uID;

    CircleImageView imageV;
    TextInputEditText Name;
    TextInputEditText Age;
    ImageView   back;
    Button      submit;
    String imgName = "";

    Uri imageURI;
    boolean uploaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        imageV = (CircleImageView) findViewById(R.id.imageV);
        Name   = (TextInputEditText) findViewById(R.id.fName);
        Age    = (TextInputEditText) findViewById(R.id.age);
        back   = (ImageView) findViewById(R.id.go_back);
        submit = (Button) findViewById(R.id.save_changes);

        storageReference = FirebaseStorage.getInstance().getReference();

        imageV.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallery, 100);
            }
        });

        submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                boolean check = false;
                if(Name.getText().toString().trim().isEmpty())
                {
                    Name.setError("Please Enter Name...");
                    Name.requestFocus();
                    check = true;
                }
                else
                {
                    check = false;
                }

                if(Age.getText().toString().trim().isEmpty())
                {
                    Age.setError("Please Enter Age...");
                    Age.requestFocus();
                    check = true;
                }
                else
                {
                    check = false;
                }

                if(uploaded == false)
                {
                    check = true;
                    Toast.makeText(userSettings.this, "Please Upload Profile Picture...", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    check = false;
                }

                if(check == false)
                {
                    String fName = Name.getText().toString().trim();
                    String age   = Age.getText().toString().trim();

                    mUser = FirebaseAuth.getInstance().getCurrentUser();
                    uID = mUser.getUid();
                    databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                    databaseReference.child(uID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot)
                        {
                            Userdata userProfile = snapshot.getValue(Userdata.class);
                            if(userProfile != null)
                            {
                                imgName = userProfile.fullName + ".jpg";

                                StorageReference fileRef1 = storageReference.child(imgName);
                                fileRef1.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                                {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                                    {
                                        Toast.makeText(userSettings.this, "Updated Successfully...", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener()
                                {
                                    @Override
                                    public void onFailure(@NonNull Exception e)
                                    {

                                    }
                                });

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error)
                        {

                        }
                    });
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(userSettings.this, Dashboard.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                imageURI = data.getData();
                imageV.setImageURI(imageURI);
                uploaded = true;
            }
        }
    }
}