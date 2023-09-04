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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class MyDelAnyAdapter extends RecyclerView.Adapter<MyDelAnyAdapter.MyDelAnyViewHolder>
{
    Context context;
    ArrayList<PreviousElectionData> list;
    String pos;


    public MyDelAnyAdapter(Context context, ArrayList<PreviousElectionData> list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyDelAnyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.del_any_election,parent,false);

        return new MyDelAnyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyDelAnyViewHolder holder, @SuppressLint("RecyclerView") int position)
    {
        PreviousElectionData previousElectionData = list.get(position);
        holder.title.setText(previousElectionData.getTitle());
        holder.votes1.setText("Votes of Person 1: " + previousElectionData.getVotes1());
        holder.votes2.setText("Votes of Person 2: " + previousElectionData.getVotes2());
        holder.status.setText("STATUS: " + previousElectionData.getStatus());

        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreviousElectionData previous = list.get(position);

                //Delete
                StorageReference sto1 = FirebaseStorage.getInstance().getReference(previous.getImg1());
                sto1.delete();
                StorageReference sto2 = FirebaseStorage.getInstance().getReference(previous.getImg2());
                sto2.delete();

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("ElectionData");
                int count = previous.getImg1().length() - previous.getName1().length();

                String name = previous.getImg1().substring(0,count-5);

                ref.child(name).removeValue();
                list.remove(list.get(position));
                Toast.makeText(context, "Deleted...", Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public static class MyDelAnyViewHolder extends RecyclerView.ViewHolder
    {
        TextView title;
        TextView votes1;
        TextView votes2;
        TextView status;
        Button   del;

        public MyDelAnyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            title  = (TextView) itemView.findViewById(R.id.title_election);
            votes1 = (TextView) itemView.findViewById(R.id.votes1);
            votes2 = (TextView) itemView.findViewById(R.id.votes2);
            status = (TextView) itemView.findViewById(R.id.status);
            del = (Button)   itemView.findViewById(R.id.delete);

        }
    }
}
