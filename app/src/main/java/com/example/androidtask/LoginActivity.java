package com.example.androidtask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText etUserName, etPassword;
    Spinner spinner;
    Button btnLogin;
    TextView tvSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUserName = findViewById(R.id.et_usernameLogin);
        etPassword = findViewById(R.id.et_passwordLogin);
        btnLogin = findViewById(R.id.btn_Login);
        tvSignUp = findViewById(R.id.tv_signUp);
        spinner = findViewById(R.id.spinner);

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(v);
            }
        });
    }

    private boolean validateUserName(){
        String username = etUserName.getText().toString();
        if (username.isEmpty()){
            etUserName.setError("Username cannot be empty");
            return false;
        }else {
            etUserName.setError(null);
            return true;
        }
    }

    private Boolean validatePassword(){
        String password = etPassword.getText().toString();
        if (password.isEmpty()){
            etPassword.setError("Password cannot be empty");
            return false;
        }else {
            etPassword.setError(null);
            return true;
        }
    }

    public void loginUser(View view){
        if (!validateUserName() | !validatePassword()){
            return;
        }else {
            isUser();
        }
    }

    private void isUser() {
        final String userEnteredUsername = etUserName.getText().toString().trim();
        final String userEnteredPassword = etPassword.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        Query checkUser = reference.orderByChild("userName").equalTo(userEnteredUsername);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    etUserName.setError(null);

                    String passwordFromDb = snapshot.child(userEnteredUsername).child("userPassword").getValue(String.class);
                    String typeFromDb = snapshot.child(userEnteredUsername).child("type").getValue(String.class);

                    if (passwordFromDb.equals(userEnteredPassword)){
                        if(typeFromDb.equals("A")) {
                            startActivity(new Intent(LoginActivity.this, ImageOneActivity.class));
                        }
                        else if(typeFromDb.equals("B")) {
                            startActivity(new Intent(LoginActivity.this, HorizontalImageActivity.class));
                        }
                        else if(typeFromDb.equals("C")) {
                            startActivity(new Intent(LoginActivity.this, VerticalImageActivity.class));
                        }
                    }else {
                        Toast.makeText(LoginActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(LoginActivity.this, "No such user exist", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(getApplicationContext(),typeList[position] , Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}