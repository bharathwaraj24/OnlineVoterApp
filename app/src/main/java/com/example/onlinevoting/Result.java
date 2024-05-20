package com.example.onlinevoting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Result extends AppCompatActivity {
    private TextView votecount1;
    private TextView votecount2;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_result);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.resultpage), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        votecount1 = findViewById(R.id.votecount1);
        votecount2 = findViewById(R.id.votecount2);
        Button calculate = findViewById(R.id.calculation);

        // Initialize Firestore reference
        db = FirebaseFirestore.getInstance();

        // Retrieve and display the vote counts
        displayVoteCounts();

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Vote_Calculation.class);
                startActivity(intent);
            }
        });
    }

    private void displayVoteCounts() {
        DocumentReference candidate1Ref = db.collection("votes").document("candidate1");
        DocumentReference candidate2Ref = db.collection("votes").document("candidate2");

        candidate1Ref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Long voteCount1 = document.getLong("count");
                    votecount1.setText("Total Votes for Candidate 1: " + (voteCount1 != null ? voteCount1 : 0));
                } else {
                    votecount1.setText("Total Votes for Candidate 1: 0");
                }
            }
        });

        candidate2Ref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Long voteCount2 = document.getLong("count");
                    votecount2.setText("Total Votes for Candidate 2: " + (voteCount2 != null ? voteCount2 : 0));
                } else {
                    votecount2.setText("Total Votes for Candidate 2: 0");
                }
            }
        });
    }
}
