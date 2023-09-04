package com.example.votingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity
{
    TextInputEditText name;
    TextInputEditText email;
    TextInputEditText pass;
    TextInputEditText age;
    Button   submit;
    Spinner  semester, section, session;
    RadioButton asFac, asStud;
    String   type = "Faculty";

    ArrayAdapter sem;
    ArrayAdapter sec;
    ArrayAdapter ses;

    private FirebaseAuth mAuth;
//    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        name     = (TextInputEditText)findViewById(R.id.Full_Name);
        email    = (TextInputEditText)findViewById(R.id.Email);
        pass     = (TextInputEditText)findViewById(R.id.Password);
        age      = (TextInputEditText)findViewById(R.id.Age);

        semester = (Spinner) findViewById(R.id.Semester);
        section  = (Spinner) findViewById(R.id.Section);
        session  = (Spinner) findViewById(R.id.Session);
        asFac    = (RadioButton) findViewById(R.id.asFaculty);
        asStud   = (RadioButton) findViewById(R.id.asStudent);

//        asFac.setVisibility(View.GONE);
//        asStud.setVisibility(View.GONE);

        asFac.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                asStud.setChecked(false);
                type = "Faculty";
            }
        });
        asStud.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                asFac.setChecked(false);
                type = "Student";
            }
        });


        String[] seme = {"1st", "2nd", "3rd", "4th", "5th", "6th", "7th", "8th"};
        sem = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,seme);
        semester.setAdapter(sem);

        String[] sect = {"A", "B", "C", "D"};
        sec = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,sect);
        section.setAdapter(sec);

        String[] sess = {"F15", "F16", "F17", "F18", "F19", "F20", "F21"};
        ses = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,sess);
        session.setAdapter(ses);

        submit   = (Button)  findViewById(R.id.submit_btn);
        submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                boolean check = false;

                if(email.getText().toString().trim().isEmpty())
                {
                    check = true;
                    email.setError("Please Enter Email...");
                    email.requestFocus();
                }
                else
                {
                    check = false;
                }

                if(pass.getText().toString().trim().isEmpty())
                {
                    check = true;
                    pass.setError("Please Enter Password...");
                    pass.requestFocus();
                }
                else
                {
                    check = false;
                }

                if(name.getText().toString().trim().isEmpty())
                {
                    check = true;
                    name.setError("Please Enter Your Name...");
                }
                else
                {
                    check = false;
                }

                if(age.getText().toString().trim().isEmpty())
                {
                    check = true;
                    age.setError("Please Enter Your Age...");
                }
                else
                {
                    check = false;
                }

                if(type.equals(""))
                {
                    Toast.makeText(Register.this, "Select Type...", Toast.LENGTH_SHORT).show();
                }

                if(check == false)
                {
                    String mail = email.getText().toString().trim();
                    String pas  = pass.getText().toString().trim();
                    String fName = name.getText().toString().trim();
                    String Age = age.getText().toString().trim();
                    String Sem = semester.getSelectedItem().toString();
                    String Sec = section.getSelectedItem().toString();
                    String Ses = session.getSelectedItem().toString();
                    String Img = fName + ".jpg";

                    Register(fName, Age, mail, pas, Sec, Ses, Sem, type, Img);
                }
            }
        });
    }

    public void Register(String fullName, String age, String mail, String pass,
                         String Sec, String Ses, String Sem, String Type, String img)
    {
        mAuth.createUserWithEmailAndPassword(mail, pass)
        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if(task.isSuccessful())
                 {
                    Userdata user = new
                            Userdata(fullName, age, mail, pass, Sec, Ses, Sem, Type, img, FirebaseAuth.getInstance().getCurrentUser().getUid());

                    // Users is a Database Table
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(Register.this,"Successfully registered...",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(Register.this,"Registration Error...",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(Register.this,"Registration Error...",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}