package com.example.votingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class Dashboard extends AppCompatActivity
{

    TextView txt1, txt2;
    CircleImageView pic;
    Button logout;
    Button add;
    Button prev;
    Button del;
    Button change;
    Button current;
    Button setting;

    public String usertype, fName;

    private FirebaseUser mUser;
    private DatabaseReference reference;

    private String uID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        uID = mUser.getUid();

        txt1 = (TextView) findViewById(R.id.type_of_login);
        txt2 = (TextView) findViewById(R.id.full_name);
        pic  = (CircleImageView)findViewById(R.id.picture);

        pic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Dashboard.this, userSettings.class);
                startActivity(intent);
            }
        });

        reference.child(uID).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                Userdata userProfile = snapshot.getValue(Userdata.class);

                if(userProfile != null)
                {
                    fName = userProfile.fullName;
                    usertype = userProfile.type;

                    StorageReference storageReference = FirebaseStorage.getInstance().getReference(userProfile.Img);
                    try{
                        File localfile2 = File.createTempFile("tempfile",".jpg");
                        storageReference.getFile(localfile2)
                                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>()
                                {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot)
                                    {
                                        Bitmap bitmap = BitmapFactory.decodeFile(localfile2.getAbsolutePath());
                                        pic.setImageBitmap(bitmap);
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

                    txt1.setText(usertype);
                    txt2.setText(fName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(Dashboard.this, "Error: Something went wrong...", Toast.LENGTH_LONG).show();
            }
        });

        current = (Button) findViewById(R.id.on_going);
        current.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Dashboard.this, CurrentElections.class);
                startActivity(intent);
            }
        });

        add = (Button) findViewById(R.id.add_new);
        add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Dashboard.this, AddNewElection.class);
                startActivity(intent);
            }
        });

        prev = (Button) findViewById(R.id.my_prev);
        prev.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Dashboard.this, CheckPreviousElections.class);
                startActivity(intent);
            }
        });

        del = (Button) findViewById(R.id.del_elec);
        del.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                reference.child(uID).addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        Userdata userProfile = snapshot.getValue(Userdata.class);
                        if(userProfile != null)
                        {
                            if(userProfile.type.equals("Student"))
                            {
                                Toast.makeText(Dashboard.this, "Sorry, Only For Faculty...", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Intent intent = new Intent(Dashboard.this, Del_Any.class);
                                startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {

                    }
                });
            }
        });

        change = (Button) findViewById(R.id.make_faculty);
        change.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                reference.child(uID).addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        Userdata userProfile = snapshot.getValue(Userdata.class);
                        if(userProfile != null)
                        {
                            if(userProfile.type.equals("Student"))
                            {
                                Toast.makeText(Dashboard.this, "Sorry, Only For Faculty...", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Intent intent = new Intent(Dashboard.this, Change_Permission.class);
                                startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {

                    }
                });
            }
        });

        setting = (Button) findViewById(R.id.setting_btn);
        setting.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Dashboard.this, userSettings.class);
                startActivity(intent);
            }
        });

        logout = (Button) findViewById(R.id.logout_btn);
        logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Dashboard.this, MainActivity.class));
                finish();
            }
        });
    }
}