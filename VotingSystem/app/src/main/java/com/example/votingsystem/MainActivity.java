package com.example.votingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity
{

    TextInputEditText email;
    TextInputEditText password;
    TextView    register_now;
    Button      login_btn;

    private FirebaseAuth mAuth;

    public void to_Register(View view)
    {
        register_now = (TextView) findViewById(R.id.to_register);

        register_now.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        email       = (TextInputEditText) findViewById(R.id.email);
        password    = (TextInputEditText) findViewById(R.id.password);
        login_btn   = (Button) findViewById(R.id.loginbtn);


        login_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String mail = email.getText().toString().trim();
                String pass = password.getText().toString().trim();

                if(mail.isEmpty() && pass.isEmpty())
                {
                    email.setError("");
                    password.setError("");
                    Toast.makeText(MainActivity.this, "Error: Enter Email & Password", Toast.LENGTH_SHORT).show();
                }
                else if(mail.isEmpty())
                {
                    email.setError("");
                    Toast.makeText(MainActivity.this, "Error: Enter Email", Toast.LENGTH_SHORT).show();
                }
                else if(pass.isEmpty())
                {
                    password.setError("");
                    Toast.makeText(MainActivity.this, "Error: Enter Password", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    userLogin(mail,pass);
                }
            }

            private void userLogin(String mail, String pass)
            {
                mAuth.signInWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if(task.isSuccessful())
                        {
                            Intent intent = new Intent(MainActivity.this, Dashboard.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Failed to Login, Please Check Entered Credentials... ", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

    }
}