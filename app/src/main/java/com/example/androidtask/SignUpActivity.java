package com.example.androidtask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText etUsername, etPassword;
    Spinner spinner;
    Button btnSignUp;
    TextView tvLogin;
    String[] typeList = {"A", "B", "C"};
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    List<String> usersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");

        etUsername = findViewById(R.id.et_usernameSignUp);
        etPassword = findViewById(R.id.et_passwordSignUp);
        btnSignUp = findViewById(R.id.btn_sigUp);
        tvLogin = findViewById(R.id.tv_Login);
        spinner = findViewById(R.id.spinner);

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot snap: snapshot.getChildren()) {
                    UserHelperClass user = snap.getValue(UserHelperClass.class);
                    Log.d("TAG", "onDataChange: "+ user.userName);
                    usersList.add(user.userName);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });

        spinner.setOnItemSelectedListener(this);
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,typeList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidation()){
                    addData();
                }else {
                    Toast.makeText(SignUpActivity.this, "Please submit Data", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public boolean checkValidation(){
        String userNmae = etUsername.getText().toString();
        String userPassword = etPassword.getText().toString();

        if (TextUtils.isEmpty(userNmae)){
            etUsername.setError("Username is required");
            return false;

        }else if (etUsername.length() < 6){
            etUsername.setError("username is too short");
            return false;

        }else if (TextUtils.isEmpty(userPassword)){
            etPassword.setError("password is required");
            return false;

        } else {
            String userType = spinner.getSelectedItem().toString();
        }
        return true;
    }

    public void addData(){
        String userName = etUsername.getText().toString();
        String userPassword = etPassword.getText().toString();
        final String type = spinner.getSelectedItem().toString();

        if (usersList.contains(userName)){
            Toast.makeText(this, "User Already Exists", Toast.LENGTH_SHORT).show();
        }else {
            UserHelperClass helperClass = new UserHelperClass(userName,userPassword,type);

            reference.child(userName).setValue(helperClass).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(type.equals("A")) {
                        startActivity(new Intent(SignUpActivity.this, ImageOneActivity.class));
                    }
                    else if(type.equals("B")) {
                        startActivity(new Intent(SignUpActivity.this, HorizontalImageActivity.class));
                    }
                    else if(type.equals("C")) {
                        startActivity(new Intent(SignUpActivity.this, VerticalImageActivity.class));
                    }
                    Toast.makeText(SignUpActivity.this, "Thanks for submit", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(getApplicationContext(),typeList[position] , Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
