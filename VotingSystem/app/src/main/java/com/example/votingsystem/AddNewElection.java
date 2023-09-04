package com.example.votingsystem;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class AddNewElection extends AppCompatActivity
{
    ImageView back;
    ImageView pic_1;
    ImageView pic_2;
    EditText  title, name_1, name_2, email_1, email_2, roll_1, roll_2;
    Button    submit;
    RadioButton finish, start;

    Uri imageUri1, imageUri2;

    String Tit;
    String V1;
    String V2;
    String usID;
    String Stat;
    String I1;
    String I2;
    String e1;
    String e2;
    String n1;
    String n2;
    String r1;
    String r2;

    FirebaseAuth firebaseAuth;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    int incrementElection = 0;
    int incrementImage    = 0;

    boolean uploaded1 = false;
    boolean uploaded2 = false;

    private FirebaseUser mUser;
    private DatabaseReference reference;
    private String uID;

    String tempName  = "";
    String tempName2 = "";
    String v1 = "0",v2 = "0";
    String status = "On-Going";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_election);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        uID = mUser.getUid();

        start  = (RadioButton) findViewById(R.id.ongoin);
        finish = (RadioButton) findViewById(R.id.conc);
        title  = (EditText)  findViewById(R.id.title_for_election);
        pic_1  = (ImageView) findViewById(R.id.pic1);
        pic_2  = (ImageView) findViewById(R.id.pic2);
        email_1= (EditText)  findViewById(R.id.email1);
        email_2= (EditText)  findViewById(R.id.email2);
        name_1 = (EditText)  findViewById(R.id.name1);
        name_2 = (EditText)  findViewById(R.id.name2);
        roll_1 = (EditText)  findViewById(R.id.roll1);
        roll_2 = (EditText)  findViewById(R.id.roll2);
        submit = (Button)    findViewById(R.id.submitbtn);

        start.setVisibility(View.GONE);
        finish.setVisibility(View.GONE);

        start.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish.setChecked(false);
                status = "On-Going";
            }
        });
        finish.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                start.setChecked(false);
                status = "Concluded";
            }
        });

        Bundle intent = getIntent().getExtras();
        if(intent != null)
        {
            Tit = intent.getString("Tit");
            e1 = intent.getString("e1");
            e2 = intent.getString("e2");
            n1 = intent.getString("n1");
            n2 = intent.getString("n2");
            r1 = intent.getString("r1");
            r2 = intent.getString("r2");
            V1 = intent.getString("V1");
            V2 = intent.getString("V2");
            usID = intent.getString("uID");
            Stat = intent.getString("Stat");
            I1 = intent.getString("I1");
            I2 = intent.getString("I2");

            incrementImage = Integer.parseInt(intent.getString("Position"));
            incrementElection = Integer.parseInt(intent.getString("Position"));
//            incrementLocation--;

            title.setText(Tit);
            email_1.setText(e1);
            email_2.setText(e2);
            name_1.setText(n1);
            name_2.setText(n2);
            roll_1.setText(r1);
            roll_2.setText(r2);
            v1 = V1;
            v2 = V2;
            status = Stat;
            start.setVisibility(View.VISIBLE);
            finish.setVisibility(View.VISIBLE);

            submit.setText("Update Changes");
            uploaded1 = true;
            uploaded2 = true;

            StorageReference storageReference = FirebaseStorage.getInstance().getReference(I1);
            try{
                File localfile = File.createTempFile("tempfile",".jpg");
                storageReference.getFile(localfile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>()
                        {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot)
                            {
                                Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                                pic_1.setImageBitmap(bitmap);
                            }
                        }).addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {

                    }
                });
            }catch (IOException e)
            {
                e.printStackTrace();
            }


            StorageReference storageReference2 = FirebaseStorage.getInstance().getReference(I2);
            try{
                File localfile = File.createTempFile("tempfile",".jpg");
                storageReference2.getFile(localfile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>()
                        {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot)
                            {
                                Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                                pic_2.setImageBitmap(bitmap);
                            }
                        }).addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {

                    }
                });
            }catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        setValueElectionIncrement();
        SetValueImageIncrement();

        back   = (ImageView) findViewById(R.id.go_back);
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AddNewElection.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });

        pic_1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallery, 100);
            }
        });
        pic_2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallery, 200);
            }
        });

        submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                boolean check = false;

                if(title.getText().toString().trim().isEmpty())
                {
                    check = true;
                    title.setError("Please Enter a Title");
                }
                if(name_1.getText().toString().trim().isEmpty())
                {
                    check = true;
                    name_1.setError("Please Enter Person 1's Name");
                }
                if(name_2.getText().toString().trim().isEmpty())
                {
                    check = true;
                    name_2.setError("Please Enter Person 2's Name");
                }
                if(email_1.getText().toString().trim().isEmpty())
                {
                    check = true;
                    email_1.setError("Please Enter Person 1's Email");
                }
                if(email_2.getText().toString().trim().isEmpty())
                {
                    check = true;
                    email_2.setError("Please Enter Person 2's Email");
                }
                if(roll_1.getText().toString().trim().isEmpty())
                {
                    check = true;
                    roll_1.setError("Please Enter Person 1's Email");
                }
                if(roll_2.getText().toString().trim().isEmpty())
                {
                    check = true;
                    roll_2.setError("Please Enter Person 2's Email");
                }

                if(uploaded1 == false && uploaded2 == false)
                {
                    check = true;
                    Toast.makeText(AddNewElection.this, "Please Upload Person 1's & Person 2's Picture", Toast.LENGTH_SHORT).show();
                }
                else if(uploaded1 == false)
                {
                    check = true;
                    Toast.makeText(AddNewElection.this, "Please Upload Person 1's Picture", Toast.LENGTH_SHORT).show();
                }
                else if(uploaded2 == false)
                {
                    check = true;
                    Toast.makeText(AddNewElection.this, "Please Upload Person 2's Picture", Toast.LENGTH_SHORT).show();
                }

                if(check == false)
                {
                    String Title  = title.getText().toString().trim();
                    String name1  = name_1.getText().toString().trim();
                    String name2  = name_2.getText().toString().trim();
                    String email1 = email_1.getText().toString().trim();
                    String email2 = email_2.getText().toString().trim();
                    String roll1  = roll_1.getText().toString().trim();
                    String roll2  = roll_2.getText().toString().trim();
                    String userID = uID;

                    submitData(Title, name1, name2, email1, email2, roll1, roll2, imageUri1, imageUri2, v1, v2, userID);
                }
            }
        });
    }

    private void setValueElectionIncrement()
    {
        reference.child(uID).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                Userdata userProfile = snapshot.getValue(Userdata.class);

                if(userProfile != null)
                {
                    tempName = userProfile.fullName;

                    if(tempName.substring(tempName.length() - 1).matches("[0-9]") == false)
                    {
                        String nameCopy = tempName + "_" + incrementElection;
                        incrementElection = Integer.parseInt(nameCopy.substring(nameCopy.length() - 1 ));
                    }
                    else
                    {
                        incrementElection = Integer.parseInt(tempName.substring(tempName.length() - 1 ));
                    }
                    incrementElection++;

                    tempName = tempName + "_" + incrementElection;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(AddNewElection.this, "Error: Something went wrong...", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void SetValueImageIncrement()
    {
        reference.child(uID).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                Userdata userProfile = snapshot.getValue(Userdata.class);

                if(userProfile != null)
                {
                    tempName2 = userProfile.fullName;

                    if(tempName2.substring(tempName2.length() - 1).matches("[0-9]") == false)
                    {
                        String nameCopy = tempName2 + "_" + incrementImage;
                        incrementImage = Integer.parseInt(nameCopy.substring(nameCopy.length() - 1 ));
                    }
                    else
                    {
                        incrementElection = Integer.parseInt(tempName2.substring(tempName2.length() - 1 ));
                    }
                    incrementImage++;

                    tempName2 = tempName2 + "_" + incrementImage;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(AddNewElection.this, "Error: Something went wrong...", Toast.LENGTH_LONG).show();
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
                imageUri1 = data.getData();
                pic_1.setImageURI(imageUri1);
                uploaded1 = true;
            }
        }
        else if(requestCode == 200)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                imageUri2 = data.getData();
                pic_2.setImageURI(imageUri2);
                uploaded2 = true;
            }
        }
    }

    private void submitData(String title, String name1, String name2, String email1, String email2,
                            String roll1, String roll2, Uri imageUri1, Uri imageUri2, String votes1, String votes2, String userID)
    {
        SetValueImageIncrement();

        String img1 = tempName2 + "_" + name1 + ".jpg";
        String img2 = tempName2 + "_" + name2 + ".jpg";

        StorageReference fileRef1 = storageReference.child(img1);
        fileRef1.putFile(imageUri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(AddNewElection.this, "Uploaded Successfully...", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {

            }
        });
        StorageReference fileRef2 = storageReference.child(img2);
        fileRef2.putFile(imageUri2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(AddNewElection.this, "Uploaded Successfully...", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {

            }
        });

        Electiondata electiondata = new
                Electiondata(title, name1, name2, email1, email2,
                roll1, roll2, votes1, votes2, userID, status, img1, img2);

        setValueElectionIncrement();

//        FirebaseDatabase.getInstance().getReference("ElectionData")
//                .child(tempName)
//                .setValue(electiondata).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task)
//            {
//                if(task.isSuccessful())
//                {
//                    Toast.makeText(AddNewElection.this,"Successfully Created...",Toast.LENGTH_LONG).show();
//                }
//                else
//                {
//                    Toast.makeText(AddNewElection.this,"Error in Data Entry...",Toast.LENGTH_LONG).show();
//                }
//            }
//        });

        FirebaseDatabase.getInstance().getReference("ElectionData")
                .child(tempName).child("title").setValue(title);
        FirebaseDatabase.getInstance().getReference("ElectionData")
                .child(tempName).child("name1").setValue(name1);
        FirebaseDatabase.getInstance().getReference("ElectionData")
                .child(tempName).child("name2").setValue(name2);
        FirebaseDatabase.getInstance().getReference("ElectionData")
                .child(tempName).child("email1").setValue(email1);
        FirebaseDatabase.getInstance().getReference("ElectionData")
                .child(tempName).child("email2").setValue(email2);
        FirebaseDatabase.getInstance().getReference("ElectionData")
                .child(tempName).child("roll1").setValue(roll1);
        FirebaseDatabase.getInstance().getReference("ElectionData")
                .child(tempName).child("roll2").setValue(roll2);
        FirebaseDatabase.getInstance().getReference("ElectionData")
                .child(tempName).child("votes1").setValue(votes1);
        FirebaseDatabase.getInstance().getReference("ElectionData")
                .child(tempName).child("votes2").setValue(votes2);
        FirebaseDatabase.getInstance().getReference("ElectionData")
                .child(tempName).child("userID").setValue(userID);
        FirebaseDatabase.getInstance().getReference("ElectionData")
                .child(tempName).child("status").setValue(status);
        FirebaseDatabase.getInstance().getReference("ElectionData")
                .child(tempName).child("img1").setValue(img1);
        FirebaseDatabase.getInstance().getReference("ElectionData")
                .child(tempName).child("img2").setValue(img2);

        Toast.makeText(AddNewElection.this,"Successfully Created...",Toast.LENGTH_LONG).show();

    }
}