package com.example.votingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CurrentElections extends AppCompatActivity
{
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    CurrAdapter currAdapter;
    ArrayList<CurrElectionData> currlist;

    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_elections);

        back = (ImageView) findViewById(R.id.go_back);
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(CurrentElections.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("ElectionData");

        recyclerView = findViewById(R.id.recyclerViewCurr);
        recyclerView.setLayoutManager(new LinearLayoutManager(CurrentElections.this));

        currlist = new ArrayList<>();
        currAdapter = new CurrAdapter(CurrentElections.this,currlist);
        recyclerView.setAdapter(currAdapter);

        databaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    CurrElectionData currElectionData = dataSnapshot.getValue(CurrElectionData.class);

                    if(currElectionData.getStatus().equals("On-Going"))
                    {
                        currlist.add(dataSnapshot.getValue(CurrElectionData.class));
                    }
                }
                currAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }
}