package com.example.onlinevoting;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.onlinevoting.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        ImageView imageView = findViewById(R.id.giphy);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        Button btn1 = findViewById(R.id.login);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateUsername() || !validatePassword()) {
                    // Do nothing if validation fails
                } else {
                    checkUser();
                }
            }
        });

        Button btn2 = findViewById(R.id.signup);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.resultpage), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public Boolean validateUsername() {
        String val = username.getText().toString();
        if (val.isEmpty()) {
            username.setError("Username field cannot be empty!");
            return false;
        } else {
            username.setError(null);
            return true;
        }
    }

    public Boolean validatePassword() {
        String val = password.getText().toString();
        if (val.isEmpty()) {
            password.setError("Password field cannot be empty!");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }

    public void checkUser() {
        String uname = username.getText().toString().trim();
        String upass = password.getText().toString().trim();

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("users")
                .whereEqualTo("uname", uname)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            String passwordFromDb = documentSnapshot.getString("pass");

                            if (passwordFromDb != null && passwordFromDb.equals(upass)) {
                                username.setError(null);
                                Intent intent = new Intent(MainActivity.this, Home.class);
                                intent.putExtra("username", uname); // Pass username to Home activity
                                startActivity(intent);
                                finish();
                            } else {
                                password.setError("Invalid Username/Password!");
                                password.requestFocus();
                            }
                        } else {
                            username.setError("Username doesn't exist!");
                            username.requestFocus();
                        }
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Failed to check user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
