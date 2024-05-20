package com.example.onlinevoting;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;

public class Vote extends AppCompatActivity {
    private String username;
    private FirebaseFirestore db;
    private DocumentReference userReference;
    private CollectionReference voteReference;
    private LottieAnimationView animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vote);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.resultpage), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get the username from the intent
        username = getIntent().getStringExtra("username");

        Button vote1 = findViewById(R.id.vote1);
        Button vote2 = findViewById(R.id.vote2);
        animationView = findViewById(R.id.voted);

        // Initialize Firebase references
        db = FirebaseFirestore.getInstance();
        voteReference = db.collection("votes"); // Corrected line

        // Query the users collection to find the document with the specified username
        db.collection("users")
                .whereEqualTo("uname", username)
                .limit(1) // Assuming username is unique, limit to 1 document
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot userDocument = queryDocumentSnapshots.getDocuments().get(0);
                        userReference = userDocument.getReference(); // Get the reference to the user document

                        // Set up button click listeners after obtaining user reference
                        vote1.setOnClickListener(v -> checkAndVote(1));
                        vote2.setOnClickListener(v -> checkAndVote(2));
                    } else {
                        Toast.makeText(Vote.this, "User document not found!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Vote.this, "Failed to retrieve user document: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void checkAndVote(int candidate) {
        if (userReference == null) {
            Toast.makeText(Vote.this, "User reference is null!", Toast.LENGTH_SHORT).show();
            return;
        }

        userReference.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Boolean hasVoted = documentSnapshot.getBoolean("hasVoted");
                if (hasVoted != null && hasVoted) {
                    Toast.makeText(Vote.this, "You have already voted!", Toast.LENGTH_SHORT).show();
                } else {
                    castVote(candidate);
                }
            } else {
                Toast.makeText(Vote.this, "User data not found!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(Vote.this, "Failed to check user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void castVote(int candidate) {
        db.runTransaction(transaction -> {
            DocumentReference voteDocRef = voteReference.document("candidate" + candidate);
            DocumentSnapshot voteSnapshot = transaction.get(voteDocRef);

            long currentVotes = voteSnapshot.exists() ? voteSnapshot.getLong("count") : 0;
            transaction.set(voteDocRef, new HashMap<String, Object>() {{
                put("count", currentVotes + 1);
            }}, SetOptions.merge());

            transaction.update(userReference, "hasVoted", true);

            return null;
        }).addOnSuccessListener(aVoid -> {
            Button vote1 = findViewById(R.id.vote1);
            Button vote2 = findViewById(R.id.vote2);
            vote1.setEnabled(false);
            vote2.setEnabled(false);
            animationView.setVisibility(View.VISIBLE);

            animationView.setAnimation(R.raw.voted);
            animationView.playAnimation();
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(Vote.this, Result.class);
                startActivity(intent);
                finish();
            }, 2000);
        }).addOnFailureListener(e -> {
            Toast.makeText(Vote.this, "Database error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}
