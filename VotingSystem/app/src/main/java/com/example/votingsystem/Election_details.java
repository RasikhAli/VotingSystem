package com.example.votingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class Election_details extends AppCompatActivity
{
    ImageView goBack;
    CircleImageView image1, image2;
    TextView tit, name1, name2, roll1, roll2, vote1, vote2;
    Button btn1, btn2;

    String Tit, V1, V2, r1, r2, n1, n2, I1, I2;
    Integer getValue1, getValue2;
    boolean voted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_election_details);

        tit    = (TextView) findViewById(R.id.title_election);
        name1  = (TextView) findViewById(R.id.Name1);
        name2  = (TextView) findViewById(R.id.Name2);
        roll1  = (TextView) findViewById(R.id.Roll1);
        roll2  = (TextView) findViewById(R.id.Roll2);
        vote1  = (TextView) findViewById(R.id.Votes1);
        vote2  = (TextView) findViewById(R.id.Votes2);
        image1 = (CircleImageView) findViewById(R.id.image1);
        image2 = (CircleImageView) findViewById(R.id.image2);
        btn1   = (Button) findViewById(R.id.votesBtn1);
        btn2   = (Button) findViewById(R.id.votesBtn2);

        Bundle intent = getIntent().getExtras();
        if(intent != null)
        {
            Tit = intent.getString("sendTitle");
            n1 = intent.getString("sendName1");
            n2 = intent.getString("sendName2");
            r1 = intent.getString("sendRoll1");
            r2 = intent.getString("sendRoll2");
            V1 = intent.getString("sendVotes1");
            V2 = intent.getString("sendVotes2");
            I1 = intent.getString("sendImg1");
            I2 = intent.getString("sendImg2");

            tit.setText(Tit);
            name1.setText(n1);
            name2.setText(n2);
            roll1.setText(r1);
            roll2.setText(r2);
            vote1.setText("Votes: " + V1);
            vote2.setText("Votes: " + V2);
            btn1.setText(n1);
            btn2.setText(n2);

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
                                image1.setImageBitmap(bitmap);
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
                                image2.setImageBitmap(bitmap);
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

        getValue1 = Integer.parseInt(V1);
        getValue2 = Integer.parseInt(V2);

        goBack = (ImageView) findViewById(R.id.go_back);
        goBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Election_details.this, CurrentElections.class);
                startActivity(intent);
            }
        });

        btn1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(voted == true)
                {
                    Toast.makeText(Election_details.this, "Sorry, can't have multiple votes...", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    voted = true;
                    getValue1 = getValue1 + 1;
                    vote1.setText("Votes: " + getValue1.toString());
                    Electiondata electiondata = new Electiondata();
                    electiondata.setVotes1(getValue1.toString());

                    int count = I1.length() - n1.length();
                    String temp = I1.substring(0,count-5);

                    FirebaseDatabase.getInstance().getReference("ElectionData")
                    .child(temp).child("votes1")
                    .setValue(getValue1.toString()).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(Election_details.this,"Successfully Voted...",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(Election_details.this,"Error in Voting...",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(voted == true)
                {
                    Toast.makeText(Election_details.this, "Sorry, can't have multiple votes...", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    voted = true;
                    getValue2 = getValue2 + 1;
                    vote2.setText("Votes: " + getValue2.toString());
                    Electiondata electiondata = new Electiondata();
                    electiondata.setVotes2(getValue2.toString());

                    int count = I2.length() - n2.length();
                    String temp = I2.substring(0,count-5);

                    FirebaseDatabase.getInstance().getReference("ElectionData")
                            .child(temp).child("votes2")
                            .setValue(getValue2.toString()).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(Election_details.this,"Successfully Voted...",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(Election_details.this,"Error in Voting...",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }
}