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

public class MyPreviousElectionAdapter extends RecyclerView.Adapter<MyPreviousElectionAdapter.MyPreviousElectionViewHolder>
{
    Context context;
    ArrayList<PreviousElectionData> list;
    String pos;

    private FirebaseUser mUser;
    private DatabaseReference reference;
    private String usID;

    public MyPreviousElectionAdapter(Context context, ArrayList<PreviousElectionData> list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyPreviousElectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.prev_elections,parent,false);

        return new MyPreviousElectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPreviousElectionViewHolder holder, @SuppressLint("RecyclerView") int position)
    {
        PreviousElectionData previousElectionData = list.get(position);
        holder.title.setText(previousElectionData.getTitle());
        holder.votes1.setText("Votes of Person 1: " + previousElectionData.getVotes1());
        holder.votes2.setText("Votes of Person 2: " + previousElectionData.getVotes2());
        holder.status.setText("STATUS: " + previousElectionData.getStatus());

        holder.options_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreviousElectionData previous = list.get(position);
                String Tit = previous.getTitle();
                String V1 = previous.getVotes1();
                String V2 = previous.getVotes2();
                String uID = previous.getUserID();
                String Stat = previous.getStatus();
                String I1 = previous.getImg1();
                String I2 = previous.getImg2();
                String e1 = previous.getEmail1();
                String e2 = previous.getEmail2();
                String n1 = previous.getName1();
                String n2 = previous.getName2();
                String r1 = previous.getRoll1();
                String r2 = previous.getRoll2();
                pos = Integer.toString(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                String[] options = {"Update", "Delete"};
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        if(i == 0)
                        {
                            //Update
                            Intent intent = new Intent(context, AddNewElection.class);
                            intent.putExtra("Tit", Tit);
                            intent.putExtra("V1", V1);
                            intent.putExtra("V2",  V2);
                            intent.putExtra("uID", uID);
                            intent.putExtra("Stat", Stat);
                            intent.putExtra("e1",  e1);
                            intent.putExtra("e2", e2);
                            intent.putExtra("n1", n1);
                            intent.putExtra("n2",  n2);
                            intent.putExtra("r1",  r1);
                            intent.putExtra("r2", r2);
                            intent.putExtra("I1", I1);
                            intent.putExtra("I2",  I2);
                            intent.putExtra("Position", pos);
                            context.startActivity(intent);
                        }
                        if(i == 1)
                        {
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
                    }
                });
                builder.create().show();
            }
        });

        holder.linear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
//                CurrElectionData curr = list.get(position);
                Intent intent = new Intent(context, Election_details.class);
                intent.putExtra("sendTitle", previousElectionData.getTitle());
                intent.putExtra("sendImg1", previousElectionData.getImg1());
                intent.putExtra("sendImg2", previousElectionData.getImg2());
                intent.putExtra("sendName1", previousElectionData.getName1());
                intent.putExtra("sendName2", previousElectionData.getName2());
                intent.putExtra("sendRoll1", previousElectionData.getRoll1());
                intent.putExtra("sendRoll2", previousElectionData.getRoll2());
                intent.putExtra("sendVotes1", previousElectionData.getVotes1());
                intent.putExtra("sendVotes2", previousElectionData.getVotes2());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public static class MyPreviousElectionViewHolder extends RecyclerView.ViewHolder
    {
        TextView title;
        TextView votes1;
        TextView votes2;
        TextView status;
        Button   options_Btn;
        LinearLayout linear;

        public MyPreviousElectionViewHolder(@NonNull View itemView)
        {
            super(itemView);

            title  = (TextView) itemView.findViewById(R.id.title_election);
            votes1 = (TextView) itemView.findViewById(R.id.votes1);
            votes2 = (TextView) itemView.findViewById(R.id.votes2);
            status = (TextView) itemView.findViewById(R.id.status);
            options_Btn= (Button)   itemView.findViewById(R.id.options);
            linear = (LinearLayout) itemView.findViewById(R.id.linearlayout);

        }
    }
}
