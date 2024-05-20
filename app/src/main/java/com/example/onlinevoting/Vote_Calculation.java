package com.example.onlinevoting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class Vote_Calculation extends AppCompatActivity {
    private EditText etTotalVotes, etVotesReceived;
    private TextView tvResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vote_calculation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etTotalVotes = findViewById(R.id.etTotalVotes);
        etVotesReceived = findViewById(R.id.etVotesReceived);
        tvResult = findViewById(R.id.tvResult);

        Button btnCalculate = findViewById(R.id.btnCalculate);
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateWinningPercentage();
            }
        });
        Button waittime = findViewById(R.id.Waitbtn);
        waittime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), VoterWaittimeCalc.class);
                startActivity(intent);
            }
        });
    }
    private void calculateWinningPercentage() {
        try {
            int totalVotes = Integer.parseInt(etTotalVotes.getText().toString());
            int votesReceived = Integer.parseInt(etVotesReceived.getText().toString());

            double winningPercentage = (double) votesReceived / totalVotes * 100;

            tvResult.setText(String.format(Locale.getDefault(), "Winning Percentage: %.2f%%", winningPercentage));
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers!", Toast.LENGTH_SHORT).show();
        } catch (ArithmeticException e) {
            Toast.makeText(this, "Total votes cannot be zero!", Toast.LENGTH_SHORT).show();
        }
    }
    }
