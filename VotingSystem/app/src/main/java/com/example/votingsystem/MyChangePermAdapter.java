package com.example.votingsystem;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class MyChangePermAdapter extends RecyclerView.Adapter<MyChangePermAdapter.MyChangePermViewHolder>
{
    Context context;
    ArrayList<ChangePermissionData> list;

    public MyChangePermAdapter(Context context, ArrayList<ChangePermissionData> list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyChangePermViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.change_perm,parent,false);

        return new MyChangePermViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyChangePermViewHolder holder, @SuppressLint("RecyclerView") int position)
    {
        ChangePermissionData changePermissionData = list.get(position);
        holder.Fname.setText(changePermissionData.getFullName());
        holder.sec.setText("Section: " + changePermissionData.getSection());
        holder.sem.setText("Semester: " + changePermissionData.getSemester());
        holder.ses.setText("Session: " + changePermissionData.getSession());
        holder.type.setText("TYPE: " + changePermissionData.getType());

        holder.options_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                String[] options = {"To Student", "To Faculty"};
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        if(i == 0)
                        {
                            ChangePermissionData changePermissionData1 = list.get(position);
                            changePermissionData1.setType("Student");
                            FirebaseDatabase.getInstance().getReference("Users").child(changePermissionData1.getKey())
                                    .setValue(changePermissionData1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(context,"Successfully Created...",Toast.LENGTH_LONG).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(context,"Error in Data Entry...",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                        else
                        {
                            changePermissionData.setType("Faculty");
                            FirebaseDatabase.getInstance().getReference("Users").child(changePermissionData.getKey())
                                    .setValue(changePermissionData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(context,"Successfully Created...",Toast.LENGTH_LONG).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(context,"Error in Data Entry...",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    }
                });
                builder.create().show();
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public static class MyChangePermViewHolder extends RecyclerView.ViewHolder
    {
        TextView Fname;
        TextView sec;
        TextView sem;
        TextView ses;
        TextView type;
        Button   options_Btn;

        public MyChangePermViewHolder(@NonNull View itemView)
        {
            super(itemView);

            Fname  = (TextView) itemView.findViewById(R.id.Fullname);
            sec = (TextView) itemView.findViewById(R.id.section);
            sem = (TextView) itemView.findViewById(R.id.semester);
            ses = (TextView) itemView.findViewById(R.id.session);
            type= (TextView)   itemView.findViewById(R.id.Type);
            options_Btn = (Button) itemView.findViewById(R.id.options);

        }
    }
}
