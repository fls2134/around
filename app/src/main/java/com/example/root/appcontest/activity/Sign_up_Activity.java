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

public class Sign_up_Activity extends AppCompatActivity{

    EditText editEmail;
    EditText editPassword;
    Button sign_up;
    FirebaseAuth mAuth;
    boolean isEmptyEditField()
    {
        if((editEmail.getText().toString().length() == 0)||(editPassword.getText().toString().length() == 0))
            return true;
        else
            return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity);
        editEmail = (EditText)findViewById(R.id.editEmail);
        editPassword = (EditText)findViewById(R.id.editPassword);
        //sign_up = (Button)findViewById(R.id.sign_up);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEmptyEditField()) {
                    Toast.makeText(Sign_up_Activity.this, "모든 칸을 채워주세요!", Toast.LENGTH_SHORT).show();
                } else if (editPassword.getText().toString().length() < 6) {
                    Toast.makeText(Sign_up_Activity.this, "비밀번호는 최소 6자리 이상이어야 합니다!", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.createUserWithEmailAndPassword(editEmail.getText().toString(), editPassword.getText().toString())
                            .addOnCompleteListener(Sign_up_Activity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    //    checkTaskException(task);
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(Sign_up_Activity.this, "가입 실패~~", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(Sign_up_Activity.this, "가입 성공!!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Sign_up_Activity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                }
                            });
                }
            }
        });

    }
}
