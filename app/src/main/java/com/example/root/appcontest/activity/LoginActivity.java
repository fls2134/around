package com.example.root.appcontest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.root.appcontest.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity{

    private FirebaseAuth mAuth;
    Button login;
    Button sign_up;
    EditText editEmail;
    EditText editPassword;

    boolean isEmptyEditField()
    {
        if((editEmail.getText().toString().length() == 0)||(editPassword.getText().toString().length() == 0))
            return true;
        else
            return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        //login = (Button)findViewById(R.id.login);
        //sign_up = (Button)findViewById(R.id.sign_up);
        editEmail = (EditText)findViewById(R.id.editEmail);
        editEmail = (EditText)findViewById(R.id.editPassword);
        mAuth = FirebaseAuth.getInstance();
        this.setTitle("로그인");
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEmptyEditField()) {
                    Toast.makeText(LoginActivity.this, "모든 칸을 채워주세요!!", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.signInWithEmailAndPassword(editEmail.getText().toString(), editPassword.getText().toString())
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    //   checkTaskException(task); ??머임??
                                    /*
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, "로그인 실패~~", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "로그인 성공!!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    */
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                }
            }
        });
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,Sign_up_Activity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
