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

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    EditText username;
    EditText password;
    Button loginButton;

    private int checker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        mAuth = FirebaseAuth.getInstance();

        username = findViewById(R.id.editText_username);
        password = findViewById(R.id.editText_password);
        loginButton = findViewById(R.id.button_login);

        /**
         * 심사를 위해 제공되는 마스터 아이디 입니다
         */
        username.setText("master01");
        password.setText("1q2w3e4r");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((checker = checkEmpty()) == 1) {
                    Toast.makeText(getApplicationContext(), "ID를 입력해 주세요", Toast.LENGTH_SHORT).show();
                } else if (checker == 2) {
                    Toast.makeText(getApplicationContext(), "Password를 입력해 주세요", Toast.LENGTH_SHORT).show();
                } else {
                    /**
                     * 서버를 통해 로그인 컨트롤 하는 부분입니다.
                     * 심사를 위해 마스터 아이디를 제공하기 위해 주석처리
                     */
                    /*
                    mAuth.signInWithEmailAndPassword(username.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, "로그인 실패~~", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "로그인 성공!!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
<<<<<<< HEAD
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
=======
                            }); */
                    /**
                     * 마스터 아이디를 통한 로그인입니다
                     */
                    if(username.getText().toString().matches("master01")
                            && password.getText().toString().matches("1q2w3e4r")) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "ID와 비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private int checkEmpty() {
        if (username.getText().toString().matches(""))
            return 1;
        else if (password.getText().toString().matches(""))
            return 2;

        return 0;
    }
}
