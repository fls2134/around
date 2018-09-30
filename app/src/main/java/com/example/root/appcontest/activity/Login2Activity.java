package com.example.root.appcontest.activity;
/*
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.root.appcontest.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Login2Activity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @BindView(R.id.login)
    Button login;
    @BindView(R.id.sign_up) Button sign_up;
    @BindView(R.id.editEmail)
    EditText editEmail;
    @BindView(R.id.editPassword) EditText editPassword;

    boolean isEmptyEditField()
    {
        if((editEmail.getText().toString().length() == 0)||(editPassword.getText().toString().length() == 0))
            return true;
        else
            return false;
    }

    @OnClick(R.id.login)
    void clickLogin() {
        if (isEmptyEditField()) {
            Toast.makeText(this, "모든 칸을 채워주세요!!", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(editEmail.getText().toString(), editPassword.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //   checkTaskException(task); ??머임??

                            if (!task.isSuccessful()) {
                                Toast.makeText(Login2Activity.this, "로그인 실패~~", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Login2Activity.this, "로그인 성공!!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Login2Activity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });

        }
    }

    @OnClick(R.id.sign_up)
    void clickSignUp(){
        Intent intent=new Intent(Login2Activity.this,Sign_up.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        this.setTitle("로그인");
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user != null){
                    Toast.makeText(Login2Activity.this, "로그인 성공!!", Toast.LENGTH_SHORT).show();


                }
                else{
                    Toast.makeText(Login2Activity.this,"로그아웃 성공!!", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }
}
*/