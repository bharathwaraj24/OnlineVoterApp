package com.example.onlinevoting;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseHelper {

    private final DatabaseReference databaseReference;

    public DatabaseHelper(Context context) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
    }

    public void addVote(int candidate, int count) {
        String candidateKey = candidate == 1 ? "candidate1" : "candidate2";
        databaseReference.child(candidateKey).setValue(count);
    }
}