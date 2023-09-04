package com.example.votingsystem;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CurrAdapter extends RecyclerView.Adapter<CurrAdapter.myViewHolder>
{
    Context context;
    ArrayList<CurrElectionData> list;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    String pos = "";

    public CurrAdapter(Context context, ArrayList<CurrElectionData> list)
    {
        this.context = context;
        this.list = list;
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.curr_elections,parent,false);

        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") int position)
    {
        CurrElectionData currElectionData = list.get(position);
        holder.title.setText(currElectionData.getTitle());
        holder.votes1.setText("Votes 1: " + currElectionData.getVotes1());
        holder.votes2.setText("Votes 2: " + currElectionData.getVotes2());

        StorageReference storageReference1 = FirebaseStorage.getInstance().getReference(currElectionData.getImg1());
        try{
            File localfile1 = File.createTempFile("tempfile",".jpg");
            storageReference1.getFile(localfile1)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>()
                    {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot)
                        {
                            Bitmap bitmap = BitmapFactory.decodeFile(localfile1.getAbsolutePath());
                            holder.img1.setImageBitmap(bitmap);
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
        StorageReference storageReference2 = FirebaseStorage.getInstance().getReference(currElectionData.getImg2());
        try{
            File localfile2 = File.createTempFile("tempfile",".jpg");
            storageReference2.getFile(localfile2)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>()
                    {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot)
                        {
                            Bitmap bitmap = BitmapFactory.decodeFile(localfile2.getAbsolutePath());
                            holder.img2.setImageBitmap(bitmap);
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

        holder.linear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
//                CurrElectionData curr = list.get(position);
                Intent intent = new Intent(context, Election_details.class);
                intent.putExtra("sendTitle", currElectionData.getTitle());
                intent.putExtra("sendImg1", currElectionData.getImg1());
                intent.putExtra("sendImg2", currElectionData.getImg2());
                intent.putExtra("sendName1", currElectionData.getName1());
                intent.putExtra("sendName2", currElectionData.getName2());
                intent.putExtra("sendRoll1", currElectionData.getRoll1());
                intent.putExtra("sendRoll2", currElectionData.getRoll2());
                intent.putExtra("sendVotes1", currElectionData.getVotes1());
                intent.putExtra("sendVotes2", currElectionData.getVotes2());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder
    {
        CircleImageView img1, img2;
        TextView title,votes1,votes2;
        LinearLayout linear;

        public myViewHolder(@NonNull View itemView)
        {
            super(itemView);
//
            img1 = (CircleImageView) itemView.findViewById(R.id.img1);
            img2 = (CircleImageView) itemView.findViewById(R.id.img2);
            title= (TextView)  itemView.findViewById(R.id.title_election);
            votes1= (TextView)  itemView.findViewById(R.id.votes1);
            votes2= (TextView)  itemView.findViewById(R.id.votes2);
            linear = (LinearLayout) itemView.findViewById(R.id.linear_id);
        }
    }
}

